package com.xabaohui.modules.trade.dao;

import java.util.List;

import com.xabaohui.modules.trade.bean.OrderCity;
import com.xabaohui.modules.trade.bean.OrderProvince;

public interface OrderProvinceDao {

	// property constants
	public static final String PROVINCE_NAME = "provinceName";
	public static final String VERSION = "version";

	public abstract void save(OrderProvince transientInstance);

	public abstract void update(OrderProvince transientInstance);
	
	public abstract OrderProvince findById(java.lang.Integer id);


}