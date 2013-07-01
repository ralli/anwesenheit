package de.fisp.anwesenheit.core.service.impl;

import de.fisp.anwesenheit.core.dao.ParameterDao;
import de.fisp.anwesenheit.core.entities.Parameter;
import de.fisp.anwesenheit.core.service.ParameterService;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ParameterServiceImpl implements ParameterService {
  private final ParameterDao parameterDao;
  private final Ehcache cache;
  private static final Logger logger = LoggerFactory.getLogger(ParameterServiceImpl.class);

  public ParameterServiceImpl(ParameterDao parameterDao, Ehcache cache) {
    this.parameterDao = parameterDao;
    this.cache        = cache;
  }

  @Override
  @Transactional
  public List<Parameter> findAll() {
    return parameterDao.findAll();
  }

  @Override
  @Transactional
  public void setValue(String key, String value) {
    logger.debug("setValue({}, {})", key, value);
    Element element = getParameterElement(key);
    if(element == null) {
      Parameter parameter = new Parameter();
      parameter.setId(key);
      parameter.setBeschreibung(key);
      parameter.setWert(value);
      parameterDao.insert(parameter);
      cache.put(new Element(key, parameter));
    }
    else {
      Parameter parameter = (Parameter) element.getObjectValue();
      parameter.setWert(value);
      parameterDao.update(parameter);
      cache.put(new Element(key, parameter));
    }
  }

  private Element getParameterElement(String key) {
    Element element = cache.get(key);
    if(element == null) {
      Parameter parameter = parameterDao.findById(key);
      if(parameter == null)
        return null;
      element = new Element(key, parameter);
      cache.put(element);
    }
    return element;
  }

  @Override
  @Transactional
  public String getValue(String key) {
    Parameter parameter = getParameter(key);
    String result = parameter == null ? null : parameter.getWert();
    logger.debug("getValue({})={}", key, result);
    return result;
  }

  @Override
  @Transactional
  public Parameter getParameter(String key) {
    Element element = getParameterElement(key);
    if(element == null)
      return null;
    return (Parameter) element.getObjectValue();
  }
}
