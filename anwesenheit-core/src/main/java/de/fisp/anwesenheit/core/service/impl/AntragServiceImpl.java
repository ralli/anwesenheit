package de.fisp.anwesenheit.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragListeEintrag;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.service.AntragService;

@Service
public class AntragServiceImpl implements AntragService {
	private static final Logger log = LoggerFactory
			.getLogger(AntragService.class);
	private AntragDao antragDao;

	private BewilligungDao bewilligungDao;

	private BenutzerDao benutzerDao;

	@Autowired
	public AntragServiceImpl(AntragDao antragDao,
			BewilligungDao bewilligungDao, BenutzerDao benutzerDao) {
		this.antragDao = antragDao;
		this.bewilligungDao = bewilligungDao;
		this.benutzerDao = benutzerDao;
	}

	@Override
	@Transactional
	public AntragsDaten findAntragById(long id) {
		Antrag antrag = antragDao.findById(id);
		if (antrag == null) {
			log.warn("Antrag mit id = {} nicht gefunden", id);
			return null;
		}
		BenutzerDaten benutzerDaten = createBenutzerDaten(antrag.getBenutzer());
		AntragsDaten result = new AntragsDaten(antrag.getId(),
				antrag.getAntragArt(), antrag.getAntragStatus(),
				antrag.getVon(), antrag.getBis(), benutzerDaten,
				loadBewilligungsDaten(id));
		log.debug("findAntragById({}) = {}", id, result);
		return result;
	}

	private BenutzerDaten createBenutzerDaten(Benutzer benutzer) {
		BenutzerDaten benutzerDaten = new BenutzerDaten(
				benutzer.getBenutzerId(), benutzer.getVorname(),
				benutzer.getNachname(), benutzer.getEmail());
		return benutzerDaten;
	}

	private BewilligungsDaten createBewilligungsDaten(Bewilligung bewilligung) {
		BewilligungsDaten result = new BewilligungsDaten(bewilligung.getId(),
				bewilligung.getAntragId(), bewilligung.getPosition(),
				bewilligung.getBewilligungsStatus(),
				createBenutzerDaten(bewilligung.getBenutzer()));
		return result;
	}

	private List<BewilligungsDaten> loadBewilligungsDaten(long antragId) {
		List<Bewilligung> list = bewilligungDao.findByAntrag(antragId);
		List<BewilligungsDaten> result = new ArrayList<BewilligungsDaten>();
		for (Bewilligung bewilligung : list) {
			result.add(createBewilligungsDaten(bewilligung));
		}
		return result;
	}

	@Override
	@Transactional
	public AntragListe findByBenutzer(String benutzerId) {
		Benutzer benutzer = benutzerDao.findById(benutzerId);
		if (benutzer == null) {
			log.warn("Benutzer {} nicht gefunden", benutzerId);
			return null;
		}
		List<Antrag> antraege = antragDao.findByBenutzerId(benutzerId);
		AntragListe liste = createAntragListe(benutzer, antraege);
		log.debug("findByBenutzer({}) = {}", benutzerId, liste);
		return liste;
	}

	private AntragListe createAntragListe(Benutzer benutzer,
			List<Antrag> antraege) {
		List<AntragListeEintrag> eintraege = new ArrayList<AntragListeEintrag>();
		for (Antrag antrag : antraege) {
			AntragListeEintrag eintrag = createAntragListeEintrag(antrag);
			eintraege.add(eintrag);
		}
		BenutzerDaten benutzerDaten = createBenutzerDaten(benutzer);
		AntragListe liste = new AntragListe(benutzerDaten, eintraege);

		return liste;
	}

	private AntragListeEintrag createAntragListeEintrag(Antrag antrag) {
		AntragListeEintrag eintrag = new AntragListeEintrag(antrag.getId(),
				antrag.getAntragArt(), antrag.getAntragStatus(),
				antrag.getVon(), antrag.getBis());
		return eintrag;
	}
}
