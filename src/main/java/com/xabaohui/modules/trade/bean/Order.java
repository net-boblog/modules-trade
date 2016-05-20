package com.xabaohui.modules.trade.bean;

import java.util.Date;

/**
 * Order entity. @author MyEclipse Persistence Tools
 */

public class Order implements java.io.Serializable {

	// Fields

	private Integer orderId;
	private Integer sellerId;
	private Integer receiverId;
	private String receiverName;
	private String receiverPhone;
	private Integer receiverCityId;
	private String receiverDetailAddr;
	private Integer postId;
	private String postCompany;
	private Double postMoney;
	private Integer orderItemCount;
	private Double orderMoney;
	private String orderStatus;
	private Integer paymentId;
	private Date payTime;
	private Date arrangeTime;
	private Date sendTime;
	private Date receiveTime;
	private String salerRemark;
	private String buyerMessage;
	private Date gmtCreate;
	private Date gmtModify;
	private Integer version;
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public Integer getReceiverCityId() {
		return receiverCityId;
	}
	public void setReceiverCityId(Integer receiverCityId) {
		this.receiverCityId = receiverCityId;
	}
	public String getReceiverDetailAddr() {
		return receiverDetailAddr;
	}
	public void setReceiverDetailAddr(String receiverDetailAddr) {
		this.receiverDetailAddr = receiverDetailAddr;
	}
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	public String getPostCompany() {
		return postCompany;
	}
	public void setPostCompany(String postCompany) {
		this.postCompany = postCompany;
	}
	public Double getPostMoney() {
		return postMoney;
	}
	public void setPostMoney(Double postMoney) {
		this.postMoney = postMoney;
	}
	public Integer getOrderItemCount() {
		return orderItemCount;
	}
	public void setOrderItemCount(Integer orderItemCount) {
		this.orderItemCount = orderItemCount;
	}
	public Double getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(Double orderMoney) {
		this.orderMoney = orderMoney;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getArrangeTime() {
		return arrangeTime;
	}
	public void setArrangeTime(Date arrangeTime) {
		this.arrangeTime = arrangeTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getSalerRemark() {
		return salerRemark;
	}
	public void setSalerRemark(String salerRemark) {
		this.salerRemark = salerRemark;
	}
	public String getBuyerMessage() {
		return buyerMessage;
	}
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModify() {
		return gmtModify;
	}
	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	
	
}