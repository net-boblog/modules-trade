package com.xabaohui.modules.trade.daoimpl;

import java.util.Date;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderPostageDetail;
import com.xabaohui.modules.trade.dao.OrderPostageDetailDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderPostageDetail entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zis.trade.BO.OrderPostageDetail
 * @author MyEclipse Persistence Tools
 */
public class OrderPostageDetailDaoImpl extends HibernateDaoSupport implements OrderPostageDetailDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderPostageDetailDaoImpl.class);
	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderPostageDetailDao#save(com.zis.trade.bean.OrderPostageDetail)
	 */
	public void save(OrderPostageDetail transientInstance) {
		log.debug("saving OrderPostageDetail instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}


	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderPostageDetailDao#findById(java.lang.Integer)
	 */
	public OrderPostageDetail findById(java.lang.Integer id) {
		log.debug("getting OrderPostageDetail instance with id: " + id);
		try {
			OrderPostageDetail instance = (OrderPostageDetail) getHibernateTemplate()
					.get("com.xabaohui.modules.trade.bean.OrderPostageDetail", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OrderPostageDetail instance) {
		log.debug("finding Order instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public static OrderPostageDetailDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (OrderPostageDetailDao) ctx.getBean("OrderPostageDetailDAO");
	}

	public void update(OrderPostageDetail transientInstance) {
		log.debug("update OrderPostageDetail instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}

		
	}

}