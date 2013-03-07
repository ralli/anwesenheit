package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragStatus;

public interface AntragStatusDao {
	List<AntragStatus> findAll();
}
