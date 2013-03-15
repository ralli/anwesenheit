package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Bewilligung;

/**
 * Zugriff auf die Bewilligungen zu einem Antrag.
 */
public interface BewilligungDao {
	/**
	 * Liefert die Liste der Bewilligungen zu einem Antrag.
	 * 
	 * @param antragId
	 *            Die Antrags-ID
	 * @return Die Liste der gefundenden Bewilligungen.
	 */
	List<Bewilligung> findByAntrag(long antragId);

	/**
	 * Liefert eine Bewilligung anhand ihres Schlüssels.
	 * 
	 * @param id
	 *            Die ID der Bewilligung.
	 * 
	 * @return Die Bewilligung oder null, wenn keine Bewilligung gefunden wurde.
	 */
	Bewilligung findById(long id);

	/**
	 * Speichert eine neue Bewilligung.
	 * 
	 * @param bewilligung
	 *            Die zu speichernde Bewilligung.
	 */
	void insert(Bewilligung bewilligung);

	/**
	 * Speichert die Änderungen an einer Bewilligung.
	 * 
	 * @param bewilligung
	 *            Die zu speichernde Bewilligung.
	 */
	void update(Bewilligung bewilligung);

	/**
	 * Löscht eine Bewilligung.
	 * 
	 * @param bewilligung
	 *            Die zu löschende Bewilligung.
	 */
	void delete(Bewilligung bewilligung);
	
	int getMaxPosition(long antragId);
}
