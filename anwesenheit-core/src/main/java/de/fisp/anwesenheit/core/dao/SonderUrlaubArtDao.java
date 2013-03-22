package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;

public interface SonderUrlaubArtDao {
  /**
   * Liefert eine Liste aller Sonderurlaubarten sortiert nach Position
   * 
   * @return die Liste aller Sonderurlaubarten
   */
  List<SonderUrlaubArt> findAll();

  /**
   * Ließt eine Sonderurlaubart anhand ihres Schlüssels
   * 
   * @param sonderUrlaubArtId
   *          Der Schlüssel der Sonderurlaubart
   *          
   * @return Die gefundene Sonderurlaubart oder null, wenn keine passsende
   *         Sonderurlaubart gefunden wurde
   */
  SonderUrlaubArt findById(String sonderUrlaubArtId);
}
