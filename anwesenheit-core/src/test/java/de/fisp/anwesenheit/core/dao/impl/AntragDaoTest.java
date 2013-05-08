package de.fisp.anwesenheit.core.dao.impl;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.TestConfig;
import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.AntragDao;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.domain.AntragsFilter;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
public class AntragDaoTest {
  @Autowired
  private BenutzerDao benutzerDao;
  @Autowired
  private AntragDao antragDao;
  @Autowired
  private TestDataFactory testDataFactory;

  private Benutzer insertBenutzer(String benutzerId) {
    Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    benutzerDao.insert(benutzer);
    return benutzer;
  }

  @Test
  public void testInsertUpdateDelete() {
    final String benutzerId = "demotest";
    final String antragArt = "URLAUB";
    insertBenutzer(benutzerId);
    Antrag antrag = testDataFactory.createAntrag(antragArt, benutzerId);
    antragDao.insert(antrag);
    long id = antrag.getId();
    assertNotSame(0L, id);
    antrag = antragDao.findById(id);
    assertNotNull(antrag);
    antrag.setBis(testDataFactory.createDate(13, 11, 2013));
    antragDao.update(antrag);
    antrag = antragDao.findById(id);
    assertNotNull(antrag);
    antragDao.delete(antrag);
    antrag = antragDao.findById(id);
    assertNull(antrag);
  }

  @Test
  public void testFind() {
    antragDao.findByBenutzerIdAndBewilliger("juhnke_r", "chef");
  }

  @Test
  public void testFindByBenutzerAndFilter() {
    AntragsFilter filter = new AntragsFilter();
    final String benutzerId = "test";
    filter.setVon(testDataFactory.createDate(13, 11, 1969));
    filter.setBis(testDataFactory.createDate(13, 11, 1969));
    filter.setAntragsStatusFilter("OFFEN");
    antragDao.findByBenutzerAndFilter(benutzerId, filter);
  }

  @Test
  public void testFindByBewilligerAndFilter() {
    AntragsFilter filter = new AntragsFilter();
    final String benutzerId = "test";
    filter.setVon(testDataFactory.createDate(13, 11, 1969));
    filter.setBis(testDataFactory.createDate(13, 11, 1969));
    filter.setAntragsStatusFilter("OFFEN");
    antragDao.findByBewilligerAndFilter(benutzerId, filter);
  }
  
  @Test
  public void testFindByFilter() {
    AntragsFilter filter = new AntragsFilter();
    filter.setVon(testDataFactory.createDate(13, 11, 1969));
    filter.setBis(testDataFactory.createDate(13, 11, 1969));
    filter.setAntragsStatusFilter("OFFEN");
    antragDao.findByFilter(filter);
  }
}
