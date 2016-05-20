package com.xabaohui.modules.trade.dao;

import java.util.List;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderCity;
import com.xabaohui.modules.trade.bean.OrderPostageDetail;

public interface OrderPostageDetailDao {

	// property constants
	public static final String POSTAGE_ID = "postageId";
	public static final String FIRST_FEE = "firstFee";
	public static final String OTHER_FEE = "otherFee";
	public static final String CITY_ID = "cityId";
	public static final String VERSION = "version";

	public abstract void save(OrderPostageDetail transientInstance);
	
	public abstract List findByExample(OrderPostageDetail instance);

	public abstract void update(OrderPostageDetail transientInstance);

	
	public abstract OrderPostageDetail findById(java.lang.Integer id);


}