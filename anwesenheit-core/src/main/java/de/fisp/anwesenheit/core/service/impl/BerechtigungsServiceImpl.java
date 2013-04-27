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

  @Override
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

  @Override
  public boolean darfAntragAnsehen(Antrag antrag, String benutzerId) {
    if (benutzerId == null)
      return false;

    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    return darfAntragAnsehen(antrag, benutzer);
  }

  @Override
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

  @Override
  public boolean isAntragEigentuemerOderErfasser(Antrag antrag, String benutzerId) {
    if (benutzerId == null)
      return false;

    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    return isAntragEigentuemerOderErfasser(antrag, benutzer);
  }

  @Override
  public boolean darfAlleAntraegeSehen(String currentBenutzerId, String benutzerId) {
    if (currentBenutzerId == null)
      return false;

    if (currentBenutzerId.equals(benutzerId))
      return true;

    return hatSonderBerechtigungen(currentBenutzerId);
  }

  @Override
  public boolean hatSonderBerechtigungen(Benutzer benutzer) {
    for (BenutzerRolle br : benutzer.getBenutzerRollen()) {
      if ("ERFASSER".equals(br.getRolle()))
        return true;
    }

    return false;
  }

  @Override
  public boolean hatSonderBerechtigungen(String benutzerId) {
    if (benutzerId == null)
      return false;

    Benutzer benutzer = benutzerDao.findById(benutzerId);
    if (benutzer == null) {
      throw new NotFoundException(String.format("Benutzer %s nicht gefunden", benutzerId));
    }
    return hatSonderBerechtigungen(benutzer);
  }

  @Override
  public boolean darfBewilligungenAnsehen(String currentBenutzerId, String benutzerId) {
    if (currentBenutzerId == null)
      return false;

    if (currentBenutzerId.equals(benutzerId))
      return true;

    return hatSonderBerechtigungen(currentBenutzerId);
  }

  @Override
  public boolean darfBewilligungenAendern(String currentBenutzerId, String benutzerId) {
    if (currentBenutzerId.equals(benutzerId))
      return true;

    return hatSonderBerechtigungen(currentBenutzerId);
  }
}
