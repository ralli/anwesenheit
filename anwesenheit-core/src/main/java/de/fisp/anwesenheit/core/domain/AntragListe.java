package de.fisp.anwesenheit.core.domain;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AntragListe {
  private BenutzerDaten benutzer;
  private List<AntragListeEintrag> antraege;

  public AntragListe(BenutzerDaten benutzer, List<AntragListeEintrag> antraege) {
    this.benutzer = benutzer;
    this.antraege = antraege;
  }

  public BenutzerDaten getBenutzer() {
    return benutzer;
  }

  public List<AntragListeEintrag> getAntraege() {
    return antraege;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
