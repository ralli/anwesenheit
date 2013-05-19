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
import de.fisp.anwesenheit.core.entities.Benutzer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class BenutzerDaoTest {
  @Autowired
  private BenutzerDao benutzerDao;
  @Autowired
  private TestDataFactory testDataFactory;

  @Test
  public void benutzerDaoShouldBeWired() {
    Assert.assertNotNull(benutzerDao);
  }

  @Test
  public void testInsertUpdateDelete() {
    final String benutzerId = "demotest";
    final String vorname = "Bongo";
    Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
    benutzerDao.insert(benutzer);
    benutzer = benutzerDao.findById(benutzerId);
    Assert.assertNotNull(benutzer);
    benutzer.setVorname(vorname);
    benutzerDao.update(benutzer);
    benutzer = benutzerDao.findById(benutzerId);
    Assert.assertNotNull(benutzer);
    List<Benutzer> list = benutzerDao.search("demotest");
    Assert.assertEquals(1, list.size());
    Assert.assertEquals(vorname, benutzer.getVorname());
    benutzerDao.delete(benutzer);
    benutzer = benutzerDao.findById(benutzerId);
    Assert.assertNull(benutzer);

  }

}
