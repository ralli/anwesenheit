package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.FeiertagDefinition;

/**
 * Zugriff auf Feiertagsdefinitionen
 * 
 */
public interface FeiertagDefinitionDao {

  /**
   * Liefert die Liste aller Feiertagsdefinitionen
   * 
   * @return Die Liste aller Feiertagsdefinitionen
   */
  List<FeiertagDefinition> findAll();

}