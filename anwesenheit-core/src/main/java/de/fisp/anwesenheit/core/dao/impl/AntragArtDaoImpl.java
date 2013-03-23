package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.AntragArtDao;
import de.fisp.anwesenheit.core.entities.AntragArt;

@Service
public class AntragArtDaoImpl implements AntragArtDao {
  private Logger log = LoggerFactory.getLogger(AntragArtDaoImpl.class);
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
  public List<AntragArt> findAll() {
    Session session = getCurrentSession();
    Query query = session.createQuery("from AntragArt a order by a.position");
    @SuppressWarnings("unchecked")
    List<AntragArt> list = query.list();
    log.debug("findAll: count = {}", list.size());
    return list;
  }

}
