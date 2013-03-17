package de.fisp.anwesenheit.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.LabelValue;
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
	public List<LabelValue> search(String searchTerm) {
		List<Benutzer> list = benutzerDao.search(searchTerm);
		List<LabelValue> result = new ArrayList<LabelValue>();
		for (Benutzer b : list) {
			result.add(createLabelValueFromBenutzer(b));
		}
		return result;
	}

	private LabelValue createLabelValueFromBenutzer(Benutzer benutzer) {
		List<String> nameParts = new ArrayList<String>();

		if (StringUtils.isNotBlank(benutzer.getVorname()))
			nameParts.add(benutzer.getVorname());

		if (StringUtils.isNotBlank(benutzer.getNachname()))
			nameParts.add(benutzer.getNachname());

		if (nameParts.isEmpty())
			nameParts.add(benutzer.getBenutzerId());

		String fullName = StringUtils.join(nameParts, ' ');

		return new LabelValue(fullName, benutzer.getBenutzerId());
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
