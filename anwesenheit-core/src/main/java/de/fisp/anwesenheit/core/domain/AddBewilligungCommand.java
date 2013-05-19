package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AddBewilligungCommand {
  private long antragId;
  private String benutzerId;

  public long getAntragId() {
    return antragId;
  }

  public void setAntragId(long antragId) {
    this.antragId = antragId;
  }

  public String getBenutzerId() {
    return benutzerId;
  }

  public void setBenutzerId(String benutzerId) {
    this.benutzerId = benutzerId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
