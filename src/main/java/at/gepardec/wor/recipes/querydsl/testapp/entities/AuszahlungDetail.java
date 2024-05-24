package at.gepardec.wor.recipes.querydsl.testapp.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the AUSZAHLUNG_DETAIL database table.
 */
@Entity
@Table(name = "AUSZAHLUNG_DETAIL")
public class AuszahlungDetail extends DatabaseManagedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "AUSZAHLUNG_DETAIL_ID_GENERATOR", sequenceName = "SEQ_AUSZAHLUNG_DETAIL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUSZAHLUNG_DETAIL_ID_GENERATOR")
    @Column(name = "AZD_ID", unique = true, nullable = false)
    private long id;

    @Column(name = "AMT_INSTDAMT", length = 30)
    private BigDecimal amtInstdamt;

    @Column(name = "CDTR_NM", length = 200)
    private String cdtrNm;

    @Column(name = "CDTR_PSTLADR_ADRLINE", length = 255)
    private String cdtrPstladrAdrline;

    @Column(name = "CDTR_PSTLADR_CTRY", length = 5)
    private String cdtrPstladrCtry;

    @Column(name = "CDTRACCT_ID_IBAN", length = 50)
    private String cdtracctIdIban;

    @Column(name = "CDTRAGT_FININSTNID_BIC", length = 50)
    private String cdtragtFininstnidBic;

    @Column(name = "ENDTOENDID", length = 30)
    private String endtoendid;

    @Column(name = "PMTID_INSTRID", length = 30)
    private String pmtidInstrid;

    @Column(name = "RMTINF", length = 500)
    private String rmtinf;

    @Column(name = "ULTMTCDTR", length = 200)
    private String ultmtcdtr;

    @Column(name = "AZD_ZAH_ID")
    private Long azdZahId;

    @ManyToOne
    @JoinColumn(name = "AZH_ID")
    private AuszahlungKopf auszahlungKopf;

    public AuszahlungDetail() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmtInstdamt() {
        return this.amtInstdamt;
    }

    public void setAmtInstdamt(BigDecimal amtInstdamt) {
        this.amtInstdamt = amtInstdamt;
    }

    public String getCdtrNm() {
        return this.cdtrNm;
    }

    public void setCdtrNm(String cdtrNm) {
        this.cdtrNm = cdtrNm;
    }

    public String getCdtrPstladrAdrline() {
        return this.cdtrPstladrAdrline;
    }

    public void setCdtrPstladrAdrline(String cdtrPstladrAdrline) {
        this.cdtrPstladrAdrline = cdtrPstladrAdrline;
    }

    public String getCdtrPstladrCtry() {
        return this.cdtrPstladrCtry;
    }

    public void setCdtrPstladrCtry(String cdtrPstladrCtry) {
        this.cdtrPstladrCtry = cdtrPstladrCtry;
    }

    public String getCdtracctIdIban() {
        return this.cdtracctIdIban;
    }

    public void setCdtracctIdIban(String cdtracctIdIban) {
        this.cdtracctIdIban = cdtracctIdIban;
    }

    public String getCdtragtFininstnidBic() {
        return this.cdtragtFininstnidBic;
    }

    public void setCdtragtFininstnidBic(String cdtragtFininstnidBic) {
        this.cdtragtFininstnidBic = cdtragtFininstnidBic;
    }

    public String getEndtoendid() {
        return this.endtoendid;
    }

    public void setEndtoendid(String endtoendid) {
        this.endtoendid = endtoendid;
    }

    public String getPmtidInstrid() {
        return this.pmtidInstrid;
    }

    public void setPmtidInstrid(String pmtidInstrid) {
        this.pmtidInstrid = pmtidInstrid;
    }

    public String getRmtinf() {
        return this.rmtinf;
    }

    public void setRmtinf(String rmtinf) {
        this.rmtinf = rmtinf;
    }

    public String getUltmtcdtr() {
        return this.ultmtcdtr;
    }

    public void setUltmtcdtr(String ultmtcdtr) {
        this.ultmtcdtr = ultmtcdtr;
    }

    public Long getAzdZahId() {
        return azdZahId;
    }

    public void setAzdZahId(Long azdZahId) {
        this.azdZahId = azdZahId;
    }

    public AuszahlungKopf getAuszahlungKopf() {
        return this.auszahlungKopf;
    }

    public void setAuszahlungKopf(AuszahlungKopf auszahlungKopf) {
        this.auszahlungKopf = auszahlungKopf;
    }

}