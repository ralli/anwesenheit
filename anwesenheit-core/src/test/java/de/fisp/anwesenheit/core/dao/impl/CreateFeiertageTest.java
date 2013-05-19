package de.fisp.anwesenheit.core.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.fisp.anwesenheit.core.TestConfig;
import de.fisp.anwesenheit.core.service.FeiertagService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CreateFeiertageTest {
  @Autowired
  private FeiertagService feiertagService;

  @Test
  public void insertFeiertage() {
    for (int year = 2013; year <= 2023; ++year) {
      feiertagService.generateForYear(year);
    }
  }
}
