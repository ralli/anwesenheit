package de.fisp.anwesenheit.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.service.BenutzerService;

@Service
public class BenutzerServiceImpl implements BenutzerService {
	private BenutzerDao benutzerDao;
	private static final Logger logger = LoggerFactory
			.getLogger(BenutzerServiceImpl.class);

	@Autowired
	public BenutzerServiceImpl(BenutzerDao benutzerDao) {
		this.benutzerDao = benutzerDao;
	}

	@Override
	@Transactional
	public BenutzerDaten findByBenutzerId(String benutzerId) {
		Benutzer benutzer = benutzerDao.findById(benutzerId);
		BenutzerDaten daten = createBenutzerDaten(benutzer);
		logger.debug("findByBenutzerId({}) = {}", benutzerId, daten);
		return daten;
	}

	@Override
	@Transactional
	public List<BenutzerDaten> search(String searchTerm) {
		List<Benutzer> list = benutzerDao.search(searchTerm);
		List<BenutzerDaten> result = new ArrayList<BenutzerDaten>();
		for (Benutzer b : list) {
			result.add(createBenutzerDaten(b));
		}
		return result;
	}

	private BenutzerDaten createBenutzerDaten(Benutzer benutzer) {
		if (benutzer == null)
			return null;
		BenutzerDaten benutzerDaten = new BenutzerDaten(
				benutzer.getBenutzerId(), benutzer.getVorname(),
				benutzer.getNachname(), benutzer.getEmail());
		return benutzerDaten;
	}
}
