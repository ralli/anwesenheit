package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Antrag;

public interface AntragDao {
	List<Antrag> findByBenutzerId(String benutzerId);

	Antrag findById(long id);

	void insert(Antrag  antrag);

	void update(Antrag  antrag);

	void delete(Antrag  antrag);
}
