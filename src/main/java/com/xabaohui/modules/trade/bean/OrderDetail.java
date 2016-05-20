package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * OrderDetail entity. @author MyEclipse Persistence Tools
 */

public class OrderDetail implements java.io.Serializable {

	// Fields

	private Integer orderDetailId;
	private Integer itemId;
	private Integer skuId;
	private String  skuDesc;
	private Integer postageDetailId;
	private String status;
	private String itemName;
	private Integer itemCount;
	private Double itemPrice;
	private Integer orderId;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;

	// Constructors

	/** default constructor */
	public OrderDetail() {
	}

	/** minimal constructor */
	public OrderDetail(Integer itemId, Integer skuId) {
		this.itemId = itemId;
		this.skuId = skuId;
	}

	/** full constructor */
	public OrderDetail(Integer itemId, Integer skuId, Integer postageDetailId,
			String status, String itemName, Integer itemCount,
			Double itemPrice, Integer orderId, Date gmtCreate, Date gmtModify,
			Integer version) {
		this.itemId = itemId;
		this.skuId = skuId;
		this.postageDetailId = postageDetailId;
		this.status = status;
		this.itemName = itemName;
		this.itemCount = itemCount;
		this.itemPrice = itemPrice;
		this.orderId = orderId;
		this.gmtCreate = gmtCreate;
		this.gmtModify = gmtModify;
		this.version = version;
	}

	// Property accessors

	public Integer getOrderDetailId() {
		return this.orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getSkuId() {
		return this.skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getPostageDetailId() {
		return this.postageDetailId;
	}

	public void setPostageDetailId(Integer postageDetailId) {
		this.postageDetailId = postageDetailId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemCount() {
		return this.itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public Double getItemPrice() {
		return this.itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

}