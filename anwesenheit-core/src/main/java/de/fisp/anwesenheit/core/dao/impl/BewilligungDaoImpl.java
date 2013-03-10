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
	private Logger log = LoggerFactory.getLogger(BenutzerRolleDaoImpl.class);
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
		Query query = getCurrentSession()
				.createQuery(
						"from Bewilligung b join fetch b.benutzer where b.antragId=:antragId order by b.position");
		query.setLong("antragId", antragId);
		@SuppressWarnings("unchecked")
		List<Bewilligung> list = query.list();
		log.debug("findByAntrag({}) = {}", antragId, list);
		return list;
	}

	@Override
	public Bewilligung findById(long id) {
		Query query = getCurrentSession().createQuery(
				"from Bewilligung b join fetch b.benutzer where b.id=:id");
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
}
