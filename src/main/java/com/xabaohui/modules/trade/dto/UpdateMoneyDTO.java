package com.xabaohui.modules.trade.dto;

public class UpdateMoneyDTO {
	
	private Integer orderdetailID;
	private Integer itemCount;
	private Double itemPrice;
	private Double orderMoney;
	private Integer orderId;
	
	
	
	public Integer getItemCount() {
		return itemCount;
	}
	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}
	
	public Integer getOrderdetailID() {
		return orderdetailID;
	}
	public void setOrderdetailID(Integer orderdetailID) {
		this.orderdetailID = orderdetailID;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Double getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(Double orderMoney) {
		this.orderMoney = orderMoney;
	}
	
	
	
}
