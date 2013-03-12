package de.fisp.anwesenheit.core.service;

import java.util.List;

import de.fisp.anwesenheit.core.domain.BenutzerDaten;

public interface BenutzerService {
	/**
	 * Macht eine Suche auf die Attribute BenutzerId, Vorname und Nachname
	 * 
	 * @param searchTerm
	 *            Der Suchbegriff
	 * 
	 * @return Die Liste der passenden Benutzer
	 */
	List<BenutzerDaten> search(String searchTerm);

	/**
	 * Liefert die Daten zu einem Benutzer anhand seiner BenutzerId.
	 * 
	 * @param benutzerId
	 *            Die BenutzerId
	 * @return Die Benutzerdaten oder null, wenn kein passender Benutzer
	 *         gefunden wurde.
	 */
	BenutzerDaten findByBenutzerId(String benutzerId);
}
