package de.fisp.anwesenheit.core.service;

import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;

public interface AntragService {
	@Transactional
	AntragsDaten findAntragById(long id);

	@Transactional
	AntragListe findByBenutzer(String benutzerId);
}
