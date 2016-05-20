package com.xabaohui.modules.trade.dto;

public class AssignStoreDTO {

	private Integer operatorId;
	private Integer orderId;
	private Integer storeId;
	private String orderAssignStatus;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getOrderAssignStatus() {
		return orderAssignStatus;
	}

	public void setOrderAssignStatus(String orderAssignStatus) {
		this.orderAssignStatus = orderAssignStatus;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

}
