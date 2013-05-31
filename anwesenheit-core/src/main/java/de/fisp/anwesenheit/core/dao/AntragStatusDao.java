package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragStatus;

/**
 * Zugriff auf die Liste der möglichen Antragstatus.
 */
public interface AntragStatusDao {
  /**
   * Ließt einen AngtragStatus anhand seiner AntragStatusId
   * @param antragStatus Die ID des zu lesenden Antragstatus
   * @return Der Antragstatus oder null, wenn kein passender Antragstatus gefunden wurde.
   */
  AntragStatus findById(String antragStatus);

  /**
   * Liefert die Liste der möglichen Antragstatus.
   * <p/>
   * Die Liste ist aufsteigend anhand des Attributs "position" sortiert.
   *
   * @return Die Liste der Antragstatus.
   */
  List<AntragStatus> findAll();
}
