package de.fisp.anwesenheit.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "feiertag")
public class Feiertag implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	@Size(min = 1, max = 40)
	private String name;
	@Column(name = "definition_id", nullable = true)
	private Long definitionId;
	@ManyToOne(optional = true)
	@JoinColumn(name = "definition_id", insertable = false, updatable = false, nullable = true)
	private FeiertagDefinition definition;
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date datum;
	@Column(name = "anteil_arbeitszeit")
	private double anteilArbeitszeit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(Long definitionId) {
		this.definitionId = definitionId;
	}

	public FeiertagDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(FeiertagDefinition definition) {
		this.definition = definition;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public double getAnteilArbeitszeit() {
		return anteilArbeitszeit;
	}

	public void setAnteilArbeitszeit(double anteilArbeitszeit) {
		this.anteilArbeitszeit = anteilArbeitszeit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result
				+ ((definitionId == null) ? 0 : definitionId.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feiertag other = (Feiertag) obj;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (definitionId == null) {
			if (other.definitionId != null)
				return false;
		} else if (!definitionId.equals(other.definitionId))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(id).append(name).append(datum)
				.toString();
	}
}
