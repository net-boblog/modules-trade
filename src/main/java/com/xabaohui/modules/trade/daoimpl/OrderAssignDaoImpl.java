package com.xabaohui.modules.trade.daoimpl;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderAssign;
import com.xabaohui.modules.trade.dao.OrderAssignDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderAssign entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.xabaohui.modules.trade.bean.OrderAssign
 * @author MyEclipse Persistence Tools
 */
public class OrderAssignDaoImpl extends HibernateDaoSupport implements OrderAssignDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderAssignDaoImpl.class);
	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#save(com.zis.order.bean.OrderAssign)
	 */
	public void save(OrderAssign transientInstance) {
		log.debug("saving OrderAssign instance");
		try {
			getHibernateTemplate().save(transientInstance);
			getSession().beginTransaction().commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(OrderAssign transientInstance) {
		log.debug("update Order instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#findById(java.lang.Integer)
	 */
	public OrderAssign findById(java.lang.Integer id) {
		log.debug("getting OrderAssign instance with id: " + id);
		try {
			OrderAssign instance = (OrderAssign) getHibernateTemplate().get(
					"com.xabaohui.modules.trade.bean.OrderAssign", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#findByExample(com.zis.order.bean.OrderAssign)
	 */
	public List findByExample(OrderAssign instance) {
		log.debug("finding OrderAssign instance by example");
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

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#findByProperty(java.lang.String, java.lang.Object)
	 */
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding OrderAssign instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from OrderAssign as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#findByOrderId(java.lang.Object)
	 */
	public List findByOrderId(Object orderId) {
		return findByProperty(ORDER_ID, orderId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#findByOrderDetailId(java.lang.Object)
	 */
	public List findByOrderDetailId(Object orderDetailId) {
		return findByProperty(ORDER_DETAIL_ID, orderDetailId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#findByOrderDetailCount(java.lang.Object)
	 */
	public List findByOrderDetailCount(Object orderDetailCount) {
		return findByProperty(ORDER_DETAIL_COUNT, orderDetailCount);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderAssignDao#findByStoreId(java.lang.Object)
	 */
	public List findByStoreId(Object storeId) {
		return findByProperty(STORE_ID, storeId);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all OrderAssign instances");
		try {
			String queryString = "from OrderAssign";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OrderAssign merge(OrderAssign detachedInstance) {
		log.debug("merging OrderAssign instance");
		try {
			OrderAssign result = (OrderAssign) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OrderAssign instance) {
		log.debug("attaching dirty OrderAssign instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrderAssign instance) {
		log.debug("attaching clean OrderAssign instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OrderAssignDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (OrderAssignDao) ctx.getBean("OrderAssignDAO");
	}
}