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
@Table(name = "antrag")
public class Antrag {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	@Column(name = "benutzer_id")
	private String benutzerId;
	@Column(name = "antrag_art")
	private String antragArt;
	@Temporal(TemporalType.DATE)
	private Date von;
	@Temporal(TemporalType.DATE)
	private Date bis;
	@Column(name = "antrag_status")
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

	public String getAntragArt() {
		return antragArt;
	}

	public void setAntragArt(String antragArt) {
		this.antragArt = antragArt;
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
