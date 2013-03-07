package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.AntragArt;

public interface AntragArtDao {
	List<AntragArt> findAll();
}
