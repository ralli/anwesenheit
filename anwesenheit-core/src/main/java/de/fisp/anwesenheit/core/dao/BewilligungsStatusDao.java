package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.BewilligungsStatus;

public interface BewilligungsStatusDao {
	List<BewilligungsStatus> findAll();
}
