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
	 * ���ɶ������ݲ���
	 */
	@Test(expected = RuntimeException.class)
	public void testCreateOrderDTODataCheck() {
		CreateOrderDTO co = null;
		trade.createOrder(co);
	}

	/**
	 * ���ɶ���
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
			Assert.fail("������������ʧ�ܣ���Ӧ�ó����쳣");
		}
	}

	/**
	 * �����Ʒ
	 */
	@Test
	public void testAddItem() {

		CreateOrderDTO co = createOrder();
		// ��ȡ��������
		Order order = trade.createOrder(co);
		// �޸�֮ǰ�����ܼ�
		Double OldOrderMoney = order.getOrderMoney();

		AddItemDTO additemdto = new AddItemDTO();
		additemdto.setItemCount(2);
		additemdto.setItemId(1);
		additemdto.setItemName("ţ��");
		additemdto.setItemPrice(2.5);
		additemdto.setOrderId(order.getOrderId());
		additemdto.setSkuId(1);
		additemdto.setStatus(OrderStatus.ORDER_STATUS_USEABLE);
		additemdto.setUserId(1);
		try {
			trade.addItem(additemdto);
			Order dbOrder = orderDao.findById(additemdto.getOrderId());
			// ����޸ĺ���ܼ��Ƿ�һ��
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
	 * �޸���Ʒ���������ݲ���
	 */
	@Test(expected = RuntimeException.class)
	public void testUpdateCountDataCheck() {
		UpdateCountDTO upc = new UpdateCountDTO();
		trade.updateCount(upc);
	}

	/**
	 * �޸���Ʒ��������
	 */
	@Test
	public void testUpdateCount() {

		CreateOrderDTO co = createOrder();
		// ��ö�������
		Order order = trade.createOrder(co);

		// ��ȡ��ǰ��������Ҫ�����Ķ�����ϸ����
		OrderDetail orderDetail = new OrderDetail();
		BeanUtils.copyProperties(co.getList().get(0), orderDetail);
		orderDetail.setOrderId(order.getOrderId());
		List<OrderDetail> listOrderDtail = orderDetailDao
				.findByExample(orderDetail);
		// ��֤
		if (listOrderDtail == null || listOrderDtail.isEmpty()) {
			throw new RuntimeException("�޸���Ʒ������֤��δ��ȡ������ϸ����");
		}
		if (listOrderDtail.size() > 1) {
			throw new RuntimeException("�޸���Ʒ������֤����ȡ����������ϸ��Ψһ");
		}
		OrderDetail orderDetailFind = listOrderDtail.get(0);

		// ��ȡ�����г��޸���Ʒ������֮�������
		Integer oldOrderItemCount = order.getOrderItemCount()
				- orderDetailFind.getItemCount();

		UpdateCountDTO upc = new UpdateCountDTO();
		upc.setItemCount(5);
		upc.setOrderDetailId(orderDetailFind.getOrderDetailId());
		upc.setOrderId(order.getOrderId());
		upc.setUserId(1);
		try {
			// ִ�в���
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
	 * ɾ����Ʒ����(�۲충����ϸ��״̬�仯)
	 */
	@Test
	public void testDeleteItem() {

		CreateOrderDTO co = createOrder(); // ��ö�������
		Order order = trade.createOrder(co);

		//��ȡ��������Ʒ����
		Integer oldOrderItemCount = order.getOrderItemCount();
		// �����е���Ʒ�ܼ�
		Double oldOrderMoney = order.getOrderMoney();
		OrderDetail orderDetail = new OrderDetail();
		BeanUtils.copyProperties(co.getList().get(0), orderDetail);
		orderDetail.setOrderId(order.getOrderId());
		List<OrderDetail> listOrderDtail = orderDetailDao
				.findByExample(orderDetail);
		// ��֤
		if (listOrderDtail == null || listOrderDtail.isEmpty()) {
			throw new RuntimeException("ɾ����Ʒ������֤��δ��ȡ������ϸ����");
		}
		if (listOrderDtail.size() > 1) {
			throw new RuntimeException("ɾ����Ʒ������֤����ȡ����������ϸ��Ψһ");
		}

		// ��ȡ��ǰ��������Ҫ�����Ķ�����ϸ����
		OrderDetail orderDetailFind = listOrderDtail.get(0);
		try {
			// ִ�в���
			trade.deleteItem(orderDetailFind.getOrderDetailId(),
					order.getOrderId(), 2);
			// �ȽϽ��
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
	 * �޸��ռ��˵�ַ����
	 */
	@Test
	public void testUpdateReceiveAddr() {

		CreateOrderDTO co = createOrder(); // ��ö�������
		Order order = trade.createOrder(co);

		UpdateReceiverAddrDTO updr = new UpdateReceiverAddrDTO();
		updr.setUserId(3);
		updr.setReceiverDetailAddr("�޸Ĺ��ĵ�ַ");
		updr.setCityId(1);
		updr.setOrderId(order.getOrderId());
		try {
			trade.updateReceiveAddr(updr);
			Order dbOrder = orderDao.findById(updr.getOrderId());
			Assert.assertEquals("�޸Ĺ��ĵ�ַ", dbOrder.getReceiverDetailAddr());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * ȡ����������
	 */
	@Test
	public void testCancelOrder() {

		CreateOrderDTO co = createOrder(); // ��ö�������
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
	 * �������
	 */
	@Test
	public void testPayForOrder() {

		CreateOrderDTO co = createOrder(); // ��ö�������
		Order order = trade.createOrder(co);

		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		try {
			trade.payForOrder(pfo);
			Order dbOrder = orderDao.findById(order.getOrderId());
			// �Ƚ�����
			Assert.assertEquals(OrderStatus.ORDER_STATUS_PAID,
					dbOrder.getOrderStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * ��˲��� ,���㶩��״̬
	 */
	@Test
	public void testAuditOrder() {

		CreateOrderDTO co = createOrder(); // ��ö�������
		Order order = trade.createOrder(co);

		// ֧��
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);

		try {
			String salerRemark = "�Ѵ���";
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
	 * ��˲��� ,δ����
	 */
	@Test(expected = RuntimeException.class)
	public void testAuditOrderFail() {

		CreateOrderDTO co = createOrder(); // ��ö�������
		Order order = trade.createOrder(co);

		String salerRemark = "�Ѵ���";
		trade.auditOrder(order.getOrderId(), salerRemark, 12);
		Order dbOrder = orderDao.findById(order.getOrderId());
		Assert.assertEquals(OrderStatus.ORDER_STATUS_ACCEPT,
				dbOrder.getOrderStatus());
	}

	/**
	 * ����ֿ����,δ���
	 */
	@Test(expected = RuntimeException.class)
	public void testAssignStoreFail() {

		CreateOrderDTO co = createOrder();
		// ��ö�������
		Order order = trade.createOrder(co);
		// ֧�� 
		// ��� 

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
	 * ����ֿ����
	 */
	@Test
	public void testAssignStore() {

		CreateOrderDTO co = createOrder();
		// ��ö�������
		Order order = trade.createOrder(co);
		// ֧��
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);

		// ���
		trade.auditOrder(order.getOrderId(), "ͨ��", 2);
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
	 * �˿�:���Ҳ�ͬ��
	 */
	@Test(expected = RuntimeException.class)
	public void testRefundRefuse() {

		CreateOrderDTO co = createOrder();
		// ��ö�������
		Order order = trade.createOrder(co);

		// ��ȡ��ǰ�����ܶ�
		Double oldOrderMoney = order.getOrderMoney();

		RefundDTO ref = new RefundDTO();
		ref.setOrderId(order.getOrderId());
		ref.setUserId(1);
		ref.setRefundMoney(5.5);
		ref.setAgree(false);
		trade.refund(ref);
		// �Ƚ�����
		Order dbOrder = orderDao.findById(order.getOrderId());
		Assert.assertEquals((Double) (oldOrderMoney - ref.getRefundMoney()),
				dbOrder.getOrderMoney());
	}

	/**
	 * �˿�:����ͬ����δ����
	 */
	@Test
	public void testRefundNoAssign() {

		CreateOrderDTO co = createOrder();
		// ��ö�������
		Order order = trade.createOrder(co);
		// ��ȡ��ǰ�����ܶ�
		Double oldOrderMoney = order.getOrderMoney();
		// ֧��
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);
		// ���
		trade.auditOrder(order.getOrderId(), "ͨ��", 2);

		try {
			RefundDTO ref = new RefundDTO();
			ref.setOrderId(order.getOrderId());
			ref.setUserId(1);
			ref.setRefundMoney(5.5);
			ref.setAgree(true);
			trade.refund(ref);
			Order dbOrder = orderDao.findById(order.getOrderId());
			// �Ƚ�����
			Assert.assertEquals(
					(Double) (oldOrderMoney - ref.getRefundMoney()),
					dbOrder.getOrderMoney());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * �˿�:����ͬ�����ѷ���ֿ�
	 */
	@Test
	public void testRefundAssign() {

		CreateOrderDTO co = createOrder();
		// ��ö�������
		Order order = trade.createOrder(co);
		// ��ȡ��ǰ�����ܶ�
		Double oldOrderMoney = order.getOrderMoney();
		// ֧��
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);
		// ���
		trade.auditOrder(order.getOrderId(), "ͨ��", 2);
		// ����ֿ�
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
			// �Ƚ�����
			Assert.assertEquals(
					(Double) (oldOrderMoney - ref.getRefundMoney()),
					dbOrder.getOrderMoney());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * �˿�:����ͬ��
	 */
	@Test
	public void testRefundSended() {

		CreateOrderDTO co = createOrder();
		// ��ö�������
		Order order = trade.createOrder(co);
		// ��ȡ��ǰ�����ܶ�
		Double oldOrderMoney = order.getOrderMoney();
		// ֧��
		PayForOrderDTO pfo = new PayForOrderDTO();
		pfo.setOrderId(order.getOrderId());
		pfo.setReceiverId(order.getReceiverId());
		pfo.setSenderId(order.getSellerId());
		pfo.setUserId(3);
		trade.payForOrder(pfo);
		// ���
		trade.auditOrder(order.getOrderId(), "ͨ��", 2);
		// ����ֿ�
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
			// �Ƚ�����
			Assert.assertEquals(
					(Double) (oldOrderMoney - ref.getRefundMoney()),
					dbOrder.getOrderMoney());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * ���ɶ��� ����׼��
	 * 
	 * @return
	 */
	private CreateOrderDTO createOrder() {

		// �����˷�ģ��
		CreatePostageDTO cpt = innerCreatePostaget();
		postage.createPostage(cpt);

		CreateOrderDTO co = new CreateOrderDTO();
		co.setUserId(1);
		co.setReceiverId(1);
		co.setReceiverName("Tom");
		co.setReceiverPhone("1311111111");
		co.setReceiverCityId(cpt.getList().get(0).getCityId());
		co.setReceiverDetailAddr("�����/.1123");
		co.setBuyerMessage("hello");
		co.setSellerId(cpt.getSellerId());
		// ������ϸ
		OrderDetailDTO od = new OrderDetailDTO();
		od.setSkuId(1);
		od.setItemId(2);
		od.setItemCount(10);
		od.setItemName("����");
		od.setItemPrice(2.0);

		OrderDetailDTO ods = new OrderDetailDTO();
		ods.setSkuId(1);
		ods.setItemId(21);
		ods.setItemCount(20);
		ods.setItemName("����");
		ods.setItemPrice(1.0);

		List<OrderDetailDTO> list = new ArrayList<OrderDetailDTO>();
		list.add(od);
		list.add(ods);
		co.setList(list);

		return co;
	}

	/**
	 * ����ģ�� ����׼��
	 * 
	 * @return
	 */
	private CreatePostageDTO innerCreatePostaget() {
		CreatePostageDTO cpt = new CreatePostageDTO();
		cpt.setDefaultFirstFee(2.0);
		cpt.setDefaultOtherFee(1.0);
		Integer sellerId = (int) (Math.random() * 1000);
		cpt.setSellerId(sellerId);

		// ����ģ����ϸ����
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
