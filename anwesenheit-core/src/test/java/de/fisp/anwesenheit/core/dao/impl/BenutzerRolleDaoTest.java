package de.fisp.anwesenheit.core.dao.impl;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.TestConfig;
import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.dao.BenutzerRolleDao;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.BenutzerRolle;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class BenutzerRolleDaoTest {
  @Autowired
  private BenutzerDao benutzerDao;
  @Autowired
  private BenutzerRolleDao benutzerRolleDao;
  @Autowired
  private TestDataFactory testDataFactory;

  private void insertBenutzer(String benutzerId) {
    // final String benutzerId = "demo";
    Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    benutzerDao.insert(benutzer);
  }

  @Test
  public void testInsertUpdateDelete() {
    final String benutzerId = "demotest";
    final String rolle = "ADMIN";
    insertBenutzer(benutzerId);
    BenutzerRolle benutzerRolle = testDataFactory.createBenutzerRolle(
            benutzerId, rolle);
    benutzerRolleDao.insert(benutzerRolle);
    benutzerRolle = benutzerRolleDao.findByBenutzerAndRolle(benutzerId,
            rolle);
    Assert.assertNotNull(benutzerRolle);
    List<BenutzerRolle> list = benutzerRolleDao.findByBenutzer(benutzerId);
    Assert.assertEquals(1, list.size());
    benutzerRolleDao.delete(benutzerRolle);
    benutzerRolle = benutzerRolleDao.findByBenutzerAndRolle(benutzerId,
            rolle);
    Assert.assertNull(benutzerRolle);
  }
}
