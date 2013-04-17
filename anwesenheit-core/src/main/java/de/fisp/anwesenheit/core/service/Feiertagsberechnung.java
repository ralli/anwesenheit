package de.fisp.anwesenheit.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.fisp.anwesenheit.core.entities.Feiertag;
import de.fisp.anwesenheit.core.entities.FeiertagDefinition;

public class Feiertagsberechnung {
  private List<FeiertagDefinition> definitionen;

  public Feiertagsberechnung(List<FeiertagDefinition> definitionen) {
    this.definitionen = definitionen;
  }

  private Date getOstersonntag(int year) {
    /* ............................ 1. Ostersonntag */
    Calendar cal = Calendar.getInstance();
    cal.set(year, 2, 22, 0, 0, 0);

    int val1 = (19 * (year % 19) + 24) % 30;
    int val2 = (2 * (year % 4) + 4 * (year % 7) + 6 * val1 + 5) % 7;
    cal.add(Calendar.DATE, val1 + val2);
    return cal.getTime();
  }

  private Date getZweiterSonntagImMai(int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, 4, 8, 0, 0, 0);
    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
      cal.add(Calendar.DATE, 1);
    return cal.getTime();
  }

  private Date getDritterDienstagImNovember(int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, 10, 15, 0, 0, 0);
    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY)
      cal.add(Calendar.DATE, 1);
    return cal.getTime();
  }

  private Date getErsterMontagImJuli(int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, 6, 1, 0, 0, 0);
    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
      cal.add(Calendar.DATE, 1);
    return cal.getTime();
  }

  public List<Feiertag> getFeiertageForYear(int year) {
    List<Date> referenzdatum = new ArrayList<Date>();

    referenzdatum.add(getOstersonntag(year));
    referenzdatum.add(getZweiterSonntagImMai(year));
    referenzdatum.add(getDritterDienstagImNovember(year));
    referenzdatum.add(getErsterMontagImJuli(year));

    List<Feiertag> result = new ArrayList<Feiertag>();
    for (FeiertagDefinition definition : definitionen) {
      Calendar cal = calculateDateFromDefinition(year, definition, referenzdatum);
      Feiertag feiertag = new Feiertag();
      feiertag.setName(definition.getName());
      feiertag.setDatum(cal.getTime());
      feiertag.setDefinitionId(definition.getId());
      feiertag.setDefinition(definition);
      feiertag.setAnteilArbeitszeit(definition.getAnteilArbeitszeit());
      result.add(feiertag);
    }

    return result;
  }

  private Calendar calculateDateFromDefinition(int year, FeiertagDefinition definition, List<Date> referenzdatum) {
    Calendar cal = Calendar.getInstance();
    if (definition.getType() == FeiertagDefinition.FIX) {
      cal.set(year, definition.getReferenzMonat() - 1, definition.getReferenzTag(), 0, 0, 0);
    } else {
      int idx = definition.getType() - 2;
      cal.setTime(referenzdatum.get(idx));
      cal.add(Calendar.DATE, definition.getOffset());
    }
    return cal;
  }
}
