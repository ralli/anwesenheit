package de.fisp.anwesenheit.core.service;

public interface MailBenachrichtigungsService {

  /**
   * Versendet eine Mail für einen neu angelegten Antrag
   *
   * @param benutzerId Die Benutzer-ID des Benutzers, der den Antrag anglegt (der gerade
   *                   angemeldete Benutzer)
   * @param antragId   Die ID des neuen Antrags
   */
  void sendeErsteBewilligungsMail(String benutzerId, long antragId);

  /**
   * Versendet eine Mail an den zweiten Bewilliger, nachdem der erste
   * Bewilliger dem Antrag zugestimmt hat.
   *
   * @param benutzerId Die ID des angemeldeten Benutzers (der erste Bewilliger)
   * @param antragId   Die ID des Antrags
   */
  void sendeZweiteBewilligungsMail(String benutzerId, long antragId);

  /**
   * Versendet Mails an den Antragsteller und die relevanten Bewilliger. Relevante Bewilliger sind die, die
   * bereits Kenntnis vom Antrag haben. Der zweite Unterzeichner hat Kenntnis vom Antrag,
   * wenn der erste Unterzeichner unterschrieben hat.
   *
   * @param benutzerId Die ID des angemeldeten Benutzers (der erste oder zweite Bewilliger)
   * @param antragId   Die ID des zugehörigen Antrags
   */
  void sendeAbgelehntMail(String benutzerId, long antragId, long bewilligungId);

  /**
   * Versendet Mails an den Antragsteller und die alle Bewilliger.
   *
   * @param benutzerId Die ID des angemeldeten Benutzers (der erste oder zweite Bewilliger)
   * @param antragId   Die ID des zugehörigen Antrags
   */
  void sendeBewilligtMail(String benutzerId, long antragId, long bewilligungId);

  /**
   * Versendet Mails an den Antragsteller und die relevanten Bewilliger. Relevante Bewilliger sind die, die
   * bereits Kenntnis vom Antrag haben. Der zweite Unterzeichner hat Kenntnis vom Antrag,
   * wenn der erste Unterzeichner unterschrieben hat.
   *
   * @param benutzerId Die ID des angemeldeten Benutzers (der Antragsteller)
   * @param antragId   Die ID des zugehörigen Antrags
   */
  void sendeStornoMail(String benutzerId, long antragId);
}