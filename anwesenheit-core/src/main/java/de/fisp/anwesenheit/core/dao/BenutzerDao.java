package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Benutzer;

/**
 * Zugriff auf die Anwendungsbenutzer.
 * 
 */
public interface BenutzerDao {
	/**
	 * Liefert die Liste aller Benutzer.
	 * 
	 * @return Die Liste aller Benutzer.
	 */
	List<Benutzer> findAll();

	/**
	 * Ließt einen Benutze anhand seiner BenutzerId.
	 * 
	 * @param benutzerId
	 *            Die BenutzerId
	 * @return Der Benutzer oder null, wenn kein passender Benutzer gefunden
	 *         wurde.
	 */
	Benutzer findById(String benutzerId);

	/**
	 * Speichert einen neuen Benutzer.
	 * 
	 * @param benutzer
	 *            Der zu speichernde Benutzer.
	 */
	void insert(Benutzer benutzer);

	/**
	 * Speichert die Änderungen an einem bestehenden Benutzer.
	 * 
	 * @param benutzer
	 *            Der zu speichernde Benutzer.
	 */
	void update(Benutzer benutzer);

	/**
	 * Löscht einen Benutzer.
	 * 
	 * @param benutzer
	 *            Der zu löschende Benutzer.
	 */
	void delete(Benutzer benutzer);

	List<Benutzer> search(String searchTerm);
}
