package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * OrderProvince entity. @author MyEclipse Persistence Tools
 */

public class OrderProvince implements java.io.Serializable {

	// Fields

	private Integer provinceId;
	private String provinceName;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderProvince() {
	}

	/** full constructor */
	public OrderProvince(String provinceName, Date gmtCreate, Date gmtModify,
			Integer version) {
		this.provinceName = provinceName;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	// Property accessors

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Date getGmtCreate() {
		return this.gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return this.gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}