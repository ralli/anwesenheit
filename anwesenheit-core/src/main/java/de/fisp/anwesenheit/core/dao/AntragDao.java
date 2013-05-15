package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.domain.AntragUebersichtFilter;
import de.fisp.anwesenheit.core.domain.AntragsFilter;
import de.fisp.anwesenheit.core.entities.Antrag;

/**
 * Zugriff auf Anträge.
 * 
 */
public interface AntragDao {
  /**
   * Liefert die Liste aller zu einem Benutzer gehörenden Anträge.
   * 
   * Das sind die Anträge, bei denen der Benutzer Antragsteller ist.
   * 
   * @param benutzerId
   *          Die BenutzerId zu der die Anträge gesucht werden sollen.
   * 
   * @return Die Liste der gefundenen Anträge
   */
  List<Antrag> findByBenutzerId(String benutzerId);

  /**
   * Sucht einen Antrag anhand seiner Antragsid.
   * 
   * @param id
   *          Die id des Antrags, der gelesen werden soll.
   * 
   * @return Der Antrag oder null, wenn kein passender Antrag gefunden wurde.
   */
  Antrag findById(long id);

  /**
   * Findet eine Liste der eigenen Anträge unter Berücksichtigung eines
   * zusätzlichen Filters
   * 
   * @param benutzerId
   *          Der Benutzer, zu dem die Anträge gelesen werden sollen
   * @param filter
   *          Das zusätzliche Filterkriterium
   * @return Die Liste der gefundenen Anträge
   */
  List<Antrag> findByBenutzerAndFilter(String benutzerId, AntragsFilter filter);

  /**
   * Findet eine Liste der Anträge, zu denen für einen Benutzer ein
   * Bewilligungsantrag besteht unter Berücksichtigung eines zusätzlichen
   * Filters
   * 
   * @param bewilligerId
   *          Der Benutzer, zu dem die Anträge gelesen werden sollen
   * @param filter
   *          Das zusätzliche Filterkriterium
   * @return Die Liste der gefundenen Anträge
   */
  List<Antrag> findByBewilligerAndFilter(String bewilligerId, AntragsFilter filter);

  /**
   * Findet eine Liste von Anträgen unter Berücksichtigung eines Filters
   * 
   * @param filter
   *          Das Filterkriterium
   * @return Die Liste der gefundenen Anträge
   */
  List<Antrag> findByFilter(AntragsFilter filter);

  /**
   * Liefert alle Anträge zu einem bestimmten Benutzer, bei dem der aktuelle
   * Benutzer ein Bewilliger ist.
   * 
   * @param benutzerId Die Benutzerkennung des Benutzers, zu dem die Anträge geliefert werden sollen
   * @param bewilligerBenutzerId Die Benutzerkennung des Bewilligers
   * 
   * @return Die Liste der gefundenen Anträge
   */
  List<Antrag> findByBenutzerIdAndBewilliger(String benutzerId, String bewilligerBenutzerId);

  /**
   * Liefert alle Antraege der Antragsübersicht, die einem Filterkriterium entsprechen
   *
   * @param filter das Filterkriterium
   * @return Die Liste der gefundenen Anträge
   */
  List<Antrag> findByUebersichtFilter(AntragUebersichtFilter filter);

  /**
   * Liefert alle Anträge der Antragsübersicht, die einem Filterkriterium entsprechen und
   * für den angegebenen Bewilliger sichtbar sind.
   *
   * @param bewilligerBenutzerId Die Benutzerkennung des Bewilligers
   * @param filter Das Filterkriterium
   * @return Die Liste der gefundenen Anträge
   */
  List<Antrag> findByBewilligerAndUebersichtFilter(String bewilligerBenutzerId, AntragUebersichtFilter filter);

  /**
   * Speichert einen neuen Antrag in der Datenbank.
   * 
   * @param antrag
   *          Der zu speichernde Antrag.
   */
  void insert(Antrag antrag);

  /**
   * Speichert die Änderungen an einem existierenden Antrag.
   * 
   * @param antrag
   *          Der zu speichernde Antrag.
   */
  void update(Antrag antrag);

  /**
   * Löscht einen Antrag.
   * 
   * @param antrag
   *          Der zu löschende Antrag.
   */
  void delete(Antrag antrag);

}
