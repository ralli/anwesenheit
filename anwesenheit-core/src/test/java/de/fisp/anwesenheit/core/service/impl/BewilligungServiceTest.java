package de.fisp.anwesenheit.core.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import de.fisp.anwesenheit.core.service.MailBenachrichtigungsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.dao.BewilligungsStatusDao;
import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.BewilligungListe;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.service.BerechtigungsService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;
import de.fisp.anwesenheit.core.util.NotValidException;

public class BewilligungServiceTest {
  private BewilligungDao bewilligungDao;
  private AntragDao antragDao;
  private AntragHistorieDao antragHistorieDao;
  private BenutzerDao benutzerDao;
  private BerechtigungsService berechtigungsService;
  private BewilligungServiceImpl bewilligungService;
  private BewilligungsStatusDao bewilligungsStatusDao;
  private MailBenachrichtigungsService mailBenachrichtigungsService;

  private static final Logger logger = LoggerFactory.getLogger(BewilligungServiceTest.class);
  private static final TestDataFactory testDataFactory = new TestDataFactory();

  @Before
  public void setUp() {
    bewilligungDao = mock(BewilligungDao.class);
    antragDao = mock(AntragDao.class);
    antragHistorieDao = mock(AntragHistorieDao.class);
    benutzerDao = mock(BenutzerDao.class);
    berechtigungsService = mock(BerechtigungsService.class);
    bewilligungsStatusDao = mock(BewilligungsStatusDao.class);
    mailBenachrichtigungsService = mock(MailBenachrichtigungsService.class);
    bewilligungService = new BewilligungServiceImpl(bewilligungDao, antragDao, antragHistorieDao, benutzerDao,
            berechtigungsService, bewilligungsStatusDao, mailBenachrichtigungsService);
  }

