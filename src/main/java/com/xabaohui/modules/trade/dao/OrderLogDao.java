package com.xabaohui.modules.trade.dao;

import java.util.List;

import com.xabaohui.modules.trade.bean.OrderDetail;
import com.xabaohui.modules.trade.bean.OrderLog;

public interface OrderLogDao {

	// property constants
	public static final String ORDER_ID = "orderId";
	public static final String OPERATE_DETAIL = "operateDetail";
	public static final String OPERATER_ID = "operaterId";
	public static final String VERSION = "version";

	public abstract void save(OrderLog transientInstance);

	public abstract void update(OrderLog transientInstance);
	
	public abstract OrderLog findById(java.lang.Integer id);

	public abstract List findByExample(OrderLog instance);

	public abstract List findByProperty(String propertyName, Object value);

	public abstract List findByOrderId(Object orderId);

	public abstract List findByOperateDetail(Object operateDetail);

	public abstract List findByOperaterId(Object operaterId);

	public abstract List findByVersion(Object version);

}