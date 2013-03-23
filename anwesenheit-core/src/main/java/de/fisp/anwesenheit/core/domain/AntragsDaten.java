package de.fisp.anwesenheit.core.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;
import de.fisp.anwesenheit.core.util.JsonDateSerializer;

public class AntragsDaten {
  private long id;
  private AntragArt antragArt;
  private AntragStatus antragStatus;
  private SonderUrlaubArt sonderUrlaubArt;
  private Date von;
  private Date bis;
  private BenutzerDaten benutzer;
  private List<BewilligungsDaten> bewilligungen;
  private double anzahlTage;

  public AntragsDaten(long id, AntragArt antragArt, AntragStatus antragStatus, SonderUrlaubArt sonderUrlaubArt, Date von, Date bis, double anzahlTage,
      BenutzerDaten benutzer, List<BewilligungsDaten> bewilligungen) {
    super();
    this.id = id;
    this.antragArt = antragArt;
    this.sonderUrlaubArt = sonderUrlaubArt;
    this.antragStatus = antragStatus;
    this.von = von;
    this.bis = bis;
    this.benutzer = benutzer;
    this.bewilligungen = bewilligungen;
    this.anzahlTage = anzahlTage;
  }

  public long getId() {
    return id;
  }

  public AntragArt getAntragArt() {
    return antragArt;
  }

  public AntragStatus getAntragStatus() {
    return antragStatus;
  }

  public SonderUrlaubArt getSonderUrlaubArt() {
    return sonderUrlaubArt;
  }
  
  @JsonSerialize(using = JsonDateSerializer.class)
  public Date getVon() {
    return von;
  }

  @JsonSerialize(using = JsonDateSerializer.class)
  public Date getBis() {
    return bis;
  }

  public double getAnzahlTage() {
    return anzahlTage;
  }

  public BenutzerDaten getBenutzer() {
    return benutzer;
  }

  public List<BewilligungsDaten> getBewilligungen() {
    return bewilligungen;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
