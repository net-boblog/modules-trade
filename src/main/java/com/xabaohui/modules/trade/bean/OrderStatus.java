package com.xabaohui.modules.trade.bean;

public class OrderStatus {
	
	public static final String ORDER_STATUS_INIT ="init";           	  //��ʼ��
	public static final String ORDER_STATUS_WAILTING = "waiting";	      //������
	public static final String ORDER_STATUS_PAID ="paid";				  //�Ѹ���	
	public static final String ORDER_STATUS_ACCEPT = "accept";			  //�����	
	public static final String ORDER_STATUS_CANCEL = "cancel";			  //��ȡ��
	public static final String ORDER_STATUS_ASSIGNED ="assigned";		  //�ѷ���
	public static final String ORDER_STATUS_SENDED ="sended";			  //�ѷ���
	
	public static final String ORDER_STATUS_RECEIVED ="received";		  //�ѽ���
	public static final String ORDER_STATUS_REFUNDSOME ="refundSome";		  //�Ѳ����˿�
	//�����ʷ�ģ��״̬(��δ������֤״̬)
	public static final String ORDER_STATUS_ABANDON ="abandon";		      //����
	public static final String ORDER_STATUS_USEABLE ="useable";		      //����
}
