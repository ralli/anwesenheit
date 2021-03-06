package de.fisp.anwesenheit.core.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.util.JsonDateSerializer;

public class AntragListeEintrag {
  private final long id;
  private final BenutzerDaten benutzer;
  private final AntragArt antragArt;
  private final AntragStatus antragStatus;
  private final Date von;
  private final Date bis;
  private final double anzahlTage;

  public AntragListeEintrag(long id, BenutzerDaten benutzer, AntragArt antragArt, AntragStatus antragStatus, Date von, Date bis,
                            double anzahlTage) {
    this.id = id;
    this.benutzer = benutzer;
    this.antragArt = antragArt;
    this.antragStatus = antragStatus;
    this.von = von;
    this.bis = bis;
    this.anzahlTage = anzahlTage;
  }

  public long getId() {
    return id;
  }

  public BenutzerDaten getBenutzer() {
    return benutzer;
  }

  public AntragArt getAntragArt() {
    return antragArt;
  }

  public AntragStatus getAntragStatus() {
    return antragStatus;
  }

  @JsonSerialize(using = JsonDateSerializer.class)
  public Date getVon() {
    return von;
  }

  @JsonSerialize(using = JsonDateSerializer.class)
  public Date getBis() {
    return bis;
  }

  public double getAnzahlTage() {
    return anzahlTage;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
