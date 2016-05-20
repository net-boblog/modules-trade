package com.xabaohui.modules.trade.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderDetail;

public interface OrderDetailDao {

	// property constants
	public static final String ITEM_ID = "itemId";
	public static final String SKU_ID = "skuId";
	public static final String ITEM_NAME = "itemName";
	public static final String ITEM_COUNT = "itemCount";
	public static final String ITEM_PRICE = "itemPrice";
	public static final String ORDER_ID = "orderId";
	public static final String VERSION = "version";
	public static final String STATUS = "status";

	public abstract void save(OrderDetail transientInstance);

	public abstract void update(OrderDetail transientInstance);
	
	public abstract OrderDetail findById(java.lang.Integer id);

	public  abstract List findByCritera(DetachedCriteria criteria);
	
	public abstract List findByExample(OrderDetail instance);

	public abstract List findByProperty(String propertyName, Object value);

	public abstract List findByItemId(Object itemId);

	public abstract List findBySkuId(Object skuId);

	public abstract List findByItemName(Object itemName);

	public abstract List findByItemCount(Object itemCount);

	public abstract List findByItemPrice(Object itemPrice);

	public abstract List findByOrderId(Object orderId);

}