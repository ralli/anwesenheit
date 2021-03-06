package de.fisp.anwesenheit.core.service.impl;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fisp.anwesenheit.core.service.*;
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

@Service
public class MailBenachrichtigungsServiceImpl implements MailBenachrichtigungsService {
  private static final Logger log = LoggerFactory.getLogger(MailBenachrichtigungsServiceImpl.class);

  public void setAntragService(AntragService antragService) {
    this.antragService = antragService;
  }

  @Autowired
  private AntragService antragService;
  private final MailService mailService;
  private final VelocityEngine velocityEngine;
  private final ToolManager toolManager;
  private final MarkDownFormatter markDownFormatter;
  private final ParameterService parameterService;

  @Autowired
  public MailBenachrichtigungsServiceImpl(MailService mailService,
                                          VelocityEngine velocityEngine,
                                          ToolManager toolManager,
                                          MarkDownFormatter markDownFormatter,
                                          ParameterService parameterService) {
    this.mailService = mailService;
    this.velocityEngine = velocityEngine;
    this.toolManager = toolManager;
    this.markDownFormatter = markDownFormatter;
    this.parameterService = parameterService;
  }

  @Override
  public void sendeErsteBewilligungsMail(String benutzerId, long antragId) {
    AntragsDaten antrag = findAntragById(benutzerId, antragId);
    for (BewilligungsDaten bewilligungsDaten : antrag.getBewilligungen()) {
      if (istBewilligerFuerErsteUnterschrift(bewilligungsDaten)) {
        BenutzerDaten bewilliger = bewilligungsDaten.getBenutzer();
        String email = bewilliger.getEmail();
        String betreff = getBetreff(antrag);
        String text = getAntragsTextForBewilligung(antrag, bewilligungsDaten, "neuerantrag.vm");
        mailService.sendeMail(betreff, text, getReplyToAddress(), email);
      }
    }
  }

  private AntragsDaten findAntragById(String benutzerId, long antragId) {
    AntragsDaten daten = antragService.findAntragById(benutzerId, antragId);
    daten.setKommentar(formatMarkDown(daten.getKommentar()));
    return daten;
  }

  @Override
  public void sendeZweiteBewilligungsMail(String benutzerId, long antragId) {
    AntragsDaten antrag = findAntragById(benutzerId, antragId);
    for (BewilligungsDaten bewilligungsDaten : antrag.getBewilligungen()) {
      if (istBewilligerFuerZweiteUnterschrift(bewilligungsDaten)) {
        BenutzerDaten bewilliger = bewilligungsDaten.getBenutzer();
        String email = bewilliger.getEmail();
        String betreff = getBetreff(antrag);
        String text = getAntragsTextForBewilligung(antrag, bewilligungsDaten, "neuerantrag.vm");
        mailService.sendeMail(betreff, text, getReplyToAddress(), email);
      }
    }
  }

  @Override
  public void sendeAbgelehntMail(String benutzerId, long antragId, long bewilligungId) {
    AntragsDaten antrag = findAntragById(benutzerId, antragId);
    for (BewilligungsDaten bewilligungsDaten : antrag.getBewilligungen()) {
      if (bewilligungsDaten.getId() == bewilligungId) {
        String text = getAntragsTextForBewilligung(antrag, bewilligungsDaten, "antragabgelehnt.vm");
        String email = antrag.getBenutzer().getEmail();
        String betreff = "ABGELEHNT: " + getBetreff(antrag);
        mailService.sendeMail(betreff, text, getReplyToAddress(), email);
      }
    }
  }

  @Override
  public void sendeBewilligtMail(String benutzerId, long antragId, long bewilligungId) {
    AntragsDaten antrag = findAntragById(benutzerId, antragId);
    String betreff = "BEWILLIGT: " + getBetreff(antrag);
    String text = getAntragsText(antrag, "antragbewilligt.vm");
    mailService.sendeMail(betreff, text, getReplyToAddress(), antrag.getBenutzer().getEmail());
  }

  @Override
  public void sendeStornoMail(String benutzerId, long antragId) {
    AntragsDaten antrag = findAntragById(benutzerId, antragId);
    String betreff = "STORNIERT: " + getBetreff(antrag);
    String text = getAntragsText(antrag, "antragstorniert.vm");
    mailService.sendeMail(betreff, text, getReplyToAddress(), antrag.getBenutzer().getEmail());
    for (BewilligungsDaten bewilligungsDaten : antrag.getBewilligungen()) {
      BenutzerDaten bewilliger = bewilligungsDaten.getBenutzer();
      mailService.sendeMail(betreff, text, getReplyToAddress(), bewilliger.getEmail());
    }
  }

  private boolean istBewilligerFuerErsteUnterschrift(BewilligungsDaten b) {
    return b.getPosition() == 1;
  }

  private boolean istBewilligerFuerZweiteUnterschrift(BewilligungsDaten b) {
    return b.getPosition() == 2;
  }

  private String getBetreff(AntragsDaten antrag) {
    BenutzerDaten antragSteller = antrag.getBenutzer();

    return String.format("Urlaubsantrag für %s vom %s bis %s (%s Tage)", getBenutzerName(antragSteller),
            formatDate(antrag.getVon()), formatDate(antrag.getBis()), formatTage(antrag.getAnzahlTage()));
  }

  private String getAntragsText(AntragsDaten antrag, String template) {
    Context context = toolManager.createContext();
    context.put("antrag", antrag);
    context.put("antragsUrl", getUrlForAntrag(antrag.getId()));
    StringWriter writer = new StringWriter();
    velocityEngine.mergeTemplate("de/fisp/anwesenheit/mailtemplates/" + template, "UTF-8", context, writer);
    String text = writer.toString();
    log.debug(text);
    return text;
  }


  private String getAntragsTextForBewilligung(AntragsDaten antrag, BewilligungsDaten bewilligungsDaten, String template) {
    Context context = toolManager.createContext();
    context.put("antrag", antrag);
    context.put("bewilligung", bewilligungsDaten);
    context.put("bewilligungsUrl", getUrlForBewilligung(bewilligungsDaten.getId()));
    context.put("antragsUrl", getUrlForAntrag(antrag.getId()));
    StringWriter writer = new StringWriter();
    velocityEngine.mergeTemplate("de/fisp/anwesenheit/mailtemplates/" + template, "UTF-8", context, writer);
    String text = writer.toString();
    log.debug(text);
    return text;
  }

  private String formatTage(double tage) {
    NumberFormat fmt = new DecimalFormat("0.#");
    return fmt.format(tage);
  }

  private String getUrlForAntrag(long antragId) {
    return getBasisUrl() + "/?deepLink=uebersicht/" + antragId;
  }

  private String getUrlForBewilligung(long bewilligungsId) {
    return getBasisUrl() + "/?deepLink=bewilligungen/" + bewilligungsId;
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

  private String formatMarkDown(String input) {
    return markDownFormatter.formatMarkDown(input);
  }

  private String getReplyToAddress() {
    return parameterService.getValue("email.replyToAddress");
  }

  private String getBasisUrl() {
    return parameterService.getValue("base.url");
  }
}
