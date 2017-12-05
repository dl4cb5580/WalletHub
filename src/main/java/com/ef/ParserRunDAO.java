package com.ef;
// Generated Dec 4, 2017 3:50:19 AM by Hibernate Tools 5.2.6.Final

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Home object for domain model class ParserRun.
 * @see com.ef.ParserRun
 * @author amit.kumar
 */
public class ParserRunDAO {

	private static final Logger logger = Logger.getLogger(ParserRunDAO.class);

	public void persist(ParserRun parserRun) {
		logger.debug("persisting parserRun instance");
		Session session = null;
	    Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(parserRun); 
			tx.commit();
			logger.debug("persist successful");
		} catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
	
	@SuppressWarnings("rawtypes")
	public int fetchMaxRunId() {
		logger.debug("fetching max run id");
		Session session = null;
	    Transaction tx = null;
	    int maxId = 0;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			List list = session.createNativeQuery("SELECT max(run_id) FROM Parser_Run").list();
			if(list != null && list.size() >= 1 && list.get(0) != null) {
				maxId = ((Integer) list.get(0)).intValue();
			}
			tx.commit();
			logger.debug("fecth successful");
		} catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return maxId;
	}

}
