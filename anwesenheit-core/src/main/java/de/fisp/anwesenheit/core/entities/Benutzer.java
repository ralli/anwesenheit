package de.fisp.anwesenheit.core.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "benutzer")
public class Benutzer {
  @Id
  @Column(name = "benutzer_id")
  private String benutzerId;
  private String vorname;
  private String nachname;
  private String benutzertyp; // native / ldap
  private String email;
  private String salt;
  @Column(name = "password_hash")
  private String passwordHash;

  @OneToMany(targetEntity = BenutzerRolle.class)  
  private Set<BenutzerRolle> benutzerRollen;

  public String getBenutzerId() {
    return benutzerId;
  }

  public void setBenutzerId(String benutzerId) {
    this.benutzerId = benutzerId;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  public String getBenutzertyp() {
    return benutzertyp;
  }

  public void setBenutzertyp(String benutzertyp) {
    this.benutzertyp = benutzertyp;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public Set<BenutzerRolle> getBenutzerRollen() {
    return benutzerRollen;
  }

  public void setBenutzerRollen(Set<BenutzerRolle> benutzerRollen) {
    this.benutzerRollen = benutzerRollen;
  }

  void addBenutzerRolle(BenutzerRolle benutzerRolle) {
    benutzerRollen.add(benutzerRolle);
  }

  @Override
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.append("benutzerId", benutzerId).append("vorname", vorname).append("nachname", nachname).append("benutzerTyp", benutzertyp)
        .append("email", email);
    return b.toString();
  }
}
