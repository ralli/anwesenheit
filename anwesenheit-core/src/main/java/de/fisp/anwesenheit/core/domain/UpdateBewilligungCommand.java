package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UpdateBewilligungCommand {
  private long id;
  private String bewilligungsStatus;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getBewilligungsStatus() {
    return bewilligungsStatus;
  }

  public void setBewilligungsStatus(String bewilligungsStatus) {
    this.bewilligungsStatus = bewilligungsStatus;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
