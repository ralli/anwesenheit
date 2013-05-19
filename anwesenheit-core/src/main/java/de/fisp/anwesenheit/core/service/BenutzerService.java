package de.fisp.anwesenheit.core.service;

import java.util.List;

import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.LabelValue;

public interface BenutzerService {
  /**
   * Macht eine Suche auf die Attribute BenutzerId, Vorname und Nachname.
   * <p/>
   * Ergebnis ist eine Liste der Benutzer als LabelValue mit Vor- und Nachname
   * im "Label" und der Benutzer-ID als "Value".
   *
   * @param searchTerm Der Suchbegriff
   * @return Die Liste der passenden Benutzer
   */
  List<LabelValue> search(String searchTerm);

  /**
   * Liefert die Daten zu einem Benutzer anhand seiner BenutzerId.
   *
   * @param benutzerId Die BenutzerId
   * @return Die Benutzerdaten oder null, wenn kein passender Benutzer
   *         gefunden wurde.
   */
  BenutzerDaten findByBenutzerId(String benutzerId);
}
