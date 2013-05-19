package de.fisp.anwesenheit.core.service;

import java.util.Date;

public interface FeiertagService {
  /**
   * Berechnet die Liste aller Feiertage für ein gegebenes Jahr und speichert
   * diese in der Datenbank ab.
   *
   * @param year Das Jahr für das die Feiertage generiert werden sollen
   */
  void generateForYear(int year);

  /**
   * Berechnet die Anzahl Arbeitstage zwischen zwei Zeitpunkten. Das Anfangs und
   * Enddatum werden bei der Berechnung mit berücksichtigt.
   *
   * @param von das Anfangsdatum
   * @param bis das Enddatum
   * @return Die Anzahl der Arbeitstage
   */
  double berechneAnzahlArbeitstage(Date von, Date bis);
}
