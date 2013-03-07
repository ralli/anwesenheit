package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Benutzer;

public interface BenutzerDao {
	List<Benutzer> findAll();

	Benutzer findById(String benutzerId);

	void insert(Benutzer benutzer);

	void update(Benutzer benutzer);

	void delete(Benutzer benutzer);
}
