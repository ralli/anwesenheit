package de.fisp.anwesenheit.core.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.TestConfig;
import de.fisp.anwesenheit.core.dao.FeiertagDefinitionDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
public class FeiertagDefinitionDaoTest {
  @Autowired
  private FeiertagDefinitionDao urlaubDefinitionDao;

  @Test
  public void testListAll() {
    urlaubDefinitionDao.findAll();
  }
}
