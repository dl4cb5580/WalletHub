package com.ef;
// Generated Dec 4, 2017 3:50:19 AM by Hibernate Tools 5.2.6.Final

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * Home object for domain model class ServerLog.
 * @see com.ef.ServerLog
 * @author amit.kumar
 */

public class ServerLogDAO {

	private static final Logger logger = Logger.getLogger(ServerLogDAO.class);

	public void deleteAll() {
		logger.debug("deleting Serverlog instance");
		Session session = null;
	    Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.createQuery("DELETE FROM ServerLog").executeUpdate(); 
			tx.commit();
			logger.debug("delete successful");
		} catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
	
	@SuppressWarnings("unchecked")
	public List<ServerLog> findByQuery(String queryStr) {
		logger.debug("quering Serverlog instance");
		Session session = null;
	    Transaction tx = null;
	    Query<ServerLog> query = null; 
	    List<ServerLog> list = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			query = session.createQuery(queryStr); 
			list = query.list();
			tx.commit();
			logger.debug("query successful");
		} catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return list;
	}
	
	public void persist(ServerLog serverLog) {
		logger.debug("persisting Serverlog instance");
		Session session = null;
	    Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(serverLog); 
			tx.commit();
			logger.debug("persist successful");
		} catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
	
	public void persistAll(List<ServerLog> list) {
		logger.debug("persisting all Serverlog instance");
		System.out.println("Loading log file data into Database using Hibernate...");
		Session session = null;
	    Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				session.save(list.get(i));
				if(i % 50 == 0) {
					session.flush();
					session.clear();
				}
			} 
			tx.commit();
			logger.debug("persist all serverlog instance successful");
		} catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}

	public ServerLog findById(Integer id) {
		logger.debug("getting Serverlog instance with id: " + id);
		Session session = null;
	    Transaction tx = null;
	    ServerLog serverLog = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			serverLog = session.get(ServerLog.class, id);
			logger.debug("get successful");
		} catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return serverLog;
	}

}
