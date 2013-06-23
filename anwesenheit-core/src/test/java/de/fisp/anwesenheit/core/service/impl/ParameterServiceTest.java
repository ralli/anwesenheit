package de.fisp.anwesenheit.core.service.impl;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.ParameterDao;
import de.fisp.anwesenheit.core.entities.Parameter;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterServiceTest {
  private ParameterDao parameterDao;
  private Ehcache cache;
  private ParameterServiceImpl parameterService;
  private final TestDataFactory testDataFactory = new TestDataFactory();

  @Before
  public void setUp() {
    parameterDao = mock(ParameterDao.class);
    cache = mock(Ehcache.class);
    parameterService = new ParameterServiceImpl(parameterDao, cache);
  }

  @Test
  public void testFindAll() {
    when(parameterDao.findAll()).thenReturn(testDataFactory.createParameters());
    parameterService.findAll();
  }

  @Test
  public void testSetValueNotPresentInCache() {
    final String newValue = "new value";
    Parameter parameter = testDataFactory.createParameter();
    when(parameterDao.findById(parameter.getId())).thenReturn(parameter);
    when(cache.get(parameter.getId())).thenReturn(null);
    parameterService.setValue(parameter.getId(), newValue);
    Assert.assertEquals(newValue, parameter.getWert());
  }

  @Test
  public void testSetValuePresentInCache() {
    final String newValue = "new value";
    Parameter parameter = testDataFactory.createParameter();
    Element element = createElement(parameter);
    when(cache.get(parameter.getId())).thenReturn(element);
    parameterService.setValue(parameter.getId(), newValue);
    Assert.assertEquals(newValue, parameter.getWert());
  }

  @Test
  public void testSetValueNotPresentInDB() {
    final String newValue = "new value";
    Parameter parameter = testDataFactory.createParameter();
    when(cache.get(parameter.getId())).thenReturn(null);
    when(parameterDao.findById(parameter.getId())).thenReturn(null);
    parameterService.setValue(parameter.getId(), newValue);
  }

  @Test
  public void testGetParameterNotPresentInCache() {
    Parameter parameter = testDataFactory.createParameter();
    when(cache.get(parameter.getId())).thenReturn(null);
    when(parameterDao.findById(parameter.getId())).thenReturn(parameter);
    Assert.assertEquals(parameter.getId(), parameterService.getParameter(parameter.getId()).getId());
  }

  @Test
  public void testGetParameterNotPresentInDB() {
    Parameter parameter = testDataFactory.createParameter();
    when(cache.get(parameter.getId())).thenReturn(null);
    when(parameterDao.findById(parameter.getId())).thenReturn(null);
    Assert.assertNull(parameterService.getParameter(parameter.getId()));
  }

  private Element createElement(Parameter parameter) {
    return new Element(parameter.getId(), parameter);
  }

}
