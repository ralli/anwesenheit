package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.domain.BewilligungsFilter;
import de.fisp.anwesenheit.core.entities.Bewilligung;

/**
 * Zugriff auf die Bewilligungen zu einem Antrag.
 */
public interface BewilligungDao {
  /**
   * Liefert die Liste der Bewilligungen zu einem Antrag.
   * 
   * @param antragId
   *          Die Antrags-ID
   * @return Die Liste der gefundenden Bewilligungen.
   */
  List<Bewilligung> findByAntrag(long antragId);

  /**
   * Liefert eine Bewilligung anhand ihres Schlüssels.
   * 
   * @param id
   *          Die ID der Bewilligung.
   * 
   * @return Die Bewilligung oder null, wenn keine Bewilligung gefunden wurde.
   */
  Bewilligung findById(long id);

  /**
   * Speichert eine neue Bewilligung.
   * 
   * @param bewilligung
   *          Die zu speichernde Bewilligung.
   */
  void insert(Bewilligung bewilligung);

  /**
   * Speichert die Änderungen an einer Bewilligung.
   * 
   * @param bewilligung
   *          Die zu speichernde Bewilligung.
   */
  void update(Bewilligung bewilligung);

  /**
   * Löscht eine Bewilligung.
   * 
   * @param bewilligung
   *          Die zu löschende Bewilligung.
   */
  void delete(Bewilligung bewilligung);

  int getMaxPosition(long antragId);

  /**
   * Liefert eine Bewilligung anhand der Antrag-Id und der Benutzer-ID des
   * Bewilligers
   * 
   * @param antragId
   *          Die ID des zugehörigen Antrags
   * @param benutzerId
   *          Die ID des Bewilligers
   * @return Die gefundenen Bewilligung oder null, wenn keine passende
   *         Bewilligung gefunden wurde.
   */
  Bewilligung findByAntragIdAndBewilliger(long antragId, String benutzerId);

  /**
   * Liefert eine List aller einem Benutzer zugewiesenen Bewilligungen
   * 
   * @param benutzerId
   *          Die ID des Bewilligers
   * 
   * @return Die Liste der Bewilligungen
   */
  List<Bewilligung> findByBewilliger(String benutzerId);

  /**
   * Liefert eine Liste aller Bewilligungsanträge zu einem Bewilliger.
   * Zusätzlich werden die Anträge noch nach verschiedenen Kriterien gefiltert
   * 
   * @param bewilligerId
   *          Die BenutzerId des Bewilligers
   * @param filter
   *          Die zusätzlichen Filterkriterien
   * @return Die Liste der gefundenen Bewilligungen
   */
  List<Bewilligung> findByBewilligerAndFilter(String bewilligerId, BewilligungsFilter filter);

  /**
   * Liefert eine Liste aller Bewilligungsanträge die einem Filterkriterium
   * entsprechen.
   * 
   * @param filter
   *          Das Filterkriterien
   *          
   * @return Die Liste der gefundenen Bewilligungen
   */
  List<Bewilligung> findByFilter(BewilligungsFilter filter);
}