  /**
   * Test: Die Bewilligung existiert nicht => NotFoundException
   */
  @Test
  public void deleteBewilligungShouldThrowNotFoundIfBewilligungDoesNotExist() {
    final long bewilligungId = 1234L;
    final String benutzerId = "testbenutzer";
    when(bewilligungDao.findById(bewilligungId)).thenReturn(null);
    try {
      bewilligungService.deleteBewilligung(benutzerId, bewilligungId);
    } catch (NotFoundException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotFoundException expected");
  }

  /**
   * Test: Die Bewilligung ist nicht mehr offen (also bewilligt oder abgelehnt)
   * => NotAuthorizedException
   */
  @Test
  public void deleteBewilligungShouldThrowNotAuthorizedIfBewilligungIsNotOpen() {
    final long bewilligungId = 1234L;
    final long antragId = 4321L;
    final String benutzerId = "testbenutzer";
    Bewilligung bewilligung = testDataFactory.createBewilligung(antragId, benutzerId);
    bewilligung.setBewilligungsStatusId("BEWILLIGT");
    when(bewilligungDao.findById(bewilligungId)).thenReturn(bewilligung);
    try {
      bewilligungService.deleteBewilligung(benutzerId, bewilligungId);
    } catch (NotAuthorizedException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotAuthorizedException expected");
  }

  /**
   * Test: Der aktuelle Benutzer ist nicht Eigentümer des Antrags und hat auch
   * keine Sonderberechtigungen => NotAuthorizedException
   */
  @Test
  public void deleteBewilligungShouldThrowNotAuthorized() {
    final long bewilligungId = 1234L;
    final long antragId = 4321L;
    final String bewilligerId = "testbenutzer";
    final String abweichendeBenutzerId = "falscherbenutzer";
    final String eigentuemerId = "eigentuemer";
    final Bewilligung bewilligung = testDataFactory.createBewilligung(antragId, eigentuemerId, bewilligerId);

    when(bewilligungDao.findById(bewilligungId)).thenReturn(bewilligung);
    when(berechtigungsService.isAntragEigentuemerOderErfasser(bewilligung.getAntrag(), abweichendeBenutzerId)).thenReturn(false);

    try {
      bewilligungService.deleteBewilligung(abweichendeBenutzerId, bewilligungId);
    } catch (NotAuthorizedException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotAuthorizedException expected");
  }

  /**
   * Test: Der aktuell angemeldete Benutzer hat Sonderberechtigungen. Die
   * Bewilligung kann auch gelöscht werden, obwohl der Benutzer nicht Eigentümer
   * des Antrags ist.
   */
  @Test
  public void deleteBewilligungShouldBeOkIfBenutzerIsErfasser() {
    final long bewilligungId = 1234L;
    final long antragId = 4321L;
    final String bewilligerId = "testbenutzer";
    final String abweichendeBenutzerId = "falscherbenutzer";
    final String eigentuemerId = "eigentuemer";

    Bewilligung bewilligung = testDataFactory.createBewilligung(antragId, eigentuemerId, bewilligerId);
    when(bewilligungDao.findById(bewilligungId)).thenReturn(bewilligung);
    when(berechtigungsService.isAntragEigentuemerOderErfasser(bewilligung.getAntrag(), abweichendeBenutzerId)).thenReturn(true);

    bewilligungService.deleteBewilligung(abweichendeBenutzerId, bewilligungId);
  }

  private AddBewilligungCommand createAddBewilligungCommand(long antragId, String benutzerId) {
    AddBewilligungCommand addBewilligungCommand = new AddBewilligungCommand();
    addBewilligungCommand.setAntragId(antragId);
    addBewilligungCommand.setBenutzerId(benutzerId);
    return addBewilligungCommand;
  }

  /**
   * Test: Der Bewilliger existiert nicht => NotFoundException
   */
  @Test
  public void addBewilligungFailsIfBewilligerDoesNotExist() {
    final long antragId = 4321L;
    final String bewilligerId = "bewilliger";
    final String benutzerId = "benutzer";
    final AddBewilligungCommand addBewilligungCommand = createAddBewilligungCommand(antragId, bewilligerId);

    when(benutzerDao.findById(bewilligerId)).thenReturn(null);
    try {
      bewilligungService.addBewilligung(benutzerId, addBewilligungCommand);
    } catch (NotFoundException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotFoundException expected");
  }

  /**
   * Test: Der Antrag existiert nicht => NotFoundException
   */
  @Test
  public void addBewilligungFailsIfAntragDoesNotExist() {
    final long antragId = 4321L;
    final String bewilligerId = "bewilliger";
    final String benutzerId = "benutzer";
    final Benutzer bewilliger = testDataFactory.createBenutzer(bewilligerId);
    final AddBewilligungCommand addBewilligungCommand = createAddBewilligungCommand(antragId, bewilligerId);

    when(benutzerDao.findById(bewilligerId)).thenReturn(bewilliger);
    when(antragDao.findById(antragId)).thenReturn(null);
    try {
      bewilligungService.addBewilligung(benutzerId, addBewilligungCommand);
    } catch (NotFoundException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotFoundException expected");
  }

  /**
   * Test: Der der angemeldete Benutzer ist nicht Eigentümer des Antrags und hat
   * keine Sonderberechtigungen => NotAuthorizedException
   */
  @Test
  public void addBewilligungFailsIfNotOwner() {
    final long antragId = 4321L;
    final String bewilligerId = "bewilliger";
    final String benutzerId = "benutzer";
    final String eigentuemerId = "eigentuemer";
    final Benutzer bewilliger = testDataFactory.createBenutzer(bewilligerId);
    final Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    final AddBewilligungCommand addBewilligungCommand = createAddBewilligungCommand(antragId, bewilligerId);
    final Antrag antrag = testDataFactory.createAntrag("URLAUB", eigentuemerId);
    antrag.setId(antragId);

    when(benutzerDao.findById(bewilligerId)).thenReturn(bewilliger);
    when(benutzerDao.findById(benutzerId)).thenReturn(benutzer);
    when(antragDao.findById(antragId)).thenReturn(antrag);
    try {
      bewilligungService.addBewilligung(benutzerId, addBewilligungCommand);
    } catch (NotAuthorizedException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotAuthorizedException expected");
  }

  /**
   * Test: Alle Vorbedingungen sind erfüllt
   */
  @Test
  public void addBewilligungSucceeds() {
    final long antragId = 4321L;
    final String bewilligerId = "bewilliger";
    final String benutzerId = "benutzer";
    final String eigentuemerId = "eigentuemer";
    final Benutzer bewilliger = testDataFactory.createBenutzer(bewilligerId);
    final Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    final AddBewilligungCommand addBewilligungCommand = createAddBewilligungCommand(antragId, bewilligerId);
    final Antrag antrag = testDataFactory.createAntrag("URLAUB", eigentuemerId);
    antrag.setId(antragId);

    benutzer.getBenutzerRollen().add(testDataFactory.createBenutzerRolle(benutzerId, "ERFASSER"));

    when(benutzerDao.findById(bewilligerId)).thenReturn(bewilliger);
    when(antragDao.findById(antragId)).thenReturn(antrag);
    when(berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)).thenReturn(true);

    bewilligungService.addBewilligung(benutzerId, addBewilligungCommand);
  }

  /**
   * Test: Die Bewilligung existiert bereits => NotValidException
   */
  @Test
  public void addBewilligungFailsIfBewilligungExists() {
    final long antragId = 4321L;
    final String bewilligerId = "bewilliger";
    final String benutzerId = "benutzer";
    final String eigentuemerId = benutzerId;
    final Benutzer bewilliger = testDataFactory.createBenutzer(bewilligerId);
    final Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    final AddBewilligungCommand addBewilligungCommand = createAddBewilligungCommand(antragId, bewilligerId);
    final Antrag antrag = testDataFactory.createAntrag("URLAUB", eigentuemerId);
    final Bewilligung bewilligung = testDataFactory.createBewilligung(antragId, bewilligerId);

    antrag.setId(antragId);

    when(bewilligungDao.findByAntragIdAndBewilliger(antragId, bewilligerId)).thenReturn(bewilligung);
    when(benutzerDao.findById(bewilligerId)).thenReturn(bewilliger);
    when(benutzerDao.findById(benutzerId)).thenReturn(benutzer);
    when(antragDao.findById(antragId)).thenReturn(antrag);
    try {
      bewilligungService.addBewilligung(benutzerId, addBewilligungCommand);
    } catch (NotValidException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotValidException expected");
  }

  /**
   * Test: Der Antragsteller möchte den Antrag selbst bewilligen =>
   * NotValidException
   */
  @Test
  public void addBewilligungFailsIfBewilligerGleichAntragSteller() {
    final long antragId = 4321L;
    final String benutzerId = "benutzer";
    final String bewilligerId = benutzerId;
    final String eigentuemerId = benutzerId;
    final Benutzer bewilliger = testDataFactory.createBenutzer(bewilligerId);
    final Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    final AddBewilligungCommand addBewilligungCommand = createAddBewilligungCommand(antragId, bewilligerId);
    final Antrag antrag = testDataFactory.createAntrag("URLAUB", eigentuemerId);
    final Bewilligung bewilligung = testDataFactory.createBewilligung(antragId, bewilligerId);

    antrag.setId(antragId);

    when(bewilligungDao.findByAntragIdAndBewilliger(antragId, bewilligerId)).thenReturn(bewilligung);
    when(benutzerDao.findById(bewilligerId)).thenReturn(bewilliger);
    when(benutzerDao.findById(benutzerId)).thenReturn(benutzer);
    when(antragDao.findById(antragId)).thenReturn(antrag);
    try {
      bewilligungService.addBewilligung(benutzerId, addBewilligungCommand);
    } catch (NotValidException ex) {
      logger.debug("Alles prima: " + ex.getMessage());
      return;
    }
    Assert.fail("NotValidException expected");
  }

  @Test
  public void findByBenutzerIdShouldFailIfBenutzerNotFound() {
    final String benutzerId = "chef";
    final String currentUserId = "backoffice";
    final Benutzer chef = null;
    /*
     * schlägt ebenfalls fehl, ist aber nicht Bestandteil des Tests (die
     * NotFoundException wird vom BerechtigungsService geworfen)
     */
    final Benutzer backoffice = testDataFactory.createBenutzer(currentUserId);
    when(benutzerDao.findById(benutzerId)).thenReturn(chef);
    when(benutzerDao.findById(currentUserId)).thenReturn(backoffice);
    try {
      bewilligungService.findByBenutzer(currentUserId, benutzerId);
    } catch (NotFoundException ex) {
      // alles ok
      return;
    }
    Assert.fail("NotFoundException erwartet");
  }

  @Test
  public void findByBenutzerIdShouldFailIfInsufficientRights() {
    final String benutzerId = "chef";
    final String currentUserId = "backoffice";
    final Benutzer chef = testDataFactory.createBenutzer(benutzerId);
    final Benutzer backoffice = testDataFactory.createBenutzer(currentUserId);

    when(benutzerDao.findById(benutzerId)).thenReturn(chef);
    when(benutzerDao.findById(currentUserId)).thenReturn(backoffice);
    when(berechtigungsService.darfBewilligungenAnsehen(currentUserId, benutzerId)).thenReturn(false);
    try {
      bewilligungService.findByBenutzer(currentUserId, benutzerId);
    } catch (NotAuthorizedException ex) {
      // alles ok
      return;
    }
    Assert.fail("NotAuthorizedException erwartet");
  }

  @Test
  public void findByBenutzerIdShouldSucceed() {
    final String benutzerId = "chef";
    final String currentUserId = "backoffice";
    final Benutzer chef = testDataFactory.createBenutzer(benutzerId);
    final Benutzer backoffice = testDataFactory.createBenutzer(currentUserId);
    final List<Bewilligung> list = new ArrayList<Bewilligung>();
    list.add(testDataFactory.createBewilligung(1, currentUserId, benutzerId));
    when(benutzerDao.findById(benutzerId)).thenReturn(chef);
    when(benutzerDao.findById(currentUserId)).thenReturn(backoffice);
    when(berechtigungsService.darfBewilligungenAnsehen(currentUserId, benutzerId)).thenReturn(true);
    when(bewilligungDao.findByBewilliger(benutzerId)).thenReturn(list);
    BewilligungListe result = bewilligungService.findByBenutzer(currentUserId, benutzerId);
    logger.debug("{}", result);
    Assert.assertEquals(1, result.getBewilligungen().size());
  }
}
