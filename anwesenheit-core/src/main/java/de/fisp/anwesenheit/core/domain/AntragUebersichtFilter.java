package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

public class AntragUebersichtFilter {
  private String antragsteller;
  private Date von;
  private Date bis;
  private List<String> statusList;

  public String getAntragsteller() {
    return antragsteller;
  }

  public void setAntragsteller(String antragsteller) {
    this.antragsteller = antragsteller;
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

  public void setStatusList(List<String> statusList) {
    this.statusList = statusList;
  }

  public List<String> getStatusList() {
    return statusList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
