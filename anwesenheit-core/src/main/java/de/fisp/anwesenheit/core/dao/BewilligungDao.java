package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Bewilligung;

public interface BewilligungDao {
	List<Bewilligung> findByAntrag(long antragId);

	Bewilligung findById(long id);

	void insert(Bewilligung bewilligung);

	void update(Bewilligung bewilligung);

	void delete(Bewilligung bewilligung);
}
