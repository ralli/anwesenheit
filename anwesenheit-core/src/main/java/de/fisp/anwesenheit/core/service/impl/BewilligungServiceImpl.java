package de.fisp.anwesenheit.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fisp.anwesenheit.core.service.MailBenachrichtigungsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.dao.BewilligungsStatusDao;
import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.AntragListeEintrag;
import de.fisp.anwesenheit.core.domain.AntragsFilter;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungListe;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDetails;
import de.fisp.anwesenheit.core.domain.BewilligungsFilter;
import de.fisp.anwesenheit.core.domain.BewilligungsListeEintrag;
import de.fisp.anwesenheit.core.domain.UpdateBewilligungCommand;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.AntragHistorie;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;
import de.fisp.anwesenheit.core.service.BerechtigungsService;
import de.fisp.anwesenheit.core.service.BewilligungService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;
import de.fisp.anwesenheit.core.util.NotValidException;

/**
 * Verschiedene Operationen rund um Bewilligungen.
 */
@Service
public class BewilligungServiceImpl implements BewilligungService {
  private static final Logger logger = LoggerFactory.getLogger(BewilligungServiceImpl.class);
  private final BewilligungDao bewilligungDao;
  private final AntragDao antragDao;
  private final AntragHistorieDao antragHistorieDao;
  private final BenutzerDao benutzerDao;
  private final BerechtigungsService berechtigungsService;
  private final BewilligungsStatusDao bewilligungsStatusDao;
  private final MailBenachrichtigungsService mailBenachrichtigungsService;

  @Autowired
  public BewilligungServiceImpl(BewilligungDao bewilligungDao,
                                AntragDao antragDao,
                                AntragHistorieDao antragHistorieDao,
                                BenutzerDao benutzerDao,
                                BerechtigungsService berechtigungsService,
                                BewilligungsStatusDao bewilligungsStatusDao,
                                MailBenachrichtigungsService mailBenachrichtigungsService) {
    this.bewilligungDao = bewilligungDao;
    this.antragDao = antragDao;
    this.antragHistorieDao = antragHistorieDao;
    this.benutzerDao = benutzerDao;
    this.berechtigungsService = berechtigungsService;
    this.bewilligungsStatusDao = bewilligungsStatusDao;
    this.mailBenachrichtigungsService = mailBenachrichtigungsService;
  }

  private void addAntragHistorie(long antragId, String benutzerId, String message) {
    AntragHistorie antragHistorie = new AntragHistorie();
    antragHistorie.setAntragId(antragId);
    antragHistorie.setBenutzerId(benutzerId);
    antragHistorie.setZeitpunkt(new Date());
    antragHistorie.setBeschreibung(message);
    antragHistorieDao.insert(antragHistorie);
  }

  @Override
  @Transactional
  public void deleteBewilligung(String benutzerId, long bewilligungId) {
    logger.debug("deleteBewilligung({})", bewilligungId);

    Bewilligung bewilligung = bewilligungDao.findById(bewilligungId);
    if (bewilligung == null) {
      throw new NotFoundException("Bewilligung nicht gefunden");
    }

    if (!("OFFEN".equals(bewilligung.getBewilligungsStatusId()))) {
      throw new NotAuthorizedException("Nur offene Bewilligungen dürfen gelöscht werden");
    }

    if (!berechtigungsService.isAntragEigentuemerOderErfasser(bewilligung.getAntrag(), benutzerId)) {
      throw new NotAuthorizedException("Keine ausreichende Berechtigung zum Löschen der Bewilligung");
    }

    String message = String.format("Bewilligung Bewilliger: %s, Status: %s gelöscht", bewilligung.getBenutzerId(),
            bewilligung.getBewilligungsStatusId());

    aktualisiereAntragStatus(benutzerId, bewilligung);

    addAntragHistorie(bewilligung.getAntragId(), benutzerId, message);

    bewilligungDao.delete(bewilligung);
  }

  private BenutzerDaten createBenutzerDaten(Benutzer benutzer) {
    return new BenutzerDaten(benutzer.getBenutzerId(), benutzer.getVorname(), benutzer.getNachname(),
            benutzer.getEmail());
  }

