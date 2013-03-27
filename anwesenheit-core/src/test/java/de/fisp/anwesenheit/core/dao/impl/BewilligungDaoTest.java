package de.fisp.anwesenheit.core.dao.impl;

import java.util.Date;
import java.util.List;

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
import de.fisp.anwesenheit.core.dao.BewilligungDao;
import de.fisp.anwesenheit.core.domain.BewilligungsFilter;
import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.Bewilligung;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
public class BewilligungDaoTest {
	@Autowired
	private TestDataFactory testDataFactory;
	@Autowired
	private BenutzerDao benutzerDao;
	@Autowired
	private AntragDao antragDao;
	@Autowired
	private BewilligungDao bewilligungDao;

	private Benutzer insertBenutzer(String benutzerId) {
		Benutzer benutzer = testDataFactory.createBenutzer(benutzerId);
		benutzerDao.insert(benutzer);
		return benutzer;
	}

	private Antrag insertAntrag(String antragsArt, String benutzerId) {
		Antrag antrag = testDataFactory.createAntrag(antragsArt, benutzerId);
		antragDao.insert(antrag);
		return antrag;
	}

	@Test
	public void testInsertUpdateDelete() {
		final String benutzerId = "testmitarbeiter";
		final String chefId = "testchef";
		final String antragsArt = "URLAUB";
		final String bewilligungsStatus = "BEWILLIGT";
		
		insertBenutzer(benutzerId);
		insertBenutzer(chefId);
		Antrag antrag = insertAntrag(antragsArt, benutzerId);
		Bewilligung bewilligung = testDataFactory.createBewilligung(
				antrag.getId(), chefId);
		bewilligungDao.insert(bewilligung);
		
		long id = bewilligung.getId();
		Assert.assertTrue("Muss ein generierter Wert sein", id != 0L);
		
		bewilligung = bewilligungDao.findById(id);
		Assert.assertNotNull(bewilligung);
		
		List<Bewilligung> list = bewilligungDao.findByAntrag(antrag.getId());
		Assert.assertEquals(1, list.size());
		
		bewilligung.setBewilligungsStatusId(bewilligungsStatus);
		bewilligungDao.update(bewilligung);
		
		bewilligung = bewilligungDao.findById(id);
		Assert.assertNotNull(bewilligung);
		Assert.assertEquals(bewilligungsStatus, bewilligung.getBewilligungsStatusId());
		
		int maxPosition = bewilligungDao.getMaxPosition(antrag.getId());
		Assert.assertEquals(1, maxPosition);
		
		list = bewilligungDao.findByBewilliger(chefId);
		Assert.assertEquals(1, list.size());
		
		bewilligungDao.delete(bewilligung);
		bewilligung = bewilligungDao.findById(id);
		Assert.assertNull(bewilligung);
	}
	
	@Test
	public void findByBewilligerAndFilterShouldSucceed() {
	  final String bewilligerId = "bewilliger";
	  final String benutzerId = "benutzer";
	  BewilligungsFilter filter = new BewilligungsFilter();
	  filter.setBenutzerId(benutzerId);
	  filter.setVon(new Date());
	  filter.setBis(new Date());
	  bewilligungDao.findByBewilligerAndFilter(bewilligerId, filter);
	}
}
