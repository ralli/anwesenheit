package de.fisp.anwesenheit.core.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.FeiertagDao;
import de.fisp.anwesenheit.core.dao.FeiertagDefinitionDao;
import de.fisp.anwesenheit.core.entities.Feiertag;
import de.fisp.anwesenheit.core.entities.FeiertagDefinition;
import de.fisp.anwesenheit.core.service.FeiertagService;

public class FeiertagServiceTest {
  private FeiertagService feiertagService;
  private FeiertagDefinitionDao feiertagDefinitionDao;
  private FeiertagDao feiertagDao;
  private List<FeiertagDefinition> feiertagDefinitionen;
  private TestDataFactory testDataFactory = new TestDataFactory();

  private void setAttribute(Object obj, String fieldName, Object value) throws Exception {
    Class<?> c = obj.getClass();
    Field field = c.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }

  @Before
  public void setUp() throws Exception {
    feiertagService = new FeiertagServiceImpl();
    feiertagDao = mock(FeiertagDao.class);
    feiertagDefinitionDao = mock(FeiertagDefinitionDao.class);
    setAttribute(feiertagService, "feiertagDao", feiertagDao);
    setAttribute(feiertagService, "feiertagDefinitionDao", feiertagDefinitionDao);
    feiertagDefinitionen = testDataFactory.createDefinitionen();
  }

  @Test
  public void testSetAttribute() {
    assertNotNull(feiertagService);
    when(feiertagDefinitionDao.findAll()).thenReturn(feiertagDefinitionen);
    feiertagService.generateForYear(1972);
    /*
     * Test: findAll wird genau einmal aufgerufen (ist eigentlich unwichtig)
     */
    verify(feiertagDefinitionDao).findAll();
    /*
     * Test: für jede der Definitionen wird ein Feiertagseintrag angelegt
     */
    verify(feiertagDao, times(feiertagDefinitionen.size())).insert(isA(Feiertag.class));
  }
}
