package de.fisp.anwesenheit.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "antrag_art")
public class AntragArt {
  @Id
  @Column(name = "antrag_art")
  private String antragArt;
  private int position;
  private String bezeichnung;

  public String getAntragArt() {
    return antragArt;
  }

  public void setAntragArt(String antragArt) {
    this.antragArt = antragArt;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
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
