package de.fisp.anwesenheit.core.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.TestConfig;
import de.fisp.anwesenheit.core.dao.AntragStatusDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class AntragStatusDaoTest {
  @Autowired
  private AntragStatusDao antragStatusDao;

  @Test
  public void testFindAll() {
    antragStatusDao.findAll();
  }

  @Test
  public void testFindById() {
    antragStatusDao.findById("NEU");
  }
}
