package de.fisp.anwesenheit.core.entities;

import java.util.Date;

public class Antrag {
	private long id;
	private String benutzerId;
	private String antragsArt;
	private Date von;
	private Date bis;
	private String antragStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBenutzerId() {
		return benutzerId;
	}

	public void setBenutzerId(String benutzerId) {
		this.benutzerId = benutzerId;
	}

	public String getAntragsArt() {
		return antragsArt;
	}

	public void setAntragsArt(String antragsArt) {
		this.antragsArt = antragsArt;
	}

	public Date getVon() {
		return von;
	}

	public void setVon(Date von) {
		this.von = von;
	}

	public Date getBis() {
		return bis;
	}

	public void setBis(Date bis) {
		this.bis = bis;
	}

	public String getAntragStatus() {
		return antragStatus;
	}

	public void setAntragStatus(String antragStatus) {
		this.antragStatus = antragStatus;
	}
}
