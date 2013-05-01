package de.fisp.anwesenheit.core.service.impl;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.service.AntragService;
import de.fisp.anwesenheit.core.service.MailBenachrichtigungsService;
import de.fisp.anwesenheit.core.service.MailService;

@Service
public class MailBenachrichtigungsServiceImpl implements MailBenachrichtigungsService {
  private AntragService antragService;
  private MailService mailService;
  private VelocityEngine velocityEngine;
  private ToolManager toolManager;
  private static final Logger log = LoggerFactory.getLogger(MailBenachrichtigungsServiceImpl.class);

  @Autowired
  public MailBenachrichtigungsServiceImpl(AntragService antragService, MailService mailService, VelocityEngine velocityEngine, ToolManager toolManager) {
    this.antragService = antragService;
    this.mailService = mailService;
    this.velocityEngine = velocityEngine;
    this.toolManager = toolManager;
  }

  @Override
  public void sendeAntragsMail(String benutzerId, long antragId) {
    AntragsDaten antrag = antragService.findAntragById(benutzerId, antragId);
    for (BewilligungsDaten b : antrag.getBewilligungen()) {
      if(b.getPosition() == 2) {
        BenutzerDaten bewilliger = b.getBenutzer();
        String email = bewilliger.getEmail();
        String betreff = getBetreff(antrag, b);
        String text = getAntragsText(antrag, b);
        mailService.sendeMail(betreff, text, "noreply@f-i-solutions-plus.de", email);
      }
    }
  }

  public String getBetreff(AntragsDaten antrag, BewilligungsDaten bewilligungsDaten) {
    BenutzerDaten antragSteller = antrag.getBenutzer();

    String betreff = String.format("Urlaubsantrag für %s vom %s bis %s (%s Tage)", getBenutzerName(antragSteller),
        formatDate(antrag.getVon()), formatDate(antrag.getBis()), formatTage(antrag.getAnzahlTage()));
    return betreff;
  }

  public String getAntragsText(AntragsDaten antrag, BewilligungsDaten bewilligungsDaten) {  
    Context context = toolManager.createContext();
    context.put("antrag", antrag);
    context.put("bewilligungsUrl", getUrlForBewilligung(bewilligungsDaten.getId()));
    StringWriter writer = new StringWriter();
    velocityEngine.mergeTemplate("de/fisp/anwesenheit/mailtemplates/neuerantrag.vm", "UTF-8", context, writer);
    String text = writer.toString();
    log.debug(text);
    return text;
  }

  private String formatTage(double tage) {
    NumberFormat fmt = new DecimalFormat("0.#");
    return fmt.format(tage);
  }

  private String getUrlForBewilligung(long bewilligungsId) {
    return "http://srv-1822direkt2:8080/anwesenheit-web/?deepLinkUrl=bewilligungen/" + bewilligungsId;
  }

  private String formatDate(Date date) {
    DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    return format.format(date);
  }

  private String getBenutzerName(BenutzerDaten benutzer) {
    List<String> namen = new ArrayList<String>();
    if (StringUtils.isNotBlank(benutzer.getVorname()))
      namen.add(benutzer.getVorname());
    if (StringUtils.isNotBlank(benutzer.getNachname()))
      namen.add(benutzer.getNachname());
    if (namen.isEmpty())
      namen.add(benutzer.getBenutzerId());
    return StringUtils.join(namen, ' ');
  }
}
