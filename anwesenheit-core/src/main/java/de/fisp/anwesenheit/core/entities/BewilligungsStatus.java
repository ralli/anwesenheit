package de.fisp.anwesenheit.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "bewilligungs_status")
public class BewilligungsStatus {
	@Id
	@Column(name = "bewilligungs_status")
	private String bewilligungsStatus;
	private int position;
	private String bezeichnung;

	public String getBewilligungsStatus() {
		return bewilligungsStatus;
	}

	public void setBewilligungsStatus(String bewilligungsStatus) {
		this.bewilligungsStatus = bewilligungsStatus;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
