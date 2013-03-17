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
   * Liefert die Angaben, die für die Anzeige eines einzelnen Antrags notwendig
   * sind. Die Methode liefert nur benutzer, für die der angemeldete Benutzer
   * eine Zugriffsberechtigung hat.
   * 
   * Zu den Angaben gehören Angaben zum Benutzer. Die Liste der Bewilligungen
   * und deren Status sowie die Änderungshistorie des Antrags.
   * 
   * @param benutzerId
   *          Der angemeldete Benutzer
   * @param id
   *          Die Id des zu lesenden Antrags.
   * 
   * @return Die Daten zum Antrag oder null, wenn kein passender Antrag
   *         existiert.
   * 
   * @throws NotFoundException
   *           , wenn der Benuzter nicht gefunden wurde.
   */
  AntragsDaten findAntragById(String benutzerId, long id);

  /**
   * Liefert die Angaben die zur Anzeige der Antragsliste eines Benutzers
   * notwendig sind. Es werden Anträge geliefert, auf die der Benutzer eine
   * Zugriffsberechtigung hat.
   * 
   * Zu diesen Angaben gehören die Anträge, deren Status und die Angaben zum
   * Benutzer.
   * 
   * @param currentBenutzerId
   *          Der aktuell angemeldete Benutzer.
   * @param benutzerId
   *          Die Benutzer-ID zu der die Anträge gelesen werden sollen.
   * 
   * @return Die Antragsliste oder null, wenn kein passender Benutzer existiert.
   * 
   * @throws NotFoundException
   *           Wenn ein Benutzer nicht gefunden wurde.
   */
  AntragListe findByBenutzer(String currentBenutzerId, String benutzerId);

  /**
   * Legt einen neuen Antrag in der Datenbank an.
   * 
   * @param benutzerId
   *          Der aktuell angemeldete Benutzer
   * @param command
   *          Die Angaben zur Anlage des Antrags.
   * 
   * @return Die id des angelegten Antrags
   * 
   * @throws NotFoundException
   *           wenn ein Eintrag nicht gefunden wurde
   * @throws NotValidException
   *           wenn der Antrag inkonsistente oder falsche Daten enthält
   * @throws NotAuthorizedException
   *           wenn der aktuelle Benutzer den Antrag nicht anlegen darf
   */
  long createAntrag(String benutzerId, CreateAntragCommand command);

  /**
   * Löscht einen Antrag.
   * 
   * @param benutzerId
   *          Der aktuell angemeldete Benutzer
   * @param antragId
   *          Der Antrag, der gelöscht werden soll
   * @throws NotFoundException
   *           wenn ein Eintrag nicht gefunden wurde
   * @throws NotValidException
   *           wenn der Antrag inkonsistente oder falsche Daten enthält
   * @throws NotAuthorizedException
   *           wenn der aktuelle Benutzer den Antrag nicht anlegen darf
   */
  boolean deleteAntrag(String benutzerId, long antragId);
}
