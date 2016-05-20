package com.xabaohui.modules.trade.bo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon;
import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderDetail;
import com.xabaohui.modules.trade.bean.OrderLog;
import com.xabaohui.modules.trade.bean.OrderLogOperateType;
import com.xabaohui.modules.trade.bean.OrderStatus;
import com.xabaohui.modules.trade.dao.OrderDao;
import com.xabaohui.modules.trade.dao.OrderDetailDao;
import com.xabaohui.modules.trade.dao.OrderLogDao;
import com.xabaohui.modules.trade.dao.Trade;
import com.xabaohui.modules.trade.dto.AddItemDTO;
import com.xabaohui.modules.trade.dto.AssignStoreDTO;
import com.xabaohui.modules.trade.dto.CreateOrderDTO;
import com.xabaohui.modules.trade.dto.CreateOrderLogDTO;
import com.xabaohui.modules.trade.dto.OrderDetailDTO;
import com.xabaohui.modules.trade.dto.PayForOrderDTO;
import com.xabaohui.modules.trade.dto.RefundDTO;
import com.xabaohui.modules.trade.dto.UpdateCountDTO;
import com.xabaohui.modules.trade.dto.UpdateReceiverAddrDTO;

/**
 * 交易
 * @author Zhang Hongwei
 *
 */
public class TradeBO implements Trade {

	private OrderDao orderDao;
	private OrderDetailDao orderDetailDao;
	private OrderLogDao orderLogDao;
	private PostageBO postage;
	private Logger log = LoggerFactory.getLogger(TradeBO.class);

	/**
	 * 生成订单
	 */
	public Order createOrder(CreateOrderDTO createorderDTO) {
		// 检测数据
		checkForCreateOrder(createorderDTO);
		log.info("数据检验完毕");

		// 商品总数计算 和 商品总价计算
		Integer orderItemCount = 0;
		Double orderMoney = 0.0;
		for (OrderDetailDTO orderDetailDTO : createorderDTO.getList()) {
			orderItemCount += orderDetailDTO.getItemCount();
			orderMoney += orderDetailDTO.getItemPrice()
					* orderDetailDTO.getItemCount();
		}
		log.info("商品总数=" + orderItemCount + "商品总价=" + orderMoney);

		// 开始运费的初始化;
		Double postFee = postage.calculatePostage(
				createorderDTO.getReceiverCityId(), orderItemCount,
				createorderDTO.getSellerId());
		log.info("初始化的运费=" + postFee);

		// 生成订单对象
		log.info("开始生成订单对象");
		Order order = new Order();
		BeanUtils.copyProperties(createorderDTO, order);
		order.setOrderItemCount(orderItemCount);
		order.setOrderMoney(orderMoney);
		order.setOrderStatus(OrderStatus.ORDER_STATUS_INIT);
		order.setPostMoney(postFee);
		innerSaveOrder(order);
		log.info("录入完成");

		// 生成订单明细对象
		for (OrderDetailDTO orderDetailDTO : createorderDTO.getList()) {
			OrderDetail orderDetail = new OrderDetail();
			BeanUtils.copyProperties(orderDetailDTO, orderDetail);
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setStatus(OrderStatus.ORDER_STATUS_USEABLE);
			innerSaveOrderDetail(orderDetail);
			log.info("订单明细《" + orderDetail.getItemName() + "》录入完成");
		}

		// TODO 生成支付协议(调接口)

		// 日志
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_CREATE;
		String logText = "新建一份订单";
		createOrderLog(logText, operateType, order.getOrderId(),
				createorderDTO.getUserId());

		return order;
	}

