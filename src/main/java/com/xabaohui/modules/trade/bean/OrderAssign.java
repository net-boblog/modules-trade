package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * OrderAssign entity. @author MyEclipse Persistence Tools
 */

public class OrderAssign implements java.io.Serializable {

	// Fields

	private Integer orderAssignId;
	private Integer orderId;
	private Integer orderDetailId;
	private Integer orderDetailCount;
	private Integer storeId;
	private String orderAssignStatus;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderAssign() {
	}

	/** minimal constructor */
	public OrderAssign(Integer orderId, Integer orderDetailId,
			Integer orderDetailCount, Integer storeId, Date gmtCreate,
			Date gmtModify, Integer version) {
		this.orderId = orderId;
		this.orderDetailId = orderDetailId;
		this.orderDetailCount = orderDetailCount;
		this.storeId = storeId;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	/** full constructor */
	public OrderAssign(Integer orderId, Integer orderDetailId,
			Integer orderDetailCount, Integer storeId,
			String orderAssignStatus, Date gmtCreate, Date gmtModify,
			Integer version) {
		this.orderId = orderId;
		this.orderDetailId = orderDetailId;
		this.orderDetailCount = orderDetailCount;
		this.storeId = storeId;
		this.orderAssignStatus = orderAssignStatus;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	// Property accessors

	public Integer getOrderAssignId() {
		return this.orderAssignId;
	}

	public void setOrderAssignId(Integer orderAssignId) {
		this.orderAssignId = orderAssignId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailId() {
		return this.orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getOrderDetailCount() {
		return this.orderDetailCount;
	}

	public void setOrderDetailCount(Integer orderDetailCount) {
		this.orderDetailCount = orderDetailCount;
	}

	public Integer getStoreId() {
		return this.storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getOrderAssignStatus() {
		return this.orderAssignStatus;
	}

	public void setOrderAssignStatus(String orderAssignStatus) {
		this.orderAssignStatus = orderAssignStatus;
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