package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;

public interface BerechtigungsService {
  /**
   * Prüft, ob ein Antrag für einen Benutzer sichtbar ist.
   * <p>
   * 
   * Ein Antrag ist für einen Benutzer sichtbar wenn
   * <ul>
   * <li>Der Benutzer Eigentümer des Antrags ist
   * <li>Der Benutzer Sonderberechtigungen hat
   * <li>Wenn ein Bewilligungsantrag für den Benutzer zum Antrag existiert
   * </ul>
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzer
   *          Der zu prüfende Benutzer
   * @return true, wenn der Antrag für den Benutzer sichtbar ist
   */
  boolean darfAntragAnsehen(Antrag antrag, Benutzer benutzer);

  /**
   * Prüft, ob ein Antrag für einen Benutzer sichtbar ist.
   * <p>
   * 
   * Ein Antrag ist für einen Benutzer sichtbar wenn
   * <ul>
   * <li>Der Benutzer Eigentümer des Antrags ist
   * <li>Der Benutzer Sonderberechtigungen hat
   * <li>Wenn ein Bewilligungsantrag für den Benutzer zum Antrag existiert
   * </ul>
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzerId
   *          Die Id des zu prüfenden Benutzers
   * @return true, wenn der Antrag für den Benutzer sichtbar ist
   */
  boolean darfAntragAnsehen(Antrag antrag, String benutzerId);

  /**
   * Prüft, ob ein Benutzer Eigentümer eines Antrags ist oder ob der Benutzer
   * über Sonderberechtigungen verfügt.
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzer
   *          Der zu prüfende Benutzer
   * @return true, wenn die Prüfung zu einem positiven Ergebnis kommt
   */

  boolean isAntragEigentuemerOderErfasser(Antrag antrag, Benutzer benutzer);

  /**
   * Prüft, ob ein Benutzer Eigentümer eines Antrags ist oder ob der Benutzer
   * über Sonderberechtigungen verfügt.
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzerId
   *          Der zu prüfende Benutzer
   * @return true, wenn die Prüfung zu einem positiven Ergebnis kommt
   */

  boolean isAntragEigentuemerOderErfasser(Antrag antrag, String benutzerId);
}