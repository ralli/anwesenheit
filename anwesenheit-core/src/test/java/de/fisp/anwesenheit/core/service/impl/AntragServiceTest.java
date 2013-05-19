package de.fisp.anwesenheit.core.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.dao.SonderUrlaubArtDao;
import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;
import de.fisp.anwesenheit.core.domain.UpdateAntragCommand;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.service.BerechtigungsService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;
import de.fisp.anwesenheit.core.util.NotValidException;

public class AntragServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(AntragServiceTest.class);
  private AntragDao antragDao;
  private BewilligungDao bewilligungDao;
  private BenutzerDao benutzerDao;
  private SonderUrlaubArtDao sonderUrlaubArtDao;
  private AntragHistorieDao antragHistorieDao;
  private BerechtigungsService berechtigungsService;
  private AntragServiceImpl antragService;
  private TestDataFactory testDataFactory;

  @Before
  public void setUp() {
    testDataFactory = new TestDataFactory();
    antragDao = mock(AntragDao.class);
    bewilligungDao = mock(BewilligungDao.class);
    benutzerDao = mock(BenutzerDao.class);
    sonderUrlaubArtDao = mock(SonderUrlaubArtDao.class);
    antragHistorieDao = mock(AntragHistorieDao.class);
    berechtigungsService = mock(BerechtigungsService.class);
    antragService = new AntragServiceImpl(antragDao, bewilligungDao, benutzerDao, sonderUrlaubArtDao, antragHistorieDao,
            berechtigungsService);
  }

  @Test
  public void testFindAntragByIdSucceeds() throws Exception {
    final long antragId = 10L;
    final long bewilligungId = 1234L;
    final String antragArt = "URLAUB";
    final String benutzerId = "demo123";
    final String chefId = "chef123";
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antrag.setId(antragId);
    Bewilligung bewilligung = testDataFactory.createBewilligung(antragId, chefId);
    bewilligung.setId(bewilligungId);
    List<Bewilligung> bewilligungen = new ArrayList<Bewilligung>();
    bewilligungen.add(bewilligung);

    when(antragDao.findById(antragId)).thenReturn(antrag);
    when(bewilligungDao.findByAntrag(antragId)).thenReturn(bewilligungen);
    when(berechtigungsService.darfAntragAnsehen(antrag, benutzerId)).thenReturn(true);
    AntragsDaten antragsDaten = antragService.findAntragById(benutzerId, antragId);
    Assert.assertEquals(antragId, antragsDaten.getId());
    Assert.assertEquals(benutzerId, antragsDaten.getBenutzer().getBenutzerId());
    Assert.assertEquals(antragArt, antragsDaten.getAntragArt().getAntragArt());
  }

  @Test
  public void testFindAntragByIdFailsIfNotAuthorized() throws Exception {
    final long antragId = 10L;
    final String antragArt = "URLAUB";
    final String benutzerId = "demo123";
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antrag.setId(antragId);
    List<Bewilligung> bewilligungen = new ArrayList<Bewilligung>();

    when(antragDao.findById(antragId)).thenReturn(antrag);
    when(bewilligungDao.findByAntrag(antragId)).thenReturn(bewilligungen);
    when(berechtigungsService.darfAntragAnsehen(antrag, benutzerId)).thenReturn(false);
    try {
      antragService.findAntragById(benutzerId, antragId);
    } catch (NotAuthorizedException ex) {
      // alles ok
      return;
    }
    Assert.fail("NotAuthorizedException erwartet");
  }

  @Test
  public void testFindAntragByIdFailsIfNotFound() throws Exception {
    final long antragId = 10L;
    final String benutzerId = "demo123";

    when(antragDao.findById(antragId)).thenReturn(null);
    try {
      antragService.findAntragById(benutzerId, antragId);
    } catch (NotFoundException ex) {
      // alles ok.
      return;
    }
    Assert.fail("NotFoundException erwartet");
  }

  @Test
  public void testFindByBenutzer() throws Exception {
    final String benutzerId = "demo123";
    final long antragId = 10L;
    final String antragArt = "URLAUB";
    Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    List<Antrag> antragList = new ArrayList<Antrag>();
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antrag.setId(antragId);
    antragList.add(antrag);

    when(benutzerDao.findById(benutzerId)).thenReturn(benutzer);
    when(antragDao.findByBenutzerId(benutzerId)).thenReturn(antragList);

    AntragListe bla = antragService.findByBenutzer(benutzerId, benutzerId);
    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, bla);
    logger.info(writer.toString());
  }

  @Test
  public void antragImStatusNeuDarfGeaendertWerden() {
    final String benutzerId = "demo123";
    final long antragId = 10L;
    final String antragArt = "URLAUB";
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antrag.setId(antragId);

    UpdateAntragCommand command = new UpdateAntragCommand();
    command.setId(antragId);
    command.setAntragArt(antragArt);
    command.setSonderUrlaubArt("");
    command.setVon(antrag.getVon());
    command.setBis(antrag.getBis());
    when(antragDao.findById(antragId)).thenReturn(antrag);
    when(berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)).thenReturn(true);
    antragService.updateAntrag(benutzerId, antragId, command);
  }

  @Test
  public void antragImStatusInBearbeitungDarfNichtGeaendertWerden() {
    final String benutzerId = "demo123";
    final long antragId = 10L;
    final String antragArt = "URLAUB";
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antrag.setId(antragId);
    antrag.setAntragStatusId("IN_BEARBEITUNG");
    UpdateAntragCommand command = new UpdateAntragCommand();
    command.setId(antragId);
    command.setAntragArt(antragArt);
    command.setSonderUrlaubArt("");
    command.setVon(antrag.getVon());
    command.setBis(antrag.getBis());
    when(antragDao.findById(antragId)).thenReturn(antrag);
    when(berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)).thenReturn(true);
    try {
      antragService.updateAntrag(benutzerId, antragId, command);
    } catch (NotValidException ex) {
      return;
    }
    Assert.fail("NotValidException erwartet");
  }

  @Test
  public void antragImStatusNeuDarfGeloeschtWerden() {
    final String benutzerId = "demo123";
    final long antragId = 10L;
    final String antragArt = "URLAUB";
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antrag.setId(antragId);
    when(antragDao.findById(antragId)).thenReturn(antrag);
    when(berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)).thenReturn(true);
    antragService.deleteAntrag(benutzerId, antragId);
  }

  @Test
  public void antragImStatusInBearbeitungDarfNichtGeloeschtWerden() {
    final String benutzerId = "demo123";
    final long antragId = 10L;
    final String antragArt = "URLAUB";
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antrag.setId(antragId);
    antrag.setAntragStatusId("IN_BEARBEITUNG");
    when(antragDao.findById(antragId)).thenReturn(antrag);
    when(berechtigungsService.isAntragEigentuemerOderErfasser(antrag, benutzerId)).thenReturn(true);
    try {
      antragService.deleteAntrag(benutzerId, antragId);
    } catch (NotValidException ex) {
      return;
    }
    Assert.fail("NotValidException erwartet");
  }

  @Test
  public void antragMitWenigerAlsZweiBewilligernDarfNichtAngelegtWerden() {
    final String benutzerId = "testbenutzer";

    CreateAntragCommand cmd = new CreateAntragCommand();
    cmd.setAntragArt("URLAUB");
    cmd.setAnzahlTage(1);
    cmd.setVon(testDataFactory.createDate(7, 12, 2013));
    cmd.setBis(testDataFactory.createDate(7, 12, 2013));
    cmd.setBenutzerId(benutzerId);
    cmd.setBewilliger(new String[]{});
    try {
      antragService.createAntrag(benutzerId, cmd);
    } catch (NotValidException ex) {
      return;
    }
    Assert.fail("NotValidException erwartet");
  }

  @Test
  public void antragAnlageOhneBerechtigung() {
    final String benutzerId = "testbenutzer";

    CreateAntragCommand cmd = new CreateAntragCommand();
    cmd.setAntragArt("URLAUB");
    cmd.setAnzahlTage(1);
    cmd.setVon(testDataFactory.createDate(7, 12, 2013));
    cmd.setBis(testDataFactory.createDate(7, 12, 2013));
    cmd.setBenutzerId(benutzerId);
    cmd.setBewilliger(new String[]{"chef", "boss"});
    when(berechtigungsService.isAntragEigentuemerOderErfasser(Mockito.isA(Antrag.class), Mockito.eq(benutzerId))).thenReturn(false);
    try {
      antragService.createAntrag(benutzerId, cmd);
    } catch (NotAuthorizedException ex) {
      return;
    }
    Assert.fail("NotAuthorizedException erwartet");
  }

  @Test
  public void antragAnlageErfolgreich() {
    final String benutzerId = "testbenutzer";

    CreateAntragCommand cmd = new CreateAntragCommand();
    cmd.setAntragArt("URLAUB");
    cmd.setAnzahlTage(1);
    cmd.setVon(testDataFactory.createDate(7, 12, 2013));
    cmd.setBis(testDataFactory.createDate(7, 12, 2013));
    cmd.setBenutzerId(benutzerId);
    cmd.setBewilliger(new String[]{"chef", "boss"});
    when(berechtigungsService.isAntragEigentuemerOderErfasser(Mockito.isA(Antrag.class), Mockito.eq(benutzerId))).thenReturn(true);
    antragService.createAntrag(benutzerId, cmd);
  }
}
