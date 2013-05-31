package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragArt;

/**
 * Zugriff auf die Liste der möglichen Antragsarten
 */
public interface AntragArtDao {
  /**
   * Ließt eine Antragart anhand ihrer ID
   *
   * @param antragArt Die Antragart, die gelesen werden soll
   * @return Die Antragart oder null, wenn keine passende Antragart gefunden wurde.
   */
  AntragArt findById(String antragArt);

  /**
   * Liefert die Liste der möglichen Antragsarten
   *
   * @return Die Liste der möglichen Antragsarten
   */
  List<AntragArt> findAll();
}
