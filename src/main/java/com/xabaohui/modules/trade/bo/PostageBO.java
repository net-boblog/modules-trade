package com.xabaohui.modules.trade.bo;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.xabaohui.modules.trade.bean.OrderPostage;
import com.xabaohui.modules.trade.bean.OrderPostageDetail;
import com.xabaohui.modules.trade.bean.OrderStatus;
import com.xabaohui.modules.trade.dao.OrderPostageDao;
import com.xabaohui.modules.trade.dao.OrderPostageDetailDao;
import com.xabaohui.modules.trade.dto.CreatePostageDTO;
import com.xabaohui.modules.trade.dto.PostageDetailDTO;

/**
 * 运费计算模块
 * 
 * @author Zhang Hong wei
 * 
 */
public class PostageBO {

	private OrderPostageDao orderPostageDao;
	private OrderPostageDetailDao orderPostageDetailDao;
	private Logger log = LoggerFactory.getLogger(PostageBO.class);

	/**
	 * 生成模板及模板明细
	 * 
	 * @param cpt
	 * @return
	 */
	public OrderPostage createPostage(CreatePostageDTO cpt) {

		checkForCreatePostage(cpt);
		log.info("检测通过");

		OrderPostage postage = new OrderPostage();

		// 运费默认模板初始化
		BeanUtils.copyProperties(cpt, postage);
		Date date = new Date();
		postage.setGmtCreate(date);
		postage.setGmtModify(date);
		postage.setPostageStatus(OrderStatus.ORDER_STATUS_USEABLE);
		postage.setVersion(1);

		orderPostageDao.save(postage);
		log.info("默认模板初始化成功");

		// 生成明细模板
		for (PostageDetailDTO postageDetailDTO : cpt.getList()) {
			OrderPostageDetail orderPostageDetail = new OrderPostageDetail();
			BeanUtils.copyProperties(postageDetailDTO, orderPostageDetail);
			orderPostageDetail.setGmtCreate(date);
			orderPostageDetail.setGmtModify(date);
			orderPostageDetail
					.setPostageDetailStatus(OrderStatus.ORDER_STATUS_USEABLE);
			orderPostageDetail.setVersion(1);
			orderPostageDetail.setPostageId(innerGetPostageBySellerId(cpt)
					.getPostageId());
			orderPostageDetailDao.save(orderPostageDetail);
			log.info("模板明细添加成功");
		}
		return postage;
	}

	/**
	 * 根据卖家ID获取邮费模板
	 * 
	 * @param cpt
	 * @return
	 */
	private OrderPostage innerGetPostageBySellerId(CreatePostageDTO cpt) {

		OrderPostage p = new OrderPostage();
		p.setSellerId(cpt.getSellerId());
		List<OrderPostage> list = orderPostageDao.findByExample(p);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("初始化模板：获取模板为空");
		}
		if (list.size() > 1) {
			throw new RuntimeException("初始化模板：获取模板不唯一");
		}
		return list.get(0);
	}

	/**
	 * 生成运费模板数据检测
	 * 
	 * @param cpt
	 */
	private void checkForCreatePostage(CreatePostageDTO cpt) {
		if (cpt == null) {
			throw new RuntimeException("初始化邮费模板：获取当前对象为空");
		}
		if (cpt.getSellerId() == null) {
			throw new RuntimeException("初始化邮费模板：卖家ID为空");
		}
		if (cpt.getDefaultFirstFee() == null || cpt.getDefaultFirstFee() < 0.0) {
			throw new RuntimeException("初始化邮费模板：默认首件邮费异常");
		}
		if (cpt.getDefaultOtherFee() == null || cpt.getDefaultOtherFee() < 0.0) {
			throw new RuntimeException("初始化邮费模板：默认续件邮费异常");
		}
		// 验证卖家ID不能重复
		OrderPostage p = new OrderPostage();
		p.setSellerId(cpt.getSellerId());
		List<OrderPostage> list = orderPostageDao.findByExample(p);
		if (list.size() > 0) {
			throw new RuntimeException("初始化邮费模板：一个卖家只有一个模板");
		}
	}

	/**
	 * 运费计算
	 * 
	 * @param cityId
	 * @param itemCount
	 * @param sellerId
	 * @return
	 */
	public Double calculatePostage(Integer cityId, Integer itemCount,
			Integer sellerId) {

		checkForCalculatePostage(cityId, itemCount, sellerId);
		log.debug("计算运费：参数检验通过");

		// 获取模板对象
		CreatePostageDTO cpt = new CreatePostageDTO();
		cpt.setSellerId(sellerId);
		OrderPostage opt = innerGetPostageBySellerId(cpt);

		// 验证订单状态
		if (OrderStatus.ORDER_STATUS_ABANDON == opt.getPostageStatus()) {
			throw new RuntimeException("计算运费：当前模板不可用");
		}
		log.info("获取了邮费模板" + opt.getPostageId());
		// 获取邮费模板明细
		List<OrderPostageDetail> list = getPostageDetails(cityId,
				opt.getPostageId());

		log.info("获取模板明细");
		// 未获取邮费明细，使用默认值
		if (list == null || list.isEmpty()) {
			log.info("按照默认设置计算运费");
			return innerCalculatePostage(itemCount, opt.getDefaultFirstFee(),
					opt.getDefaultOtherFee());
		}
		// 获取到了多条记录，异常数据
		if (list.size() > 1) {
			throw new RuntimeException("计算运费：获取邮费明细表异常");
		}
		// 只有一条数据
		log.info("指定模板计算运费");
		// 过期验证
		if (OrderStatus.ORDER_STATUS_ABANDON.equals(list.get(0)
				.getPostageDetailStatus())) {
			throw new RuntimeException("计算运费：模板明细已过期");
		}
		return innerCalculatePostage(itemCount, list.get(0).getFirstFee(), list
				.get(0).getOtherFee());
	}

	// ①检测传入数据的方法
	private void checkForCalculatePostage(Integer cityId, Integer itemCount,
			Integer sellerId) {
		// 验证
		if (itemCount == null || itemCount < 1) {
			throw new RuntimeException("计算运费失败：商品数量不能为空且必须大于0");
		}
		if (sellerId == null) {
			throw new RuntimeException("计算运费失败：商家Id不能为空");
		}
		if (cityId == null) {
			throw new RuntimeException("计算运费失败：城市Id不能为空");
		}
	}

	// ②获取订单明细表
	private List<OrderPostageDetail> getPostageDetails(int cityId, int postageId) {
		OrderPostageDetail opd = new OrderPostageDetail();
		opd.setCityId(cityId);
		opd.setPostageId(postageId);
		return orderPostageDetailDao.findByExample(opd);
	}

	// ③计算指定模板的计算运费的方式
	private double innerCalculatePostage(int itemCount, double firstFee,
			double otherFee) {
		if (itemCount == 1) {
			log.info("指定模板数量为1，运费=" + firstFee);
			return firstFee;
		} else {
			double postMoney = firstFee + otherFee * (itemCount - 1);
			log.info("指定模板数量大于1，运费=" + postMoney);
			return postMoney;
		}
	}

	public void setOrderPostageDao(OrderPostageDao orderPostageDao) {
		this.orderPostageDao = orderPostageDao;
	}

	public void setOrderPostageDetailDao(
			OrderPostageDetailDao orderPostageDetailDao) {
		this.orderPostageDetailDao = orderPostageDetailDao;
	}
}
