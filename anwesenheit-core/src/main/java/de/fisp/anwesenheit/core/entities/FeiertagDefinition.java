package de.fisp.anwesenheit.core.entities;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "feiertagdefinition")
public class FeiertagDefinition implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Typ: Fixes Datum
     */
    public static final int FIX = 1;
    /**
     * Typ: Relativ zum Ostersonntag
     */
    public static final int OSTERN = 2;
    /**
     * Typ: Muttertag
     */
    public static final int MUTTERTAG = 3;
    /**
     * Typ: Relativ zum Buß und Bettag
     */
    public static final int ERSTER_DIENSTAG_IM_NOVEMBER = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "typ")
    private int type;
    @Column(name = "anteil_arbeitszeit")
    private double anteilArbeitszeit;
    private int offset;
    @Column(name = "referenz_tag")
    private int referenzTag;
    @Column(name = "referenz_monat")
    private int referenzMonat;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAnteilArbeitszeit() {
        return anteilArbeitszeit;
    }

    public void setAnteilArbeitszeit(double anteilArbeitszeit) {
        this.anteilArbeitszeit = anteilArbeitszeit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getReferenzTag() {
        return referenzTag;
    }

    public void setReferenzTag(int referenzTag) {
        this.referenzTag = referenzTag;
    }

    public int getReferenzMonat() {
        return referenzMonat;
    }

    public void setReferenzMonat(int referenzMonat) {
        this.referenzMonat = referenzMonat;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