  private BewilligungsDaten createBewilligungsDaten(Bewilligung bewilligung) {
    return new BewilligungsDaten(bewilligung.getId(), bewilligung.getAntragId(), bewilligung.getPosition(),
            bewilligung.getBewilligungsStatus(), createBenutzerDaten(bewilligung.getBenutzer()));
  }

  @Override
  @Transactional
  public BewilligungsDaten addBewilligung(String benutzerId, AddBewilligungCommand command) {
    logger.debug("addBewilligung({}, {})", benutzerId, command);

    Benutzer bewilliger = benutzerDao.findById(command.getBenutzerId());
    if (bewilliger == null) {
      throw new NotFoundException(String.format("Bewilliger %s nicht gefunden", command.getBenutzerId()));
    }

    Antrag antrag = antragDao.findById(command.getAntragId());
    if (antrag == null) {
      throw new NotFoundException("Antrag nicht gefunden");
    }

    if (benutzerId.equals(command.getBenutzerId())) {
      throw new NotValidException("Antragsteller dürfen Ihren Antrag nicht selbst bewilligen");
    }

    Bewilligung bewilligung = bewilligungDao.findByAntragIdAndBewilliger(command.getAntragId(), command.getBenutzerId());
    if (bewilligung != null) {
      throw new NotValidException("Die Bewilligung kann nicht mehrfach hinzugefügt werden");
    }

    if (!berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)) {
      throw new NotAuthorizedException("Keine ausreichenden Berechtigungen zum Hinzufügen einer Bewilligung");
    }

    bewilligung = new Bewilligung();
    bewilligung.setAntragId(command.getAntragId());
    bewilligung.setAntrag(antrag);
    bewilligung.setBenutzerId(command.getBenutzerId());
    bewilligung.setBewilligungsStatusId("OFFEN");
    bewilligung.setPosition(bewilligungDao.getMaxPosition(command.getAntragId()) + 1);

    bewilligungDao.insert(bewilligung);
    bewilligung.setBenutzer(bewilliger);
    BewilligungsStatus bewilligungsStatus = new BewilligungsStatus();
    bewilligungsStatus.setBewilligungsStatus("OFFEN");
    bewilligungsStatus.setBezeichnung("Offen");
    bewilligungsStatus.setPosition(1);
    bewilligung.setBewilligungsStatus(bewilligungsStatus);

    BewilligungsDaten daten = createBewilligungsDaten(bewilligung);

    String message = String.format("Bewilligung Bewilliger: %s, Status: %s hinzugefügt", bewilligung.getBenutzerId(),
            bewilligung.getBewilligungsStatusId());
    addAntragHistorie(bewilligung.getAntragId(), benutzerId, message);
    aktualisiereAntragStatus(benutzerId, bewilligung);

    logger.debug("addBewilligung({}, {}) = {}", benutzerId, command, daten);

