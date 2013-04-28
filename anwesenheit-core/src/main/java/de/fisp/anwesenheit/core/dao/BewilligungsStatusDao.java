package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.BewilligungsStatus;

/**
 * Zugriff auf die Liste der möglichen Bewilligungsstatus.
 */
public interface BewilligungsStatusDao {
  /**
   * Liefert die Liste der möglichen Bewilligungsstatus.
   * 
   * @return Die Liste der möglichen Bewilligungsstatus.
   */
  List<BewilligungsStatus> findAll();

  /**
   * Liefert einen Bewilligungstatus anhand seiner Id.
   * 
   * @param bewilligungsStatusId
   *          Die ID des zu lesenden BewilligungsStatus
   *          
   * @return Der BewilligungsStatus oder null, wenn kein passender
   *         BewilligungsStatus gefunden wurde
   */
  BewilligungsStatus findById(String bewilligungsStatusId);
}