	/**
	 * 检测生成订单数据
	 * 
	 * @param createorderDTO
	 */
	private void checkForCreateOrder(CreateOrderDTO createorderDTO) {
		if (createorderDTO == null) {
			throw new RuntimeException("生成订单：获取createOrderDTO异常");
		}
		// 验证无法为空的值是否取到
		if (StringUtils.isBlank(createorderDTO.getReceiverName())) {
			throw new RuntimeException("生成订单：获取收件人信息异常");
		}
		if (StringUtils.isBlank(createorderDTO.getReceiverName())) {
			throw new RuntimeException("生成订单：获取收件人姓名失败");
		}
		if (StringUtils.isBlank(createorderDTO.getReceiverPhone())) {
			throw new RuntimeException("生成订单：获取收件人电话异常");
		}
		if (createorderDTO.getReceiverCityId() == null) {
			throw new RuntimeException("生成订单：获取城市Id异常");
		}
		if (StringUtils.isBlank(createorderDTO.getReceiverDetailAddr())) {
			throw new RuntimeException("生成订单：获取收件人地址失败");
		}
		if (createorderDTO.getUserId() == null) {
			throw new RuntimeException("生成订单：获取操作员Id有误");
		}
		// 验证订单明细
		List<OrderDetailDTO> listOrderDetail = createorderDTO.getList();
		if (listOrderDetail == null || listOrderDetail.isEmpty()) {
			throw new RuntimeException("生成订单：订单明细获取异常");
		}
		if (listOrderDetail.size() == 0 || listOrderDetail.size() < 0) {
			throw new RuntimeException("生成订单：获取商品总量有误");
		}
		for (OrderDetailDTO orderDetailDTO : listOrderDetail) {
			if (orderDetailDTO.getItemCount() == null
					|| orderDetailDTO.getItemCount() <= 0) {
				throw new RuntimeException("生成订单：获取订单明细数量有误");
			}
			if (orderDetailDTO.getItemName() == null) {
				throw new RuntimeException("生成订单：获取订单明细名称异常");
			}
			if (orderDetailDTO.getItemPrice() == null
					|| orderDetailDTO.getItemPrice() <= 0) {
				throw new RuntimeException("生成订单：获取订单明细单价异常");
			}
			if (orderDetailDTO.getSkuId() == null) {
				throw new RuntimeException("生成订单：获取订单明细规格异常");
			}
		}
	}

	/**
	 * 添加商品
	 */
	public void addItem(AddItemDTO additemDto) {
		// 验证
		checkForAddItem(additemDto);
		log.info("验证通过");

		OrderDetail od = new OrderDetail();
		BeanUtils.copyProperties(additemDto, od);
		innerSaveOrderDetail(od);
		log.info("新增商品录入完成");
		// 更新订单
		flashOrder(additemDto.getOrderId());

		// TODO 将NewMoney传给支付模块 do something..........

		// 日志
		String logText = "添加了商品，商品名:" + additemDto.getItemName() + "  数量:"
				+ additemDto.getItemCount();
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_ADD;
		createOrderLog(logText, operateType, additemDto.getOrderId(),
				additemDto.getUserId());
	}

