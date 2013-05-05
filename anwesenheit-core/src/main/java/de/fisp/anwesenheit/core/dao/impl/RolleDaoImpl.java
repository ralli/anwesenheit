package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.RolleDao;
import de.fisp.anwesenheit.core.entities.Rolle;

@Service
public class RolleDaoImpl implements RolleDao {
  private Logger log = LoggerFactory.getLogger(RolleDaoImpl.class);
  @Autowired
  private SessionFactory sessionFactory;

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  private Session getCurrentSession() {
    Session session = sessionFactory.getCurrentSession();
    if (session == null) {
    	throw new RuntimeException("No current session found!");
    }
    return session;
  }

	@Override
	public List<Rolle> findAll() {
		Query query = getCurrentSession().createQuery(
				"from Rolle r order by r.position");
		@SuppressWarnings("unchecked")
		List<Rolle> list = query.list();
		log.debug("findAll: count = {}", list);
		return list;
	}
}
