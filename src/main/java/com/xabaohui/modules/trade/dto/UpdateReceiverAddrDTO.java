package com.xabaohui.modules.trade.dto;

public class UpdateReceiverAddrDTO {

	private Integer cityId;
	private String receiverDetailAddr;
	private Integer orderId;
	private Integer userId;
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getReceiverDetailAddr() {
		return receiverDetailAddr;
	}
	public void setReceiverDetailAddr(String receiverDetailAddr) {
		this.receiverDetailAddr = receiverDetailAddr;
	}
	
	
}
