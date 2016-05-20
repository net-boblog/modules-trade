package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * OrderPostage entity. @author MyEclipse Persistence Tools
 */

public class OrderPostage implements java.io.Serializable {

	// Fields

	private Integer postageId;
	private Integer sellerId;
	private String postageStatus;
	private Double defaultFirstFee;
	private Double defaultOtherFee;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderPostage() {
	}

	/** full constructor */
	public OrderPostage(Integer sellerId, String postageStatus,
			Double defaultFirstFee, Double defaultOtherFee, Date gmtCreate,
			Date gmtModify, Integer version) {
		this.sellerId = sellerId;
		this.postageStatus = postageStatus;
		this.defaultFirstFee = defaultFirstFee;
		this.defaultOtherFee = defaultOtherFee;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	// Property accessors

	public Integer getPostageId() {
		return this.postageId;
	}

	public void setPostageId(Integer postageId) {
		this.postageId = postageId;
	}

	public Integer getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public String getPostageStatus() {
		return this.postageStatus;
	}

	public void setPostageStatus(String postageStatus) {
		this.postageStatus = postageStatus;
	}

	public Double getDefaultFirstFee() {
		return this.defaultFirstFee;
	}

	public void setDefaultFirstFee(Double defaultFirstFee) {
		this.defaultFirstFee = defaultFirstFee;
	}

	public Double getDefaultOtherFee() {
		return this.defaultOtherFee;
	}

	public void setDefaultOtherFee(Double defaultOtherFee) {
		this.defaultOtherFee = defaultOtherFee;
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