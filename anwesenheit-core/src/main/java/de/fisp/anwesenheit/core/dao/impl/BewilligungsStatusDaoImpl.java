package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.BewilligungsStatusDao;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;

@Service
public class BewilligungsStatusDaoImpl implements BewilligungsStatusDao {
	private Logger log = LoggerFactory.getLogger(AntragStatusDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		Session session = sessionFactory.getCurrentSession();
		if (session == null) {
			throw new RuntimeException("no current session");
		}
		return session;
	}

	@Override
	public List<BewilligungsStatus> findAll() {
		Query query = getCurrentSession().createQuery(
				"from BewilligungsStatus b order by b.position");
		@SuppressWarnings("unchecked")
		List<BewilligungsStatus> list = query.list();
		log.debug("findAll: {}", list);
		return list;
	}

}
