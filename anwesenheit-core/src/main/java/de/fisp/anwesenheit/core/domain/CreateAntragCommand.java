package de.fisp.anwesenheit.core.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class CreateAntragCommand {
  private String benutzerId;
  private String antragArt;
  private String sonderUrlaubArt;
  private Date von;
  private Date bis;
  private double anzahlTage;
  private String[] bewilliger;

  public String getBenutzerId() {
    return benutzerId;
  }

  public void setBenutzerId(String benutzerId) {
    this.benutzerId = benutzerId;
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

  public String[] getBewilliger() {
    return bewilliger;
  }

  public void setBewilliger(String[] bewilliger) {
    this.bewilliger = bewilliger;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public String getAntragArt() {
    return antragArt;
  }

  public void setAntragArt(String antragArt) {
    this.antragArt = antragArt;
  }

  public double getAnzahlTage() {
    return anzahlTage;
  }

  public void setAnzahlTage(double anzahlTage) {
    this.anzahlTage = anzahlTage;
  }

  public String getSonderUrlaubArt() {
    return sonderUrlaubArt;
  }

  public void setSonderUrlaubArt(String sonderUrlaubArt) {
    this.sonderUrlaubArt = sonderUrlaubArt;
  }
}
