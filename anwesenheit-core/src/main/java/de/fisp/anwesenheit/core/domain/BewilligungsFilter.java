package de.fisp.anwesenheit.core.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BewilligungsFilter {
  private String benutzerId;
  private Date von;
  private Date bis;
  private String statusFilter; // OFFEN, BEWILLIGT, ABGELEHNT (Default:
  // ALLE)

  public String getBenutzerId() {
    return benutzerId;
  }

  public void setBenutzerId(String benutzerId) {
    this.benutzerId = benutzerId;
  }

  public void setVon(Date von) {
    this.von = von;
  }

  public Date getVon() {
    return von;
  }

  public void setBis(Date bis) {
    this.bis = bis;
  }

  public Date getBis() {
    return bis;
  }

  public void setStatusFilter(String statusFilter) {
    this.statusFilter = statusFilter;
  }

  public String getStatusFilter() {
    return statusFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
