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
import de.fisp.anwesenheit.core.service.BewilligungService;

@Service
public class BewilligungServiceImpl implements BewilligungService {
  private static final Logger logger = LoggerFactory.getLogger(BewilligungServiceImpl.class);
  private BewilligungDao bewilligungDao;
  private AntragDao antragDao;
  private AntragHistorieDao antragHistorieDao;
  private BenutzerDao benutzerDao;

  @Autowired
  public BewilligungServiceImpl(BewilligungDao bewilligungDao, AntragDao antragDao, AntragHistorieDao antragHistorieDao,
      BenutzerDao benutzerDao) {
    this.bewilligungDao = bewilligungDao;
    this.antragDao = antragDao;
    this.antragHistorieDao = antragHistorieDao;
    this.benutzerDao = benutzerDao;
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
  public boolean deleteBewilligung(String benutzerId, long bewilligungId) {
    logger.debug("deleteBewilligung({})", bewilligungId);
    Bewilligung bewilligung = bewilligungDao.findById(bewilligungId);
    if (bewilligung == null) {
      logger.error("Bewilligung nicht gefunden");
      return false;
    }
    String message = String.format("Bewilligung Bewilliger: %s, Status: %s gelöscht", bewilligung.getBenutzerId(),
        bewilligung.getBewilligungsStatusId());
    addAntragHistorie(bewilligung.getAntragId(), benutzerId, message);
    bewilligungDao.delete(bewilligung);

    return true;
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
      logger.error("Bewilliger nicht gefunden");
      return null;
    }

    Antrag antrag = antragDao.findById(command.getAntragId());    
    if (antrag == null) {
      logger.error("Antrag nicht gefunden");
      return null;
    }

    Bewilligung bewilligung = new Bewilligung();
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
