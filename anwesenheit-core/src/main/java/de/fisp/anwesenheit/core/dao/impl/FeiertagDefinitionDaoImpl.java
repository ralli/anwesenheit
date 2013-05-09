package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.FeiertagDefinitionDao;
import de.fisp.anwesenheit.core.entities.FeiertagDefinition;

@Service
public class FeiertagDefinitionDaoImpl implements FeiertagDefinitionDao {
  private static final Logger log = LoggerFactory.getLogger(FeiertagDaoImpl.class);
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
  public List<FeiertagDefinition> findAll() {
    Query q = getCurrentSession().createQuery("from FeiertagDefinition o order by o.id");
    log.info("findAll");
    @SuppressWarnings("unchecked")
    List<FeiertagDefinition> list = q.list();
    log.info("findAll: count = {}", list.size());
    return list;
  }
}
