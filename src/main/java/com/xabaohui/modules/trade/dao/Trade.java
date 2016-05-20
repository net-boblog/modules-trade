package com.xabaohui.modules.trade.dao;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.dto.AddItemDTO;
import com.xabaohui.modules.trade.dto.CreateOrderDTO;
import com.xabaohui.modules.trade.dto.UpdateCountDTO;
import com.xabaohui.modules.trade.dto.UpdateReceiverAddrDTO;

public interface Trade {

	/**
	 * 生成订单
	 * 
	 * @param createorderDTO
	 */
	public abstract Order createOrder(CreateOrderDTO createorderDTO);

	
	/**
	 * 添加商品
	 * 
	 * @param additemDto
	 */
	public abstract void addItem(AddItemDTO additemDto);

	/**
	 * 修改商品数量
	 * 
	 * @return
	 */
	public abstract void updateCount(UpdateCountDTO upcDto);
	
	/**
	 *取消订单 
	 * @param orderId
	 */
	public abstract void cancelOrder(Integer orderId,Integer userId);
	

	/**
	 * 修改收件人地址
	 * @param updDto
	 */
	public abstract void updateReceiveAddr(UpdateReceiverAddrDTO updr);

}