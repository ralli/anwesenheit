package de.fisp.anwesenheit.core;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.BenutzerRolle;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;

@Service
public class TestDataFactory {
  public Benutzer createBenutzer(String benutzerId) {
    Benutzer benutzer = new Benutzer();
    benutzer.setBenutzerId(benutzerId);
    benutzer.setVorname("King");
    benutzer.setNachname("Kong");
    benutzer.setBenutzertyp("native");
    benutzer.setEmail("demo@demo.de");
    benutzer.setSalt("12092109382019380");
    benutzer.setPasswordHash("xxxxxxxxxx12092109382019380");
    benutzer.setBenutzerRollen(new HashSet<BenutzerRolle>());

    return benutzer;
  }

  public AntragArt createAntragArt(String antragArt) {
    AntragArt art = new AntragArt();
    art.setAntragArt(antragArt);
    art.setPosition(1);
    art.setBezeichnung("Test");
    return art;
  }

  public AntragStatus createAntragStatus(String antragStatus) {
    AntragStatus status = new AntragStatus();
    status.setAntragStatus(antragStatus);
    status.setPosition(1);
    status.setBezeichnung("Teststatus");
    return status;
  }

  public Date createDate(int day, int month, int year) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day, 0, 0, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public Antrag createAntrag(String antragArt, String benutzerId) {
    final String antragStatus = "NEU";

    Antrag antrag = new Antrag();
    antrag.setBenutzerId(benutzerId);
    antrag.setVon(createDate(1, 3, 2013));
    antrag.setBis(createDate(20, 3, 2013));
    antrag.setAntragArtId(antragArt);
    antrag.setAntragStatusId(antragStatus);
    antrag.setAntragArt(createAntragArt(antragArt));
    antrag.setAntragStatus(createAntragStatus(antragStatus));
    antrag.setBenutzer(createBenutzer(benutzerId));
    return antrag;
  }

  public BenutzerRolle createBenutzerRolle(String benutzerId, String rolle) {
    BenutzerRolle benutzerRolle = new BenutzerRolle();
    benutzerRolle.setBenutzerId(benutzerId);
    benutzerRolle.setRolle(rolle);
    return benutzerRolle;
  }

  public BewilligungsStatus createBewilligungsStatus(String bewilligungsStatusId) {
    BewilligungsStatus bewilligungsStatus = new BewilligungsStatus();
    bewilligungsStatus.setBewilligungsStatus(bewilligungsStatusId);
    bewilligungsStatus.setBezeichnung(bewilligungsStatusId);
    bewilligungsStatus.setPosition(1);
    return bewilligungsStatus;
  }

  public Bewilligung createBewilligung(long antragId, String antragBenutzerId, String benutzerId) {
    final String bewilligungsStatusId = "OFFEN";

    Bewilligung bewilligung = new Bewilligung();
    bewilligung.setAntragId(antragId);
    bewilligung.setPosition(1);
    bewilligung.setBenutzerId(benutzerId);
    bewilligung.setBewilligungsStatusId(bewilligungsStatusId);
    bewilligung.setBewilligungsStatus(createBewilligungsStatus(bewilligungsStatusId));
    bewilligung.setBenutzer(createBenutzer(benutzerId));
    Antrag antrag = createAntrag("URLAUB", antragBenutzerId);
    antrag.setId(antragId);
    bewilligung.setAntrag(antrag);
    return bewilligung;
  }

  public Bewilligung createBewilligung(long antragId, String benutzerId) {
    return createBewilligung(antragId, "testbenutzer", benutzerId);
  }
}
