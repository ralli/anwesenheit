package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.SonderUrlaubArtDao;
import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;

@Service
public class SonderUrlaubArtDaoImpl implements SonderUrlaubArtDao {
  private Logger log = LoggerFactory.getLogger(SonderUrlaubArtDaoImpl.class);
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
  public List<SonderUrlaubArt> findAll() {
    Query query = getCurrentSession().createQuery("from SonderUrlaubArt s order by s.position");
    @SuppressWarnings("unchecked")
    List<SonderUrlaubArt> list = query.list();
    log.debug("findAll: {}", list);
    return list;
  }
  
  @Override
  public SonderUrlaubArt findById(String sonderUrlaubArtId) {
    Query query = getCurrentSession().createQuery("from SonderUrlaubArt s where s.sonderUrlaubArt=:sonderUrlaubArt");
    query.setString("sonderUrlaubArt", sonderUrlaubArtId);
    @SuppressWarnings("unchecked")
    List<SonderUrlaubArt> list = query.list();    
    SonderUrlaubArt result = list.isEmpty() ? null : list.get(0);
    log.debug("findById({})={}", sonderUrlaubArtId, result);
    return result;
  }
}
