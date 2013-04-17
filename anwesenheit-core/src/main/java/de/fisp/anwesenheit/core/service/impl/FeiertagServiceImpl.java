package de.fisp.anwesenheit.core.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  private String dateStr(Date d) {
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    return fmt.format(d);
  }
  
  @Override
  public double berechneAnzahlArbeitstage(Date von, Date bis) {
    List<Feiertag> feiertage = feiertagDao.findByZeitraum(von, bis);
    Set<String> dateStrings = new HashSet<String>();    
    for(Feiertag f : feiertage) {
      dateStrings.add(dateStr(f.getDatum()));
    }
    
    Calendar d = Calendar.getInstance();
    d.setTime(von);
    Calendar tbis = Calendar.getInstance();
    tbis.setTime(bis);
    int anzahlTage = 0;
    while(d.compareTo(tbis) <= 0) {
      int dow = d.get(Calendar.DAY_OF_WEEK);
      boolean arbeitsTagFlag; 
      if(dow == Calendar.SUNDAY || dow == Calendar.SATURDAY)
        arbeitsTagFlag = false;
      else if(dateStrings.contains(dateStr(d.getTime())))
        arbeitsTagFlag = false;
      else
        arbeitsTagFlag = true;
      if(arbeitsTagFlag)
        ++anzahlTage;
      d.add(Calendar.DAY_OF_MONTH, 1);
    }
    return anzahlTage;
  }
}
