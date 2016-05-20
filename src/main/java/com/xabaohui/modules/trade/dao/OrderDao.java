package com.xabaohui.modules.trade.dao;

import java.util.List;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderAssign;

public interface OrderDao {

	// property constants
	public static final String SENDER_NAME = "senderName";
	public static final String SENDER_PHONE = "senderPhone";
	public static final String SENDER_ADDR = "senderAddr";
	public static final String RECEIVER_NAME = "receiverName";
	public static final String RECEIVER_PHONE = "receiverPhone";
	public static final String RECEIVER_ADDR = "receiverAddr";
	public static final String POST_ID = "postId";
	public static final String POST_COMPANY = "postCompany";
	public static final String POST_MONEY = "postMoney";
	public static final String ORDER_MONEY = "orderMoney";
	public static final String ORDER_STATUS = "orderStatus";
	public static final String SALER_REMARK = "salerRemark";
	public static final String BUYER_MESSAGE = "buyerMessage";

	public abstract void save(Order transientInstance);

	public abstract void update(Order transientInstance);
	
	public abstract Order findById(java.lang.Integer id);

	public abstract List findByExample(Order instance);

	public abstract List findByProperty(String propertyName, Object value);

	public abstract List findBySenderName(Object senderName);

	public abstract List findBySenderPhone(Object senderPhone);

	public abstract List findBySenderAddr(Object senderAddr);

	public abstract List findByReceiverName(Object receiverName);

	public abstract List findByReceiverPhone(Object receiverPhone);

	public abstract List findByReceiverAddr(Object receiverAddr);

	public abstract List findByPostId(Object postId);

	public abstract List findByPostCompany(Object postCompany);

	public abstract List findByPostMoney(Object postMoney);

	public abstract List findByOrderMoney(Object orderMoney);

	public abstract List findByOrderStatus(Object orderStatus);

	public abstract List findBySalerRemark(Object salerRemark);

	public abstract List findByBuyerMessage(Object buyerMessage);

}