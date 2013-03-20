package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.BewilligungDao;
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
    final String hql = "from Bewilligung b join fetch b.antrag join fetch b.benutzer join fetch b.bewilligungsStatus join fetch b.antrag.benutzer join fetch b.antrag.antragStatus join fetch b.antrag.antragArt where b.benutzerId=:benutzerId order by b.antrag.von";
    Query query = getCurrentSession().createQuery(hql);
    query.setString("benutzerId", benutzerId);
    @SuppressWarnings("unchecked")
    List<Bewilligung> list = query.list();
    log.debug("findByBewilliger({}): count = {}", new Object[] { benutzerId, list.size() });
    return list;
  }
}
