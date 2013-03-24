package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.domain.AntragsFilter;
import de.fisp.anwesenheit.core.entities.Antrag;

@Service
public class AntragDaoImpl implements AntragDao {
  private Logger log = LoggerFactory.getLogger(AntragDaoImpl.class);
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
  public List<Antrag> findByBenutzerId(String benutzerId) {
    Query query = getCurrentSession()
        .createQuery(
            "from Antrag a join fetch a.benutzer join fetch a.antragArt join fetch a.antragStatus where a.benutzerId=:benutzerId order by a.von");
    query.setString("benutzerId", benutzerId);
    @SuppressWarnings("unchecked")
    List<Antrag> list = query.list();
    log.debug("findByBenutzerId({}): count = {}", benutzerId, list.size());
    return list;
  }

  @Override
  public Antrag findById(long id) {
    Query query = getCurrentSession().createQuery(
        "from Antrag a join fetch a.benutzer join fetch a.antragArt join fetch a.antragStatus where a.id=:id");
    query.setLong("id", id);
    @SuppressWarnings("unchecked")
    List<Antrag> list = query.list();
    Antrag result = list.isEmpty() ? null : list.get(0);
    log.debug("findById({}): {}", id, result);
    return result;
  }

  @Override
  public List<Antrag> findByBenutzerIdAndBewilliger(String benutzerId, String bewilligerBenutzerId) {
    final String hql = "select b.antrag from Bewilligung b join fetch b.antrag.benutzer join fetch b.antrag.antragArt join fetch b.antrag.antragStatus where b.antrag.benutzerId=:benutzerId and b.benutzerId=:bewilligerBenutzerId order by b.antrag.von";
    Query query = getCurrentSession().createQuery(hql);
    query.setString("benutzerId", benutzerId);
    query.setString("bewilligerBenutzerId", bewilligerBenutzerId);
    @SuppressWarnings("unchecked")
    List<Antrag> list = query.list();
    log.debug("findByBenutzerIdAndBewilliger({},{}) = {}", new Object[] { benutzerId, bewilligerBenutzerId, list });
    return list;
  }

  @Override
  public void insert(Antrag antrag) {
    log.debug("insert({})", antrag);
    getCurrentSession().save(antrag);
  }

  @Override
  public void update(Antrag antrag) {
    log.debug("update({})", antrag);
    getCurrentSession().update(antrag);
  }

  @Override
  public void delete(Antrag antrag) {
    String hqlDelete = "delete Antrag a where a.id = :id";
    log.debug("delete({})", antrag);
    Query query = getCurrentSession().createQuery(hqlDelete);
    query.setLong("id", antrag.getId());
    query.executeUpdate();
  }

  @Override
  public List<Antrag> findByBenutzerAndFilter(String benutzerId, AntragsFilter filter) {
    Criteria criteria = createFilterCriteria(filter);
    criteria.add(Restrictions.eq("benutzerId", benutzerId));
    criteria.addOrder(Order.asc("von"));
    @SuppressWarnings("unchecked")
    List<Antrag> list = criteria.list();

    log.debug("findByBenutzerAndFilter({}, {}): count = {}", new Object[] { benutzerId, filter, list.size() });

    return list;
  }

  private Criteria createFilterCriteria(AntragsFilter filter) {
    Criteria criteria = getCurrentSession().createCriteria(Antrag.class);

    if (filter.getVon() != null) {
      criteria.add(Restrictions.ge("bis", filter.getVon()));
    }

    if (filter.getBis() != null) {
      criteria.add(Restrictions.le("von", filter.getBis()));
    }

    // OFFEN, BEWILLIGT, ABGELEHNT
    if ("OFFEN".equals(filter.getAntragsStatusFilter())) {
      criteria.add(Restrictions.in("antragStatusId", new String[] { "NEU", "IN_ARBEIT" }));
    } else if ("BEWILLIGT".equals(filter.getAntragsStatusFilter())) {
      criteria.add(Restrictions.eq("antragStatusId", "BEWILLIGT"));
    } else if ("ABGELEHNT".equals(filter.getAntragsStatusFilter())) {
      criteria.add(Restrictions.eq("antragStatusId", "ABGELEHNT"));
    }

    criteria.setFetchMode("benutzer", FetchMode.JOIN);
    criteria.setFetchMode("antragArt", FetchMode.JOIN);
    criteria.setFetchMode("antragStatus", FetchMode.JOIN);

    return criteria;
  }

  @Override
  public List<Antrag> findByBewilligerAndFilter(String bewilligerId, AntragsFilter filter) {
    Criteria criteria = createFilterCriteria(filter);
    criteria.createCriteria("bewilligungen").add(Restrictions.eq("benutzerId", bewilligerId));
    criteria.addOrder(Order.asc("von"));
    @SuppressWarnings("unchecked")
    List<Antrag> list = criteria.list();
    log.debug("findByBewilligerAndFilter({}, {}): count = {}", new Object[] { bewilligerId, filter, list.size() });
    return list;
  }

  @Override
  public List<Antrag> findByFilter(AntragsFilter filter) {
    Criteria criteria = createFilterCriteria(filter);
    criteria.addOrder(Order.asc("von"));
    @SuppressWarnings("unchecked")
    List<Antrag> list = criteria.list();
    log.debug("findByFilter({}): count = {}", new Object[] { filter, list.size() });
    return list;
  }
}
