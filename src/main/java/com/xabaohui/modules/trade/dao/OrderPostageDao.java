package com.xabaohui.modules.trade.dao;


import java.util.List;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderPostage;

public interface OrderPostageDao {

	// property constants
	public static final String SELLER_ID = "sellerId";
	public static final String DEFAULT_FIRST_FEE = "defaultFirstFee";
	public static final String DEFAULT_OTHER_FEE = "defaultOtherFee";
	public static final String CITY_ID = "cityId";
	public static final String VERSION = "version";

	public abstract void save(OrderPostage transientInstance);
	
	public abstract void update(OrderPostage transientInstance);

	public abstract List findByExample(OrderPostage instance);
	
	public abstract List findBySellerId(Integer sellerId);
	
	public abstract OrderPostage findById(java.lang.Integer id);


}