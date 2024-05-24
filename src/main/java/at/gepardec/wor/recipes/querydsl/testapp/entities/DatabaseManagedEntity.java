package at.gepardec.wor.recipes.querydsl.testapp.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class DatabaseManagedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ERSTELLT_AM", updatable = false)
    private Date erstelltAm;

    @Column(name = "ERSTELLT_VON", length = 30, updatable = false)
    private String erstelltVon;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GEAENDERT_AM", updatable = false)
    private Date geaendertAm;

    @Column(name = "GEAENDERT_VON", length = 30, updatable = false)
    private String geaendertVon;

    public Date getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(Date erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public String getErstelltVon() {
        return erstelltVon;
    }

    public void setErstelltVon(String erstelltVon) {
        this.erstelltVon = erstelltVon;
    }

    public Date getGeaendertAm() {
        return geaendertAm;
    }

    public void setGeaendertAm(Date geaendertAm) {
        this.geaendertAm = geaendertAm;
    }

    public String getGeaendertVon() {
        return geaendertVon;
    }

    public void setGeaendertVon(String geaendertVon) {
        this.geaendertVon = geaendertVon;
    }
}
