package com.xabaohui.modules.trade.daoimpl;

import java.util.Date;
import java.util.List;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xabaohui.modules.trade.bean.OrderCity;
import com.xabaohui.modules.trade.bean.OrderDetail;
import com.xabaohui.modules.trade.dao.OrderCityDao;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderCity entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zis.trade.BO.OrderCity
 * @author MyEclipse Persistence Tools
 */
public class OrderCityDaoImpl extends HibernateDaoSupport implements OrderCityDao {
	private static final Logger log = LoggerFactory
			.getLogger(OrderCityDaoImpl.class);
	// property constants
	public static final String CITY_NAME = "cityName";
	public static final String PROVINCE_ID = "provinceId";
	public static final String VERSION = "version";

	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderCityDao#save(com.zis.trade.bean.OrderCity)
	 */
	public void save(OrderCity transientInstance) {
		log.debug("saving OrderCity instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderCityDao#findById(java.lang.Integer)
	 */
	public OrderCity findById(java.lang.Integer id) {
		log.debug("getting OrderCity instance with id: " + id);
		try {
			OrderCity instance = (OrderCity) getHibernateTemplate().get(
					"com.xabaohui.modules.trade.bean.OrderCity", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.zis.trade.DaoImpl.OrderCityDao#findByExample(com.zis.trade.bean.OrderCity)
	 */
	public List findByExample(OrderCity instance) {
		log.debug("finding OrderCity instance by example");
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

		public static OrderCityDao getFromApplicationContext(ApplicationContext ctx) {
		return (OrderCityDao) ctx.getBean("OrderCityDAO");
	}

		public void update(OrderCity transientInstance) {
			log.debug("update OrderCity instance");
			try {
				getHibernateTemplate().update(transientInstance);
				log.debug("update successful");
			} catch (RuntimeException re) {
				log.error("update failed", re);
				throw re;
			}
		}
}