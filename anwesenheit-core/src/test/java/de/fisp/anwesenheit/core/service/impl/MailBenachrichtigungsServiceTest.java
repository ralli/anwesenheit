package de.fisp.anwesenheit.core.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class MailBenachrichtigungsServiceTest {
	private AntragService antragService;
	private MailService mailService;
	private MailBenachrichtigungsServiceImpl mailBenachrichtigungsServiceImpl;
	private TestDataFactory testDataFactory = new TestDataFactory();

	@Before
	public void setUp() {
		antragService = mock(AntragService.class);
		mailService = mock(MailService.class);
		// mailService = new MailServiceImpl();
		mailBenachrichtigungsServiceImpl = new MailBenachrichtigungsServiceImpl(
				antragService, mailService);
	}

	private AntragsDaten createAntragsDaten(long antragId) {
		AntragArt antragArt = testDataFactory.createAntragArt("Urlaub");
		SonderUrlaubArt sonderUrlaubArt = null;
		Date von = testDataFactory.createDate(24, 12, 2013);
		Date bis = testDataFactory.createDate(6, 1, 2014);
		AntragStatus antragStatus = testDataFactory.createAntragStatus("NEU");
		double anzahlTage = 0.5;
		BenutzerDaten benutzer = new BenutzerDaten("test", "Horst", "Hrubesch",
				"test.test.de");
		BewilligungsStatus bewilligungsStatus = testDataFactory.createBewilligungsStatus("OFFEN");
		BenutzerDaten bewilliger = new BenutzerDaten("bewilliger", "Charly", "Chan", "oliver.porzel@f-i-solutions-plus.de");
		BewilligungsDaten bewilligungsDaten = new BewilligungsDaten(0, antragId, 1, bewilligungsStatus, bewilliger);
		List<BewilligungsDaten> bewilligungen = new ArrayList<BewilligungsDaten>();
		bewilligungen.add(bewilligungsDaten);
		AntragsDaten daten = new AntragsDaten(antragId, antragArt, antragStatus,
				sonderUrlaubArt, von, bis, anzahlTage, benutzer, bewilligungen);
		return daten;
	}

	@Test
	public void sendMail() {
		String benutzerId = "testbenutzer";
		long antragId = 1L;
		AntragsDaten antragsDaten = createAntragsDaten(antragId);
		when(antragService.findAntragById(benutzerId, antragId)).thenReturn(antragsDaten);
		mailBenachrichtigungsServiceImpl.sendeAntragsMail(benutzerId,
				antragId);
	}
}
