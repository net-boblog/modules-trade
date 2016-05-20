package com.xabaohui.modules.trade.daoimpl;

import java.util.Date;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderPostage;
import com.xabaohui.modules.trade.dao.OrderPostageDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderPostage entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zis.trade.BO.OrderPostage
 * @author MyEclipse Persistence Tools
 */
public class OrderPostageDaoImpl extends HibernateDaoSupport implements OrderPostageDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderPostageDaoImpl.class);
	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderPostageDao#save(com.zis.trade.bean.OrderPostage)
	 */
	public void save(OrderPostage transientInstance) {
		log.debug("saving OrderPostage instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}


	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderPostageDao#findById(java.lang.Integer)
	 */
	public OrderPostage findById(java.lang.Integer id) {
		log.debug("getting OrderPostage instance with id: " + id);
		try {
			OrderPostage instance = (OrderPostage) getHibernateTemplate().get(
					"com.xabaohui.modules.trade.bean.OrderPostage", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}


	public static OrderPostageDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (OrderPostageDao) ctx.getBean("OrderPostageDAO");
	}
	
	public List findByExample(OrderPostage instance) {
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

	
	public void update(OrderPostage transientInstance) {
		log.debug("update OrderPostage instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	@Override
	public List findBySellerId(Integer sellerId) {
		// TODO Auto-generated method stub
		return null;
	}
	
		
	
}