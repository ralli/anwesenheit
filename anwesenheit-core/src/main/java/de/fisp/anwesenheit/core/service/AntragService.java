package de.fisp.anwesenheit.core.service;

import java.util.List;

import de.fisp.anwesenheit.core.domain.*;

/**
 * Implementiert die Geschäftslogik für die Verwaltung von Anträgen.
 */
public interface AntragService {
  /**
   * Liefert die Angaben, die für die Anzeige eines einzelnen Antrags notwendig
   * sind. Die Methode liefert nur benutzer, für die der angemeldete Benutzer
   * eine Zugriffsberechtigung hat.
   * <p/>
   * Zu den Angaben gehören Angaben zum Benutzer. Die Liste der Bewilligungen
   * und deren Status sowie die Änderungshistorie des Antrags.
   *
   * @param benutzerId Der angemeldete Benutzer
   * @param id         Die Id des zu lesenden Antrags.
   * @return Die Daten zum Antrag oder null, wenn kein passender Antrag
   *         existiert.
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          , wenn der Benuzter nicht gefunden wurde.
   */
  AntragsDaten findAntragById(String benutzerId, long id);

  /**
   * Liefert die Angaben die zur Anzeige der Antragsliste eines Benutzers
   * notwendig sind. Es werden Anträge geliefert, auf die der Benutzer eine
   * Zugriffsberechtigung hat.
   * <p/>
   * Zu diesen Angaben gehören die Anträge, deren Status und die Angaben zum
   * Benutzer.
   *
   * @param currentBenutzerId Der aktuell angemeldete Benutzer.
   * @param benutzerId        Die Benutzer-ID zu der die Anträge gelesen werden sollen.
   * @return Die Antragsliste oder null, wenn kein passender Benutzer existiert.
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          Wenn ein Benutzer nicht gefunden wurde.
   */
  AntragListe findByBenutzer(String currentBenutzerId, String benutzerId);

  /**
   * Liefert eine Liste aller eigenen Anträge, die einem Filterkriterium
   * entsprechen.
   *
   * @param benutzerId Die ID des angemeldeten Benutzers
   * @param filter     Das Filterkriterium
   * @return Die Liste der gefundenen Benutzer
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          Wenn der Benutzer nicht gefunden wurde
   */
  AntragListe findEigeneByFilter(String benutzerId, AntragsFilter filter);

  /**
   * Liefert eine Liste aller für einen Benutzer sichtbaren Anträge, die einem
   * Filterkriterium entsprechen. Das sind die eigenen Angträge und die Anträge
   * zu denen ein Bewilligungsauftrag für den Benutzer existiert. Bei Benutzern
   * mit Sonderrechten sind alle Anträge sichtbar.
   *
   * @param benutzerId Die ID des angemeldeten Benutzers
   * @param filter     Das Filterkriterium
   * @return Die Liste der gefundenen Benutzer
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          Wenn der Benutzer nicht gefunden wurde
   */
  AntragListe findSichtbareByFilter(String benutzerId, AntragUebersichtFilter filter);

  /**
   * Legt einen neuen Antrag in der Datenbank an.
   *
   * @param benutzerId Der aktuell angemeldete Benutzer
   * @param command    Die Angaben zur Anlage des Antrags.
   * @return Die id des angelegten Antrags
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          wenn ein Eintrag nicht gefunden wurde
   * @throws de.fisp.anwesenheit.core.util.NotValidException
   *          wenn der Antrag inkonsistente oder falsche Daten enthält
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *          wenn der aktuelle Benutzer den Antrag nicht anlegen darf
   */
  long createAntrag(String benutzerId, CreateAntragCommand command);

  /**
   * Ändert die Antragsdaten.
   *
   * @param benutzerId Der aktuell angemeldete Benutzer Benutzer
   * @param antragId   Die Id des zu ändernden Antrags
   * @param command    Die zu ändernden Antragsdaten
   * @return Die geänderten Antragsdaten
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *
   * @throws de.fisp.anwesenheit.core.util.NotValidException
   *
   */
  AntragsDaten updateAntrag(String benutzerId, long antragId, UpdateAntragCommand command);

  /**
   * Ließt die Historie zu einem gegebenen Antrag
   *
   * @param benutzerId Der Benutzer der auf die Historie zugreifen möchte
   * @param antragid   Die ID des zu lesenden Antrags
   * @return Die Liste der Historieeinträge
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          Wenn der Benutzer oder der Antrag nicht existiert
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *          Wenn der Benutzer den Antrag nicht ansehen darf
   */
  List<AntragHistorieDaten> leseHistorie(String benutzerId, long antragid);

  /**
   * Löscht einen Antrag.
   *
   * @param benutzerId Der aktuell angemeldete Benutzer
   * @param antragId   Der Antrag, der gelöscht werden soll
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          wenn ein Eintrag nicht gefunden wurde
   * @throws de.fisp.anwesenheit.core.util.NotValidException
   *          wenn der Antrag nicht gelöscht werden darf
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *          wenn der aktuelle Benutzer den Antrag nicht löschen darf
   */
  void deleteAntrag(String benutzerId, long antragId);

  /**
   * Storniert einen Antrag
   *
   * @param benutzerId Der aktuell angemeldete Benutzer
   * @param antragId   Der Antrag, der gelöscht werden soll
   * @throws de.fisp.anwesenheit.core.util.NotFoundException
   *          wenn ein Eintrag nicht gefunden wurde
   * @throws de.fisp.anwesenheit.core.util.NotAuthorizedException
   *          wenn der aktuelle Benutzer den Antrag nicht stornieren darf
   */
  void storniereAntrag(String benutzerId, long antragId);
}
