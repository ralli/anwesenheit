package de.fisp.anwesenheit.core.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
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

@Service
public class BewilligungServiceImpl implements BewilligungService {
  private static final Logger logger = LoggerFactory.getLogger(BewilligungServiceImpl.class);
  private BewilligungDao bewilligungDao;
  private AntragDao antragDao;
  private AntragHistorieDao antragHistorieDao;
  private BenutzerDao benutzerDao;
  private BerechtigungsService berechtigungsService;

  @Autowired
  public BewilligungServiceImpl(BewilligungDao bewilligungDao, AntragDao antragDao, AntragHistorieDao antragHistorieDao,
      BenutzerDao benutzerDao, BerechtigungsService berechtigungsService) {
    this.bewilligungDao = bewilligungDao;
    this.antragDao = antragDao;
    this.antragHistorieDao = antragHistorieDao;
    this.benutzerDao = benutzerDao;
    this.berechtigungsService = berechtigungsService;
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
    addAntragHistorie(bewilligung.getAntragId(), benutzerId, message);
    bewilligungDao.delete(bewilligung);
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
      throw new NotValidException("Anträge dürfen nicht durch sich selbst bewilligt werden");
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

    logger.debug("addBewilligung({}) = {}", command, daten);

    return daten;
  }
}
