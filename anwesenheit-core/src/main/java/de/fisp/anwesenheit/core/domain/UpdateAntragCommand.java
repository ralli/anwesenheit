package de.fisp.anwesenheit.core.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UpdateAntragCommand {
  private String antragArt;
  private Date von;
  private Date bis;

  public String getAntragArt() {
    return antragArt;
  }

  public void setAntragArt(String antragArt) {
    this.antragArt = antragArt;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
