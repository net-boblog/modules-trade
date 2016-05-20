package com.xabaohui.modules.trade.bean;

public class OrderStatus {
	
	public static final String ORDER_STATUS_INIT ="init";           	  //初始化
	public static final String ORDER_STATUS_WAILTING = "waiting";	      //待付款
	public static final String ORDER_STATUS_PAID ="paid";				  //已付款	
	public static final String ORDER_STATUS_ACCEPT = "accept";			  //已审核	
	public static final String ORDER_STATUS_CANCEL = "cancel";			  //已取消
	public static final String ORDER_STATUS_ASSIGNED ="assigned";		  //已分配
	public static final String ORDER_STATUS_SENDED ="sended";			  //已发送
	
	public static final String ORDER_STATUS_RECEIVED ="received";		  //已接收
	public static final String ORDER_STATUS_REFUNDSOME ="refundSome";		  //已部分退款
	//订单邮费模板状态(尚未进行验证状态)
	public static final String ORDER_STATUS_ABANDON ="abandon";		      //废弃
	public static final String ORDER_STATUS_USEABLE ="useable";		      //可用
}
