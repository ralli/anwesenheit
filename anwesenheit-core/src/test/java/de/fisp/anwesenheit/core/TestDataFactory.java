package de.fisp.anwesenheit.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import de.fisp.anwesenheit.core.entities.*;
import org.springframework.stereotype.Service;

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

  public AntragArt createAntragArt(String antragArt, String bezeichnung) {
    AntragArt art = new AntragArt();
    art.setAntragArt(antragArt);
    art.setPosition(1);
    art.setBezeichnung(bezeichnung);
    return art;
  }

  public AntragArt createAntragArt(String antragArt) {
    return createAntragArt(antragArt, antragArt);
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
    calendar.clear();
    calendar.set(year, month - 1, day, 0, 0, 0);
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

  public List<FeiertagDefinition> createDefinitionen() {
    List<FeiertagDefinition> result = new ArrayList<FeiertagDefinition>();
    result.add(createFeiertagDefinition(0, 0, 1.0, 2, -2, "Karfreitag"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 2, 0, "Ostersonntag"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 2, 1, "Ostermontag"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 2, 39, "Himmelfahrt"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 2, 49, "Pfingstsonntag"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 2, 50, "Pfingstmontag"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 2, 60, "Fronleichnam"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 3, 0, "Muttertag"));
    result.add(createFeiertagDefinition(0, 0, 1.0, 4, 1, "Buß und Bettag"));
    return result;
  }

  public FeiertagDefinition createFeiertagDefinition(int tag, int monat, double anteil, int typ, int offset, String name) {
    FeiertagDefinition definition = new FeiertagDefinition();
    definition.setName(name);
    definition.setReferenzTag(tag);
    definition.setReferenzMonat(monat);
    definition.setOffset(offset);
    definition.setAnteilArbeitszeit(anteil);
    definition.setType(typ);
    return definition;
  }

  public Feiertag createFeiertag(Date date, double anteilArbeitszeit, String name) {
    Feiertag feiertag = new Feiertag();
    feiertag.setAnteilArbeitszeit(anteilArbeitszeit);
    feiertag.setDatum(date);
    feiertag.setName(name);
    return feiertag;
  }

  public Parameter createParameter() {
    Parameter parameter = new Parameter();

    parameter.setId("test");
    parameter.setBeschreibung("Beschreibung des Parameters");
    parameter.setName("test.name");
    parameter.setWert("Test.Wert");

    return parameter;
  }

  public List<Parameter> createParameters() {
    List<Parameter> list = new ArrayList<Parameter>();
    list.add(createParameter());
    return list;
  }
}
