package de.fisp.anwesenheit.core.service;

public interface MailBenachrichtigungsService {

  /**
   * Versendet eine Mail für einen neu angelegten Antrag
   *
   * @param benutzerId Die Benutzer-ID des Benutzers, der den Antrag anglegt (der gerade
   *                   angemeldete Benutzer)
   * @param antragId   Die ID des neuen Antrags
   */
  public abstract void sendeAntragsMail(String benutzerId, long antragId);

}