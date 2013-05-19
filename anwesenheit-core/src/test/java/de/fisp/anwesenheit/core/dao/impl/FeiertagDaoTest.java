package de.fisp.anwesenheit.core.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.TestConfig;
import de.fisp.anwesenheit.core.dao.FeiertagDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class FeiertagDaoTest {
  @Autowired
  private FeiertagDao feiertagDao;

  @Test
  public void testListAll() {
    feiertagDao.findAll();
  }

  @Test
  public void testFindByYear() {
    feiertagDao.findByYear(2013);
  }

  @Test
  public void testDeleteByYear() {
    feiertagDao.deleteByYear(1902);
  }

}
