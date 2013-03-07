package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.BenutzerRolle;

public interface BenutzerRolleDao {
	List<BenutzerRolle> findByBenutzer(String benutzerId);

	BenutzerRolle findByBenutzerAndRolle(String benutzerId, String rolle);

	void insert(BenutzerRolle benutzerRolle);

	void delete(BenutzerRolle benutzerRolle);
}
