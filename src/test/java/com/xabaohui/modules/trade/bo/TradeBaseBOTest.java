package com.xabaohui.modules.trade.bo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import sun.misc.FpUtils;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderStatus;
import com.xabaohui.modules.trade.dao.OrderDao;
import com.xabaohui.modules.trade.dto.AssignStoreDTO;
import com.xabaohui.modules.trade.dto.CreateOrderDTO;
import com.xabaohui.modules.trade.dto.CreatePostageDTO;
import com.xabaohui.modules.trade.dto.OrderDetailDTO;
import com.xabaohui.modules.trade.dto.PayForOrderDTO;
import com.xabaohui.modules.trade.dto.PostageDetailDTO;
import com.xabaohui.modules.trade.dto.RefundDTO;

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:applicationContext.xml")
public class TradeBaseBOTest {

	@Resource
	private TradeBaseBO tradeBase;
	@Resource
	private PostageBO postage;
	@Resource
	private TradeBO trade;
	@Resource
	private OrderDao orderDao;

	/**
	 * 测试支付成功后状态
	 */
	@Test
	public void testPayForOrderSuccess() {

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

		try {
			tradeBase.payForOrderSuccess(order.getOrderId(), pfo.getUserId());
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较
			Assert.assertEquals(OrderStatus.ORDER_STATUS_PAID,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	/**
	 * 发货成功状态测试
	 */
	@Test
	public void testSendedSuccess() {

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
		tradeBase.payForOrderSuccess(order.getOrderId(), pfo.getUserId());
		// 分配仓库
		AssignStoreDTO assign = new AssignStoreDTO();
		assign.setOrderId(order.getOrderId());
		assign.setStoreId(1);
		assign.setOperatorId(2);
		trade.assignStore(assign);
		try {
			tradeBase.sendedSuccess(order.getOrderId(), assign.getOperatorId());
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较
			Assert.assertEquals(OrderStatus.ORDER_STATUS_SENDED,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 部分退款成功状态测试
	 */
	@Test
	public void testRefundForOrderSuccess() {

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
		tradeBase.payForOrderSuccess(order.getOrderId(), pfo.getUserId());
		// 分配仓库
		AssignStoreDTO assign = new AssignStoreDTO();
		assign.setOrderId(order.getOrderId());
		assign.setStoreId(1);
		assign.setOperatorId(2);
		trade.assignStore(assign);
		try {
			tradeBase.refundForOrderSuccess(order.getOrderId(),
					assign.getOperatorId());
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较
			Assert.assertEquals(OrderStatus.ORDER_STATUS_REFUNDSOME,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * 部分退款成功状态测试
	 */
	@Test
	public void testRefundForOrderOver() {

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
		tradeBase.payForOrderSuccess(order.getOrderId(), pfo.getUserId());
		// 分配仓库
		AssignStoreDTO assign = new AssignStoreDTO();
		assign.setOrderId(order.getOrderId());
		assign.setStoreId(1);
		assign.setOperatorId(2);
		trade.assignStore(assign);
		try {
			tradeBase.refundForOrderOver(order.getOrderId(),
					assign.getOperatorId());
			Order dbOrder = orderDao.findById(order.getOrderId());
			// 比较
			Assert.assertEquals(OrderStatus.ORDER_STATUS_CANCEL,
					dbOrder.getOrderStatus());
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
