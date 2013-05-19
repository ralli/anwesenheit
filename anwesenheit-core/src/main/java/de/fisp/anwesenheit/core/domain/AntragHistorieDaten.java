package de.fisp.anwesenheit.core.domain;

import java.util.Date;

public class AntragHistorieDaten {
  private final long id;
  private final long antragId;
  private final String benutzerId;
  private final Date zeitpunkt;
  private final String beschreibung;
  private final BenutzerDaten benutzer;

  public AntragHistorieDaten(long id, long antragId, String benutzerId, Date zeitpunkt, String beschreibung,
                             BenutzerDaten benutzerDaten) {
    super();
    this.id = id;
    this.antragId = antragId;
    this.benutzerId = benutzerId;
    this.zeitpunkt = zeitpunkt;
    this.beschreibung = beschreibung;
    this.benutzer = benutzerDaten;
  }

  public long getId() {
    return id;
  }

  public long getAntragId() {
    return antragId;
  }

  public String getBenutzerId() {
    return benutzerId;
  }

  public Date getZeitpunkt() {
    return zeitpunkt;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public BenutzerDaten getBenutzer() {
    return benutzer;
  }

}
