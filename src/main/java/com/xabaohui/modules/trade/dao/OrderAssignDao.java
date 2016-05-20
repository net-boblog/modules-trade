package com.xabaohui.modules.trade.dao;

import java.util.List;

import com.xabaohui.modules.trade.bean.OrderAssign;

public interface OrderAssignDao {

	// property constants
	public static final String ORDER_ID = "orderId";
	public static final String ORDER_DETAIL_ID = "orderDetailId";
	public static final String ORDER_DETAIL_COUNT = "orderDetailCount";
	public static final String STORE_ID = "storeId";
	public static final String VERSION = "version";

	public abstract void save(OrderAssign transientInstance);
	
	public abstract void update(OrderAssign transientInstance); 
	
	public abstract OrderAssign findById(java.lang.Integer id);

	public abstract List findByExample(OrderAssign instance);

	public abstract List findByProperty(String propertyName, Object value);

	public abstract List findByOrderId(Object orderId);

	public abstract List findByOrderDetailId(Object orderDetailId);

	public abstract List findByOrderDetailCount(Object orderDetailCount);

	public abstract List findByStoreId(Object storeId);

}