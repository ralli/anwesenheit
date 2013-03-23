package de.fisp.anwesenheit.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "bewilligung")
public class Bewilligung {
  @Id
  @GeneratedValue
  private long id;
  private int position;
  @Column(name = "antrag_id")
  private long antragId;
  @Column(name = "benutzer_id")
  private String benutzerId;
  @Column(name = "bewilligungs_status")
  private String bewilligungsStatusId;

  @ManyToOne
  @JoinColumn(name = "benutzer_id", insertable = false, updatable = false)
  private Benutzer benutzer;
  @ManyToOne
  @JoinColumn(name = "bewilligungs_status", insertable = false, updatable = false)
  private BewilligungsStatus bewilligungsStatus;
  @ManyToOne
  @JoinColumn(name = "antrag_id", insertable = false, updatable = false)
  private Antrag antrag;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

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

  public String getBewilligungsStatusId() {
    return bewilligungsStatusId;
  }

  public void setBewilligungsStatusId(String bewilligungsStatusId) {
    this.bewilligungsStatusId = bewilligungsStatusId;
  }

  public BewilligungsStatus getBewilligungsStatus() {
    return bewilligungsStatus;
  }

  public void setBewilligungsStatus(BewilligungsStatus bewilligungsStatus) {
    this.bewilligungsStatus = bewilligungsStatus;
  }

  public Benutzer getBenutzer() {
    return benutzer;
  }

  public void setBenutzer(Benutzer benutzer) {
    this.benutzer = benutzer;
  }
  
  public Antrag getAntrag() {
    return antrag;
  }
  
  public void setAntrag(Antrag antrag) {
    this.antrag = antrag;
  }

  @Override
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.append("id", id).append("position", position).append("antragId", antragId).append("benutzerId", benutzerId)
        .append("bewilligungsStatus", bewilligungsStatusId);
    return b.toString();
  }
}