	/**
	 * 检测添加商品数据
	 * 
	 * @param additemDto
	 */
	private void checkForAddItem(AddItemDTO additemDto) {

		if (additemDto == null) {
			throw new RuntimeException("添加商品：当前对象为空");
		}
		if (StringUtils.isBlank(additemDto.getItemName())) {
			throw new RuntimeException("添加商品：商品名称为空");
		}
		if (additemDto.getItemId() == null || additemDto.getOrderId() == null
				|| additemDto.getSkuId() == null) {
			throw new RuntimeException("添加商品：ID获取有误");
		}
		if (additemDto.getItemCount() < 1) {
			throw new RuntimeException("添加商品：数量不得小于1");
		}
		if (additemDto.getItemPrice() <= 0) {
			throw new RuntimeException("添加商品：价格有误");
		}
		Order order = orderDao.findById(additemDto.getOrderId());
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT.equals((order
						.getOrderStatus()))) {
			throw new RuntimeException("添加商品：当前状态不允许修改订单");
		}
	}

	/**
	 * 修改商品数量
	 */
	public void updateCount(UpdateCountDTO upcDto) {

		// 验证
		checkForUpdateCount(upcDto);
		log.info("验证通过");

		Date date = new Date();
		OrderDetail od = orderDetailDao.findById(upcDto.getOrderDetailId());

		// 在修改前获得当前商品数量
		Integer itemCount = od.getItemCount();
		log.info("修改之前的商品数量：" + itemCount);

		od.setItemCount(upcDto.getItemCount());
		od.setOrderDetailId(upcDto.getOrderDetailId());
		innerUpdateOrderDetail(od);
		log.info("修改成功");
		// 更新订单
		flashOrder(upcDto.getOrderId());

		// TODO 在支付模块传过去newMoney do something......

		// 日志
		String logText = "将" + od.getItemName() + "的数量更改为了:"
				+ upcDto.getItemCount();
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_UPDATE;
		createOrderLog(logText, operateType, upcDto.getOrderId(),
				upcDto.getUserId());
	}

	/**
	 * 检测修改商品数量数据
	 * 
	 * @param upcDto
	 */
	private void checkForUpdateCount(UpdateCountDTO upcDto) {
		if (upcDto == null) {
			throw new RuntimeException("修改商品数量：当前对象为空");
		}
		if (upcDto.getOrderId() == null) {
			throw new RuntimeException("修改商品数量：订单Id获取异常");
		}
		if (upcDto.getOrderDetailId() == null) {
			throw new RuntimeException("修改商品数量：订单明细表ID获取异常");
		}
		if (upcDto.getItemCount() <= 0) {
			throw new RuntimeException("修改商品数量：数量输入有误");
		}
		// 判断订单状态
		Order order = orderDao.findById(upcDto.getOrderId());
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT
						.equals(order.getOrderStatus())) {
			throw new RuntimeException("修改商品数量：当前状态不允许修改订单");
		}
	}

	/**
	 * 删除商品
	 */
	public void deleteItem(Integer orderDetailId, Integer orderId,
			Integer userId) {
		// 验证
		if (orderDetailId == null) {
			throw new RuntimeException("删除商品：获取订单明细ID异常");
		}
		if (orderId == null) {
			throw new RuntimeException("删除商品：获取订单ID异常");
		}
		if (userId == null) {
			throw new RuntimeException("删除商品：获取操作员I异常D");
		}
		log.info("数据验证通过");
		// 判断订单状态
		Order order = orderDao.findById(orderId);
		if (!OrderStatus.ORDER_STATUS_INIT.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_WAILTING.equals(order
						.getOrderStatus())) {
			throw new RuntimeException("删除商品：当前状态不允许删除商品");
		}
		log.info("订单状态验证通过");

		OrderDetail orderd = orderDetailDao.findById(orderDetailId);
		orderd.setStatus(OrderStatus.ORDER_STATUS_ABANDON);
		innerUpdateOrderDetail(orderd);
		// 更新订单
		flashOrder(orderId);

		log.info("删除后的订单总价:" + order.getOrderMoney());
		// 日志
		String logText = "删除了" + orderId + "号订单的商品";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_DELETE;
		createOrderLog(logText, operateType, orderId, userId);

	}

	/**
	 * 修改订单后更新订单信息
	 * 
	 * @param orderId
	 */
	private void flashOrder(Integer orderId) {

		// 订单总额
		Double newOrderMoney = 0.0;
		// 订单总数
		Integer newOrderCount = 0;
		// 查询未废弃的订单明细
		DetachedCriteria criteria = DetachedCriteria.forClass(OrderDetail.class);
		criteria.add(Restrictions.eq(OrderDetailDao.STATUS, OrderStatus.ORDER_STATUS_USEABLE));
		criteria.add(Restrictions.eq(OrderDetailDao.ORDER_ID, orderId));
		List<OrderDetail> list =orderDetailDao.findByCritera(criteria);
//		List<OrderDetail> list = orderDetailDao.findByOrderId(orderId);
		for (OrderDetail orderDetail : list) {
			newOrderCount += orderDetail.getItemCount();
			newOrderMoney += orderDetail.getItemPrice()
					* orderDetail.getItemCount();
		}
		Order order = orderDao.findById(orderId);
		// 修改运费
		Double postMoney = postage.calculatePostage(order.getReceiverCityId(),
				newOrderCount, order.getSellerId());

		order.setOrderMoney(newOrderMoney);
		order.setPostMoney(postMoney);
		order.setOrderItemCount(newOrderCount);
		innerUpdateOrder(order);
		log.info("订单表中总价,邮费，商品总数更新完成");
	}

	/**
	 * 修改收件人地址
	 */
	public void updateReceiveAddr(UpdateReceiverAddrDTO uprd) {
		// 验证
		checkForUpdateReceiveAddr(uprd);
		log.info("订单状态验证通过");

		// 获取原来订单对象
		Integer orderId = uprd.getOrderId();
		Order order = orderDao.findById(orderId);

		// 修改邮费
		log.info("订单商品总数量：" + order.getOrderItemCount());
		Double postMoney = postage.calculatePostage(uprd.getCityId(),
				order.getOrderItemCount(), order.getSellerId());
		// 修改地址和运费
		BeanUtils.copyProperties(uprd, order);
		order.setPostMoney(postMoney);
		innerUpdateOrder(order);

		// 日志
		String logText = "修改了" + orderId + "号订单的地址，改为:"
				+ uprd.getReceiverDetailAddr();
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_UPDATE;
		createOrderLog(logText, operateType, orderId, uprd.getUserId());

	}

	/**
	 * 检测修改地址数据
	 * 
	 * @param uprd
	 */
	private void checkForUpdateReceiveAddr(UpdateReceiverAddrDTO uprd) {
		if (uprd == null) {
			throw new RuntimeException("修改收件人地址：当前对象为空");
		}
		String addr = uprd.getReceiverDetailAddr();
		if (uprd.getOrderId() == null) {
			throw new RuntimeException("修改收件人地址：Id获取异常");
		}
		if (uprd.getCityId() == null) {
			throw new RuntimeException("修改收件人地址：城市Id获取异常");
		}
		if (StringUtils.isBlank(addr)) {
			throw new RuntimeException("修改收件人地址：地址获取异常");
		}
		Order order = orderDao.findById(uprd.getOrderId());
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT
						.equals(order.getOrderStatus())) {
			throw new RuntimeException("修改收件人地址：当前状态不允许修改订单");
		}
		log.info("数据验证通过");
	}

	/**
	 * 取消订单
	 */
	public void cancelOrder(Integer orderId, Integer userId) {
		// 验证
		if (orderId == null) {
			throw new RuntimeException("取消订单：订单Id未获取");
		}
		if (userId == null) {
			throw new RuntimeException("取消订单：操作员Id未获取");
		}
		Order order = orderDao.findById(orderId);
		// 订单状态
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT
						.equals(order.getOrderStatus())) {
			throw new RuntimeException("取消订单：当前状态不允许取消订单");
		}
		log.info("验证通过");
		// 设置订单状态，进行取消订单
		order.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
		innerUpdateOrder(order);

		// 获取订单明细集合
		List<OrderDetail> list = orderDetailDao.findByOrderId(orderId);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("取消订单：获取订单明细集合异常");
		}
		for (OrderDetail orderDetail : list) {
			orderDetail.setStatus(OrderStatus.ORDER_STATUS_ABANDON);
		}
		log.info("订单明细状态修改成功");

		// XXX notify payment

		// 日志
		String logText = "取消了" + orderId + "号订单";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_CANCEL;
		createOrderLog(logText, operateType, orderId, userId);

	}

	/**
	 * 付款
	 * 
	 * @param pfo
	 */
	public void payForOrder(PayForOrderDTO pfo) {
		// 验证
		checkForpayForOrder(pfo);
		log.info("订单状态检验完毕");

		// TODO 调接口 进入付款模块 do something........

		Order order = orderDao.findById(pfo.getOrderId());
		order.setOrderStatus(OrderStatus.ORDER_STATUS_PAID);
		Date date = new Date();
		order.setPayTime(date);
		innerUpdateOrder(order);

		// 日志
		String logText = pfo.getOrderId() + "号订单已付款";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_PAY;
		createOrderLog(logText, operateType, pfo.getOrderId(), pfo.getUserId());
	}

	/**
	 * 检测支付数据
	 * 
	 * @param pfo
	 */
	private void checkForpayForOrder(PayForOrderDTO pfo) {

		if (pfo.getOrderId() == null) {
			throw new RuntimeException("付款：未获取订单ID");
		}
		if (pfo.getReceiverId() == null) {
			throw new RuntimeException("付款：未获取收件人ID");
		}
		if (pfo.getSenderId() == null) {
			throw new RuntimeException("付款：未获取卖家ID");
		}
		Order order = orderDao.findById(pfo.getOrderId());
		if (!OrderStatus.ORDER_STATUS_INIT.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_WAILTING.equals(order
						.getOrderStatus())) {
			throw new RuntimeException("付款：当前状态不允许付款");
		}
		log.info("数据验证完毕");
	}

	/**
	 * 审核
	 * 
	 * @param orderId
	 */
	public void auditOrder(Integer orderId, String salerRemark, Integer userId) {
		// 验证
		if (orderId == null) {
			throw new RuntimeException("审核：订单Id获取获取异常");
		}
		if (userId == null) {
			throw new RuntimeException("审核：操作员ID获取异常");
		}
		Order order = orderDao.findById(orderId);
		if (!OrderStatus.ORDER_STATUS_PAID.equals(order.getOrderStatus())) {
			throw new RuntimeException("审核：当前状态不允许审核");
		}
		order.setSalerRemark(salerRemark);
		order.setOrderStatus(OrderStatus.ORDER_STATUS_ACCEPT);
		Date date = new Date();
		innerUpdateOrder(order);

		// 日志
		String logText = orderId + "号订单审核通过";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_AUDIT;
		createOrderLog(logText, operateType, orderId, userId);
	}

	/**
	 * 分配仓库
	 * 
	 * @param assginstore
	 */
	public void assignStore(AssignStoreDTO assginstore) {
		// 验证
		if (assginstore.getStoreId() == null) {
			throw new RuntimeException("分配仓库：获取仓库Id异常");
		}
		if (assginstore.getOrderId() == null) {
			throw new RuntimeException("分配仓库：获取订单Id异常");
		}
		if (assginstore.getOperatorId() == null) {
			throw new RuntimeException("分配仓库：获取用户Id异常");
		}
		Order order = orderDao.findById(assginstore.getOrderId());
		// 检查订单状态
		if (!OrderStatus.ORDER_STATUS_PAID.equals(order.getOrderStatus())
				&&!OrderStatus.ORDER_STATUS_ACCEPT.equals(order.getOrderStatus())) {
			throw new RuntimeException("分配仓库：当前状态不得进行分配仓库");
		}

		// TODO 调分配仓库接口 do something.........

		order.setOrderStatus(OrderStatus.ORDER_STATUS_ASSIGNED);
		Date date = new Date();
		order.setArrangeTime(date);
		innerUpdateOrder(order);
		log.info("修改订单状态成功:" + order.getOrderStatus());

		// 日志
		String logText = "将" + assginstore.getOrderId() + "号订单分配到"
				+ assginstore.getStoreId() + "号仓库";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_ASSIGN;
		createOrderLog(logText, operateType, assginstore.getOrderId(),
				assginstore.getOperatorId());
	}

	/**
	 * 退款
	 * 
	 * @param refunddto
	 */
	public void refund(RefundDTO ref) {

		checkForRefund(ref);
		log.info("测试通过");
		Order order = orderDao.findById(ref.getOrderId());
		// 订单状态是已分配进行退款
		if (OrderStatus.ORDER_STATUS_ASSIGNED.equals(order.getOrderStatus())) {
			// TODO 调用配发接口 通知无需发货
			// TODO 调用退款接口
			refundSuccess(ref);
			// 订单状态是已受理,付款,已发货,确认收货 进行退款
		} else {
			// TODO 调用退款接口
			refundSuccess(ref);
		}
	}

	/**
	 * 检测退款数据
	 * 
	 * @param ref
	 */
	private void checkForRefund(RefundDTO ref) {
		if (ref.getOrderId() == null) {
			throw new RuntimeException("退款：获取对象异常");
		}
		if (ref.getUserId() == null) {
			throw new RuntimeException("退款：获取用户ID异常");
		}
		if (ref.getRefundMoney() == null) {
			throw new RuntimeException("退款：订单总额获取异常");
		}
		if (!ref.getAgree() || ref.getAgree() == null) {
			throw new RuntimeException("退款：卖家拒绝退款");
		}
		Order order = orderDao.findById(ref.getOrderId());
		if (ref.getRefundMoney() < 0
				|| ref.getRefundMoney() > order.getOrderMoney()) {
			throw new RuntimeException("退款：退款金额有误");
		}
		// 检查订单状态
		String orderStatus = order.getOrderStatus();
		if (!OrderStatus.ORDER_STATUS_ACCEPT.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_ASSIGNED.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_PAID.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_SENDED.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_RECEIVED.equals(orderStatus)) {
			throw new RuntimeException("退款：当前状态无法申请退款");
		}
	}

	/**
	 * 退款成功后更新订单
	 * 
	 * @param ref
	 */
	private void refundSuccess(RefundDTO ref) {

		Order order = orderDao.findById(ref.getOrderId());
		Date date = new Date();
		order.setOrderMoney(order.getOrderMoney() - ref.getRefundMoney());
		innerUpdateOrder(order);

		// 日志
		String logText = ref.getOrderId() + "号订单进行了退款";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_REFUND;

		createOrderLog(logText, operateType, ref.getOrderId(), ref.getUserId());
	}

	/**
	 * 关联新增订单时间，版本
	 * 
	 * @param order
	 */
	private void innerSaveOrder(Order order) {
		if (order == null) {
			throw new RuntimeException("order对象不能为空");
		}
		Date date = new Date();
		if (order.getGmtCreate() == null) {
			order.setGmtCreate(date);
		}
		if (order.getGmtModify() == null) {
			order.setGmtModify(date);
		}
		order.setVersion(1);
		orderDao.save(order);
	}

	/**
	 * 关联新增订单明细时间，版本
	 * 
	 * @param orderDetail
	 */
	private void innerSaveOrderDetail(OrderDetail orderDetail) {
		if (orderDetail == null) {
			throw new RuntimeException("orderDetail对象不能为空");
		}
		Date date = new Date();
		if (orderDetail.getGmtCreate() == null) {
			orderDetail.setGmtCreate(date);
		}
		if (orderDetail.getGmtModify() == null) {
			orderDetail.setGmtModify(date);
		}
		orderDetail.setVersion(1);
		orderDetailDao.save(orderDetail);
	}

	/**
	 * 关联修改订单时间，版本
	 * 
	 * @param order
	 */
	private void innerUpdateOrder(Order order) {
		if (order == null) {
			throw new RuntimeException("order对象不能为空");
		}
		Date date = new Date();
		order.setGmtModify(date);
		order.setVersion(order.getVersion() + 1);
		orderDao.update(order);
	}

	/**
	 * 关联修改订单明细时间，版本
	 */
	private void innerUpdateOrderDetail(OrderDetail orderDetail) {
		if (orderDetail == null) {
			throw new RuntimeException("orderDetail对象不能为空");
		}
		Date date = new Date();
		orderDetail.setGmtModify(date);
		orderDetail.setVersion(orderDetail.getVersion() + 1);
		orderDetailDao.update(orderDetail);
	}

	/**
	 * 生成订单操作日志
	 * 
	 * @param col
	 */
	private void createOrderLog(String logText, String operateType,
			Integer orderId, Integer operatorId) {
		// 日志
		CreateOrderLogDTO col = new CreateOrderLogDTO();
		col.setOperateDetail(logText);
		col.setOperateType(operateType);
		col.setOperatorId(operatorId);
		col.setOrderId(orderId);

		OrderLog orderlog = new OrderLog();
		BeanUtils.copyProperties(col, orderlog);
		Date date = new Date();
		orderlog.setGmtCreate(date);
		orderlog.setGmtModify(date);
		orderlog.setVersion(1);
		orderLogDao.save(orderlog);
		log.info("日志添加成功");
	}

	public void setOrderdao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setOrderdetaildao(OrderDetailDao orderDetailDao) {
		this.orderDetailDao = orderDetailDao;
	}

	public void setOrderLogdao(OrderLogDao orderLogDao) {
		this.orderLogDao = orderLogDao;
	}

	public void setPostage(PostageBO postage) {
		this.postage = postage;
	}
}
