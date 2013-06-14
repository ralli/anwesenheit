package de.fisp.anwesenheit.core.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UpdateAntragCommand {
  private long id;
  private String antragArt;
  private String sonderUrlaubArt;
  private Date von;
  private Date bis;
  private double anzahlTage;
  private String kommentar;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAntragArt() {
    return antragArt;
  }

  public void setAntragArt(String antragArt) {
    this.antragArt = antragArt;
  }

  public String getSonderUrlaubArt() {
    return sonderUrlaubArt;
  }

  public void setSonderUrlaubArt(String sonderUrlaubArt) {
    this.sonderUrlaubArt = sonderUrlaubArt;
  }

  public Date getVon() {
    return von;
  }

  public void setVon(Date von) {
    this.von = von;
  }

  public Date getBis() {
    return bis;
  }

  public void setBis(Date bis) {
    this.bis = bis;
  }

  public double getAnzahlTage() {
    return anzahlTage;
  }

  public void setAnzahlTage(double anzahlTage) {
    this.anzahlTage = anzahlTage;
  }

  public String getKommentar() {
    return kommentar;
  }

  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
