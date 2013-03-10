package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BewilligungsDaten {
	private long id;
	private long antragId;
	private String bewilligungsStatus;
	private BenutzerDaten benutzer;

	public BewilligungsDaten(long id, long antragId, String bewilligungsStatus,
			BenutzerDaten benutzer) {
		this.id = id;
		this.antragId = antragId;
		this.bewilligungsStatus = bewilligungsStatus;
		this.benutzer = benutzer;
	}

	public long getId() {
		return id;
	}

	public long getAntragId() {
		return antragId;
	}

	public String getBewilligungsStatus() {
		return bewilligungsStatus;
	}

	public BenutzerDaten getBenutzer() {
		return benutzer;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
