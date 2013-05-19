package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragStatus;

/**
 * Zugriff auf die Liste der möglichen Antragstatus.
 */
public interface AntragStatusDao {
  /**
   * Liefert die Liste der möglichen Antragstatus.
   * <p/>
   * Die Liste ist aufsteigend anhand des Attributs "position" sortiert.
   *
   * @return Die Liste der Antragstatus.
   */
  List<AntragStatus> findAll();
}
