package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

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

import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.entities.Benutzer;

@Service
public class BenutzerDaoImpl implements BenutzerDao {
	private Logger log = LoggerFactory.getLogger(BenutzerDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Benutzer> findAll() {
		Session session = sessionFactory.getCurrentSession();
		List<Benutzer> list = session.createQuery(
				"from Benutzer order by benutzerId").list();
		log.debug("findAll: count = {}", list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Benutzer findById(String benutzerId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("from Benutzer where benutzerId=:benutzerId");
		query.setString("benutzerId", benutzerId);
		List<Benutzer> list = query.list();
		Benutzer result = list.isEmpty() ? null : list.get(0);
		log.debug("findById({}) = {}", benutzerId, result);
		return result;
	}

	@Override
	public void insert(Benutzer benutzer) {
		log.debug("insert({})", benutzer);
		Session session = sessionFactory.getCurrentSession();
		session.save(benutzer);
	}

	@Override
	public void update(Benutzer benutzer) {
		log.debug("update({})", benutzer);
		Session session = sessionFactory.getCurrentSession();
		session.update(benutzer);
	}

	@Override
	public void delete(Benutzer benutzer) {
		log.debug("delete({})", benutzer);
		Session session = sessionFactory.getCurrentSession();
		session.delete(benutzer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Benutzer> search(String searchTerm) {
		log.debug("search({})", searchTerm);
		Session session = sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(Benutzer.class);
		String term = "%" + searchTerm + "%";
		c.add(Restrictions.disjunction()
				.add(Restrictions.ilike("benutzerId", term))
				.add(Restrictions.ilike("vorname", term))
				.add(Restrictions.ilike("nachname", term)));
		c.addOrder(Order.asc("nachname"));
		c.setMaxResults(10);
		return c.list();
	}
}
