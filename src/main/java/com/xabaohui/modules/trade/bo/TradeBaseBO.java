package com.xabaohui.modules.trade.bo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderDetail;
import com.xabaohui.modules.trade.bean.OrderLog;
import com.xabaohui.modules.trade.bean.OrderLogOperateType;
import com.xabaohui.modules.trade.bean.OrderStatus;
import com.xabaohui.modules.trade.dao.OrderDao;
import com.xabaohui.modules.trade.dao.OrderDetailDao;
import com.xabaohui.modules.trade.dao.OrderLogDao;
import com.xabaohui.modules.trade.dto.CreateOrderLogDTO;

/**
 * 接收返回状态的接口
 * 
 * @author zhang Hongwei
 * 
 */
public class TradeBaseBO {

	private OrderDao orderDao;
	private OrderDetailDao orderDetailDao;
	private OrderLogDao orderLogDao;
	private Logger log = LoggerFactory.getLogger(TradeBaseBO.class);

	/**
	 * 支付成功后
	 */
	public void payForOrderSuccess(Integer orderId, Integer operatorId) {

		checkOrderIsEmpty(orderId, operatorId);
		log.info("TradeBase:数据检测完毕");
		flashOrderMessage(orderId, OrderStatus.ORDER_STATUS_PAID);

		// 日志
		String logText = "支付成功";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_PAY;
		createOrderLog(logText, operateType, orderId, operatorId);
	}

	/**
	 * 发货成功
	 */
	public void sendedSuccess(Integer orderId, Integer operatorId) {

		checkOrderIsEmpty(orderId, operatorId);
		flashOrderMessage(orderId, OrderStatus.ORDER_STATUS_SENDED);
		// 日志
		String logText = "发货成功";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_SEND;
		createOrderLog(logText, operateType, orderId, operatorId);
	}

	/**
	 * 退款成功
	 */
	public void refundForOrderSuccess(Integer orderId, Integer operatorId) {
		checkOrderIsEmpty(orderId, operatorId);
		flashOrderMessage(orderId, OrderStatus.ORDER_STATUS_REFUNDSOME);

		// 日志
		String logText = "退款成功";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_REFUND;
		createOrderLog(logText, operateType, orderId, operatorId);
	}

	/**
	 * 完全退款之后
	 */
	public void refundForOrderOver(Integer orderId, Integer operatorId) {
		checkOrderIsEmpty(orderId, operatorId);
		flashOrderMessage(orderId, OrderStatus.ORDER_STATUS_CANCEL);
		// 日志
		String logText = "全部退款成功";
		String operateType = OrderLogOperateType.ORDER_LOG_OPERATE_TYPE_REFUND;
		createOrderLog(logText, operateType, orderId, operatorId);
	}

	/**
	 * 生成日志
	 * 
	 * @param logText
	 * @param operateType
	 * @param orderId
	 * @param operatorId
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
		log.info("TraderBase:日志添加成功");
	}

	/**
	 * 更新订单信息
	 * 
	 * @param order
	 * @param orderStatus
	 */
	private void flashOrderMessage(Integer orderId, String orderStatus) {

		Order order = orderDao.findById(orderId);
		if (order == null) {
			throw new RuntimeException("TradeBase：未获取订单对象");
		}
		order.setOrderStatus(orderStatus);
		Date date = new Date();
		order.setGmtModify(date);
		order.setVersion(order.getVersion() + 1);
		orderDao.update(order);
	}

	/**
	 * 检测Order是否为空
	 * 
	 * @param order
	 */
	private void checkOrderIsEmpty(Integer orderId, Integer operatorId) {
		if (orderId == null) {
			throw new RuntimeException("TradeBase：未获取订单ID");
		}
		if (operatorId == null) {
			throw new RuntimeException("TradeBase：未获取操作员ID");
		}
		log.info("TradeBase：验证通过");
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setOrderDetailDao(OrderDetailDao orderDetailDao) {
		this.orderDetailDao = orderDetailDao;
	}

	public void setOrderLogDao(OrderLogDao orderLogDao) {
		this.orderLogDao = orderLogDao;
	}
}
