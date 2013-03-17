package de.fisp.anwesenheit.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragListeEintrag;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.AntragHistorie;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.BenutzerRolle;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.service.AntragService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;

@Service
public class AntragServiceImpl implements AntragService {
  private static final Logger log = LoggerFactory.getLogger(AntragService.class);
  private AntragDao antragDao;

  private BewilligungDao bewilligungDao;

  private BenutzerDao benutzerDao;

  private AntragHistorieDao antragHistorieDao;

  @Autowired
  public AntragServiceImpl(AntragDao antragDao, BewilligungDao bewilligungDao, BenutzerDao benutzerDao,
      AntragHistorieDao antragHistorieDao) {
    this.antragDao = antragDao;
    this.bewilligungDao = bewilligungDao;
    this.benutzerDao = benutzerDao;
    this.antragHistorieDao = antragHistorieDao;
  }

  /**
   * Prüft, ob ein Antrag für einen Benutzer sichtbar ist.<p>
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
  private boolean darfAntragAnsehen(Antrag antrag, Benutzer benutzer) {
    if (benutzer.getBenutzerId().equals(antrag.getBenutzerId()))
      return true;

    Set<BenutzerRolle> benutzerRollen = benutzer.getBenutzerRollen();
    for (BenutzerRolle br : benutzerRollen) {
      if ("ERFASSER".equals(br.getRolle()))
        return true;
    }

    return bewilligungDao.findByAntragIdAndBewilliger(antrag.getId(), benutzer.getBenutzerId()) != null;
  }

  private boolean darfAntragAnsehen(Antrag antrag, String benutzerId) {
    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    return darfAntragAnsehen(antrag, benutzer);
  }

  @Override
  @Transactional
  public AntragsDaten findAntragById(String benutzerId, long id) {
    Antrag antrag = antragDao.findById(id);
    if (antrag == null) {
      throw new NotFoundException("Antrag mit nicht gefunden");
    }
    if (!darfAntragAnsehen(antrag, benutzerId)) {
      throw new NotAuthorizedException("Keine Berechtigung zur Anzeige des Antrags");
    }
    BenutzerDaten benutzerDaten = createBenutzerDaten(antrag.getBenutzer());
    AntragsDaten result = new AntragsDaten(antrag.getId(), antrag.getAntragArt(), antrag.getAntragStatus(), antrag.getVon(),
        antrag.getBis(), benutzerDaten, loadBewilligungsDaten(id));
    log.debug("findAntragById({}) = {}", id, result);
    return result;
  }

  private BenutzerDaten createBenutzerDaten(Benutzer benutzer) {
    BenutzerDaten benutzerDaten = new BenutzerDaten(benutzer.getBenutzerId(), benutzer.getVorname(), benutzer.getNachname(),
        benutzer.getEmail());
    return benutzerDaten;
  }

  private BewilligungsDaten createBewilligungsDaten(Bewilligung bewilligung) {
    BewilligungsDaten result = new BewilligungsDaten(bewilligung.getId(), bewilligung.getAntragId(), bewilligung.getPosition(),
        bewilligung.getBewilligungsStatus(), createBenutzerDaten(bewilligung.getBenutzer()));
    return result;
  }

  private List<BewilligungsDaten> loadBewilligungsDaten(long antragId) {
    List<Bewilligung> list = bewilligungDao.findByAntrag(antragId);
    List<BewilligungsDaten> result = new ArrayList<BewilligungsDaten>();
    for (Bewilligung bewilligung : list) {
      result.add(createBewilligungsDaten(bewilligung));
    }
    return result;
  }

  private boolean darfAlleAntraegeSehen(String currentBenutzerId, String benutzerId) {
    if (currentBenutzerId.equals(benutzerId))
      return true;

    Benutzer current = benutzerDao.findById(currentBenutzerId);
    if (current == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", currentBenutzerId));
    }

    for (BenutzerRolle br : current.getBenutzerRollen()) {
      if ("ERFASSER".equals(br.getRolle()))
        return true;
    }

    return false;
  }

  @Override
  @Transactional
  public AntragListe findByBenutzer(String currentBenutzerId, String benutzerId) {
    boolean alleAnzeigen = darfAlleAntraegeSehen(currentBenutzerId, benutzerId);

    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    List<Antrag> antraege;
    if (alleAnzeigen) {
      antraege = antragDao.findByBenutzerId(benutzerId);
    } else {
      antraege = antragDao.findByBenutzerIdAndBewilliger(benutzerId, currentBenutzerId);
    }
    AntragListe liste = createAntragListe(benutzer, antraege);
    log.debug("findByBenutzer({}) = {}", benutzerId, liste);
    return liste;
  }

  private AntragListe createAntragListe(Benutzer benutzer, List<Antrag> antraege) {
    List<AntragListeEintrag> eintraege = new ArrayList<AntragListeEintrag>();
    for (Antrag antrag : antraege) {
      AntragListeEintrag eintrag = createAntragListeEintrag(antrag);
      eintraege.add(eintrag);
    }
    BenutzerDaten benutzerDaten = createBenutzerDaten(benutzer);
    AntragListe liste = new AntragListe(benutzerDaten, eintraege);

    return liste;
  }

  private AntragListeEintrag createAntragListeEintrag(Antrag antrag) {
    AntragListeEintrag eintrag = new AntragListeEintrag(antrag.getId(), antrag.getAntragArt(), antrag.getAntragStatus(),
        antrag.getVon(), antrag.getBis());
    return eintrag;
  }

  @Override
  @Transactional
  public long createAntrag(String benutzerId, CreateAntragCommand command) {
    log.debug("createAntrag({})", command);

    Antrag antrag = new Antrag();

    antrag.setBenutzerId(command.getBenutzerId());
    antrag.setAntragArtId(command.getAntragArt());
    antrag.setAntragStatusId("NEU");
    antrag.setVon(command.getVon());
    antrag.setBis(command.getBis());

    antragDao.insert(antrag);
    int position = 1;
    for (String bewilligerId : command.getBewilliger()) {
      Bewilligung bewilligung = new Bewilligung();

      bewilligung.setAntragId(antrag.getId());
      bewilligung.setBenutzerId(bewilligerId);
      bewilligung.setBewilligungsStatusId("OFFEN");
      bewilligung.setPosition(position);
      bewilligungDao.insert(bewilligung);

      ++position;
    }

    insertAntragHistorie(antrag);

    log.debug("createAntrag: id = {}", antrag.getId());

    return antrag.getId();
  }

  private void insertAntragHistorie(Antrag antrag) {
    AntragHistorie antragHistorie = new AntragHistorie();
    antragHistorie.setAntragId(antrag.getId());
    antragHistorie.setBenutzerId(antrag.getBenutzerId());
    antragHistorie.setZeitpunkt(new Date());
    antragHistorie.setBeschreibung("Antrag angelegt");
    antragHistorieDao.insert(antragHistorie);
  }

  @Override
  @Transactional
  public boolean deleteAntrag(String benutzerId, long antragId) {
    Antrag antrag = antragDao.findById(antragId);
    if (antrag == null)
      return false;
    antragDao.delete(antrag);
    return true;
  }
}
