package de.fisp.anwesenheit.core.entities;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "antrag_historie")
public class AntragHistorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "antrag_id")
    private long antragId;
    @Column(name = "benutzer_id")
    private String benutzerId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date zeitpunkt;
    private String beschreibung;

    @ManyToOne(optional = false)
    @JoinColumn(name = "benutzer_id", insertable = false, updatable = false)
    private Benutzer benutzer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getZeitpunkt() {
        return zeitpunkt;
    }

    public void setZeitpunkt(Date zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    @Override
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.append("id", id).append("antragId", antragId).append("benutzerId", benutzerId).append("zeitpunkt", zeitpunkt)
                .append("beschreibung", beschreibung);
        return b.toString();
    }
}
