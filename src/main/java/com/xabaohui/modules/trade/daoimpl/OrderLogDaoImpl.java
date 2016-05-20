package com.xabaohui.modules.trade.daoimpl;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.OrderAssign;
import com.xabaohui.modules.trade.bean.OrderLog;
import com.xabaohui.modules.trade.dao.OrderLogDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderLog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.xabaohui.modules.trade.bean.OrderLog
 * @author MyEclipse Persistence Tools
 */
public class OrderLogDaoImpl extends HibernateDaoSupport implements OrderLogDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderLogDaoImpl.class);
	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderLogDao#save(com.zis.order.bean.OrderLog)
	 */
	public void save(OrderLog transientInstance) {
		log.debug("saving OrderLog instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(OrderLog transientInstance) {
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
	 * @see com.zis.order.DaoImpl.OrderLogDao#findById(java.lang.Integer)
	 */
	public OrderLog findById(java.lang.Integer id) {
		log.debug("getting OrderLog instance with id: " + id);
		try {
			OrderLog instance = (OrderLog) getHibernateTemplate().get(
					"com.xabaohui.modules.trade.bean.OrderLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderLogDao#findByExample(com.zis.order.bean.OrderLog)
	 */
	public List findByExample(OrderLog instance) {
		log.debug("finding OrderLog instance by example");
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
	 * @see com.zis.order.DaoImpl.OrderLogDao#findByProperty(java.lang.String, java.lang.Object)
	 */
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding OrderLog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from OrderLog as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderLogDao#findByOrderId(java.lang.Object)
	 */
	public List findByOrderId(Object orderId) {
		return findByProperty(ORDER_ID, orderId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderLogDao#findByOperateDetail(java.lang.Object)
	 */
	public List findByOperateDetail(Object operateDetail) {
		return findByProperty(OPERATE_DETAIL, operateDetail);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderLogDao#findByOperaterId(java.lang.Object)
	 */
	public List findByOperaterId(Object operaterId) {
		return findByProperty(OPERATER_ID, operaterId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderLogDao#findByVersion(java.lang.Object)
	 */
	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all OrderLog instances");
		try {
			String queryString = "from OrderLog";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OrderLog merge(OrderLog detachedInstance) {
		log.debug("merging OrderLog instance");
		try {
			OrderLog result = (OrderLog) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OrderLog instance) {
		log.debug("attaching dirty OrderLog instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrderLog instance) {
		log.debug("attaching clean OrderLog instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OrderLogDao getFromApplicationContext(ApplicationContext ctx) {
		return (OrderLogDao) ctx.getBean("OrderLogDAO");
	}
}