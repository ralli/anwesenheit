package de.fisp.anwesenheit.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.FeiertagDao;
import de.fisp.anwesenheit.core.dao.FeiertagDefinitionDao;
import de.fisp.anwesenheit.core.entities.Feiertag;
import de.fisp.anwesenheit.core.entities.FeiertagDefinition;
import de.fisp.anwesenheit.core.service.FeiertagService;
import de.fisp.anwesenheit.core.service.Feiertagsberechnung;

@Service
public class FeiertagServiceImpl implements FeiertagService {
  @Autowired
  private FeiertagDao feiertagDao;
  @Autowired
  private FeiertagDefinitionDao feiertagDefinitionDao;
  private static final Logger log = LoggerFactory.getLogger(FeiertagServiceImpl.class);

  @Transactional
  public void generateForYear(int year) {
    log.info("generateForYear({})", year);
    feiertagDao.deleteByYear(year);
    List<FeiertagDefinition> definitionen = feiertagDefinitionDao.findAll();
    Feiertagsberechnung berechnung = new Feiertagsberechnung(definitionen);
    List<Feiertag> feiertage = berechnung.getFeiertageForYear(year);
    for (Feiertag f : feiertage) {
      feiertagDao.insert(f);
    }
    log.info("generateForYear({}): feiertage = {}", year, feiertage);
  }

}
