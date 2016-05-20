package com.xabaohui.modules.trade.dto;

public class RefundDTO {
	
	private Integer userId;
	private Integer orderId;
	private Double refundMoney;
	private Boolean agree;
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Boolean getAgree() {
		return agree;
	}


	public void setAgree(Boolean agree) {
		this.agree = agree;
	}

	
}
