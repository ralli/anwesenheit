package de.fisp.anwesenheit.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "antrag_status")
public class AntragStatus {
	@Id
	@Column(name = "antrag_status")
	private String antragStatus;
	private int position;
	private String bezeichnung;

	public String getAntragStatus() {
		return antragStatus;
	}

	public void setAntragStatus(String antragStatus) {
		this.antragStatus = antragStatus;
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
