package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.AntragDao;
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
    log.debug("delete({})", antrag);
    getCurrentSession().delete(antrag);
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
}
