package de.fisp.anwesenheit.core.service.impl;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.TestDataFactory;
import de.fisp.anwesenheit.core.entities.Feiertag;
import de.fisp.anwesenheit.core.service.Feiertagsberechnung;

public class FeiertagberechnungTest {
  private Logger log = LoggerFactory.getLogger(FeiertagberechnungTest.class);
  TestDataFactory testDataFactory = new TestDataFactory();

  @Test
  public void testBerechnung() {
    Feiertagsberechnung berechnung = new Feiertagsberechnung(testDataFactory.createDefinitionen());
    List<Feiertag> list = berechnung.getFeiertageForYear(2013);
    for (Feiertag f : list) {
      log.info(f.toString());
    }
  }
}
