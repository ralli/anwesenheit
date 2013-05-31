package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.AntragStatusDao;
import de.fisp.anwesenheit.core.entities.AntragStatus;

@Service
public class AntragStatusDaoImpl implements AntragStatusDao {
  private static final Logger log = LoggerFactory.getLogger(AntragStatusDaoImpl.class);
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
  public AntragStatus findById(String antragStatus) {
    Query query = getCurrentSession().createQuery(
            "from AntragStatus a where a.antragStatus=:antragStatus");
    query.setString("antragStatus", antragStatus);
    @SuppressWarnings("unchecked")
    List<AntragStatus> list = query.list();
    AntragStatus result = list.isEmpty() ? null : list.get(0);
    log.debug("findById({})={}", antragStatus, result);
    return result;
  }

  @Override
  public List<AntragStatus> findAll() {
    Query query = getCurrentSession().createQuery(
            "from AntragStatus a order by a.position");
    @SuppressWarnings("unchecked")
    List<AntragStatus> list = query.list();
    log.debug("findAll: count = {}", list.size());
    return list;
  }
}
