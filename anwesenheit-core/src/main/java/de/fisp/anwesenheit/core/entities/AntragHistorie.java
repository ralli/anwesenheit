package de.fisp.anwesenheit.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "antrag_historie")
public class AntragHistorie {
	@Id
	@GeneratedValue
	private long id;
	@Column(name = "antrag_id")
	private long antragId;
	@Column(name = "benutzer_id")
	private String benutzerId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date zeitpunkt;
	private String beschreibung;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Date getZeitpunkt() {
		return zeitpunkt;
	}

	public void setZeitpunkt(Date zeitpunkt) {
		this.zeitpunkt = zeitpunkt;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
}
