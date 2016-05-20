package com.xabaohui.modules.trade.bo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.xabaohui.modules.trade.bean.OrderPostage;
import com.xabaohui.modules.trade.bean.OrderPostageDetail;
import com.xabaohui.modules.trade.bean.OrderStatus;
import com.xabaohui.modules.trade.dao.OrderPostageDao;
import com.xabaohui.modules.trade.dao.OrderPostageDetailDao;
import com.xabaohui.modules.trade.dto.CreatePostageDTO;
import com.xabaohui.modules.trade.dto.PostageDetailDTO;

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:applicationContext.xml")
public class PostageBOTest {

	@Resource
	private PostageBO postage;
	@Resource
	private OrderPostageDao orderPostageDao;
	@Resource
	private OrderPostageDetailDao orderPostageDetailDao;

	/**
	 * ��ʼ��ģ�壺ʧ��
	 */
	@Test(expected = RuntimeException.class)
	public void testCreatePostageFail() {
		CreatePostageDTO cpt = null;
		postage.createPostage(cpt);
	}

	/**
	 * ��ʼ��ģ�壺�ɹ�
	 */
	@Test
	public void testCreatePostage() {
		CreatePostageDTO cpt = innerCreatePostaget();
		try {
			OrderPostage orderPostage = postage.createPostage(cpt);
			OrderPostage dbOrderPostage = orderPostageDao.findById(orderPostage
					.getPostageId());
			// �Ƚ�
			Assert.assertNotNull(dbOrderPostage);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	
	/**
	 * ��ʼ��ģ��:δ����ģ����ϸ
	 */
	@Test(expected = RuntimeException.class)
	public void testCreatePostageDetailFail() {
		CreatePostageDTO cpt = new CreatePostageDTO();
		cpt.setDefaultFirstFee(2.0);
		cpt.setDefaultOtherFee(1.0);
		cpt.setSellerId(1);
		cpt.setList(null);

		OrderPostage orderPostage = postage.createPostage(cpt);
		OrderPostage dbOrderPostage = orderPostageDao.findById(orderPostage
				.getPostageId());
		// �Ƚ�
		Assert.assertNotNull(dbOrderPostage);
	}

	/**
	 * ʧ�ܣ���Ʒ����Ϊ0
	 */
	@Test(expected = RuntimeException.class)
	public void testGetPostageDataCheck() {
		// ׼������ ����ģ��
		CreatePostageDTO cpt = innerCreatePostaget();
		postage.createPostage(cpt);

		Integer sellerId = cpt.getSellerId();
		Integer cityId = cpt.getList().get(0).getCityId();
		Integer itemCount = 0;
		// ִ�в���
		postage.calculatePostage(cityId, itemCount, sellerId);

	}

	/**
	 * ģ����ϸ��ֻ�����أ�
	 */
	@Test
	public void testGetPostageCount() {

		// ׼������ ����ģ��
		CreatePostageDTO cpt = innerCreatePostaget();
		postage.createPostage(cpt);

		Integer sellerId = cpt.getSellerId();
		Integer cityId = cpt.getList().get(0).getCityId();
		Integer itemCount = 1;

		try {
			Double result = postage.calculatePostage(cityId, itemCount, sellerId);
			Assert.assertEquals((Object) 5.0, result);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * ģ����ϸ�������أ�
	 */
	@Test
	public void testGetPostageCounts() {

		// ׼������ ����ģ��
		CreatePostageDTO cpt = innerCreatePostaget();
		postage.createPostage(cpt);

		Integer sellerId = cpt.getSellerId();
		Integer cityId = cpt.getList().get(0).getCityId();
		Integer itemCount = 10;

		try {
			Double result = postage.calculatePostage(cityId, itemCount, sellerId);
			Assert.assertEquals((Object) 14.0, result);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Ĭ��ģ�壬ֻ������
	 */
	@Test
	public void testGetPostageDefault() {

		// ׼������ ����ģ��
		CreatePostageDTO cpt = innerCreatePostaget();
		postage.createPostage(cpt);
		
		Integer itemCount = 1;
		Integer sellerId = cpt.getSellerId();
		//������ϸ�����ڵĳ��е�Id
		Integer cityId = 3;
		List<PostageDetailDTO> list = cpt.getList();
		for (PostageDetailDTO postageDetailDTO : list) {
			if(cityId==postageDetailDTO.getCityId()){
				cityId = (int)(Math.random()*1000);
			}else{
				cityId = 3;
			}
		}
		try {
			Double result = postage.calculatePostage(cityId, itemCount, sellerId);
			Assert.assertEquals((Object) 2.0, result);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Ĭ��ģ�壬������
	 */
	@Test
	public void testGetPostageDefaults() {
		
		// ׼������ ����ģ��
		CreatePostageDTO cpt = innerCreatePostaget();
		postage.createPostage(cpt);

		Integer sellerId = cpt.getSellerId();
		Integer cityId = cpt.getList().get(0).getCityId();
		Integer itemCount = 3;
		
		try {
			Double result = postage.calculatePostage(cityId, itemCount, sellerId);
			Assert.assertEquals((Object) 7.0, result);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	/**
	 * ����ģ�� ����׼��
	 * 
	 * @return
	 */
	private CreatePostageDTO innerCreatePostaget() {
		CreatePostageDTO cpt = new CreatePostageDTO();
		cpt.setDefaultFirstFee(2.0);
		cpt.setDefaultOtherFee(1.0);
		Integer sellerId = (int) (Math.random() * 1000);
		cpt.setSellerId(sellerId);

		// ����ģ����ϸ����
		List<PostageDetailDTO> list = new ArrayList<PostageDetailDTO>();
		PostageDetailDTO opd = new PostageDetailDTO();
		opd.setCityId(3);
		opd.setFirstFee(3.0);
		opd.setOtherFee(2.0);
		opd.setPostageDetailStatus(OrderStatus.ORDER_STATUS_USEABLE);

		PostageDetailDTO opds = new PostageDetailDTO();
		opds.setCityId(4);
		opds.setFirstFee(5.0);
		opds.setOtherFee(1.0);
		opds.setPostageDetailStatus(OrderStatus.ORDER_STATUS_USEABLE);

		list.add(opds);
		list.add(opd);
		cpt.setList(list);
		return cpt;

	}

}
