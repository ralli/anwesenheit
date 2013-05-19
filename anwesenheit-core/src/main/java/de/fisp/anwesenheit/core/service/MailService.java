package de.fisp.anwesenheit.core.service;

public interface MailService {
  /**
   * Verschickt eine Email
   *
   * @param subject  Der Betreff der Email
   * @param text     Der Text der Email
   * @param from     Die Absendeadresse der Email
   * @param adressen Die Adressen der Mail-Empfänger
   */
  void sendeMail(String subject, String text, String from, String adressen);
}
