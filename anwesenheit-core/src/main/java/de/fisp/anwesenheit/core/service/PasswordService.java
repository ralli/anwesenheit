package de.fisp.anwesenheit.core.service;

/**
 * Verschlüsseln von Passwörtern mit einer Hash-Funktion
 */
public interface PasswordService {
  /**
   * Liefert einen Salt-Wert, der beim Passworthashing verwendet werden kann.
   *
   * @return Der Salt
   */
  String generateSalt();

  /**
   * Bildet einen Passworthash. Der Hash wird im Base64-Format zurückgeliefert.
   *
   * @param salt     Ein Salt, so wie er von generateSalt generiert wurde.
   * @param password Das Passwort, für das der Hash gebildet werden soll
   * @return Das gehashte Passwort als String im Base64-Format
   */
  String encodePassword(String salt, String password);

  /**
   * Prüft, ob ein Passwort dem (Salt, PasswortHash) entspricht.
   *
   * @param salt         Der Salt der für das Hashing verwendet wird
   * @param passwortHash Der Hash von (Salt+Passwort)
   * @param password     Das zu überprüfende Passwort
   * @return true, wenn das Passwort mit dem Passwort des Passworthash
   *         übereinstimmt
   */
  boolean checkPassword(String salt, String passwortHash, String password);
}
