package com.xabaohui.modules.trade.bo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderDetail;
import com.xabaohui.modules.trade.bean.OrderStatus;
import com.xabaohui.modules.trade.bo.TradeBO;
import com.xabaohui.modules.trade.dao.OrderDao;
import com.xabaohui.modules.trade.dao.OrderDetailDao;
import com.xabaohui.modules.trade.dto.AddItemDTO;
import com.xabaohui.modules.trade.dto.AssignStoreDTO;
import com.xabaohui.modules.trade.dto.CreateOrderDTO;
import com.xabaohui.modules.trade.dto.CreatePostageDTO;
import com.xabaohui.modules.trade.dto.OrderDetailDTO;
import com.xabaohui.modules.trade.dto.PayForOrderDTO;
import com.xabaohui.modules.trade.dto.PostageDetailDTO;
import com.xabaohui.modules.trade.dto.RefundDTO;
import com.xabaohui.modules.trade.dto.UpdateCountDTO;
import com.xabaohui.modules.trade.dto.UpdateReceiverAddrDTO;

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:applicationContext.xml")
public class TradeBOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource
	private TradeBO trade;
	@Resource
	private OrderDetailDao orderDetailDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private PostageBO postage;

	/**
	 * 生成订单数据测试
	 */
	@Test(expected = RuntimeException.class)
	public void testCreateOrderDTODataCheck() {
		CreateOrderDTO co = null;
		trade.createOrder(co);
	}

	/**
	 * 生成订单
	 */
	@Test
	public void testCreateOrder() {

		CreateOrderDTO co = createOrder();
		try {
			Order order = trade.createOrder(co);
			Order dbOrder = this.orderDao.findById(order.getOrderId());
			Assert.assertNotNull(dbOrder);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("创建订单测试失败，不应该出现异常");
		}
	}

	/**
	 * 添加商品
	 */
	@Test
	public void testAddItem() {

		CreateOrderDTO co = createOrder();
		// 获取订单对象
		Order order = trade.createOrder(co);
		// 修改之前订单总价
		Double OldOrderMoney = order.getOrderMoney();

		AddItemDTO additemdto = new AddItemDTO();
		additemdto.setItemCount(2);
		additemdto.setItemId(1);
		additemdto.setItemName("牛奶");
		additemdto.setItemPrice(2.5);
		additemdto.setOrderId(order.getOrderId());
		additemdto.setSkuId(1);
		additemdto.setStatus(OrderStatus.ORDER_STATUS_USEABLE);
		additemdto.setUserId(1);
		try {
			trade.addItem(additemdto);
			Order dbOrder = orderDao.findById(additemdto.getOrderId());
			// 检测修改后的总价是否一致
			Assert.assertEquals(
					(Double) (OldOrderMoney + additemdto.getItemCount()
							* additemdto.getItemPrice()),
					order.getOrderMoney());
		} catch (Exception e) {
			e.getMessage();
			Assert.fail();
		}

	}

	/**
	 * 修改商品数量中数据测试
	 */
	@Test(expected = RuntimeException.class)
	public void testUpdateCountDataCheck() {
		UpdateCountDTO upc = new UpdateCountDTO();
		trade.updateCount(upc);
	}

	/**
	 * 修改商品数量测试
	 */
	@Test
	public void testUpdateCount() {

		CreateOrderDTO co = createOrder();
		// 获得订单对象
		Order order = trade.createOrder(co);

		// 获取当前订单中需要操作的订单明细对象
		OrderDetail orderDetail = new OrderDetail();
		BeanUtils.copyProperties(co.getList().get(0), orderDetail);
		orderDetail.setOrderId(order.getOrderId());
		List<OrderDetail> listOrderDtail = orderDetailDao
				.findByExample(orderDetail);
		// 验证
		if (listOrderDtail == null || listOrderDtail.isEmpty()) {
			throw new RuntimeException("修改商品数量验证：未获取订单明细对象");
		}
		if (listOrderDtail.size() > 1) {
			throw new RuntimeException("修改商品数量验证：获取订单对象明细不唯一");
		}
		OrderDetail orderDetailFind = listOrderDtail.get(0);

		// 获取订单中除修改商品的数量之外的总数
		Integer oldOrderItemCount = order.getOrderItemCount()
				- orderDetailFind.getItemCount();

		UpdateCountDTO upc = new UpdateCountDTO();
		upc.setItemCount(5);
		upc.setOrderDetailId(orderDetailFind.getOrderDetailId());
		upc.setOrderId(order.getOrderId());
		upc.setUserId(1);
		try {
			// 执行测试
			trade.updateCount(upc);
			Order dbOrder = orderDao.findById(upc.getOrderId());
			Assert.assertEquals((Integer) (oldOrderItemCount + orderDetailFind
					.getItemCount()), dbOrder.getOrderItemCount());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 删除商品测试(观察订单明细中状态变化)
	 */
	@Test
	public void testDeleteItem() {

		CreateOrderDTO co = createOrder(); // 获得订单对象
		Order order = trade.createOrder(co);

		//获取订单的商品总数
		Integer oldOrderItemCount = order.getOrderItemCount();
		// 订单中的商品总价
		Double oldOrderMoney = order.getOrderMoney();
		OrderDetail orderDetail = new OrderDetail();
		BeanUtils.copyProperties(co.getList().get(0), orderDetail);
		orderDetail.setOrderId(order.getOrderId());
		List<OrderDetail> listOrderDtail = orderDetailDao
				.findByExample(orderDetail);
		// 验证
		if (listOrderDtail == null || listOrderDtail.isEmpty()) {
			throw new RuntimeException("删除商品测试验证：未获取订单明细对象");
		}
		if (listOrderDtail.size() > 1) {
			throw new RuntimeException("删除商品测试验证：获取订单对象明细不唯一");
		}

		// 获取当前订单中需要操作的订单明细对象
		OrderDetail orderDetailFind = listOrderDtail.get(0);
		try {
			// 执行测试
			trade.deleteItem(orderDetailFind.getOrderDetailId(),
					order.getOrderId(), 2);
			// 比较结果
			Order resultOrder = this.orderDao.findById(order.getOrderId());
			Assert.assertEquals((Integer)(oldOrderItemCount-orderDetailFind.getItemCount()), resultOrder.getOrderItemCount());
			Double expectedMoney = (Double) (oldOrderMoney - orderDetailFind.getItemCount()
					* orderDetailFind.getItemPrice());
			Assert.assertEquals(expectedMoney , resultOrder.getOrderMoney());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 修改收件人地址测试
	 */
	@Test
	public void testUpdateReceiveAddr() {

		CreateOrderDTO co = createOrder(); // 获得订单对象
		Order order = trade.createOrder(co);

		UpdateReceiverAddrDTO updr = new UpdateReceiverAddrDTO();
		updr.setUserId(3);
		updr.setReceiverDetailAddr("修改过的地址");
		updr.setCityId(1);
		updr.setOrderId(order.getOrderId());
		try {
			trade.updateReceiveAddr(updr);
			Order dbOrder = orderDao.findById(updr.getOrderId());
			Assert.assertEquals("修改过的地址", dbOrder.getReceiverDetailAddr());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 取消订单测试
	 */
	@Test
	public void testCancelOrder() {

		CreateOrderDTO co = createOrder(); // 获得订单对象
		Order order = trade.createOrder(co);

		try {
			trade.cancelOrder(order.getOrderId(), 5);
			Order dbOrder = orderDao.findById(order.getOrderId());
			Assert.assertEquals(OrderStatus.ORDER_STATUS_CANCEL,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
	}

	/**
	 * 付款测试
	 */
	@Test
	public void testPayForOrder() {

		CreateOrderDTO co = createOrder(); // 获得订单对象
		Order order = trade.createOrder(co);

		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		try {
			trade.payForOrder(pfo);
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较数据
			Assert.assertEquals(OrderStatus.ORDER_STATUS_PAID,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 审核测试 ,满足订单状态
	 */
	@Test
	public void testAuditOrder() {

		CreateOrderDTO co = createOrder(); // 获得订单对象
		Order order = trade.createOrder(co);

		// 支付
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);

		try {
			String salerRemark = "已处理";
			trade.auditOrder(order.getOrderId(), salerRemark, 12);
			Order dbOrder = orderDao.findById(order.getOrderId());
			Assert.assertEquals(OrderStatus.ORDER_STATUS_ACCEPT,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 审核测试 ,未付款
	 */
	@Test(expected = RuntimeException.class)
	public void testAuditOrderFail() {

		CreateOrderDTO co = createOrder(); // 获得订单对象
		Order order = trade.createOrder(co);

		String salerRemark = "已处理";
		trade.auditOrder(order.getOrderId(), salerRemark, 12);
		Order dbOrder = orderDao.findById(order.getOrderId());
		Assert.assertEquals(OrderStatus.ORDER_STATUS_ACCEPT,
				dbOrder.getOrderStatus());
	}

	/**
	 * 分配仓库测试,未审核
	 */
	@Test(expected = RuntimeException.class)
	public void testAssignStoreFail() {

		CreateOrderDTO co = createOrder();
		// 获得订单对象
		Order order = trade.createOrder(co);
		// 支付 
		// 审核 

		AssignStoreDTO assign = new AssignStoreDTO();
		assign.setOrderId(order.getOrderId());
		assign.setStoreId(1);
		assign.setOperatorId(2);
		trade.assignStore(assign);
		Order dbOrder = orderDao.findById(order.getOrderId());
		Assert.assertEquals(OrderStatus.ORDER_STATUS_ASSIGNED,
				dbOrder.getOrderStatus());
	}

	/**
	 * 分配仓库测试
	 */
	@Test
	public void testAssignStore() {

		CreateOrderDTO co = createOrder();
		// 获得订单对象
		Order order = trade.createOrder(co);
		// 支付
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);

		// 审核
		trade.auditOrder(order.getOrderId(), "通过", 2);
		//
		AssignStoreDTO assign = new AssignStoreDTO();
		assign.setOrderId(order.getOrderId());
		assign.setStoreId(1);
		assign.setOperatorId(2);
		try {
			trade.assignStore(assign);
			Order dbOrder = orderDao.findById(order.getOrderId());
			Assert.assertEquals(OrderStatus.ORDER_STATUS_ASSIGNED,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	/**
	 * 退款:卖家不同意
	 */
	@Test(expected = RuntimeException.class)
	public void testRefundRefuse() {

		CreateOrderDTO co = createOrder();
		// 获得订单对象
		Order order = trade.createOrder(co);

		// 获取当前订单总额
		Double oldOrderMoney = order.getOrderMoney();

		RefundDTO ref = new RefundDTO();
		ref.setOrderId(order.getOrderId());
		ref.setUserId(1);
		ref.setRefundMoney(5.5);
		ref.setAgree(false);
		trade.refund(ref);
		// 比较数据
		Order dbOrder = orderDao.findById(order.getOrderId());
		Assert.assertEquals((Double) (oldOrderMoney - ref.getRefundMoney()),
				dbOrder.getOrderMoney());
	}

	/**
	 * 退款:卖家同意且未分配
	 */
	@Test
	public void testRefundNoAssign() {

		CreateOrderDTO co = createOrder();
		// 获得订单对象
		Order order = trade.createOrder(co);
		// 获取当前订单总额
		Double oldOrderMoney = order.getOrderMoney();
		// 支付
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);
		// 审核
		trade.auditOrder(order.getOrderId(), "通过", 2);

		try {
			RefundDTO ref = new RefundDTO();
			ref.setOrderId(order.getOrderId());
			ref.setUserId(1);
			ref.setRefundMoney(5.5);
			ref.setAgree(true);
			trade.refund(ref);
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较数据
			Assert.assertEquals(
					(Double) (oldOrderMoney - ref.getRefundMoney()),
					dbOrder.getOrderMoney());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 退款:卖家同意且已分配仓库
	 */
	@Test
	public void testRefundAssign() {

		CreateOrderDTO co = createOrder();
		// 获得订单对象
		Order order = trade.createOrder(co);
		// 获取当前订单总额
		Double oldOrderMoney = order.getOrderMoney();
		// 支付
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);
		// 审核
		trade.auditOrder(order.getOrderId(), "通过", 2);
		// 分配仓库
		AssignStoreDTO assign = new AssignStoreDTO();
		assign.setOrderId(order.getOrderId());
		assign.setStoreId(1);
		assign.setOperatorId(2);
		trade.assignStore(assign);

		RefundDTO ref = new RefundDTO();
		ref.setOrderId(order.getOrderId());
		ref.setUserId(1);
		ref.setRefundMoney(5.5);
		ref.setAgree(true);
		try {
			trade.refund(ref);
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较数据
			Assert.assertEquals(
					(Double) (oldOrderMoney - ref.getRefundMoney()),
					dbOrder.getOrderMoney());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 退款:卖家同意
	 */
	@Test
	public void testRefundSended() {

		CreateOrderDTO co = createOrder();
		// 获得订单对象
		Order order = trade.createOrder(co);
		// 获取当前订单总额
		Double oldOrderMoney = order.getOrderMoney();
		// 支付
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);
		// 审核
		trade.auditOrder(order.getOrderId(), "通过", 2);
		// 分配仓库
		AssignStoreDTO assign = new AssignStoreDTO();
		assign.setOrderId(order.getOrderId());
		assign.setStoreId(1);
		assign.setOperatorId(2);
		trade.assignStore(assign);

		RefundDTO ref = new RefundDTO();
		ref.setOrderId(order.getOrderId());
		ref.setUserId(1);
		ref.setRefundMoney(5.5);
		ref.setAgree(true);
		try {
			trade.refund(ref);
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较数据
			Assert.assertEquals(
					(Double) (oldOrderMoney - ref.getRefundMoney()),
					dbOrder.getOrderMoney());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 生成订单 数据准备
	 * 
	 * @return
	 */
	private CreateOrderDTO createOrder() {

		// 生成运费模板
		CreatePostageDTO cpt = innerCreatePostaget();
		postage.createPostage(cpt);

		CreateOrderDTO co = new CreateOrderDTO();
		co.setUserId(1);
		co.setReceiverId(1);
		co.setReceiverName("Tom");
		co.setReceiverPhone("1311111111");
		co.setReceiverCityId(cpt.getList().get(0).getCityId());
		co.setReceiverDetailAddr("灞桥区/.1123");
		co.setBuyerMessage("hello");
		co.setSellerId(cpt.getSellerId());
		// 订单明细
		OrderDetailDTO od = new OrderDetailDTO();
		od.setSkuId(1);
		od.setItemId(2);
		od.setItemCount(10);
		od.setItemName("饼干");
		od.setItemPrice(2.0);

		OrderDetailDTO ods = new OrderDetailDTO();
		ods.setSkuId(1);
		ods.setItemId(21);
		ods.setItemCount(20);
		ods.setItemName("锅巴");
		ods.setItemPrice(1.0);

		List<OrderDetailDTO> list = new ArrayList<OrderDetailDTO>();
		list.add(od);
		list.add(ods);
		co.setList(list);

		return co;
	}

	/**
	 * 生成模板 数据准备
	 * 
	 * @return
	 */
	private CreatePostageDTO innerCreatePostaget() {
		CreatePostageDTO cpt = new CreatePostageDTO();
		cpt.setDefaultFirstFee(2.0);
		cpt.setDefaultOtherFee(1.0);
		Integer sellerId = (int) (Math.random() * 1000);
		cpt.setSellerId(sellerId);

		// 生成模板明细集合
		List<PostageDetailDTO> list = new ArrayList<PostageDetailDTO>();
		PostageDetailDTO opd = new PostageDetailDTO();
		opd.setCityId(3);
		opd.setFirstFee(3.0);
		opd.setOtherFee(2.0);
		opd.setPostageDetailStatus(OrderStatus.ORDER_STATUS_USEABLE);

		PostageDetailDTO opds = new PostageDetailDTO();
		opds.setCityId(4);
		opds.setFirstFee(5.0);
		opds.setOtherFee(1.0);
		opds.setPostageDetailStatus(OrderStatus.ORDER_STATUS_USEABLE);

		list.add(opds);
		list.add(opd);
		cpt.setList(list);
		return cpt;

	}

}
