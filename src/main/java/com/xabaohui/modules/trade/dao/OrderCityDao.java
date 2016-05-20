package com.xabaohui.modules.trade.dao;

import java.util.List;

import com.xabaohui.modules.trade.bean.OrderAssign;
import com.xabaohui.modules.trade.bean.OrderCity;

public interface OrderCityDao {

	public abstract void save(OrderCity transientInstance);
	
	public abstract void update(OrderCity transientInstance);

	public abstract OrderCity findById(java.lang.Integer id);

	public abstract List findByExample(OrderCity instance);

}