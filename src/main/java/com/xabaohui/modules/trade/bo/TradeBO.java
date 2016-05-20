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
 * ����
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
	 * ���ɶ���
	 */
	public Order createOrder(CreateOrderDTO createorderDTO) {
		// �������
		checkForCreateOrder(createorderDTO);
		log.info("���ݼ������");

		// ��Ʒ�������� �� ��Ʒ�ܼۼ���
		Integer orderItemCount = 0;
		Double orderMoney = 0.0;
		for (OrderDetailDTO orderDetailDTO : createorderDTO.getList()) {
			orderItemCount += orderDetailDTO.getItemCount();
			orderMoney += orderDetailDTO.getItemPrice()
					* orderDetailDTO.getItemCount();
		}
		log.info("��Ʒ����=" + orderItemCount + "��Ʒ�ܼ�=" + orderMoney);

		// ��ʼ�˷ѵĳ�ʼ��;
		Double postFee = postage.calculatePostage(
				createorderDTO.getReceiverCityId(), orderItemCount,
				createorderDTO.getSellerId());
		log.info("��ʼ�����˷�=" + postFee);

		// ���ɶ�������
		log.info("��ʼ���ɶ�������");
		Order order = new Order();
		BeanUtils.copyProperties(createorderDTO, order);
		order.setOrderItemCount(orderItemCount);
		order.setOrderMoney(orderMoney);
		order.setOrderStatus(OrderStatus.ORDER_STATUS_INIT);
		order.setPostMoney(postFee);
		innerSaveOrder(order);
		log.info("¼�����");

		// ���ɶ�����ϸ����
		for (OrderDetailDTO orderDetailDTO : createorderDTO.getList()) {
			OrderDetail orderDetail = new OrderDetail();
			BeanUtils.copyProperties(orderDetailDTO, orderDetail);
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setStatus(OrderStatus.ORDER_STATUS_USEABLE);
			innerSaveOrderDetail(orderDetail);
			log.info("������ϸ��" + orderDetail.getItemName() + "��¼�����");
		}

		// TODO ����֧��Э��(���ӿ�)

		// ��־
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_CREATE;
		String logText = "�½�һ�ݶ���";
		createOrderLog(logText, operateType, order.getOrderId(),
				createorderDTO.getUserId());

		return order;
	}

	/**
	 * ������ɶ�������
	 * 
	 * @param createorderDTO
	 */
	private void checkForCreateOrder(CreateOrderDTO createorderDTO) {
		if (createorderDTO == null) {
			throw new RuntimeException("���ɶ�������ȡcreateOrderDTO�쳣");
		}
		// ��֤�޷�Ϊ�յ�ֵ�Ƿ�ȡ��
		if (StringUtils.isBlank(createorderDTO.getReceiverName())) {
			throw new RuntimeException("���ɶ�������ȡ�ռ�����Ϣ�쳣");
		}
		if (StringUtils.isBlank(createorderDTO.getReceiverName())) {
			throw new RuntimeException("���ɶ�������ȡ�ռ�������ʧ��");
		}
		if (StringUtils.isBlank(createorderDTO.getReceiverPhone())) {
			throw new RuntimeException("���ɶ�������ȡ�ռ��˵绰�쳣");
		}
		if (createorderDTO.getReceiverCityId() == null) {
			throw new RuntimeException("���ɶ�������ȡ����Id�쳣");
		}
		if (StringUtils.isBlank(createorderDTO.getReceiverDetailAddr())) {
			throw new RuntimeException("���ɶ�������ȡ�ռ��˵�ַʧ��");
		}
		if (createorderDTO.getUserId() == null) {
			throw new RuntimeException("���ɶ�������ȡ����ԱId����");
		}
		// ��֤������ϸ
		List<OrderDetailDTO> listOrderDetail = createorderDTO.getList();
		if (listOrderDetail == null || listOrderDetail.isEmpty()) {
			throw new RuntimeException("���ɶ�����������ϸ��ȡ�쳣");
		}
		if (listOrderDetail.size() == 0 || listOrderDetail.size() < 0) {
			throw new RuntimeException("���ɶ�������ȡ��Ʒ��������");
		}
		for (OrderDetailDTO orderDetailDTO : listOrderDetail) {
			if (orderDetailDTO.getItemCount() == null
					|| orderDetailDTO.getItemCount() <= 0) {
				throw new RuntimeException("���ɶ�������ȡ������ϸ��������");
			}
			if (orderDetailDTO.getItemName() == null) {
				throw new RuntimeException("���ɶ�������ȡ������ϸ�����쳣");
			}
			if (orderDetailDTO.getItemPrice() == null
					|| orderDetailDTO.getItemPrice() <= 0) {
				throw new RuntimeException("���ɶ�������ȡ������ϸ�����쳣");
			}
			if (orderDetailDTO.getSkuId() == null) {
				throw new RuntimeException("���ɶ�������ȡ������ϸ����쳣");
			}
		}
	}

	/**
	 * �����Ʒ
	 */
	public void addItem(AddItemDTO additemDto) {
		// ��֤
		checkForAddItem(additemDto);
		log.info("��֤ͨ��");

		OrderDetail od = new OrderDetail();
		BeanUtils.copyProperties(additemDto, od);
		innerSaveOrderDetail(od);
		log.info("������Ʒ¼�����");
		// ���¶���
		flashOrder(additemDto.getOrderId());

		// TODO ��NewMoney����֧��ģ�� do something..........

		// ��־
		String logText = "�������Ʒ����Ʒ��:" + additemDto.getItemName() + "  ����:"
				+ additemDto.getItemCount();
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_ADD;
		createOrderLog(logText, operateType, additemDto.getOrderId(),
				additemDto.getUserId());
	}

	/**
	 * ��������Ʒ����
	 * 
	 * @param additemDto
	 */
	private void checkForAddItem(AddItemDTO additemDto) {

		if (additemDto == null) {
			throw new RuntimeException("�����Ʒ����ǰ����Ϊ��");
		}
		if (StringUtils.isBlank(additemDto.getItemName())) {
			throw new RuntimeException("�����Ʒ����Ʒ����Ϊ��");
		}
		if (additemDto.getItemId() == null || additemDto.getOrderId() == null
				|| additemDto.getSkuId() == null) {
			throw new RuntimeException("�����Ʒ��ID��ȡ����");
		}
		if (additemDto.getItemCount() < 1) {
			throw new RuntimeException("�����Ʒ����������С��1");
		}
		if (additemDto.getItemPrice() <= 0) {
			throw new RuntimeException("�����Ʒ���۸�����");
		}
		Order order = orderDao.findById(additemDto.getOrderId());
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT.equals((order
						.getOrderStatus()))) {
			throw new RuntimeException("�����Ʒ����ǰ״̬�������޸Ķ���");
		}
	}

	/**
	 * �޸���Ʒ����
	 */
	public void updateCount(UpdateCountDTO upcDto) {

		// ��֤
		checkForUpdateCount(upcDto);
		log.info("��֤ͨ��");

		Date date = new Date();
		OrderDetail od = orderDetailDao.findById(upcDto.getOrderDetailId());

		// ���޸�ǰ��õ�ǰ��Ʒ����
		Integer itemCount = od.getItemCount();
		log.info("�޸�֮ǰ����Ʒ������" + itemCount);

		od.setItemCount(upcDto.getItemCount());
		od.setOrderDetailId(upcDto.getOrderDetailId());
		innerUpdateOrderDetail(od);
		log.info("�޸ĳɹ�");
		// ���¶���
		flashOrder(upcDto.getOrderId());

		// TODO ��֧��ģ�鴫��ȥnewMoney do something......

		// ��־
		String logText = "��" + od.getItemName() + "����������Ϊ��:"
				+ upcDto.getItemCount();
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_UPDATE;
		createOrderLog(logText, operateType, upcDto.getOrderId(),
				upcDto.getUserId());
	}

	/**
	 * ����޸���Ʒ��������
	 * 
	 * @param upcDto
	 */
	private void checkForUpdateCount(UpdateCountDTO upcDto) {
		if (upcDto == null) {
			throw new RuntimeException("�޸���Ʒ��������ǰ����Ϊ��");
		}
		if (upcDto.getOrderId() == null) {
			throw new RuntimeException("�޸���Ʒ����������Id��ȡ�쳣");
		}
		if (upcDto.getOrderDetailId() == null) {
			throw new RuntimeException("�޸���Ʒ������������ϸ��ID��ȡ�쳣");
		}
		if (upcDto.getItemCount() <= 0) {
			throw new RuntimeException("�޸���Ʒ������������������");
		}
		// �ж϶���״̬
		Order order = orderDao.findById(upcDto.getOrderId());
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT
						.equals(order.getOrderStatus())) {
			throw new RuntimeException("�޸���Ʒ��������ǰ״̬�������޸Ķ���");
		}
	}

	/**
	 * ɾ����Ʒ
	 */
	public void deleteItem(Integer orderDetailId, Integer orderId,
			Integer userId) {
		// ��֤
		if (orderDetailId == null) {
			throw new RuntimeException("ɾ����Ʒ����ȡ������ϸID�쳣");
		}
		if (orderId == null) {
			throw new RuntimeException("ɾ����Ʒ����ȡ����ID�쳣");
		}
		if (userId == null) {
			throw new RuntimeException("ɾ����Ʒ����ȡ����ԱI�쳣D");
		}
		log.info("������֤ͨ��");
		// �ж϶���״̬
		Order order = orderDao.findById(orderId);
		if (!OrderStatus.ORDER_STATUS_INIT.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_WAILTING.equals(order
						.getOrderStatus())) {
			throw new RuntimeException("ɾ����Ʒ����ǰ״̬������ɾ����Ʒ");
		}
		log.info("����״̬��֤ͨ��");

		OrderDetail orderd = orderDetailDao.findById(orderDetailId);
		orderd.setStatus(OrderStatus.ORDER_STATUS_ABANDON);
		innerUpdateOrderDetail(orderd);
		// ���¶���
		flashOrder(orderId);

		log.info("ɾ����Ķ����ܼ�:" + order.getOrderMoney());
		// ��־
		String logText = "ɾ����" + orderId + "�Ŷ�������Ʒ";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_DELETE;
		createOrderLog(logText, operateType, orderId, userId);

	}

	/**
	 * �޸Ķ�������¶�����Ϣ
	 * 
	 * @param orderId
	 */
	private void flashOrder(Integer orderId) {

		// �����ܶ�
		Double newOrderMoney = 0.0;
		// ��������
		Integer newOrderCount = 0;
		// ��ѯδ�����Ķ�����ϸ
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
		// �޸��˷�
		Double postMoney = postage.calculatePostage(order.getReceiverCityId(),
				newOrderCount, order.getSellerId());

		order.setOrderMoney(newOrderMoney);
		order.setPostMoney(postMoney);
		order.setOrderItemCount(newOrderCount);
		innerUpdateOrder(order);
		log.info("���������ܼ�,�ʷѣ���Ʒ�����������");
	}

	/**
	 * �޸��ռ��˵�ַ
	 */
	public void updateReceiveAddr(UpdateReceiverAddrDTO uprd) {
		// ��֤
		checkForUpdateReceiveAddr(uprd);
		log.info("����״̬��֤ͨ��");

		// ��ȡԭ����������
		Integer orderId = uprd.getOrderId();
		Order order = orderDao.findById(orderId);

		// �޸��ʷ�
		log.info("������Ʒ��������" + order.getOrderItemCount());
		Double postMoney = postage.calculatePostage(uprd.getCityId(),
				order.getOrderItemCount(), order.getSellerId());
		// �޸ĵ�ַ���˷�
		BeanUtils.copyProperties(uprd, order);
		order.setPostMoney(postMoney);
		innerUpdateOrder(order);

		// ��־
		String logText = "�޸���" + orderId + "�Ŷ����ĵ�ַ����Ϊ:"
				+ uprd.getReceiverDetailAddr();
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_UPDATE;
		createOrderLog(logText, operateType, orderId, uprd.getUserId());

	}

	/**
	 * ����޸ĵ�ַ����
	 * 
	 * @param uprd
	 */
	private void checkForUpdateReceiveAddr(UpdateReceiverAddrDTO uprd) {
		if (uprd == null) {
			throw new RuntimeException("�޸��ռ��˵�ַ����ǰ����Ϊ��");
		}
		String addr = uprd.getReceiverDetailAddr();
		if (uprd.getOrderId() == null) {
			throw new RuntimeException("�޸��ռ��˵�ַ��Id��ȡ�쳣");
		}
		if (uprd.getCityId() == null) {
			throw new RuntimeException("�޸��ռ��˵�ַ������Id��ȡ�쳣");
		}
		if (StringUtils.isBlank(addr)) {
			throw new RuntimeException("�޸��ռ��˵�ַ����ַ��ȡ�쳣");
		}
		Order order = orderDao.findById(uprd.getOrderId());
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT
						.equals(order.getOrderStatus())) {
			throw new RuntimeException("�޸��ռ��˵�ַ����ǰ״̬�������޸Ķ���");
		}
		log.info("������֤ͨ��");
	}

	/**
	 * ȡ������
	 */
	public void cancelOrder(Integer orderId, Integer userId) {
		// ��֤
		if (orderId == null) {
			throw new RuntimeException("ȡ������������Idδ��ȡ");
		}
		if (userId == null) {
			throw new RuntimeException("ȡ������������ԱIdδ��ȡ");
		}
		Order order = orderDao.findById(orderId);
		// ����״̬
		if (!OrderStatus.ORDER_STATUS_WAILTING.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_INIT
						.equals(order.getOrderStatus())) {
			throw new RuntimeException("ȡ����������ǰ״̬������ȡ������");
		}
		log.info("��֤ͨ��");
		// ���ö���״̬������ȡ������
		order.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
		innerUpdateOrder(order);

		// ��ȡ������ϸ����
		List<OrderDetail> list = orderDetailDao.findByOrderId(orderId);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("ȡ����������ȡ������ϸ�����쳣");
		}
		for (OrderDetail orderDetail : list) {
			orderDetail.setStatus(OrderStatus.ORDER_STATUS_ABANDON);
		}
		log.info("������ϸ״̬�޸ĳɹ�");

		// XXX notify payment

		// ��־
		String logText = "ȡ����" + orderId + "�Ŷ���";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_CANCEL;
		createOrderLog(logText, operateType, orderId, userId);

	}

	/**
	 * ����
	 * 
	 * @param pfo
	 */
	public void payForOrder(PayForOrderDTO pfo) {
		// ��֤
		checkForpayForOrder(pfo);
		log.info("����״̬�������");

		// TODO ���ӿ� ���븶��ģ�� do something........

		Order order = orderDao.findById(pfo.getOrderId());
		order.setOrderStatus(OrderStatus.ORDER_STATUS_PAID);
		Date date = new Date();
		order.setPayTime(date);
		innerUpdateOrder(order);

		// ��־
		String logText = pfo.getOrderId() + "�Ŷ����Ѹ���";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_PAY;
		createOrderLog(logText, operateType, pfo.getOrderId(), pfo.getUserId());
	}

	/**
	 * ���֧������
	 * 
	 * @param pfo
	 */
	private void checkForpayForOrder(PayForOrderDTO pfo) {

		if (pfo.getOrderId() == null) {
			throw new RuntimeException("���δ��ȡ����ID");
		}
		if (pfo.getReceiverId() == null) {
			throw new RuntimeException("���δ��ȡ�ռ���ID");
		}
		if (pfo.getSenderId() == null) {
			throw new RuntimeException("���δ��ȡ����ID");
		}
		Order order = orderDao.findById(pfo.getOrderId());
		if (!OrderStatus.ORDER_STATUS_INIT.equals(order.getOrderStatus())
				&& !OrderStatus.ORDER_STATUS_WAILTING.equals(order
						.getOrderStatus())) {
			throw new RuntimeException("�����ǰ״̬��������");
		}
		log.info("������֤���");
	}

	/**
	 * ���
	 * 
	 * @param orderId
	 */
	public void auditOrder(Integer orderId, String salerRemark, Integer userId) {
		// ��֤
		if (orderId == null) {
			throw new RuntimeException("��ˣ�����Id��ȡ��ȡ�쳣");
		}
		if (userId == null) {
			throw new RuntimeException("��ˣ�����ԱID��ȡ�쳣");
		}
		Order order = orderDao.findById(orderId);
		if (!OrderStatus.ORDER_STATUS_PAID.equals(order.getOrderStatus())) {
			throw new RuntimeException("��ˣ���ǰ״̬���������");
		}
		order.setSalerRemark(salerRemark);
		order.setOrderStatus(OrderStatus.ORDER_STATUS_ACCEPT);
		Date date = new Date();
		innerUpdateOrder(order);

		// ��־
		String logText = orderId + "�Ŷ������ͨ��";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_AUDIT;
		createOrderLog(logText, operateType, orderId, userId);
	}

	/**
	 * ����ֿ�
	 * 
	 * @param assginstore
	 */
	public void assignStore(AssignStoreDTO assginstore) {
		// ��֤
		if (assginstore.getStoreId() == null) {
			throw new RuntimeException("����ֿ⣺��ȡ�ֿ�Id�쳣");
		}
		if (assginstore.getOrderId() == null) {
			throw new RuntimeException("����ֿ⣺��ȡ����Id�쳣");
		}
		if (assginstore.getOperatorId() == null) {
			throw new RuntimeException("����ֿ⣺��ȡ�û�Id�쳣");
		}
		Order order = orderDao.findById(assginstore.getOrderId());
		// ��鶩��״̬
		if (!OrderStatus.ORDER_STATUS_PAID.equals(order.getOrderStatus())
				&&!OrderStatus.ORDER_STATUS_ACCEPT.equals(order.getOrderStatus())) {
			throw new RuntimeException("����ֿ⣺��ǰ״̬���ý��з���ֿ�");
		}

		// TODO ������ֿ�ӿ� do something.........

		order.setOrderStatus(OrderStatus.ORDER_STATUS_ASSIGNED);
		Date date = new Date();
		order.setArrangeTime(date);
		innerUpdateOrder(order);
		log.info("�޸Ķ���״̬�ɹ�:" + order.getOrderStatus());

		// ��־
		String logText = "��" + assginstore.getOrderId() + "�Ŷ������䵽"
				+ assginstore.getStoreId() + "�Ųֿ�";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_ASSIGN;
		createOrderLog(logText, operateType, assginstore.getOrderId(),
				assginstore.getOperatorId());
	}

	/**
	 * �˿�
	 * 
	 * @param refunddto
	 */
	public void refund(RefundDTO ref) {

		checkForRefund(ref);
		log.info("����ͨ��");
		Order order = orderDao.findById(ref.getOrderId());
		// ����״̬���ѷ�������˿�
		if (OrderStatus.ORDER_STATUS_ASSIGNED.equals(order.getOrderStatus())) {
			// TODO �����䷢�ӿ� ֪ͨ���跢��
			// TODO �����˿�ӿ�
			refundSuccess(ref);
			// ����״̬��������,����,�ѷ���,ȷ���ջ� �����˿�
		} else {
			// TODO �����˿�ӿ�
			refundSuccess(ref);
		}
	}

	/**
	 * ����˿�����
	 * 
	 * @param ref
	 */
	private void checkForRefund(RefundDTO ref) {
		if (ref.getOrderId() == null) {
			throw new RuntimeException("�˿��ȡ�����쳣");
		}
		if (ref.getUserId() == null) {
			throw new RuntimeException("�˿��ȡ�û�ID�쳣");
		}
		if (ref.getRefundMoney() == null) {
			throw new RuntimeException("�˿�����ܶ��ȡ�쳣");
		}
		if (!ref.getAgree() || ref.getAgree() == null) {
			throw new RuntimeException("�˿���Ҿܾ��˿�");
		}
		Order order = orderDao.findById(ref.getOrderId());
		if (ref.getRefundMoney() < 0
				|| ref.getRefundMoney() > order.getOrderMoney()) {
			throw new RuntimeException("�˿�˿�������");
		}
		// ��鶩��״̬
		String orderStatus = order.getOrderStatus();
		if (!OrderStatus.ORDER_STATUS_ACCEPT.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_ASSIGNED.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_PAID.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_SENDED.equals(orderStatus)
				&& !OrderStatus.ORDER_STATUS_RECEIVED.equals(orderStatus)) {
			throw new RuntimeException("�˿��ǰ״̬�޷������˿�");
		}
	}

	/**
	 * �˿�ɹ�����¶���
	 * 
	 * @param ref
	 */
	private void refundSuccess(RefundDTO ref) {

		Order order = orderDao.findById(ref.getOrderId());
		Date date = new Date();
		order.setOrderMoney(order.getOrderMoney() - ref.getRefundMoney());
		innerUpdateOrder(order);

		// ��־
		String logText = ref.getOrderId() + "�Ŷ����������˿�";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_REFUND;

		createOrderLog(logText, operateType, ref.getOrderId(), ref.getUserId());
	}

	/**
	 * ������������ʱ�䣬�汾
	 * 
	 * @param order
	 */
	private void innerSaveOrder(Order order) {
		if (order == null) {
			throw new RuntimeException("order������Ϊ��");
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
	 * ��������������ϸʱ�䣬�汾
	 * 
	 * @param orderDetail
	 */
	private void innerSaveOrderDetail(OrderDetail orderDetail) {
		if (orderDetail == null) {
			throw new RuntimeException("orderDetail������Ϊ��");
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
	 * �����޸Ķ���ʱ�䣬�汾
	 * 
	 * @param order
	 */
	private void innerUpdateOrder(Order order) {
		if (order == null) {
			throw new RuntimeException("order������Ϊ��");
		}
		Date date = new Date();
		order.setGmtModify(date);
		order.setVersion(order.getVersion() + 1);
		orderDao.update(order);
	}

	/**
	 * �����޸Ķ�����ϸʱ�䣬�汾
	 */
	private void innerUpdateOrderDetail(OrderDetail orderDetail) {
		if (orderDetail == null) {
			throw new RuntimeException("orderDetail������Ϊ��");
		}
		Date date = new Date();
		orderDetail.setGmtModify(date);
		orderDetail.setVersion(orderDetail.getVersion() + 1);
		orderDetailDao.update(orderDetail);
	}

	/**
	 * ���ɶ���������־
	 * 
	 * @param col
	 */
	private void createOrderLog(String logText, String operateType,
			Integer orderId, Integer operatorId) {
		// ��־
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
		log.info("��־��ӳɹ�");
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
