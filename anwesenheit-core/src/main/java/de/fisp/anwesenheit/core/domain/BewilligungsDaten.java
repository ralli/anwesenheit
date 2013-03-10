package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.fisp.anwesenheit.core.entities.BewilligungsStatus;

public class BewilligungsDaten {
	private long id;
	private long antragId;
	private int position;
	private BewilligungsStatus bewilligungsStatus;
	private BenutzerDaten benutzer;

	public BewilligungsDaten(long id, long antragId, int position, 
			BewilligungsStatus bewilligungsStatus, BenutzerDaten benutzer) {
		this.id = id;
		this.antragId = antragId;
		this.position = position;
		this.bewilligungsStatus = bewilligungsStatus;
		this.benutzer = benutzer;
	}

	public long getId() {
		return id;
	}

	public long getAntragId() {
		return antragId;
	}

	public int getPosition() {
		return position;
	}
	
	public BewilligungsStatus getBewilligungsStatus() {
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
