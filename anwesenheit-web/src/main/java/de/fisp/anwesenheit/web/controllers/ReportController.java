package de.fisp.anwesenheit.web.controllers;

import de.fisp.anwesenheit.core.service.BerechtigungsService;
import de.fisp.anwesenheit.core.service.ChecksumService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Controller
public class ReportController {
  private static final Logger log = LoggerFactory.getLogger(ReportController.class);

  @Autowired
  private ChecksumService checksumService;

  @Autowired
  private BerechtigungsService berechtigungsService;

  private String getCurrentUser() {
    String result = (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId",
            RequestAttributes.SCOPE_SESSION);
    if (result == null) {
      throw new NotAuthorizedException("Sie sind nicht angemeldet");
    }

    return result;
  }

  private String getRedirectUrl() {
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      String url = request.getRequestURL().toString();
      url = url.replaceAll("anwesenheit-web.*$", "anwesenheit-birt/Anwesenheit");
      return url;
    }
    catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /*
   * localhost:8080/anwesenheit-birt/Anwesenheit?RP_DATE_monatsanfang=20130501&auth=e3f338e45394f99aa0d92e7e8c35e2e9e1da383
   */
  @RequestMapping(value = "/report", method = RequestMethod.GET)
  @Transactional
  public String showReport(@RequestParam(value = "datum", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datum) {

    String benutzerId = getCurrentUser();
    if(!berechtigungsService.hatSonderBerechtigungen(benutzerId)) {
      throw new NotAuthorizedException("Sie haben keine ausreichenden Berechtigungen für diesen Aufruf");
    }

    log.debug("showReport: {}", datum);
    String monatsAnfang = formattedDate(datum);
    return "redirect:" + getRedirectUrl() + "?RP_DATE_monatsanfang=" + monatsAnfang + "&auth=" + checksumService.generateChecksum(monatsAnfang);
  }

  private String formattedDate(Date datum) {
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(datum);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    return fmt.format(cal.getTime());
  }
}
