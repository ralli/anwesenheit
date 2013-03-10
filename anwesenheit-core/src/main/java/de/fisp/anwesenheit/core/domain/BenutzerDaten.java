package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BenutzerDaten {
	private String benutzerId;
	private String vorname;
	private String nachname;
	private String email;

	public BenutzerDaten(String benutzerId, String vorname, String nachname, String email) {
		this.benutzerId = benutzerId;
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
	}

	public String getBenutzerId() {
		return benutzerId;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public String getEmail() {
		return email;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
