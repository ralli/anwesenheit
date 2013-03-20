package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.BewilligungListe;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;

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
   * @throws NotAuthorized
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
   * Liefert eine Liste aller Bewilligungen die einem Benutzer zugewiesen sind
   * 
   * @param currentUserId
   *          Die BenutzerId des aktuell angemeldeten Benutzers
   * @param benutzerId
   *          Die BenutzerId des Benutzers
   * @return Die Liste der gefundenen Bewilligungen
   */
  BewilligungListe findByBenutzer(String currentUserId, String benutzerId);
}
