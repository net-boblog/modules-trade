package com.xabaohui.modules.trade.dto;

import java.util.List;

public class CreatePostageDTO {
	
	private Integer sellerId;
	private Double defaultFirstFee;
	private Double defaultOtherFee;
	private List<PostageDetailDTO> list;
	
	
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public Double getDefaultFirstFee() {
		return defaultFirstFee;
	}
	public void setDefaultFirstFee(Double defaultFirstFee) {
		this.defaultFirstFee = defaultFirstFee;
	}
	public Double getDefaultOtherFee() {
		return defaultOtherFee;
	}
	public void setDefaultOtherFee(Double defaultOtherFee) {
		this.defaultOtherFee = defaultOtherFee;
	}
	public List<PostageDetailDTO> getList() {
		return list;
	}
	public void setList(List<PostageDetailDTO> list) {
		this.list = list;
	}

	
}
