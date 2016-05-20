package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * OrderLog entity. @author MyEclipse Persistence Tools
 */

public class OrderLog implements java.io.Serializable {

	// Fields
	private Integer orderLogId;
	private Integer orderId;
	private String operateDetail;
	private Integer operatorId;
	private String operateType;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderLog() {
	}


	public Integer getOrderLogId() {
		return this.orderLogId;
	}

	public void setOrderLogId(Integer orderLogId) {
		this.orderLogId = orderLogId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOperateDetail() {
		return this.operateDetail;
	}

	public void setOperateDetail(String operateDetail) {
		this.operateDetail = operateDetail;
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

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}


	public Integer getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

}