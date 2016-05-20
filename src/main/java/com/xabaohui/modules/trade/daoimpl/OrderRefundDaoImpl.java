package com.xabaohui.modules.trade.daoimpl;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.OrderLog;
import com.xabaohui.modules.trade.bean.OrderRefund;
import com.xabaohui.modules.trade.dao.OrderRefundDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderRefund entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.xabaohui.modules.trade.bean.OrderRefund
 * @author MyEclipse Persistence Tools
 */
public class OrderRefundDaoImpl extends HibernateDaoSupport implements OrderRefundDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderRefundDaoImpl.class);
	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderRefundDao#save(com.zis.order.bean.OrderRefund)
	 */
	public void save(OrderRefund transientInstance) {
		log.debug("saving OrderRefund instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(OrderRefund transientInstance) {
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
	 * @see com.zis.order.DaoImpl.OrderRefundDao#findById(java.lang.Integer)
	 */
	public OrderRefund findById(java.lang.Integer id) {
		log.debug("getting OrderRefund instance with id: " + id);
		try {
			OrderRefund instance = (OrderRefund) getHibernateTemplate().get(
					"com.xabaohui.modules.trade.bean.OrderRefund", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderRefundDao#findByExample(com.zis.order.bean.OrderRefund)
	 */
	public List findByExample(OrderRefund instance) {
		log.debug("finding OrderRefund instance by example");
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
	 * @see com.zis.order.DaoImpl.OrderRefundDao#findByProperty(java.lang.String, java.lang.Object)
	 */
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding OrderRefund instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from OrderRefund as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderRefundDao#findByOrderId(java.lang.Object)
	 */
	public List findByOrderId(Object orderId) {
		return findByProperty(ORDER_ID, orderId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderRefundDao#findByOrderDetailId(java.lang.Object)
	 */
	public List findByOrderDetailId(Object orderDetailId) {
		return findByProperty(ORDER_DETAIL_ID, orderDetailId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderRefundDao#findByRefundMoney(java.lang.Object)
	 */
	public List findByRefundMoney(Object refundMoney) {
		return findByProperty(REFUND_MONEY, refundMoney);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderRefundDao#findByRefundStatus(java.lang.Object)
	 */
	public List findByRefundStatus(Object refundStatus) {
		return findByProperty(REFUND_STATUS, refundStatus);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all OrderRefund instances");
		try {
			String queryString = "from OrderRefund";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OrderRefund merge(OrderRefund detachedInstance) {
		log.debug("merging OrderRefund instance");
		try {
			OrderRefund result = (OrderRefund) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OrderRefund instance) {
		log.debug("attaching dirty OrderRefund instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrderRefund instance) {
		log.debug("attaching clean OrderRefund instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OrderRefundDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (OrderRefundDao) ctx.getBean("OrderRefundDAO");
	}
}