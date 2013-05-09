package de.fisp.anwesenheit.core.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "benutzer_rolle")
public class BenutzerRolle implements Serializable {
  private static final long serialVersionUID = -8209714587794783803L;

  @Id
  @Column(name = "benutzer_id")
  private String benutzerId;
  @Id
  private String rolle;

  @ManyToOne(targetEntity=Benutzer.class)
  @JoinColumn(name="benutzer_id", insertable=false, updatable=false)
  private Benutzer benutzer;

  public String getBenutzerId() {
    return benutzerId;
  }

  public void setBenutzerId(String benutzerId) {
    this.benutzerId = benutzerId;
  }

  public String getRolle() {
    return rolle;
  }

  public void setRolle(String rolle) {
    this.rolle = rolle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BenutzerRolle that = (BenutzerRolle) o;

    if (!benutzerId.equals(that.benutzerId)) return false;
    if (!rolle.equals(that.rolle)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = benutzerId.hashCode();
    result = 31 * result + rolle.hashCode();
    return result;
  }

  public Benutzer getBenutzer() {
    return benutzer;
  }

  public void setBenutzer(Benutzer benutzer) {
    this.benutzer = benutzer;
  }

  @Override
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.append("benutzerId", benutzerId).append("rolle", rolle);
    return b.toString();
  }

}
