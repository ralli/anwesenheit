package de.fisp.anwesenheit.core.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.BenutzerRolle;
import de.fisp.anwesenheit.core.service.BerechtigungsService;
import de.fisp.anwesenheit.core.util.NotFoundException;

@Service
public class BerechtigungsServiceImpl implements BerechtigungsService {
  private BewilligungDao bewilligungDao;
  private BenutzerDao benutzerDao;

  @Autowired
  public BerechtigungsServiceImpl(BewilligungDao bewilligungDao, BenutzerDao benutzerDao) {
    this.bewilligungDao = bewilligungDao;
    this.benutzerDao = benutzerDao;
  }

  /**
   * Prüft, ob ein Antrag für einen Benutzer sichtbar ist.
   * <p>
   * 
   * Ein Antrag ist für einen Benutzer sichtbar wenn
   * <ul>
   * <li>Der Benutzer Eigentümer des Antrags ist
   * <li>Der Benutzer Sonderberechtigungen hat
   * <li>Wenn ein Bewilligungsantrag für den Benutzer zum Antrag existiert
   * </ul>
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzer
   *          Der zu prüfende Benutzer
   * @return true, wenn der Antrag für den Benutzer sichtbar ist
   */
  public boolean darfAntragAnsehen(Antrag antrag, Benutzer benutzer) {
    if (benutzer.getBenutzerId().equals(antrag.getBenutzerId()))
      return true;

    Set<BenutzerRolle> benutzerRollen = benutzer.getBenutzerRollen();
    for (BenutzerRolle br : benutzerRollen) {
      if ("ERFASSER".equals(br.getRolle()))
        return true;
    }

    return bewilligungDao.findByAntragIdAndBewilliger(antrag.getId(), benutzer.getBenutzerId()) != null;
  }

  /**
   * Prüft, ob ein Antrag für einen Benutzer sichtbar ist.
   * <p>
   * 
   * Ein Antrag ist für einen Benutzer sichtbar wenn
   * <ul>
   * <li>Der Benutzer Eigentümer des Antrags ist
   * <li>Der Benutzer Sonderberechtigungen hat
   * <li>Wenn ein Bewilligungsantrag für den Benutzer zum Antrag existiert
   * </ul>
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzerId
   *          Die Id des zu prüfenden Benutzers
   * @return true, wenn der Antrag für den Benutzer sichtbar ist
   */
  public boolean darfAntragAnsehen(Antrag antrag, String benutzerId) {
    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    return darfAntragAnsehen(antrag, benutzer);
  }

  /**
   * Prüft, ob ein Benutzer Eigentümer eines Antrags ist oder ob der Benutzer
   * über Sonderberechtigungen verfügt.
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzer
   *          Der zu prüfende Benutzer
   * @return true, wenn die Prüfung zu einem positiven Ergebnis kommt
   */

  public boolean isAntragEigentuemerOderErfasser(Antrag antrag, Benutzer benutzer) {
    if (benutzer.getBenutzerId().equals(antrag.getBenutzerId()))
      return true;

    Set<BenutzerRolle> benutzerRollen = benutzer.getBenutzerRollen();
    for (BenutzerRolle br : benutzerRollen) {
      if ("ERFASSER".equals(br.getRolle()))
        return true;
    }

    return false;
  }

  /**
   * Prüft, ob ein Benutzer Eigentümer eines Antrags ist oder ob der Benutzer
   * über Sonderberechtigungen verfügt.
   * 
   * @param antrag
   *          Der zu prüfende Antrag
   * @param benutzerId
   *          Der zu prüfende Benutzer
   * @return true, wenn die Prüfung zu einem positiven Ergebnis kommt
   */
  public boolean isAntragEigentuemerOderErfasser(Antrag antrag, String benutzerId) {
    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    return isAntragEigentuemerOderErfasser(antrag, benutzer);
  }
}
