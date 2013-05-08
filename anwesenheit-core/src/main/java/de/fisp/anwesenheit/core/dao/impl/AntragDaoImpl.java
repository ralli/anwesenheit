package de.fisp.anwesenheit.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import de.fisp.anwesenheit.core.domain.AntragUebersichtFilter;
import org.apache.commons.lang.StringUtils;
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
      throw new RuntimeException("No current session found!");
    }
    return session;
  }

  @Override
  public List<Antrag> findByBenutzerId(String benutzerId) {
    Query query = getCurrentSession()
            .createQuery(
                    "from Antrag a join fetch a.benutzer join fetch a.antragArt join fetch a.antragStatus where a.benutzerId=:benutzerId order by a.von asc");
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
    final String hql = "select b.antrag from Bewilligung b " +
            "join fetch b.antrag.benutzer " +
            "join fetch b.antrag.antragArt " +
            "join fetch b.antrag.antragStatus " +
            "where b.antrag.benutzerId=:benutzerId and b.benutzerId=:bewilligerBenutzerId " +
            "order by b.antrag.von asc";
    Query query = getCurrentSession().createQuery(hql);
    query.setString("benutzerId", benutzerId);
    query.setString("bewilligerBenutzerId", bewilligerBenutzerId);
    @SuppressWarnings("unchecked")
    List<Antrag> list = query.list();
    log.debug("findByBenutzerIdAndBewilliger({},{}) = {}", benutzerId, bewilligerBenutzerId, list);
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

    log.debug("findByBenutzerAndFilter({}, {}): count = {}", benutzerId, filter, list.size());

    return list;
  }

  private Criteria createFilterCriteria(AntragsFilter filter) {
    Criteria criteria = getCurrentSession().createCriteria(Antrag.class);

    if (StringUtils.isNotBlank(filter.getBenutzerPattern())) {
      String pattern = filteredBenutzerPattern(filter.getBenutzerPattern());
      criteria.createAlias("benutzer", "ben");

      criteria.add(Restrictions.or(Restrictions.ilike("ben.nachname", pattern), Restrictions.ilike("ben.vorname", pattern)));
    }

    if (filter.getVon() != null) {
      criteria.add(Restrictions.ge("bis", filter.getVon()));
    }

    if (filter.getBis() != null) {
      criteria.add(Restrictions.le("von", filter.getBis()));
    }

    // OFFEN, BEWILLIGT, ABGELEHNT, STORNIERT
    if ("OFFEN".equals(filter.getAntragsStatusFilter())) {
      criteria.add(Restrictions.in("antragStatusId", new String[]{"NEU", "IN_ARBEIT"}));
    } else if ("BEWILLIGT".equals(filter.getAntragsStatusFilter())) {
      criteria.add(Restrictions.eq("antragStatusId", "BEWILLIGT"));
    } else if ("ABGELEHNT".equals(filter.getAntragsStatusFilter())) {
      criteria.add(Restrictions.eq("antragStatusId", "ABGELEHNT"));
    } else if ("STORNIERT".equals(filter.getAntragsStatusFilter())) {
      criteria.add(Restrictions.eq("antragStatusId", "STORNIERT"));
    }

    criteria.setFetchMode("benutzer", FetchMode.JOIN);
    criteria.setFetchMode("antragArt", FetchMode.JOIN);
    criteria.setFetchMode("antragStatus", FetchMode.JOIN);

    return criteria;
  }

  private String filteredBenutzerPattern(String benutzerPattern) {
    String pattern = StringUtils.remove(benutzerPattern, '?');
    pattern = StringUtils.remove(pattern, '%');
    pattern = StringUtils.lowerCase(pattern);
    pattern = StringUtils.strip(pattern);
    pattern = "%" + pattern + "%";
    return pattern;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Antrag> findByBewilligerAndFilter(String bewilligerId, AntragsFilter filter) {
    Criteria criteria = createFilterCriteria(filter);
    criteria.createAlias("bewilligungen", "bw");
    criteria.add((Restrictions.eq("bw.benutzerId", bewilligerId)));
    criteria.addOrder(Order.asc("von"));
    List<Antrag> list = findByBenutzerAndFilter(bewilligerId, filter);
    list.addAll(criteria.list());

    log.debug("findByBewilligerAndFilter({}, {}): count = {}", bewilligerId, filter, list.size());
    return list;
  }

  @Override
  public List<Antrag> findByFilter(AntragsFilter filter) {
    Criteria criteria = createFilterCriteria(filter);
    criteria.addOrder(Order.asc("von"));
    @SuppressWarnings("unchecked")
    List<Antrag> list = criteria.list();
    log.debug("findByFilter({}): count = {}", filter, list.size());
    return list;
  }

  private List<Antrag> findByBenutzerAndUebersichtFilter(String benutzerId, AntragUebersichtFilter filter) {
    Criteria criteria = createUebersichtCriteria(filter);

    criteria.add(Restrictions.eq("benutzerId", benutzerId));

    @SuppressWarnings("unchecked")
    List<Antrag> result = criteria.list();
    log.debug("findByBenutzerAndUebersichtFilter({}, {}): count = {}", benutzerId, filter, result.size());
    return result;

  }

  @Override
  public List<Antrag> findByBewilligerAndUebersichtFilter(String bewilligerBenutzerId, AntragUebersichtFilter filter) {
    Criteria criteria = createUebersichtCriteria(filter);

    criteria.createAlias("bewilligungen", "bw");
    criteria.add(Restrictions.eq("bw.benutzerId", bewilligerBenutzerId));

    @SuppressWarnings("unchecked")
    List<Antrag> result = criteria.list();
    result.addAll(findByBenutzerAndUebersichtFilter(bewilligerBenutzerId, filter));
    log.debug("findByBewilligerAndUebersichtFilter({}, {}): count = {}", bewilligerBenutzerId, filter, result.size());
    return result;
  }

  private Criteria createUebersichtCriteria(AntragUebersichtFilter filter) {
    Criteria criteria = getCurrentSession().createCriteria(Antrag.class);

    criteria.setFetchMode("benutzer", FetchMode.JOIN);
    criteria.setFetchMode("antragArt", FetchMode.JOIN);
    criteria.setFetchMode("antragStatus", FetchMode.JOIN);

    List<String> statusFilters = new ArrayList<String>();
    for (String statusFilter : filter.getStatusList()) {
      // OFFEN, BEWILLIGT, ABGELEHNT, STORNIERT
      if ("OFFEN".equals(statusFilter)) {
        statusFilters.add("NEU");
        statusFilters.add("IN_ARBEIT");
      } else {
        statusFilters.add(statusFilter);
      }
    }
    criteria.add(Restrictions.in("antragStatusId", statusFilters));

    if (filter.getVon() != null) {
      criteria.add(Restrictions.ge("bis", filter.getVon()));
    }

    if (filter.getBis() != null) {
      criteria.add(Restrictions.le("von", filter.getBis()));
    }

    if (StringUtils.isNotBlank(filter.getAntragsteller())) {
      String pattern = filteredBenutzerPattern(filter.getAntragsteller());
      criteria.createAlias("benutzer", "ben");
      criteria.add(Restrictions.or(Restrictions.ilike("ben.nachname", pattern),
              Restrictions.ilike("ben.vorname", pattern)));
    }


    criteria.addOrder(Order.asc("von"));
    return criteria;
  }


  @Override
  public List<Antrag> findByUebersichtFilter(AntragUebersichtFilter filter) {
    Criteria criteria = createUebersichtCriteria(filter);

    @SuppressWarnings("unchecked")
    List<Antrag> result = criteria.list();
    log.debug("findByUebersichtFilter({}): count = {}", filter, result.size());
    return result;
  }
}
