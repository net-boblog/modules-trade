package com.xabaohui.modules.trade.dto;

public class PostageDetailDTO {
	
	private Double firstFee;
	private Double otherFee;
	private Integer cityId;
	private String postageDetailStatus;
	public Double getFirstFee() {
		return firstFee;
	}
	public void setFirstFee(Double firstFee) {
		this.firstFee = firstFee;
	}
	public Double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getPostageDetailStatus() {
		return postageDetailStatus;
	}
	public void setPostageDetailStatus(String postageDetailStatus) {
		this.postageDetailStatus = postageDetailStatus;
	}
	
	
}
