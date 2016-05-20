package com.xabaohui.modules.trade.dao;

import java.util.List;

import com.xabaohui.modules.trade.bean.OrderLog;
import com.xabaohui.modules.trade.bean.OrderRefund;

public interface OrderRefundDao {

	// property constants
	public static final String ORDER_ID = "orderId";
	public static final String ORDER_DETAIL_ID = "orderDetailId";
	public static final String REFUND_MONEY = "refundMoney";
	public static final String REFUND_STATUS = "refundStatus";
	public static final String VERSION = "version";

	public abstract void save(OrderRefund transientInstance);

	public abstract void update(OrderRefund transientInstance);
	
	public abstract OrderRefund findById(java.lang.Integer id);

	public abstract List findByExample(OrderRefund instance);

	public abstract List findByProperty(String propertyName, Object value);

	public abstract List findByOrderId(Object orderId);

	public abstract List findByOrderDetailId(Object orderDetailId);

	public abstract List findByRefundMoney(Object refundMoney);

	public abstract List findByRefundStatus(Object refundStatus);

}