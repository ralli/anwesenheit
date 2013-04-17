package de.fisp.anwesenheit.core.service;

public interface FeiertagService {
  /**
   * Berechnet die Liste aller Feiertage für ein gegebenes Jahr und speichert
   * diese in der Datenbank ab.
   * 
   * @param year
   *          Das Jahr für das die Feiertage generiert werden sollen
   */
  public void generateForYear(int year);
}
