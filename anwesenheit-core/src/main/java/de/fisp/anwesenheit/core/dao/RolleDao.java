package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Rolle;

public interface RolleDao {
	List<Rolle> findAll();
}
