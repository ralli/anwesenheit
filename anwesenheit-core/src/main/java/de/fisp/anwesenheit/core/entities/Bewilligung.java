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
	private String bewilligungsStatus;
	@ManyToOne
	@JoinColumn(name = "benutzer_id", insertable = false, updatable = false)
	private Benutzer benutzer;

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

	public String getBewilligungsStatus() {
		return bewilligungsStatus;
	}

	public void setBewilligungsStatus(String bewilligungsStatus) {
		this.bewilligungsStatus = bewilligungsStatus;
	}

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