    return daten;
  }

  private BewilligungsListeEintrag createBewilligungsListeEintrag(Bewilligung bewilligung) {
    Antrag antrag = bewilligung.getAntrag();
    return new BewilligungsListeEintrag(bewilligung.getId(), bewilligung.getBewilligungsStatus(),
            bewilligung.getAntragId(), antrag.getAntragArt(), antrag.getAntragStatus(), createBenutzerDaten(antrag.getBenutzer()),
            antrag.getVon(), antrag.getBis());
  }

  @Override
  @Transactional
  public BewilligungListe findByBenutzer(String currentUserId, String benutzerId) {
    logger.debug("findByBenutzer({}, {})", currentUserId, benutzerId);
    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Bewilliger %s nicht gefunden", benutzerId));
    }
    if (!berechtigungsService.darfBewilligungenAnsehen(currentUserId, benutzerId)) {
      throw new NotAuthorizedException("Keine Ausreichenden Berechtigungen zum Anzeigen der Bewilligungen");
    }
    List<Bewilligung> list = bewilligungDao.findByBewilliger(benutzerId);
    return createBewilligungsListe(benutzer, list);
  }

  private void insertAntragHistorie(String benutzerId, long antragId, String message) {
    AntragHistorie antragHistorie = new AntragHistorie();
    antragHistorie.setAntragId(antragId);
    antragHistorie.setBenutzerId(benutzerId);
    antragHistorie.setZeitpunkt(new Date());
    antragHistorie.setBeschreibung(message);
    antragHistorieDao.insert(antragHistorie);
  }

  private void aktualisiereAntragStatus(String benutzerId, Bewilligung bewilligung) {
    Antrag antrag = bewilligung.getAntrag();
    String bisherigerStatus = antrag.getAntragStatusId();
    String antragStatus = ermittleAntragStatus(antrag);
    antrag.setAntragStatusId(antragStatus);
    antragDao.update(antrag);

    versendeStatusAenderungsMails(benutzerId, antrag, bewilligung, bisherigerStatus, antragStatus);
  }

  private void versendeStatusAenderungsMails(String benutzerId, Antrag antrag, Bewilligung bewilligung, String bisherigerStatus, String antragStatus) {
    if (bisherigerStatus != null && !bisherigerStatus.equals(antragStatus)) {
      if ("IN_ARBEIT".equals(antragStatus)) {
        mailBenachrichtigungsService.sendeZweiteBewilligungsMail(benutzerId, antrag.getId());
      } else if ("ABGELEHNT".equals(antragStatus)) {
        mailBenachrichtigungsService.sendeAbgelehntMail(benutzerId, antrag.getId(), bewilligung.getId());
      } else if ("BEWILLIGT".equals(antragStatus)) {
        mailBenachrichtigungsService.sendeBewilligtMail(benutzerId, antrag.getId(), bewilligung.getId());
      } else if ("STORNIERT".equals(antragStatus)) {
        mailBenachrichtigungsService.sendeStornoMail(benutzerId, antrag.getId());
      }
    }
  }

  private String ermittleAntragStatus(Antrag antrag) {
    List<Bewilligung> bewilligungen = bewilligungDao.findByAntrag(antrag.getId());
    String antragStatus;
    boolean existOffen = false;
    boolean existBewilligt = false;
    boolean existAbgelehnt = false;

    for (Bewilligung b : bewilligungen) {
      /*
       * Die Bewilligungen, mit Position in [1,2] sind die Bewilligungen, die Unterschreiben müssen.
       * Positionen > 2 sind nur "zur Info" und für die Ermittlung des Antragstatus nicht relevant.
       */
      if (isNurZurInfoBewilliger(b))
        break;

      if ("OFFEN".equals(b.getBewilligungsStatusId()))
        existOffen = true;

      if ("BEWILLIGT".equals(b.getBewilligungsStatusId()))
        existBewilligt = true;

      if ("ABGELEHNT".equals(b.getBewilligungsStatusId()))
        existAbgelehnt = true;
    }

    if (existAbgelehnt) {
      antragStatus = "ABGELEHNT";
    } else if (existBewilligt) {
      if (existOffen) {
        antragStatus = "IN_ARBEIT";
      } else {
        antragStatus = "BEWILLIGT";
      }
    } else {
      antragStatus = "NEU";
    }

    return antragStatus;
  }

  private boolean isNurZurInfoBewilliger(Bewilligung b) {
    //
    // Bewilliger, die Unterschreiben müssen haben die Position 1 (erste Unterschrift) und Position 2.
    // Alle anderen Bewilliger (position > 2) sind "nur zur Info"
    //
    return b.getPosition() > 2;
  }

  @Override
  @Transactional
  public BewilligungsDaten updateBewilligungStatus(String benutzerId, UpdateBewilligungCommand command) {
    logger.debug("updateBewilligungStatus({}, {})", new Object[]{benutzerId, command});
    Bewilligung bewilligung = bewilligungDao.findById(command.getId());

    if (bewilligung == null) {
      throw new NotFoundException("Bewilligung nicht gefunden");
    }

    if (!berechtigungsService.darfBewilligungenAendern(benutzerId, bewilligung.getBenutzerId())) {
      throw new NotAuthorizedException("Keine Ausreichenden Berechtigungen zum Anzeigen der Bewilligungen");
    }

    BewilligungsStatus bewilligungsStatus = bewilligungsStatusDao.findById(command.getBewilligungsStatus());
    if (bewilligungsStatus == null) {
      throw new NotFoundException("Unbekannter Bewilligungsstatus: " + command.getBewilligungsStatus());
    }

    String bewilligungsStatusAlt = bewilligung.getBewilligungsStatusId();
    bewilligung.setBewilligungsStatusId(command.getBewilligungsStatus());
    bewilligungDao.update(bewilligung);

    String message = String.format("Bewilligungsstatus geändert. neu: %s (vorher: %s)", command.getBewilligungsStatus(),
            bewilligungsStatusAlt);

    insertAntragHistorie(benutzerId, bewilligung.getAntragId(), message);
    aktualisiereAntragStatus(benutzerId, bewilligung);

    bewilligung.setBewilligungsStatus(bewilligungsStatus);

    return createBewilligungsDaten(bewilligung);
  }

  @Override
  public BewilligungListe findByBenutzerAndFilter(String currentUserId, BewilligungsFilter filter) {
    logger.debug("findByBenutzerAndFilter({}, {})", currentUserId, filter);

    Benutzer benutzer = benutzerDao.findById(currentUserId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Bewilliger %s nicht gefunden", currentUserId));
    }

    List<Bewilligung> list;
    if (berechtigungsService.hatSonderBerechtigungen(benutzer)) {
      list = bewilligungDao.findByFilter(filter);
    } else {
      list = bewilligungDao.findByBewilligerAndFilter(currentUserId, filter);
    }

    return createBewilligungsListe(benutzer, list);
  }

  private BewilligungListe createBewilligungsListe(Benutzer benutzer, List<Bewilligung> list) {
    BenutzerDaten benutzerDaten = createBenutzerDaten(benutzer);

    List<BewilligungsListeEintrag> eintraege = new ArrayList<BewilligungsListeEintrag>();
    for (Bewilligung b : list) {
      eintraege.add(createBewilligungsListeEintrag(b));
    }

    return new BewilligungListe(benutzerDaten, eintraege);
  }

  @Override
  @Transactional
  public BewilligungsDetails leseBewilligungsDetails(String benutzerId, long bewilligungId) {
    Bewilligung bewilligung = bewilligungDao.findById(bewilligungId);

    if (bewilligung == null) {
      throw new NotFoundException("Bewilligung nicht gefunden");
    }

    if (!berechtigungsService.darfBewilligungAnsehen(benutzerId, bewilligung)) {
      throw new NotAuthorizedException("Keine Ausreichenden Berechtigungen zum Anzeigen der Bewilligung");
    }

    Antrag antrag = bewilligung.getAntrag();

    List<BewilligungsDaten> daten = new ArrayList<BewilligungsDaten>();
    for (Bewilligung b : antrag.getBewilligungen()) {
      daten.add(createBewilligungsDaten(b));
    }

    List<AntragListeEintrag> gleichzeitigeEintraege = new ArrayList<AntragListeEintrag>();
    AntragsFilter filter = new AntragsFilter();
    filter.setVon(antrag.getVon());
    filter.setBis(antrag.getBis());
    List<Antrag> antraege;

    if (berechtigungsService.hatSonderBerechtigungen(benutzerId)) {
      antraege = antragDao.findByFilter(filter);
    } else {
      antraege = antragDao.findByBewilligerAndFilter(benutzerId, filter);
    }

    for (Antrag a : antraege) {
      AntragListeEintrag eintrag = new AntragListeEintrag(a.getId(), createBenutzerDaten(a.getBenutzer()), a.getAntragArt(),
              a.getAntragStatus(), a.getVon(), a.getBis(), a.getAnzahlTage());
      if (isAktiverAntrag(a)) {
        gleichzeitigeEintraege.add(eintrag);
      }
    }

    return new BewilligungsDetails(bewilligung.getId(), antrag.getId(), bewilligung.getPosition(),
            createBenutzerDaten(antrag.getBenutzer()), createBenutzerDaten(bewilligung.getBenutzer()), antrag.getVon(),
            antrag.getBis(), antrag.getAnzahlTage(), antrag.getAntragStatus(), antrag.getAntragArt(), antrag.getSonderUrlaubArt(),
            antrag.getKommentar(), bewilligung.getBewilligungsStatus(), daten, gleichzeitigeEintraege);
  }

  private boolean isAktiverAntrag(Antrag a) {
    return !("STORNIERT".equals(a.getAntragStatusId()) || "ABGELEHNT".equals(a.getAntragStatusId()));
  }
}
