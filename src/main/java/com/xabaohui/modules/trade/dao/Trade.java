package com.xabaohui.modules.trade.dao;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.dto.AddItemDTO;
import com.xabaohui.modules.trade.dto.CreateOrderDTO;
import com.xabaohui.modules.trade.dto.UpdateCountDTO;
import com.xabaohui.modules.trade.dto.UpdateReceiverAddrDTO;

public interface Trade {

	/**
	 * ���ɶ���
	 * 
	 * @param createorderDTO
	 */
	public abstract Order createOrder(CreateOrderDTO createorderDTO);

	
	/**
	 * �����Ʒ
	 * 
	 * @param additemDto
	 */
	public abstract void addItem(AddItemDTO additemDto);

	/**
	 * �޸���Ʒ����
	 * 
	 * @return
	 */
	public abstract void updateCount(UpdateCountDTO upcDto);
	
	/**
	 *ȡ������ 
	 * @param orderId
	 */
	public abstract void cancelOrder(Integer orderId,Integer userId);
	

	/**
	 * �޸��ռ��˵�ַ
	 * @param updDto
	 */
	public abstract void updateReceiveAddr(UpdateReceiverAddrDTO updr);

}