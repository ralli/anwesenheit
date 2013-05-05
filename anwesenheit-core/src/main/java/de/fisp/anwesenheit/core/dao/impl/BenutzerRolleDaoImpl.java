package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.BenutzerRolleDao;
import de.fisp.anwesenheit.core.entities.BenutzerRolle;

@Service
public class BenutzerRolleDaoImpl implements BenutzerRolleDao {
	private Logger log = LoggerFactory.getLogger(BenutzerRolleDaoImpl.class);
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
	public List<BenutzerRolle> findByBenutzer(String benutzerId) {
		Query query = getCurrentSession()
				.createQuery(
						"from BenutzerRolle b where b.benutzerId=:benutzerId order by b.rolle");
		query.setString("benutzerId", benutzerId);
		@SuppressWarnings("unchecked")
		List<BenutzerRolle> list = query.list();
		log.debug("findByBenutzer({}) = {}", benutzerId, list);
		return list;
	}

	@Override
	public BenutzerRolle findByBenutzerAndRolle(String benutzerId, String rolle) {
		Query query = getCurrentSession()
				.createQuery(
						"from BenutzerRolle b where b.benutzerId=:benutzerId and b.rolle=:rolle");
		query.setString("benutzerId", benutzerId);
		query.setString("rolle", rolle);
		@SuppressWarnings("unchecked")
		List<BenutzerRolle> list = query.list();
		BenutzerRolle result = list.isEmpty() ? null : list.get(0);
		log.debug("findByBenutzerAndRolle({}, {}) = {}", new Object[] {
				benutzerId, rolle, result });
		return result;
	}

	@Override
	public void insert(BenutzerRolle benutzerRolle) {
		log.debug("insert({})", benutzerRolle);
		getCurrentSession().save(benutzerRolle);
	}


	@Override
	public void delete(BenutzerRolle benutzerRolle) {
		log.debug("delete({})", benutzerRolle);
		getCurrentSession().delete(benutzerRolle);
	}

}
