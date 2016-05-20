package com.xabaohui.modules.trade.daoimpl;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.Order;
import com.xabaohui.modules.trade.dao.OrderDao;

/**
 * A data access object (DAO) providing persistence and search support for Order
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.xabaohui.modules.trade.bean.Order
 * @author MyEclipse Persistence Tools
 */
public class OrderDaoImpl extends HibernateDaoSupport implements OrderDao {
	private static final Logger log = LoggerFactory.getLogger(OrderDaoImpl.class);
	public static final String VERSION = "version";

	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#save(com.zis.order.bean.Order)
	 */
	public void save(Order transientInstance) {
		log.debug("saving Order instance");
		try {
			getHibernateTemplate().save(transientInstance);
			getSession().beginTransaction().commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(Order transientInstance) {
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
	 * @see com.zis.order.DaoImpl.OrderDao#findById(java.lang.Integer)
	 */
	public Order findById(java.lang.Integer id) {
		log.debug("getting Order instance with id: " + id);
		try {
			Order instance = (Order) getHibernateTemplate().get(
					"com.xabaohui.modules.trade.bean.Order", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
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
	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByExample(com.zis.order.bean.Order)
	 */
	public List findByExample(Order instance) {
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

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByProperty(java.lang.String, java.lang.Object)
	 */
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Order instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Order as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findBySenderName(java.lang.Object)
	 */
	public List findBySenderName(Object senderName) {
		return findByProperty(SENDER_NAME, senderName);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findBySenderPhone(java.lang.Object)
	 */
	public List findBySenderPhone(Object senderPhone) {
		return findByProperty(SENDER_PHONE, senderPhone);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findBySenderAddr(java.lang.Object)
	 */
	public List findBySenderAddr(Object senderAddr) {
		return findByProperty(SENDER_ADDR, senderAddr);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByReceiverName(java.lang.Object)
	 */
	public List findByReceiverName(Object receiverName) {
		return findByProperty(RECEIVER_NAME, receiverName);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByReceiverPhone(java.lang.Object)
	 */
	public List findByReceiverPhone(Object receiverPhone) {
		return findByProperty(RECEIVER_PHONE, receiverPhone);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByReceiverAddr(java.lang.Object)
	 */
	public List findByReceiverAddr(Object receiverAddr) {
		return findByProperty(RECEIVER_ADDR, receiverAddr);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByPostId(java.lang.Object)
	 */
	public List findByPostId(Object postId) {
		return findByProperty(POST_ID, postId);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByPostCompany(java.lang.Object)
	 */
	public List findByPostCompany(Object postCompany) {
		return findByProperty(POST_COMPANY, postCompany);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByPostMoney(java.lang.Object)
	 */
	public List findByPostMoney(Object postMoney) {
		return findByProperty(POST_MONEY, postMoney);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByOrderMoney(java.lang.Object)
	 */
	public List findByOrderMoney(Object orderMoney) {
		return findByProperty(ORDER_MONEY, orderMoney);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByOrderStatus(java.lang.Object)
	 */
	public List findByOrderStatus(Object orderStatus) {
		return findByProperty(ORDER_STATUS, orderStatus);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findBySalerRemark(java.lang.Object)
	 */
	public List findBySalerRemark(Object salerRemark) {
		return findByProperty(SALER_REMARK, salerRemark);
	}

	/* (non-Javadoc)
	 * @see com.zis.order.DaoImpl.OrderDao#findByBuyerMessage(java.lang.Object)
	 */
	public List findByBuyerMessage(Object buyerMessage) {
		return findByProperty(BUYER_MESSAGE, buyerMessage);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all Order instances");
		try {
			String queryString = "from Order";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Order merge(Order detachedInstance) {
		log.debug("merging Order instance");
		try {
			Order result = (Order) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Order instance) {
		log.debug("attaching dirty Order instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Order instance) {
		log.debug("attaching clean Order instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OrderDao getFromApplicationContext(ApplicationContext ctx) {
		return (OrderDao) ctx.getBean("OrderDAO");
	}
}