package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;

public interface AntragService {

	AntragsDaten findAntragById(long id);

	AntragListe findByBenutzer(String benutzerId);

	long createAntrag(CreateAntragCommand command);
}
