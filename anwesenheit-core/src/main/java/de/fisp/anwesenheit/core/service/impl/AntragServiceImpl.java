package de.fisp.anwesenheit.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.AntragHistorieDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragListeEintrag;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.AntragHistorie;
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

	private AntragHistorieDao antragHistorieDao;
	
	@Autowired
	public AntragServiceImpl(AntragDao antragDao,
			BewilligungDao bewilligungDao, BenutzerDao benutzerDao,
			AntragHistorieDao antragHistorieDao) {
		this.antragDao = antragDao;
		this.bewilligungDao = bewilligungDao;
		this.benutzerDao = benutzerDao;
		this.antragHistorieDao = antragHistorieDao;
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

	@Override
	@Transactional
	public long createAntrag(CreateAntragCommand command) {
		log.debug("createAntrag({})", command);

		Antrag antrag = new Antrag();

		antrag.setBenutzerId(command.getBenutzerId());
		antrag.setAntragArtId(command.getAntragArt());
		antrag.setAntragStatusId("NEU");
		antrag.setVon(command.getVon());
		antrag.setBis(command.getBis());

		antragDao.insert(antrag);
		int position = 1;
		for (String bewilligerId : command.getBewilliger()) {
			Bewilligung bewilligung = new Bewilligung();

			bewilligung.setAntragId(antrag.getId());
			bewilligung.setBenutzerId(bewilligerId);
			bewilligung.setBewilligungsStatusId("OFFEN");
			bewilligung.setPosition(position);
			bewilligungDao.insert(bewilligung);

			++position;
		}

		insertAntragHistorie(antrag);
		
		log.debug("createAntrag: id = {}", antrag.getId());
		
		return antrag.getId();
	}

	private void insertAntragHistorie(Antrag antrag) {
		AntragHistorie antragHistorie = new AntragHistorie();
		antragHistorie.setAntragId(antrag.getId());
		antragHistorie.setBenutzerId(antrag.getBenutzerId());
		antragHistorie.setZeitpunkt(new Date());
		antragHistorie.setBeschreibung("Antrag angelegt");
		antragHistorieDao.insert(antragHistorie);
	}
}
