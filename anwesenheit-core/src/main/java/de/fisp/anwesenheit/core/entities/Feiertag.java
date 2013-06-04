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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Feiertag feiertag = (Feiertag) o;

    if (Double.compare(feiertag.anteilArbeitszeit, anteilArbeitszeit) != 0) return false;
    if (id != feiertag.id) return false;
    if (datum != null ? !datum.equals(feiertag.datum) : feiertag.datum != null) return false;
    if (name != null ? !name.equals(feiertag.name) : feiertag.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (datum != null ? datum.hashCode() : 0);
    temp = Double.doubleToLongBits(anteilArbeitszeit);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Feiertag{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", datum=" + datum +
            ", anteilArbeitszeit=" + anteilArbeitszeit +
            '}';
  }
}
