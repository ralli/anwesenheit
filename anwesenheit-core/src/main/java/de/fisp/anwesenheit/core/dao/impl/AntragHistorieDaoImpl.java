package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.entities.AntragHistorie;

@Service
public class AntragHistorieDaoImpl implements AntragHistorieDao {
  private Logger logger = LoggerFactory.getLogger(AntragHistorieDaoImpl.class);
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
  public List<AntragHistorie> findByAntrag(long antragId) {
    Query query = getCurrentSession().createQuery("from AntragHistorie a where a.antragId=:antragId order by a.zeitpunkt desc");
    query.setLong("antragId", antragId);
    @SuppressWarnings("unchecked")
    List<AntragHistorie> list = query.list();
    logger.debug("findByAntrag({}): count = {}", antragId, list.size());
    return list;
  }

  @Override
  public AntragHistorie findById(long id) {
    Query query = getCurrentSession().createQuery("from AntragHistorie ah join fetch ah.benutzer where ah.id = :id");
    query.setLong("id", id);
    @SuppressWarnings("unchecked")
    List<AntragHistorie> list = query.list();
    AntragHistorie result = list.isEmpty() ? null : list.get(0);
    logger.debug("findById({}) = {}", result);
    return result;
  }

  @Override
  public void insert(AntragHistorie antragHistorie) {
    logger.debug("insert({})", antragHistorie);
    getCurrentSession().save(antragHistorie);
  }

  @Override
  public void delete(AntragHistorie antragHistorie) {
    logger.debug("delete({})", antragHistorie);
    getCurrentSession().delete(antragHistorie);
  }
}
