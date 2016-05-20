package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * OrderPostageDetail entity. @author MyEclipse Persistence Tools
 */

public class OrderPostageDetail implements java.io.Serializable {

	// Fields

	private Integer postageDetailId;
	private Integer postageId;
	private Double firstFee;
	private Double otherFee;
	private Integer cityId;
	private String postageDetailStatus;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderPostageDetail() {
	}

	/** full constructor */
	public OrderPostageDetail(Integer postageId, Double firstFee,
			Double otherFee, Integer cityId, String postageDetailStatus,
			Date gmtCreate, Date gmtModify, Integer version) {
		this.postageId = postageId;
		this.firstFee = firstFee;
		this.otherFee = otherFee;
		this.cityId = cityId;
		this.postageDetailStatus = postageDetailStatus;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	// Property accessors

	public Integer getPostageDetailId() {
		return this.postageDetailId;
	}

	public void setPostageDetailId(Integer postageDetailId) {
		this.postageDetailId = postageDetailId;
	}

	public Integer getPostageId() {
		return this.postageId;
	}

	public void setPostageId(Integer postageId) {
		this.postageId = postageId;
	}

	public Double getFirstFee() {
		return this.firstFee;
	}

	public void setFirstFee(Double firstFee) {
		this.firstFee = firstFee;
	}

	public Double getOtherFee() {
		return this.otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getPostageDetailStatus() {
		return this.postageDetailStatus;
	}

	public void setPostageDetailStatus(String postageDetailStatus) {
		this.postageDetailStatus = postageDetailStatus;
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