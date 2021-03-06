package de.fisp.anwesenheit.core.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;
import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;

public class BewilligungsDetails {
  private final long id;
  private final long antragId;
  private final int position;
  private final BenutzerDaten antragsteller;
  private final BenutzerDaten bewilliger;
  private final Date von;
  private final Date bis;
  private final double anzahlTage;
  private final AntragStatus antragStatus;
  private final AntragArt antragArt;
  private final SonderUrlaubArt sonderUrlaubArt;
  private final String kommentar;
  private final BewilligungsStatus bewilligungsStatus;
  private final List<BewilligungsDaten> bewilligungen;
  private final List<AntragListeEintrag> gleichzeitigeAntraege;

  public BewilligungsDetails(long id, long antragId, int position, BenutzerDaten antragsteller, BenutzerDaten bewilliger, Date von, Date bis,
                             double anzahlTage, AntragStatus antragStatus, AntragArt antragArt, SonderUrlaubArt sonderUrlaubArt, String kommentar,
                             BewilligungsStatus bewilligungsStatus, List<BewilligungsDaten> bewilligungen, List<AntragListeEintrag> gleichzeitigeAntraege) {
    this.id = id;
    this.antragId = antragId;
    this.antragsteller = antragsteller;
    this.position = position;
    this.bewilliger = bewilliger;
    this.von = von;
    this.bis = bis;
    this.anzahlTage = anzahlTage;
    this.antragStatus = antragStatus;
    this.antragArt = antragArt;
    this.sonderUrlaubArt = sonderUrlaubArt;
    this.kommentar = kommentar;
    this.bewilligungsStatus = bewilligungsStatus;
    this.bewilligungen = bewilligungen;
    this.gleichzeitigeAntraege = gleichzeitigeAntraege;
  }

  public long getId() {
    return id;
  }

  public long getAntragId() {
    return antragId;
  }

  public int getPosition() {
    return position;
  }

  public BenutzerDaten getAntragsteller() {
    return antragsteller;
  }

  public BenutzerDaten getBewilliger() {
    return bewilliger;
  }

  public Date getVon() {
    return von;
  }

  public Date getBis() {
    return bis;
  }

  public double getAnzahlTage() {
    return anzahlTage;
  }

  public AntragStatus getAntragStatus() {
    return antragStatus;
  }

  public AntragArt getAntragArt() {
    return antragArt;
  }

  public SonderUrlaubArt getSonderUrlaubArt() {
    return sonderUrlaubArt;
  }

  public String getKommentar() {
    return kommentar;
  }

  public BewilligungsStatus getBewilligungsStatus() {
    return bewilligungsStatus;
  }

  public List<BewilligungsDaten> getBewilligungen() {
    return bewilligungen;
  }

  public List<AntragListeEintrag> getGleichzeitigeAntraege() {
    return gleichzeitigeAntraege;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
