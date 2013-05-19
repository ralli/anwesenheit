package de.fisp.anwesenheit.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;

public class BewilligungsListeEintrag {
  private final long id;
  private final BewilligungsStatus bewilligungsStatus;
  private final long antragId;
  private final AntragArt antragArt;
  private final AntragStatus antragStatus;
  private final BenutzerDaten benutzer;
  private final Date von;
  private final Date bis;

  public BewilligungsListeEintrag(long id, BewilligungsStatus bewilligungsStatus, long antragId, AntragArt antragArt,
                                  AntragStatus antragStatus, BenutzerDaten benutzer, Date von, Date bis) {
    this.id = id;
    this.bewilligungsStatus = bewilligungsStatus;
    this.antragId = antragId;
    this.antragArt = antragArt;
    this.antragStatus = antragStatus;
    this.benutzer = benutzer;
    this.von = von;
    this.bis = bis;

  }

  public String getBenutzerName() {
    List<String> parts = new ArrayList<String>();
    if (StringUtils.isNotBlank(benutzer.getVorname())) {
      parts.add(benutzer.getVorname());
    }
    if (StringUtils.isNotBlank(benutzer.getNachname())) {
      parts.add(benutzer.getNachname());
    }
    if (parts.isEmpty()) {
      parts.add(benutzer.getBenutzerId());
    }
    return StringUtils.join(parts, ' ');
  }

  public String getStatus() {
    return bewilligungsStatus.getBewilligungsStatus();
  }

  public long getId() {
    return id;
  }

  public BewilligungsStatus getBewilligungsStatus() {
    return bewilligungsStatus;
  }

  public long getAntragId() {
    return antragId;
  }

  public AntragArt getAntragArt() {
    return antragArt;
  }

  public AntragStatus getAntragStatus() {
    return antragStatus;
  }

  public BenutzerDaten getBenutzer() {
    return benutzer;
  }

  public Date getVon() {
    return von;
  }

  public Date getBis() {
    return bis;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
