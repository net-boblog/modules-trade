package com.xabaohui.modules.trade.dto;

public class PayForOrderDTO {
	
	private Integer userId;
	private Integer orderId;
	private Integer senderId;
	private Integer receiverId;
	
	

	public PayForOrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getSenderId() {
		return senderId;
	}
	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}
	public Integer getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	

}
