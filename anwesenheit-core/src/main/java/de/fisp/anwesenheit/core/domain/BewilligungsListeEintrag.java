package de.fisp.anwesenheit.core.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;

public class BewilligungsListeEintrag {
  private long id;
  private BewilligungsStatus bewilligungsStatus;
  private long antragId;
  private AntragArt antragArt;
  private AntragStatus antragStatus;
  private BenutzerDaten benutzer;
  private Date von;
  private Date bis;

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
