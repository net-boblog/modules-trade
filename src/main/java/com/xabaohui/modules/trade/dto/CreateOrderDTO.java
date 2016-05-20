package com.xabaohui.modules.trade.dto;

import java.util.List;

public class CreateOrderDTO {
	// order
	private Integer userId; // 操作员 XXX rename to operator, 含义相比userId更加明确
	private Integer sellerId;
	private Integer receiverId;
	private String receiverName;
	private String receiverPhone;
	private Integer receiverCityId;
	private String receiverDetailAddr;
	//private Integer orderItemCount;
	private String buyerMessage;
	
	
	//订单明细集合
	private List<OrderDetailDTO> list;
	
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	public String getBuyerMessage() {
		return buyerMessage;
	}
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	public List<OrderDetailDTO> getList() {
		return list;
	}
	public void setList(List<OrderDetailDTO> list) {
		this.list = list;
	}
	
	
}
