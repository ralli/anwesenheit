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
  private final long id;
  private final AntragArt antragArt;
  private final AntragStatus antragStatus;
  private final SonderUrlaubArt sonderUrlaubArt;
  private final Date von;
  private final Date bis;
  private final BenutzerDaten benutzer;
  private final List<BewilligungsDaten> bewilligungen;
  private final double anzahlTage;
  private String kommentar;

  public AntragsDaten(long id, AntragArt antragArt, AntragStatus antragStatus, SonderUrlaubArt sonderUrlaubArt, Date von, Date bis, double anzahlTage,
                      BenutzerDaten benutzer, List<BewilligungsDaten> bewilligungen, String kommentar) {
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
    this.kommentar = kommentar;
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

  public String getKommentar() {
    return kommentar;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }
}
