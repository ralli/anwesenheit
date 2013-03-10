package de.fisp.anwesenheit.core;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.BenutzerRolle;
import de.fisp.anwesenheit.core.entities.Bewilligung;

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
		return benutzer;
	}

	public Date createDate(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public Antrag createAntrag(String antragArt, String benutzerId) {
		Antrag antrag = new Antrag();
		antrag.setBenutzerId(benutzerId);
		antrag.setVon(createDate(1, 3, 2013));
		antrag.setBis(createDate(20, 3, 2013));
		antrag.setAntragArt(antragArt);
		antrag.setAntragStatus("NEU");
		antrag.setBenutzer(createBenutzer(benutzerId));
		return antrag;
	}

	public BenutzerRolle createBenutzerRolle(String benutzerId, String rolle) {
		BenutzerRolle benutzerRolle = new BenutzerRolle();
		benutzerRolle.setBenutzerId(benutzerId);
		benutzerRolle.setRolle(rolle);
		return benutzerRolle;
	}

	public Bewilligung createBewilligung(long antragId, String benutzerId) {
		Bewilligung bewilligung = new Bewilligung();
		bewilligung.setAntragId(antragId);
		bewilligung.setPosition(1);
		bewilligung.setBenutzerId(benutzerId);
		bewilligung.setBewilligungsStatus("OFFEN");
		bewilligung.setBenutzer(createBenutzer(benutzerId));
		return bewilligung;
	}
}
