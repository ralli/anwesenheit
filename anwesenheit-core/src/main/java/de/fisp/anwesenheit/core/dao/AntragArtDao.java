package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragArt;

/**
 * Zugriff auf die Liste der möglichen Antragsarten
 */
public interface AntragArtDao {
  /**
   * Liefert die Liste der möglichen Antragsarten
   *
   * @return Die Liste der möglichen Antragsarten
   */
  List<AntragArt> findAll();
}
