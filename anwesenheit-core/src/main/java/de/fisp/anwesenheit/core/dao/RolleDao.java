package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Rolle;

/**
 * Liefert die Liste der möglichen Rollen (Administrator etc.)
 */
public interface RolleDao {
  /**
   * Liefert die Liste der möglichen Rollen (Administrator etc.)
   *
   * @return Die Liste der möglichen Rollen
   */
  List<Rolle> findAll();
}
