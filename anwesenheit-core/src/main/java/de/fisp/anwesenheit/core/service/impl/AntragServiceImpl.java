package de.fisp.anwesenheit.core.service.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.dao.SonderUrlaubArtDao;
import de.fisp.anwesenheit.core.domain.AntragHistorieDaten;
import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragListeEintrag;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.AntragsFilter;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;
import de.fisp.anwesenheit.core.domain.UpdateAntragCommand;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.AntragHistorie;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;
import de.fisp.anwesenheit.core.service.AntragService;
import de.fisp.anwesenheit.core.service.BerechtigungsService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;

@Service
public class AntragServiceImpl implements AntragService {
  private static final Logger log = LoggerFactory.getLogger(AntragService.class);
  private AntragDao antragDao;
  private BewilligungDao bewilligungDao;
  private BenutzerDao benutzerDao;
  private AntragHistorieDao antragHistorieDao;
  private BerechtigungsService berechtigungsService;
  private SonderUrlaubArtDao sonderUrlaubArtDao;

  @Autowired
  public AntragServiceImpl(AntragDao antragDao, BewilligungDao bewilligungDao, BenutzerDao benutzerDao,
      SonderUrlaubArtDao sonderUrlaubArtDao, AntragHistorieDao antragHistorieDao, BerechtigungsService berechtigungsService) {
    this.antragDao = antragDao;
    this.bewilligungDao = bewilligungDao;
    this.benutzerDao = benutzerDao;
    this.sonderUrlaubArtDao = sonderUrlaubArtDao;
    this.antragHistorieDao = antragHistorieDao;
    this.berechtigungsService = berechtigungsService;
  }

  @Override
  @Transactional
  public AntragsDaten findAntragById(String benutzerId, long id) {
    Antrag antrag = antragDao.findById(id);
    if (antrag == null) {
      throw new NotFoundException("Antrag mit nicht gefunden");
    }

    if (!berechtigungsService.darfAntragAnsehen(antrag, benutzerId)) {
      throw new NotAuthorizedException("Keine Berechtigung zur Anzeige des Antrags");
    }

    return createAntragsDatenFromAntrag(id, antrag);
  }

  private AntragsDaten createAntragsDatenFromAntrag(long id, Antrag antrag) {
    BenutzerDaten benutzerDaten = createBenutzerDaten(antrag.getBenutzer());
    AntragsDaten result = new AntragsDaten(antrag.getId(), antrag.getAntragArt(), antrag.getAntragStatus(),
        antrag.getSonderUrlaubArt(), antrag.getVon(), antrag.getBis(), antrag.getAnzahlTage(), benutzerDaten,
        loadBewilligungsDaten(id));

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

  @Override
  @Transactional
  public AntragListe findByBenutzer(String currentBenutzerId, String benutzerId) {
    boolean alleAnzeigen = berechtigungsService.darfAlleAntraegeSehen(currentBenutzerId, benutzerId);

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
    AntragListeEintrag eintrag = new AntragListeEintrag(antrag.getId(), createBenutzerDaten(antrag.getBenutzer()),
        antrag.getAntragArt(), antrag.getAntragStatus(), antrag.getVon(), antrag.getBis(), antrag.getAnzahlTage());
    return eintrag;
  }

  private void updateSonderUrlaubArt(Antrag antrag, String antragArtId, String sonderUrlaubArtId, double anzahlTage) {
    antrag.setAntragArtId(antragArtId);
    if ("SONDER".equals(antragArtId)) {
      antrag.setSonderUrlaubArtId(sonderUrlaubArtId);
      SonderUrlaubArt sonderUrlaubArt = sonderUrlaubArtDao.findById(sonderUrlaubArtId);
      if (sonderUrlaubArt == null) {
        throw new NotFoundException("Sonderurlaubart nicht gefunden");
      }
      antrag.setAnzahlTage(sonderUrlaubArt.getAnzahlTage());
    } else {
      antrag.setAnzahlTage(anzahlTage);
    }
  }

  @Override
  @Transactional
  public long createAntrag(String benutzerId, CreateAntragCommand command) {
    log.debug("createAntrag({})", command);

    Antrag antrag = new Antrag();

    antrag.setBenutzerId(command.getBenutzerId());
    antrag.setAntragArtId(command.getAntragArt());
    updateSonderUrlaubArt(antrag, command.getAntragArt(), command.getSonderUrlaubArt(), command.getAnzahlTage());
    antrag.setAntragStatusId("NEU");
    antrag.setVon(command.getVon());
    antrag.setBis(command.getBis());

    if (!berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)) {
      throw new NotAuthorizedException("Keine ausreichenden Berechtigungen zum Anlegen des Antrags.");
    }

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

    insertAntragHistorie(benutzerId, antrag, "Antrag angelegt");
    log.debug("createAntrag: id = {}", antrag.getId());
    return antrag.getId();
  }

