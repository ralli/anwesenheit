package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.BewilligungsFilter;
import de.fisp.anwesenheit.core.entities.Bewilligung;

@Service
public class BewilligungDaoImpl implements BewilligungDao {
  private Logger log = LoggerFactory.getLogger(BewilligungDaoImpl.class);
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
  public List<Bewilligung> findByAntrag(long antragId) {
    Query query = getCurrentSession().createQuery(
        "from Bewilligung b join fetch b.benutzer join fetch b.bewilligungsStatus where b.antragId=:antragId order by b.position");
    query.setLong("antragId", antragId);
    @SuppressWarnings("unchecked")
    List<Bewilligung> list = query.list();
    log.debug("findByAntrag({}) = {}", antragId, list);
    return list;
  }

  @Override
  public Bewilligung findById(long id) {
    Query query = getCurrentSession().createQuery(
        "from Bewilligung b join fetch b.benutzer join fetch b.bewilligungsStatus where b.id=:id");
    query.setLong("id", id);
    @SuppressWarnings("unchecked")
    List<Bewilligung> list = query.list();
    Bewilligung result = list.isEmpty() ? null : list.get(0);
    log.debug("findById({}) = {}", id, result);
    return result;
  }

  @Override
  public void insert(Bewilligung bewilligung) {
    log.debug("insert({})", bewilligung);
    getCurrentSession().save(bewilligung);
  }

  @Override
  public void update(Bewilligung bewilligung) {
    log.debug("update({})", bewilligung);
    getCurrentSession().update(bewilligung);
  }

  @Override
  public void delete(Bewilligung bewilligung) {
    log.debug("delete({})", bewilligung);
    getCurrentSession().delete(bewilligung);
  }

  @Override
  public int getMaxPosition(long antragId) {
    Query query = getCurrentSession().createQuery("select max(b.position) from Bewilligung b where b.antragId=:antragId");
    query.setLong("antragId", antragId);
    @SuppressWarnings("unchecked")
    List<Integer> list = query.list();
    Integer maxPosition = list.get(0);
    int result = maxPosition == null ? 0 : maxPosition.intValue();
    log.debug("getMaxPosition(antragId={}) = {}", antragId, result);
    return result;
  }

  @Override
  public Bewilligung findByAntragIdAndBewilliger(long antragId, String benutzerId) {
    Query query = getCurrentSession().createQuery("from Bewilligung b where b.antragId=:antragId and b.benutzerId=:benutzerId");
    query.setLong("antragId", antragId);
    query.setString("benutzerId", benutzerId);
    @SuppressWarnings("unchecked")
    List<Bewilligung> list = query.list();
    Bewilligung result = list.isEmpty() ? null : list.get(0);
    log.debug("findByAntragIdAndBewilliger({}, {}) = {}", new Object[] { antragId, benutzerId, result });
    return result;
  }

  @Override
  public List<Bewilligung> findByBewilliger(String benutzerId) {
    final String hql = "from Bewilligung b join fetch b.antrag join fetch b.benutzer join fetch b.bewilligungsStatus join fetch b.antrag.benutzer join fetch b.antrag.antragStatus join fetch b.antrag.antragArt where b.benutzerId=:benutzerId and b.antrag.antragStatusId <> 'STORNIERT' order by b.antrag.von";
    Query query = getCurrentSession().createQuery(hql);
    query.setString("benutzerId", benutzerId);
    @SuppressWarnings("unchecked")
    List<Bewilligung> list = query.list();
    log.debug("findByBewilliger({}): count = {}", new Object[] { benutzerId, list.size() });
    return list;
  }

  private Criteria createFilterCriteria(BewilligungsFilter filter) {
    Criteria criteria = getCurrentSession().createCriteria(Bewilligung.class);
    criteria.createAlias("antrag", "antrag");
    if (StringUtils.isNotBlank(filter.getBenutzerId())) {
      criteria.add(Restrictions.eq("antrag.benutzerId", filter.getBenutzerId()));
    }
    if (filter.getVon() != null) {
      criteria.add(Restrictions.ge("antrag.bis", filter.getVon()));
    }
    if (filter.getBis() != null) {
      criteria.add(Restrictions.le("antrag.von", filter.getBis()));
    }
    criteria.add(Restrictions.ne("antrag.antragStatusId", "STORNIERT"));
    criteria.addOrder(Order.asc("antrag.von"));
    criteria.addOrder(Order.asc("antrag.benutzerId"));
    return criteria;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Bewilligung> findByBewilligerAndFilter(String bewilligerId, BewilligungsFilter filter) {
    Criteria criteria = createFilterCriteria(filter);
    criteria.add(Restrictions.eq("benutzerId", bewilligerId));
    criteria.add(Restrictions.or(Restrictions.ne("position", 2), Restrictions.ne("antrag.antragStatusId", "NEU")));
    List<Bewilligung> list = criteria.list();
    log.debug("findByBewilligerAndFilter({}, {}): count = {}", new Object[] { bewilligerId, filter, list.size() });
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Bewilligung> findByFilter(BewilligungsFilter filter) {
    Criteria criteria = createFilterCriteria(filter);
    List<Bewilligung> list = criteria.list();
    log.debug("findByFilter({}): count = {}", new Object[] { filter, list.size() });
    return list;
  }
}
