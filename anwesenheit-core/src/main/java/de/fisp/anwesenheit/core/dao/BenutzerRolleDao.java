package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.BenutzerRolle;

/**
 * Zugriff auf die Zuordnung von Benutzern zu Benutzerrollen.
 * 
 * Eine Benutzerrolle ist so etwas wie "Administrator".
 * 
 */
public interface BenutzerRolleDao {
	/**
	 * Liefert die Benutzerrollen zu einem bestimmten Benutzer.
	 * 
	 * @param benutzerId
	 *            Die Benutzerid
	 * @return Die Liste der gefundenen Benutzerrollen.
	 */
	List<BenutzerRolle> findByBenutzer(String benutzerId);

	/**
	 * Liefertn eine Benutzerrolle anhand ihres Primärschlüssels (Benutzerid und
	 * Rolle)
	 * 
	 * @param benutzerId
	 *            Die BenutzerId des Benutzers
	 * @param rolle
	 *            Der Rollenschlüssel der Rolle.
	 * @return Die Benutzerrolle oder null, wenn eine passende Benutzerrolle
	 *         gefunden wurde.
	 */
	BenutzerRolle findByBenutzerAndRolle(String benutzerId, String rolle);

	/**
	 * Speichert eine neue Benutzerrolle
	 * 
	 * @param benutzerRolle
	 *            Die zu speichernde Benuterrolle.
	 */
	void insert(BenutzerRolle benutzerRolle);

	/**
	 * Löscht eine Benutzerolle.
	 * 
	 * @param benutzerRolle
	 *            Die zu löschende Benutzerrolle.
	 */
	void delete(BenutzerRolle benutzerRolle);
}
