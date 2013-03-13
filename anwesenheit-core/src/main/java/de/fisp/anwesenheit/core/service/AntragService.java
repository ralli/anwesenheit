package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;

/**
 * Implementiert die Geschäftslogik für die Verwaltung von Anträgen.
 * 
 */
public interface AntragService {
	/**
	 * Liefert die Angaben, die für die Anzeige eines einzelnen Antrags
	 * notwendig sind.
	 * 
	 * Zu den Angaben gehören Angaben zum Benutzer. Die Liste der Bewilligungen
	 * und deren Status sowie die Änderungshistorie des Antrags.
	 * 
	 * @param id
	 *            Die Id des zu lesenden Antrags.
	 * 
	 * @return Die Daten zum Antrag oder null, wenn kein passender Antrag
	 *         existiert.
	 */
	AntragsDaten findAntragById(long id);

	/**
	 * Liefert die Angaben die zur Anzeige der Antragsliste eines Benutzers
	 * notwendig sind.
	 * 
	 * Zu diesen Angaben gehören die Anträge, deren Status und die Angaben zum
	 * Benutzer.
	 * 
	 * @param benutzerId
	 *            Die Benutzer-ID zu der die Anträge gelesen werden sollen.
	 * 
	 * @return Die Antragsliste oder null, wenn kein passender Benutzer
	 *         existiert.
	 */
	AntragListe findByBenutzer(String benutzerId);

	/**
	 * Legt einen neuen Antrag in der Datenbank an.
	 * 
	 * @param command
	 *            Die Angaben zur Anlage des Antrags.
	 *            
	 * @return Die id des angelegten Antrags
	 */
	long createAntrag(CreateAntragCommand command);
	
	/**
	 * Löscht einen Antrag
	 * @param antragId
	 */
	boolean deleteAntrag(long antragId);
}
