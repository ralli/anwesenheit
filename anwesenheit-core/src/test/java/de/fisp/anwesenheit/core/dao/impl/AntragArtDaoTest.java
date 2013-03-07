package de.fisp.anwesenheit.core.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.TestConfig;
import de.fisp.anwesenheit.core.dao.AntragArtDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
public class AntragArtDaoTest {
	@Autowired
	private AntragArtDao antragArtDao;

	@Test
	public void testFindAll() {
		antragArtDao.findAll();
	}
}
