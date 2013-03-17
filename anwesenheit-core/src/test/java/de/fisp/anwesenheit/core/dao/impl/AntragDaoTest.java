package de.fisp.anwesenheit.core.dao.impl;

import junit.framework.Assert;

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
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;

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
    Assert.assertNotSame(0L, id);
    antrag = antragDao.findById(id);
    Assert.assertNotNull(antrag);
    antrag.setBis(testDataFactory.createDate(13, 11, 2013));
    antragDao.update(antrag);
    antrag = antragDao.findById(id);
    Assert.assertNotNull(antrag);
    antragDao.delete(antrag);
    antrag = antragDao.findById(id);
    Assert.assertNull(antrag);
  }

  @Test
  public void testFind() {
    antragDao.findByBenutzerIdAndBewilliger("juhnke_r", "chef");
  }
}
