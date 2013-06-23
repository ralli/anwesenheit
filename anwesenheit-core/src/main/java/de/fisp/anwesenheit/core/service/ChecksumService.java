package de.fisp.anwesenheit.core.service;

public interface ChecksumService {
  /**
   * Generiert eine cryptographisch sichere Prüfsumme für eine Zeichenkette.
   *
   * @param input Die Zeichenkette, für die die Prüfsumme berechnet werden soll.
   * @return Die Prüfsumme im Hex-Format
   */
  String generateChecksum(String input);
}
