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
 * �˷Ѽ���ģ��
 * 
 * @author Zhang Hong wei
 * 
 */
public class PostageBO {

	private OrderPostageDao orderPostageDao;
	private OrderPostageDetailDao orderPostageDetailDao;
	private Logger log = LoggerFactory.getLogger(PostageBO.class);

	/**
	 * ����ģ�弰ģ����ϸ
	 * 
	 * @param cpt
	 * @return
	 */
	public OrderPostage createPostage(CreatePostageDTO cpt) {

		checkForCreatePostage(cpt);
		log.info("���ͨ��");

		OrderPostage postage = new OrderPostage();

		// �˷�Ĭ��ģ���ʼ��
		BeanUtils.copyProperties(cpt, postage);
		Date date = new Date();
		postage.setGmtCreate(date);
		postage.setGmtModify(date);
		postage.setPostageStatus(OrderStatus.ORDER_STATUS_USEABLE);
		postage.setVersion(1);

		orderPostageDao.save(postage);
		log.info("Ĭ��ģ���ʼ���ɹ�");

		// ������ϸģ��
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
			log.info("ģ����ϸ��ӳɹ�");
		}
		return postage;
	}

	/**
	 * ��������ID��ȡ�ʷ�ģ��
	 * 
	 * @param cpt
	 * @return
	 */
	private OrderPostage innerGetPostageBySellerId(CreatePostageDTO cpt) {

		OrderPostage p = new OrderPostage();
		p.setSellerId(cpt.getSellerId());
		List<OrderPostage> list = orderPostageDao.findByExample(p);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("��ʼ��ģ�壺��ȡģ��Ϊ��");
		}
		if (list.size() > 1) {
			throw new RuntimeException("��ʼ��ģ�壺��ȡģ�岻Ψһ");
		}
		return list.get(0);
	}

	/**
	 * �����˷�ģ�����ݼ��
	 * 
	 * @param cpt
	 */
	private void checkForCreatePostage(CreatePostageDTO cpt) {
		if (cpt == null) {
			throw new RuntimeException("��ʼ���ʷ�ģ�壺��ȡ��ǰ����Ϊ��");
		}
		if (cpt.getSellerId() == null) {
			throw new RuntimeException("��ʼ���ʷ�ģ�壺����IDΪ��");
		}
		if (cpt.getDefaultFirstFee() == null || cpt.getDefaultFirstFee() < 0.0) {
			throw new RuntimeException("��ʼ���ʷ�ģ�壺Ĭ���׼��ʷ��쳣");
		}
		if (cpt.getDefaultOtherFee() == null || cpt.getDefaultOtherFee() < 0.0) {
			throw new RuntimeException("��ʼ���ʷ�ģ�壺Ĭ�������ʷ��쳣");
		}
		// ��֤����ID�����ظ�
		OrderPostage p = new OrderPostage();
		p.setSellerId(cpt.getSellerId());
		List<OrderPostage> list = orderPostageDao.findByExample(p);
		if (list.size() > 0) {
			throw new RuntimeException("��ʼ���ʷ�ģ�壺һ������ֻ��һ��ģ��");
		}
	}

	/**
	 * �˷Ѽ���
	 * 
	 * @param cityId
	 * @param itemCount
	 * @param sellerId
	 * @return
	 */
	public Double calculatePostage(Integer cityId, Integer itemCount,
			Integer sellerId) {

		checkForCalculatePostage(cityId, itemCount, sellerId);
		log.debug("�����˷ѣ���������ͨ��");

		// ��ȡģ�����
		CreatePostageDTO cpt = new CreatePostageDTO();
		cpt.setSellerId(sellerId);
		OrderPostage opt = innerGetPostageBySellerId(cpt);

		// ��֤����״̬
		if (OrderStatus.ORDER_STATUS_ABANDON == opt.getPostageStatus()) {
			throw new RuntimeException("�����˷ѣ���ǰģ�岻����");
		}
		log.info("��ȡ���ʷ�ģ��" + opt.getPostageId());
		// ��ȡ�ʷ�ģ����ϸ
		List<OrderPostageDetail> list = getPostageDetails(cityId,
				opt.getPostageId());

		log.info("��ȡģ����ϸ");
		// δ��ȡ�ʷ���ϸ��ʹ��Ĭ��ֵ
		if (list == null || list.isEmpty()) {
			log.info("����Ĭ�����ü����˷�");
			return innerCalculatePostage(itemCount, opt.getDefaultFirstFee(),
					opt.getDefaultOtherFee());
		}
		// ��ȡ���˶�����¼���쳣����
		if (list.size() > 1) {
			throw new RuntimeException("�����˷ѣ���ȡ�ʷ���ϸ���쳣");
		}
		// ֻ��һ������
		log.info("ָ��ģ������˷�");
		// ������֤
		if (OrderStatus.ORDER_STATUS_ABANDON.equals(list.get(0)
				.getPostageDetailStatus())) {
			throw new RuntimeException("�����˷ѣ�ģ����ϸ�ѹ���");
		}
		return innerCalculatePostage(itemCount, list.get(0).getFirstFee(), list
				.get(0).getOtherFee());
	}

	// �ټ�⴫�����ݵķ���
	private void checkForCalculatePostage(Integer cityId, Integer itemCount,
			Integer sellerId) {
		// ��֤
		if (itemCount == null || itemCount < 1) {
			throw new RuntimeException("�����˷�ʧ�ܣ���Ʒ��������Ϊ���ұ������0");
		}
		if (sellerId == null) {
			throw new RuntimeException("�����˷�ʧ�ܣ��̼�Id����Ϊ��");
		}
		if (cityId == null) {
			throw new RuntimeException("�����˷�ʧ�ܣ�����Id����Ϊ��");
		}
	}

	// �ڻ�ȡ������ϸ��
	private List<OrderPostageDetail> getPostageDetails(int cityId, int postageId) {
		OrderPostageDetail opd = new OrderPostageDetail();
		opd.setCityId(cityId);
		opd.setPostageId(postageId);
		return orderPostageDetailDao.findByExample(opd);
	}

	// �ۼ���ָ��ģ��ļ����˷ѵķ�ʽ
	private double innerCalculatePostage(int itemCount, double firstFee,
			double otherFee) {
		if (itemCount == 1) {
			log.info("ָ��ģ������Ϊ1���˷�=" + firstFee);
			return firstFee;
		} else {
			double postMoney = firstFee + otherFee * (itemCount - 1);
			log.info("ָ��ģ����������1���˷�=" + postMoney);
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
