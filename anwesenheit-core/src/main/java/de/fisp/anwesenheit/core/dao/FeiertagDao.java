package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Feiertag;

/**
 * Zugriff auf gesetzliche Feiertage
 */
public interface FeiertagDao {
  /**
   * Liefert die Liste aller im System gespeicherten Feiertage
   * 
   * @return Die Liste aller Feiertage
   */
  List<Feiertag> findAll();

  /**
   * Liefert die Liste aller Feiertage in eines Kalenderjahrs
   * 
   * @param year
   *          Das Jahr zu dem die Feiertage gelesen werden sollen
   * @return Die Liste der Feiertage
   */
  List<Feiertag> findByYear(int year);

  /**
   * Löscht alle Feiertage eines gegebenen Jahrs
   * 
   * @param year
   *          Das Jahr in dem alle Feiertage gelöscht werden sollen
   */
  void deleteByYear(int year);

  /**
   * Speichert einen neuen Feiertag in der Datenbank
   * 
   * @param feiertag
   *          Der zu speichernde Feiertag
   */
  void insert(Feiertag feiertag);

  /**
   * Speichert die Änderungen an einem bestehenden Feiertag
   * 
   * @param feiertag
   *          Der zu speichernde Feiertag
   */
  void update(Feiertag feiertag);

  /**
   * Löscht einen Feiertag
   * 
   * @param feiertag
   *          Der zu löschende Feiertag
   */
  void delete(Feiertag feiertag);
}