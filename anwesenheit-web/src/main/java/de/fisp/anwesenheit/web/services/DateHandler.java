package de.fisp.anwesenheit.web.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DateHandler {

  private static final Logger log = LoggerFactory.getLogger(DateHandler.class);

  /**
   * Converts a String in ISO-format to Date
   *
   * @param dateString the String to be converted
   * @return The converted date or <code>null</code> if a <code>null</code>
   *         String is given or the input string is invalid
   */
  public Date stringToDate(String dateString) {
    log.debug("Parsing date [{}]...", dateString);
    if (dateString == null)
      return null;
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    fmt.setLenient(true);
    try {
      return fmt.parse(dateString);
    } catch (ParseException ex) {
      throw new RuntimeException("stringToDate", ex);
    }
  }

  /**
   * Converts a date to a String in ISO-Format
   *
   * @param date the date to be formatted (may be <code>null</code>)
   * @return The formatted String or <code>null</code> if no date given.
   */
  public String dateToString(Date date) {
    if (date == null)
      return null;
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = fmt.format(date);
    log.debug("converting date [{}]...", dateString);
    return dateString;
  }
}
