package de.fisp.anwesenheit.core.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import de.fisp.anwesenheit.core.service.ParameterService;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;
import org.junit.Before;
import org.junit.Test;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;
import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;
import de.fisp.anwesenheit.core.service.AntragService;
import de.fisp.anwesenheit.core.service.MailService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MailBenachrichtigungsServiceTest {
  private AntragService antragService;
  private MailService mailService;
  private VelocityEngine velocityEngine;
  private MarkDownFormatterImpl formatter;
  private final ToolManager toolManager = createToolManager();
  private MailBenachrichtigungsServiceImpl mailBenachrichtigungsService;
  private ParameterService parameterService;
  private final TestDataFactory testDataFactory = new TestDataFactory();

  @Before
  public void setUp() {
    antragService = mock(AntragService.class);
    mailService = mock(MailService.class);
    parameterService = mock(ParameterService.class);
    formatter = new MarkDownFormatterImpl();
    velocityEngine = createVelocityEngine();
    mailBenachrichtigungsService =  new MailBenachrichtigungsServiceImpl(mailService, velocityEngine, toolManager, formatter, parameterService);
    mailBenachrichtigungsService.setAntragService(antragService);
  }

  private VelocityEngine createVelocityEngine() {
    Properties properties = new Properties();
    properties.setProperty("resource.loader", "class");
    properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    properties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
    properties.setProperty("runtime.log.logsystem.log4j.logger", "de.fisp.velocity");

    return new VelocityEngine(properties);
  }

  private ToolManager createToolManager() {
    ToolManager manager = new ToolManager();
    manager.configure("toolbox.xml");
    return manager;
  }

  private AntragsDaten createAntragsDaten(long antragId) {
    AntragArt antragArt = testDataFactory.createAntragArt("URLAUB", "Urlaub");
    SonderUrlaubArt sonderUrlaubArt = null;
    Date von = testDataFactory.createDate(24, 12, 2013);
    Date bis = testDataFactory.createDate(6, 1, 2014);
    AntragStatus antragStatus = testDataFactory.createAntragStatus("NEU");
    double anzahlTage = 0.5;
    BenutzerDaten benutzer = new BenutzerDaten("test", "Horst", "Hrubesch", "test@test.de");
    BewilligungsStatus bewilligungsStatus = testDataFactory.createBewilligungsStatus("OFFEN");
    BenutzerDaten bewilliger = new BenutzerDaten("bewilliger", "Charly", "Chan", "oliver.porzel@f-i-solutions-plus.de");
    BewilligungsDaten bewilligungsDaten = new BewilligungsDaten(1, antragId, 1, bewilligungsStatus, bewilliger);
    BenutzerDaten bewilliger2 = new BenutzerDaten("bewilliger", "King", "Kong", "ralph.juhnke@f-i-solutions-plus.de");
    BewilligungsDaten bewilligungsDaten2 = new BewilligungsDaten(2, antragId, 2, bewilligungsStatus, bewilliger2);
    List<BewilligungsDaten> bewilligungen = new ArrayList<BewilligungsDaten>();
    bewilligungen.add(bewilligungsDaten);
    bewilligungen.add(bewilligungsDaten2);
    String kommentar = String.format("Ich bin ein Kommentar zu Antrag: %d\n\n* Eins\n* Zwei\n* Drei", antragId);
    AntragsDaten daten = new AntragsDaten(antragId, antragArt, antragStatus, sonderUrlaubArt, von, bis, anzahlTage, benutzer,
            bewilligungen, kommentar);
    return daten;
  }

  @Test
  public void sendeErsteBewilligungsMail() {
    String benutzerId = "demo";
    long antragId = 2L;
    AntragsDaten antragsDaten = createAntragsDaten(antragId);
    when(antragService.findAntragById(benutzerId, antragId)).thenReturn(antragsDaten);
    when(parameterService.getValue("base.url")).thenReturn("http://base.url");
    when(parameterService.getValue("email.replyToAddress")).thenReturn("replyto@nowhere.com");
    mailBenachrichtigungsService.sendeErsteBewilligungsMail(benutzerId, antragId);
  }

  @Test
  public void sendeZweiteBewilligungsMail() {
    String benutzerId = "demo";
    long antragId = 2L;
    AntragsDaten antragsDaten = createAntragsDaten(antragId);
    when(antragService.findAntragById(benutzerId, antragId)).thenReturn(antragsDaten);
    when(parameterService.getValue("base.url")).thenReturn("http://base.url");
    when(parameterService.getValue("email.replyToAddress")).thenReturn("replyto@nowhere.com");
    mailBenachrichtigungsService.sendeZweiteBewilligungsMail(benutzerId, antragId);
  }

  @Test
  public void sendeAntragAbgelehntMails() {
    String benutzerId = "demo";
    long antragId = 2L;
    long bewilligungsId = 1L;
    AntragsDaten antragsDaten = createAntragsDaten(antragId);
    when(antragService.findAntragById(benutzerId, antragId)).thenReturn(antragsDaten);
    when(parameterService.getValue("base.url")).thenReturn("http://base.url");
    when(parameterService.getValue("email.replyToAddress")).thenReturn("replyto@nowhere.com");
    mailBenachrichtigungsService.sendeAbgelehntMail(benutzerId, antragId, bewilligungsId);
  }

  @Test
  public void sendeAntragBewilligtMails() {
    String benutzerId = "demo";
    long antragId = 2L;
    long bewilligungsId = 1L;
    AntragsDaten antragsDaten = createAntragsDaten(antragId);
    when(antragService.findAntragById(benutzerId, antragId)).thenReturn(antragsDaten);
    when(parameterService.getValue("base.url")).thenReturn("http://base.url");
    when(parameterService.getValue("email.replyToAddress")).thenReturn("replyto@nowhere.com");
    mailBenachrichtigungsService.sendeBewilligtMail(benutzerId, antragId, bewilligungsId);
  }

}
