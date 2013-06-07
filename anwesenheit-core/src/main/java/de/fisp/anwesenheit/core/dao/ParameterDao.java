package de.fisp.anwesenheit.core.dao;

import java.util.List;

import de.fisp.anwesenheit.core.entities.Parameter;

public interface ParameterDao {
	List<Parameter> findAll();
	Parameter findById(String id);
	void insert(Parameter parameter);
	void update(Parameter parameter);
}
