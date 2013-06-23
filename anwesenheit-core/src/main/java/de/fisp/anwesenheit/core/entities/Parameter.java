package de.fisp.anwesenheit.core.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

@Entity
@Table(name = "parameter")
public class Parameter implements Serializable {
  @Id
  private String id;
  private String name;
  private String wert;
  private String beschreibung;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWert() {
    return wert;
  }

  public void setWert(String wert) {
    this.wert = wert;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  @Override
  public String toString() {
    return "Parameter{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", wert='" + wert + '\'' +
            ", beschreibung='" + beschreibung + '\'' +
            '}';
  }
}