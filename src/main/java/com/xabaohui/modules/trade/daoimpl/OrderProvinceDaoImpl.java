package com.xabaohui.modules.trade.daoimpl;

import java.util.Date;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.OrderProvince;
import com.xabaohui.modules.trade.dao.OrderProvinceDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderProvince entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zis.trade.BO.OrderProvince
 * @author MyEclipse Persistence Tools
 */
public class OrderProvinceDaoImpl extends HibernateDaoSupport implements OrderProvinceDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderProvinceDaoImpl.class);
	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderProvinceDao#save(com.zis.trade.bean.OrderProvince)
	 */
	public void save(OrderProvince transientInstance) {
		log.debug("saving OrderProvince instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}


	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderProvinceDao#findById(java.lang.Integer)
	 */
	public OrderProvince findById(java.lang.Integer id) {
		log.debug("getting OrderProvince instance with id: " + id);
		try {
			OrderProvince instance = (OrderProvince) getHibernateTemplate()
					.get("com.xabaohui.modules.trade.bean.OrderProvince", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderProvinceDao#findByExample(com.zis.trade.bean.OrderProvince)
	 */

	public static OrderProvinceDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (OrderProvinceDao) ctx.getBean("OrderProvinceDAO");
	}

	public void update(OrderProvince transientInstance) {
		log.debug("update OrderProvince instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
		
	}
}