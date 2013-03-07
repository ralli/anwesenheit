package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragHistorie;

public interface AntragHistorieDao {
	List<AntragHistorie> findByAntrag(long antragId);

	AntragHistorie findById(long id);

	void insert(AntragHistorie antragHistorie);

	void delete(AntragHistorie antragHistorie);
}
