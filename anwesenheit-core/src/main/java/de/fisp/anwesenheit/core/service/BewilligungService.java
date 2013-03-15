package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;

public interface BewilligungService {
    /**
     * Löscht eine bestehende Bewilligung
     * 
     * @param bewilligungId
     *            Die Bewilligung, die gelöscht werden soll
     * @return
     */
    public boolean deleteBewilligung(String benutzerId, long bewilligungId);

    public BewilligungsDaten addBewilligung(String benutzerId,
            AddBewilligungCommand command);
}
