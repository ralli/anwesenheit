package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.ChangePasswordCommand;
import de.fisp.anwesenheit.core.domain.LoginCommand;

/**
 * Managed die Anmeldung von Benutzern am System.
 */
public interface LoginService {
  /**
   * Überprüft die Anmeldedaten eines Benutzers.
   *
   * @param loginData Die zu überprüfenden Anmeldedaten
   * @return Die Benutzerdaten des geprüften Benutzers
   */
  BenutzerDaten login(LoginCommand loginData);

  /**
   * Ändert das passwort eines Benutzers.
   *
   * @param command Die für die Änderung des Passworts notewendigen Angaben
   */
  void changePassword(ChangePasswordCommand command);
}
