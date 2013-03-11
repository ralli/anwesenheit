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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;

public class AntragServiceTest {
	private static final Logger logger = LoggerFactory
			.getLogger(AntragServiceTest.class);
	private AntragDao antragDao;
	private BewilligungDao bewilligungDao;
	private BenutzerDao benutzerDao;
	private AntragHistorieDao antragHistorieDao;

	private AntragServiceImpl antragService;
	private TestDataFactory testDataFactory;

	@Before
	public void setUp() {
		testDataFactory = new TestDataFactory();
		antragDao = mock(AntragDao.class);
		bewilligungDao = mock(BewilligungDao.class);
		benutzerDao = mock(BenutzerDao.class);
		antragHistorieDao = mock(AntragHistorieDao.class);
		antragService = new AntragServiceImpl(antragDao, bewilligungDao,
				benutzerDao, antragHistorieDao);
	}

	@Test
	public void testFindAntragById() throws Exception {
		final long antragId = 10L;
		final long bewilligungId = 1234L;
		final String antragArt = "URLAUB";
		final String benutzerId = "demo123";
		final String chefId = "chef123";
		Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
		antrag.setId(antragId);
		Bewilligung bewilligung = testDataFactory.createBewilligung(antragId,
				chefId);
		bewilligung.setId(bewilligungId);
		List<Bewilligung> bewilligungen = new ArrayList<Bewilligung>();
		bewilligungen.add(bewilligung);

		when(antragDao.findById(antragId)).thenReturn(antrag);
		when(bewilligungDao.findByAntrag(antragId)).thenReturn(bewilligungen);
		AntragsDaten antragsDaten = antragService.findAntragById(antragId);
		Assert.assertEquals(antragId, antragsDaten.getId());
		Assert.assertEquals(benutzerId, antragsDaten.getBenutzer()
				.getBenutzerId());
		Assert.assertEquals(antragArt, antragsDaten.getAntragArt().getAntragArt());
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, antragsDaten);
		logger.info(writer.toString());
	}

	@Test
	public void testFindAntragByIdNotFoundReturnsNull() throws Exception {
		final long antragId = 10L;
		when(antragDao.findById(antragId)).thenReturn(null);
		AntragsDaten antragsDaten = antragService.findAntragById(antragId);
		Assert.assertNull(antragsDaten);
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

		AntragListe bla = antragService.findByBenutzer(benutzerId);
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, bla);
		logger.info(writer.toString());
	}
}
