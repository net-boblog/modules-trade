package com.xabaohui.modules.trade.dto;

public class UpdateCountDTO {
	
	private Integer userId;
	private Integer orderId;
	private Integer orderDetailId;
	private Integer itemCount;
	
	public Integer getOrderId() {
		return orderId;
	}
	
	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Integer getItemCount() {
		return itemCount;
	}
	
	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
