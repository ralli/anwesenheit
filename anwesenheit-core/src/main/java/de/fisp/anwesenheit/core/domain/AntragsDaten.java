package de.fisp.anwesenheit.core.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.fisp.anwesenheit.core.util.JsonDateSerializer;

public class AntragsDaten {
	private long id;
	private String antragArt;	
	private Date von;
	private Date bis;
	private BenutzerDaten benutzer;
	private List<BewilligungsDaten> bewilligungen;

	public AntragsDaten(long id, String antragArt, Date von, Date bis, BenutzerDaten benutzer,
			List<BewilligungsDaten> bewilligungen) {
		super();
		this.id = id;
		this.antragArt = antragArt;
		this.von = von;
		this.bis = bis;
		this.benutzer = benutzer;
		this.bewilligungen = bewilligungen;
	}

	public long getId() {
		return id;
	}

	public String getAntragArt() {
		return antragArt;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getVon() {
		return von;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getBis() {
		return bis;
	}

	public BenutzerDaten getBenutzer() {
		return benutzer;
	}

	public List<BewilligungsDaten> getBewilligungen() {
		return bewilligungen;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
