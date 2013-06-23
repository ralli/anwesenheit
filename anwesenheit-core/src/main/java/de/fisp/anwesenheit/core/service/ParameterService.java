package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.entities.Parameter;

import java.util.List;

public interface ParameterService {
  List<Parameter> findAll();
  void setValue(String key, String value);
  String getValue(String key);
  Parameter getParameter(String key);
}
