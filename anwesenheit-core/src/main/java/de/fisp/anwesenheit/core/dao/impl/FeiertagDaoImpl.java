package de.fisp.anwesenheit.core.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.FeiertagDao;
import de.fisp.anwesenheit.core.entities.Feiertag;

@Service
public class FeiertagDaoImpl implements FeiertagDao {
  @Autowired
  private SessionFactory sessionFactory;
  private static final Logger log = LoggerFactory.getLogger(FeiertagDaoImpl.class);

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
  public List<Feiertag> findAll() {
    Query q = getCurrentSession().createQuery("from Feiertag f order by datum");
    @SuppressWarnings("unchecked")
    List<Feiertag> list = q.list();
    log.info("findAll: count = {}", list.size());
    return list;
  }

  private Date getStartDate(int year) {
    Calendar cal = Calendar.getInstance();
    cal.clear();
    cal.set(year, Calendar.JANUARY, 1, 0, 0, 0);
    return cal.getTime();
  }

  private Date getEndDate(int year) {
    Calendar cal = Calendar.getInstance();
    cal.clear();
    cal.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
    return cal.getTime();
  }

  @Override
  public List<Feiertag> findByYear(int year) {
    Query q = getCurrentSession().createQuery("from Feiertag f where f.datum between :start and :end order by f.datum");
    q.setParameter("start", getStartDate(year));
    q.setParameter("end", getEndDate(year));
    @SuppressWarnings("unchecked")
    List<Feiertag> list = q.list();
    log.info("findByYear({}) = {}", year, list);
    return list;
  }

  @Override
  public void deleteByYear(int year) {
    Query q = getCurrentSession().createQuery(
            "delete from Feiertag f where f.datum between :start and :end and f.definitionId is not null");
    q.setParameter("start", getStartDate(year));
    q.setParameter("end", getEndDate(year));
    int count = q.executeUpdate();
    log.info("deleteByYear({}): {} Feiertage gelöscht", year, count);
  }

  @Override
  public void insert(Feiertag feiertag) {
    log.info("insert: {}", feiertag);
    getCurrentSession().save(feiertag);
  }

  @Override
  public void update(Feiertag feiertag) {
    log.info("update: {}", feiertag);
    getCurrentSession().update(feiertag);
  }

  @Override
  public void delete(Feiertag feiertag) {
    log.info("delete({}", feiertag);
    getCurrentSession().delete(feiertag);
  }

  @Override
  public List<Feiertag> findByZeitraum(Date von, Date bis) {
    Query q = getCurrentSession().createQuery("from Feiertag f where f.datum >= :start and datum <= :end");
    q.setParameter("start", von);
    q.setParameter("end", bis);
    @SuppressWarnings("unchecked")
    List<Feiertag> list = q.list();
    log.info("findByZeitraum({}, {}): count={} Feiertage gelöscht", von, bis, list.size());
    return list;
  }
}
