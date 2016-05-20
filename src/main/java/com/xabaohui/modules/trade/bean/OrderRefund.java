package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * OrderRefund entity. @author MyEclipse Persistence Tools
 */

public class OrderRefund implements java.io.Serializable {

	// Fields

	private Integer orderRefundId;
	private Integer orderId;
	private Integer orderDetailId;
	private Double refundMoney;
	private String refundStatus;
	private Date refundTime;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderRefund() {
	}

	/** full constructor */
	public OrderRefund(Integer orderId, Integer orderDetailId,
			Double refundMoney, String refundStatus, Date refundTime,
			Date gmtCreate, Date gmtModify, Integer version) {
		this.orderId = orderId;
		this.orderDetailId = orderDetailId;
		this.refundMoney = refundMoney;
		this.refundStatus = refundStatus;
		this.refundTime = refundTime;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	// Property accessors

	public Integer getOrderRefundId() {
		return this.orderRefundId;
	}

	public void setOrderRefundId(Integer orderRefundId) {
		this.orderRefundId = orderRefundId;
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

	public Double getRefundMoney() {
		return this.refundMoney;
	}

	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getRefundStatus() {
		return this.refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getRefundTime() {
		return this.refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
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