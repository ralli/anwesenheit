package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.BewilligungListe;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDetails;
import de.fisp.anwesenheit.core.domain.BewilligungsFilter;
import de.fisp.anwesenheit.core.domain.UpdateBewilligungCommand;
import de.fisp.anwesenheit.core.util.NotFoundException;

public interface BewilligungService {
  /**
   * Löscht eine bestehende Bewilligung
   * 
   * @param benutzerId
   *          Der Benutzer der die Bewilligung löschen möchte
   * @param bewilligungId
   *          Die Bewilligung, die gelöscht werden soll
   * @throws NotFoundException
   *           wenn der Benutzer nicht gefunden wurde
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *           wenn der Benutzer die Bewilligung nicht löschen darf
   */
  void deleteBewilligung(String benutzerId, long bewilligungId);

  /**
   * Fügt eine neue Bewilligung zu einem Antrag hinzu
   * 
   * @param benutzerId
   *          Der Benutzer, der die Bewilligung hinzufügen möchte
   * @param command
   *          Die Daten der Bewilligung, die hinzugefügt werden soll
   * @return Die
   */
  BewilligungsDaten addBewilligung(String benutzerId, AddBewilligungCommand command);

  /**
   * Liefert eine Liste aller für einen Benutze relevanten Bewilligungen
   * 
   * @param currentUserId
   *          Die ID des aktuellen Benutzers
   * @param benutzerId
   *          Die ID des Benutzers dessen Bewilligungen angezeigt werden sollen.
   * @return Die Liste der gefundenen Bewilligungen
   * @throws NotFoundException
   *           wenn der Benutzer nicht gefunden wurde
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *           wenn der Benutzer die Bewilligungen nicht anzeigen darf
   */
  public BewilligungListe findByBenutzer(String currentUserId, String benutzerId);

  /**
   * Liefert eine Liste aller für einen Benutzer relevanten Bewilligungen.
   * Filtert nach zusätzlichen Bedingungen.
   * 
   * 
   * @param currentUserId
   *          Die ID des aktuellen Benutzers
   * @param filter Die zusätzlichen Filterkriterien
   * @return Die Liste der gefundenen Bewilligungen
   * @throws NotFoundException
   *           wenn der Benutzer nicht gefunden wurde
   */
  public BewilligungListe findByBenutzerAndFilter(String currentUserId, BewilligungsFilter filter);

  /**
   * Setzt den Status eines Urlaubsantrags
   * 
   * @param benutzerId
   *          Die Benutzer-ID des Bewilligers
   * @param command
   *          Die Angaben die Statusänderung
   * @throws NotFoundException
   *           wenn der Benutzer oder die Bewilligungsanfrage nicht gefunden
   *           wurde
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *           wenn der Benutzer die Bewilligung nicht bewilligen darf
   * 
   */
  BewilligungsDaten updateBewilligungStatus(String benutzerId, UpdateBewilligungCommand command);

  /**
   * Ließt Detailinformationen zu einer Bewilligung ein.
   * 
   * @param benutzerId
   *          Der aktuell angemeldete Benutzer (zur Berechtigungsprüfung)
   * @param bewilligungId
   *          Die ID der Bewilligung
   * @return Die eingelesenen Bewilligungsdetails
   * @throws NotFoundException
   *           wenn keine passende Bewillung oder kein passender Benutzer
   *           gefunden wurde
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *           wenn der Benutzer nicht über ausreichende Berechtigungen verfügt
   * 
   */
  BewilligungsDetails leseBewilligungsDetails(String benutzerId, long bewilligungId);
}
