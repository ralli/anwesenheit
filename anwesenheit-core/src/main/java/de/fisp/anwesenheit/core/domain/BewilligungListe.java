package de.fisp.anwesenheit.core.domain;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BewilligungListe {
  private BenutzerDaten benutzer;
  private List<BewilligungsListeEintrag> bewilligungen;

  public BewilligungListe(BenutzerDaten benutzer, List<BewilligungsListeEintrag> bewilligungen) {
    this.benutzer = benutzer;
    this.bewilligungen = bewilligungen;
  }

  public BenutzerDaten getBenutzer() {
    return benutzer;
  }

  public List<BewilligungsListeEintrag> getBewilligungen() {
    return bewilligungen;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
