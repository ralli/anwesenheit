package de.fisp.anwesenheit.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "sonderurlaub_art")
public class SonderUrlaubArt {
  @Id
  @Column(name = "sonderurlaub_art")
  private String sonderUrlaubArt;
  private int position;
  @Column(name = "anzahl_tage")
  private double anzahlTage;
  private String bezeichnung;

  public String getSonderUrlaubArt() {
    return sonderUrlaubArt;
  }

  public void setSonderUrlaubArt(String sonderUrlaubArt) {
    this.sonderUrlaubArt = sonderUrlaubArt;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public double getAnzahlTage() {
    return anzahlTage;
  }

  public void setAnzahlTage(double anzahlTage) {
    this.anzahlTage = anzahlTage;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
