package de.fisp.anwesenheit.core.entities;

import java.util.Date;

public class AntragHistorie {
	private long id;
	private long antragId;
	private String benutzerId;
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
