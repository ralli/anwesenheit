package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.AntragsDaten;

public interface AntragsService {
	AntragsDaten findAntragById(long id);
}
