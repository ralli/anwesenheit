package de.fisp.anwesenheit.core.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.domain.LabelValue;
import de.fisp.anwesenheit.core.entities.Benutzer;

public class BenutzerServiceTest {
  private BenutzerDao benutzerDao;
  private BenutzerServiceImpl benutzerService;
  private TestDataFactory testDataFactory;

  @Before
  public void setUp() {
    benutzerDao = mock(BenutzerDao.class);
    benutzerService = new BenutzerServiceImpl(benutzerDao);
    testDataFactory = new TestDataFactory();
  }

  @Test
  public void testFindByBenutzerIdReturnsNullifNotFound() {
    final String benutzerId = "xxx";
    when(benutzerDao.findById(benutzerId)).thenReturn(null);
    Assert.assertNull(benutzerService.findByBenutzerId(benutzerId));
  }

  @Test
  public void testFindByBenutzerIdReturnsBenutzerDatenWhenFound() {
    final String benutzerId = "yyy";
    final Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    when(benutzerDao.findById(benutzerId)).thenReturn(benutzer);
    Assert.assertEquals(benutzerId, benutzerService.findByBenutzerId(benutzerId).getBenutzerId());
  }

  @Test
  public void testSearchReturnsEmptyListIfNothingFound() {
    final String searchTerm = "whatever";
    when(benutzerDao.search(searchTerm)).thenReturn(new ArrayList<Benutzer>());
    List<LabelValue> result = benutzerService.search(searchTerm);
    Assert.assertTrue("Liste soll leer sein", result.isEmpty());    
  }
}
