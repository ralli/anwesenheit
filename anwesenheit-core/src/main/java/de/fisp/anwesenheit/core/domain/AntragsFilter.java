package de.fisp.anwesenheit.core.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AntragsFilter {
  private Date von;
  private Date bis;
  private String antragsStatusFilter; // OFFEN, BEWILLIGT, ABGELEHNT, STORNIERT (Default:
                                      // ALLE)

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

  public String getAntragsStatusFilter() {
    return antragsStatusFilter;
  }

  public void setAntragsStatusFilter(String antragsStatusFilter) {
    this.antragsStatusFilter = antragsStatusFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