  private void insertAntragHistorie(String benutzerId, Antrag antrag, String message) {
    AntragHistorie antragHistorie = new AntragHistorie();
    antragHistorie.setAntragId(antrag.getId());
    antragHistorie.setBenutzerId(benutzerId);
    antragHistorie.setZeitpunkt(new Date());
    antragHistorie.setBeschreibung(message);
    antragHistorieDao.insert(antragHistorie);
  }

  @Override
  @Transactional
  public boolean deleteAntrag(String benutzerId, long antragId) {
    Antrag antrag = antragDao.findById(antragId);
    if (antrag == null) {
      throw new NotFoundException("Antrag nicht gefunden");
    }
    if (!berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)) {
      throw new NotAuthorizedException("Keine ausreichenden Berechtigungen zum Löschen des Antrags");
    }
    antragDao.delete(antrag);
    return true;
  }

  private String dateToString(Date date) {
    DateFormat f = new SimpleDateFormat("dd.MM.yyyy");
    return f.format(date);
  }

  private String tageToString(double n) {
    NumberFormat f = new DecimalFormat("0.#");
    return f.format(n);
  }

  @Override
  @Transactional
  public AntragsDaten updateAntrag(String benutzerId, long antragId, UpdateAntragCommand command) {
    Antrag antrag = antragDao.findById(antragId);
    if (antrag == null) {
      throw new NotFoundException("Antrag nicht gefunden");
    }
    if (!berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)) {
      throw new NotAuthorizedException("Keine ausreichenden Berechtigungen zum Ändern des Antrags");
    }
    antrag.setAntragArtId(command.getAntragArt());
    antrag.setVon(command.getVon());
    antrag.setBis(command.getBis());
    updateSonderUrlaubArt(antrag, command.getAntragArt(), command.getSonderUrlaubArt(), command.getAnzahlTage());
    antragDao.update(antrag);
    String message = String.format("Antrag geändert: Art: %s, Von: %s, Bis: %s, Tage: %s", command.getAntragArt(),
        dateToString(command.getVon()), dateToString(command.getBis()), tageToString(command.getAnzahlTage()));
    insertAntragHistorie(benutzerId, antrag, message);
    return createAntragsDatenFromAntrag(antragId, antragDao.findById(antragId));
  }

  @Override
  @Transactional
  public AntragListe findEigeneByFilter(String benutzerId, AntragsFilter filter) {
    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    List<Antrag> antraege = antragDao.findByBenutzerAndFilter(benutzerId, filter);
    AntragListe liste = createAntragListe(benutzer, antraege);
    log.debug("findEigeneByFilter({}, {}) = {}", new Object[] { benutzerId, filter, liste });
    return liste;
  }

  @Override
  @Transactional
  public AntragListe findSichtbareByFilter(String benutzerId, AntragsFilter filter) {
    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    boolean sonderberechtigungen = berechtigungsService.hatSonderBerechtigungen(benutzer);
    List<Antrag> antraege;
    if (sonderberechtigungen) {
      antraege = antragDao.findByFilter(filter);
    } else {
      antraege = antragDao.findByBewilligerAndFilter(benutzerId, filter);
    }
    AntragListe liste = createAntragListe(benutzer, antraege);
    log.debug("findSichtbareByFilter({}, {}) = {}", new Object[] { benutzerId, filter, liste });
    return liste;
  }

  @Override
  public List<AntragHistorieDaten> leseHistorie(String benutzerId, long antragId) {
    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    Antrag antrag = antragDao.findById(antragId);
    if (antrag == null) {
      throw new NotFoundException("Antrag nicht gefunden");
    }
    if (!berechtigungsService.darfAntragAnsehen(antrag, benutzer)) {
      throw new NotAuthorizedException("Keine ausreichenden Berechtigungen zur Anzeige des Antrags");
    }
    List<AntragHistorie> list = antragHistorieDao.findByAntrag(antragId);
    List<AntragHistorieDaten> result = new ArrayList<AntragHistorieDaten>();
    for (AntragHistorie antragHistorie : list) {
      result.add(createAntragHistorieDaten(antragHistorie));

    }
    return result;
  }

  private AntragHistorieDaten createAntragHistorieDaten(AntragHistorie antragHistorie) {
    return new AntragHistorieDaten(antragHistorie.getId(), antragHistorie.getAntragId(), antragHistorie.getBenutzerId(),
        antragHistorie.getZeitpunkt(), antragHistorie.getBeschreibung(), createBenutzerDaten(antragHistorie.getBenutzer()));
  }
}
