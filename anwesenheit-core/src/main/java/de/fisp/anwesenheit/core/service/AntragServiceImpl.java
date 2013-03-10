package de.fisp.anwesenheit.core.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;

public class AntragServiceImpl implements AntragsService {
	private static final Logger log = LoggerFactory
			.getLogger(AntragsService.class);
	private AntragDao antragDao;

	private BewilligungDao bewilligungDao;

	@Autowired
	public AntragServiceImpl(AntragDao antragDao, BewilligungDao bewilligungDao) {
		this.antragDao = antragDao;
		this.bewilligungDao = bewilligungDao;
	}

	@Override
	@Transactional(readOnly = true)
	public AntragsDaten findAntragById(long id) {
		Antrag antrag = antragDao.findById(id);
		BenutzerDaten benutzerDaten = createBenutzerDaten(antrag.getBenutzer());
		AntragsDaten result = new AntragsDaten(antrag.getId(),
				antrag.getAntragArt(), antrag.getVon(), antrag.getBis(),
				benutzerDaten, loadBewilligungsDaten(id));
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
				bewilligung.getAntragId(), bewilligung.getBewilligungsStatus(),
				createBenutzerDaten(bewilligung.getBenutzer()));
		return result;
	}

	private List<BewilligungsDaten> loadBewilligungsDaten(long antragId) {
		List<Bewilligung> list = bewilligungDao.findByAntrag(antragId);
		List<BewilligungsDaten> result = new ArrayList<BewilligungsDaten>();
		for(Bewilligung bewilligung : list) {
			result.add(createBewilligungsDaten(bewilligung));
		}
		return result;
	}
}
