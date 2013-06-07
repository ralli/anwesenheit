package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.ParameterDao;
import de.fisp.anwesenheit.core.entities.Parameter;

@Service
public class ParameterDaoImpl implements ParameterDao {
	private static final Logger log = LoggerFactory
			.getLogger(ParameterDaoImpl.class);

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Parameter> findAll() {
		final String hql = "from Parameter order by id";
		Session session = getCurrentSession();
		Query q = session.createQuery(hql);
		List<Parameter> list = q.list();
		log.debug("findAll: count = {}", list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Parameter findById(String id) {
		final String hql = "from Parameter where id = :id";
		Session session = getCurrentSession();
		Query q = session.createQuery(hql);
		q.setParameter("id", id);
		List<Parameter> list = q.list();
		Parameter result = list.isEmpty() ? null : list.get(0);
		log.debug("findById({}) = {}", id, result);
		return result;		
	}

	@Override
	public void insert(Parameter parameter) {
		log.debug("insert({})", parameter);
		getCurrentSession().save(parameter);
	}

	@Override
	public void update(Parameter parameter) {
		log.debug("update({})", parameter);
		getCurrentSession().save(parameter);
	}

}
