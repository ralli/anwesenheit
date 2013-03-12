package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragHistorie;

/**
 * Zugriff auf die Historie von Anträgen.
 */
public interface AntragHistorieDao {
	/**
	 * Liefert die Historie eines Antrags aufsteigend nach Datum sortiert.
	 * 
	 * @param antragId
	 *            Die id des Antrags
	 * 
	 * @return Die Liste der Historieneinträge.
	 */
	List<AntragHistorie> findByAntrag(long antragId);

	/**
	 * Liefert einen Historieneintrag.
	 * 
	 * @param id
	 *            Die ID des Historieneintrags.
	 * 
	 * @return Der gelesende Eintrag oder aber null, wenn kein passender Eintrag
	 *         gefunden wurde.
	 */
	AntragHistorie findById(long id);

	/**
	 * Speichert einen neuen Historieneintrag.
	 * 
	 * @param antragHistorie
	 *            Der zu speichernde Eintrag.
	 */
	void insert(AntragHistorie antragHistorie);

	/**
	 * Löscht einen Historieneintrag
	 * 
	 * @param antragHistorie
	 *            Der zu löschende Eintrag
	 */
	void delete(AntragHistorie antragHistorie);
}
