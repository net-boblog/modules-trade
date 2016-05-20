package com.xabaohui.modules.trade.daoimpl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sun.tools.corba.se.idl.constExpr.Expression;
import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.bean.OrderDetail;
import com.xabaohui.modules.trade.dao.OrderDetailDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderDetail entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.xabaohui.modules.trade.bean.OrderDetail
 * @author MyEclipse Persistence Tools
 */
public class OrderDetailDaoImpl extends HibernateDaoSupport implements OrderDetailDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderDetailDaoImpl.class);
	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#save(com.zis.order.bean.OrderDetail)
	 */
	public void save(OrderDetail transientInstance) {
		log.debug("saving OrderDetail instance");
		try {
			getHibernateTemplate().save(transientInstance);
			getSession().beginTransaction().commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * Ìõ¼þ²éÑ¯
	 * @param criteria
	 * @return
	 */
	public List findByCritera(DetachedCriteria criteria) {
		return this.getHibernateTemplate().findByCriteria(criteria);
	}
	
	public void update(OrderDetail transientInstance) {
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
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findById(java.lang.Integer)
	 */
	public OrderDetail findById(java.lang.Integer id) {
		log.debug("getting OrderDetail instance with id: " + id);
		try {
			OrderDetail instance = (OrderDetail) getHibernateTemplate().get(
					"com.xabaohui.modules.trade.bean.OrderDetail", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findByExample(com.zis.order.bean.OrderDetail)
	 */
	public List findByExample(OrderDetail instance) {
		log.debug("finding OrderDetail instance by example");
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
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findByProperty(java.lang.String, java.lang.Object)
	 */
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding OrderDetail instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from OrderDetail as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findByItemId(java.lang.Object)
	 */
	public List findByItemId(Object itemId) {
		return findByProperty(ITEM_ID, itemId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findBySkuId(java.lang.Object)
	 */
	public List findBySkuId(Object skuId) {
		return findByProperty(SKU_ID, skuId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findByItemName(java.lang.Object)
	 */
	public List findByItemName(Object itemName) {
		return findByProperty(ITEM_NAME, itemName);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findByItemCount(java.lang.Object)
	 */
	public List findByItemCount(Object itemCount) {
		return findByProperty(ITEM_COUNT, itemCount);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findByItemPrice(java.lang.Object)
	 */
	public List findByItemPrice(Object itemPrice) {
		return findByProperty(ITEM_PRICE, itemPrice);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDetailDao#findByOrderId(java.lang.Object)
	 */
	public List findByOrderId(Object orderId) {
		return findByProperty(ORDER_ID, orderId);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all OrderDetail instances");
		try {
			String queryString = "from OrderDetail";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OrderDetail merge(OrderDetail detachedInstance) {
		log.debug("merging OrderDetail instance");
		try {
			OrderDetail result = (OrderDetail) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OrderDetail instance) {
		log.debug("attaching dirty OrderDetail instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrderDetail instance) {
		log.debug("attaching clean OrderDetail instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OrderDetailDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (OrderDetailDao) ctx.getBean("OrderDetailDAO");
	}
}