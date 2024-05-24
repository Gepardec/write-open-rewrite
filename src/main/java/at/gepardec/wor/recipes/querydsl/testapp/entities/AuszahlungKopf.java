package at.gepardec.wor.recipes.querydsl.testapp.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the AUSZAHLUNG_KOPF database table.
 */
@Entity
@Table(name = "AUSZAHLUNG_KOPF")
public class AuszahlungKopf extends DatabaseManagedEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "AUSZAHLUNG_KOPF_ID_GENERATOR", sequenceName = "SEQ_AUSZAHLUNG_KOPF", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUSZAHLUNG_KOPF_ID_GENERATOR")
    @Column(name = "AZH_ID", unique = true, nullable = false)
    private long id;

    @Column(name = "AZH_BEN_KBEZ_LOE", length = 4)
    private String azhBenKbezLoe;

    @Temporal(TemporalType.DATE)
    @Column(name = "AZH_DAT_LOE")
    private Date azhDatLoe;

    @Lob
    @Column(name = "AZH_DTBZ_BLOB")
    private byte[] azhDtbzBlob;

    @Lob
    @Column(name = "AZH_XML")
    private String azhXml;

    @Column(name = "CHRGBR", length = 6)
    private String chrgBr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREDTTM")
    private Date creDtTm;

    @Column(name = "CTRLSUM", length = 20)
    private BigDecimal ctrlSum;

    @Column(name = "DBTR_NM", length = 255)
    private String dbtrNm;

    @Column(name = "DBTR_PSTLADR_ADRLINE", length = 255)
    private String dbtrPstlAdrAdrLine;

    @Column(name = "DBTR_PSTLADR_CTRY", length = 5)
    private String dbtrPstlAdrCtry;

    @Column(name = "DBTRACCT_CCY", length = 6)
    private String dbtrAcctCcy;

    @Column(name = "DBTRACCT_ID_IBAN", length = 50)
    private String dbtrAcctIdIban;

    @Column(name = "DBTRAGT_FININSTNID_BIC", length = 20)
    private String dbtrAgtFinInstnIdBic;

    @Column(name = "INITGPTY_NM", length = 255)
    private String initgPtyNm;

    @Column(name = "MSGID", length = 20)
    private String msgId;

    @Column(name = "NBOFTXY")
    private String nbOfTxs;

    @Column(name = "PMTMTD", length = 6)
    private String pmtMtd;

    @Column(name = "PMTTPINF_CTGYPURP_PRTRY", length = 6)
    private String pmtTpInfCtgyPurpPrtry;

    @Column(name = "PMTTPINF_SVCLVL_CD", length = 6)
    private String pmtTpInfSvcLvlCd;

    @Column(name = "PMTTPINFID", length = 20)
    private String pmtTpInfId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REQDEXCTNDT", length = 20)
    private Date reqdExctnDt;

    // bi-directional many-to-one association to AuszahlungDetail
    @OneToMany(mappedBy = "auszahlungKopf")
    private List<AuszahlungDetail> auszahlungDetails;

    public AuszahlungKopf() {
        setDefaultValues();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAzhBenKbezLoe() {
        return azhBenKbezLoe;
    }

    public void setAzhBenKbezLoe(String azhBenKbezLoe) {
        this.azhBenKbezLoe = azhBenKbezLoe;
    }

    public Date getAzhDatLoe() {
        return azhDatLoe;
    }

    public void setAzhDatLoe(Date azhDatLoe) {
        this.azhDatLoe = azhDatLoe;
    }

    public byte[] getAzhDtbzBlob() {
        return azhDtbzBlob;
    }

    public void setAzhDtbzBlob(byte[] azhDtbzBlob) {
        this.azhDtbzBlob = azhDtbzBlob;
    }

    public String getAzhXml() {
        return azhXml;
    }

    public void setAzhXml(String azhXml) {
        this.azhXml = azhXml;
    }

    public String getChrgBr() {
        return chrgBr;
    }

    public void setChrgBr(String chrgBr) {
        this.chrgBr = chrgBr;
    }

    public Date getCreDtTm() {
        return creDtTm;
    }

    public void setCreDtTm(Date creDtTm) {
        this.creDtTm = creDtTm;
    }

    public BigDecimal getCtrlSum() {
        return ctrlSum;
    }

    public void setCtrlSum(BigDecimal ctrlSum) {
        this.ctrlSum = ctrlSum;
    }

    public String getDbtrNm() {
        return dbtrNm;
    }

    public void setDbtrNm(String dbtrNm) {
        this.dbtrNm = dbtrNm;
    }

    public String getDbtrPstlAdrAdrLine() {
        return dbtrPstlAdrAdrLine;
    }

    public void setDbtrPstlAdrAdrLine(String dbtrPstlAdrAdrLine) {
        this.dbtrPstlAdrAdrLine = dbtrPstlAdrAdrLine;
    }

    public String getDbtrPstlAdrCtry() {
        return dbtrPstlAdrCtry;
    }

    public void setDbtrPstlAdrCtry(String dbtrPstlAdrCtry) {
        this.dbtrPstlAdrCtry = dbtrPstlAdrCtry;
    }

    public String getDbtrAcctCcy() {
        return dbtrAcctCcy;
    }

    public void setDbtrAcctCcy(String dbtrAcctCcy) {
        this.dbtrAcctCcy = dbtrAcctCcy;
    }

    public String getDbtrAcctIdIban() {
        return dbtrAcctIdIban;
    }

    public void setDbtrAcctIdIban(String dbtrAcctIdIban) {
        this.dbtrAcctIdIban = dbtrAcctIdIban;
    }

    public String getDbtrAgtFinInstnIdBic() {
        return dbtrAgtFinInstnIdBic;
    }

    public void setDbtrAgtFinInstnIdBic(String dbtrAgtFinInstnIdBic) {
        this.dbtrAgtFinInstnIdBic = dbtrAgtFinInstnIdBic;
    }

    public String getInitgPtyNm() {
        return initgPtyNm;
    }

    public void setInitgPtyNm(String initgPtyNm) {
        this.initgPtyNm = initgPtyNm;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getNbOfTxs() {
        return nbOfTxs;
    }

    public void setNbOfTxs(String nbOfTxs) {
        this.nbOfTxs = nbOfTxs;
    }

    public String getPmtMtd() {
        return pmtMtd;
    }

    public void setPmtMtd(String pmtMtd) {
        this.pmtMtd = pmtMtd;
    }

    public String getPmtTpInfCtgyPurpPrtry() {
        return pmtTpInfCtgyPurpPrtry;
    }

    public void setPmtTpInfCtgyPurpPrtry(String pmtTpInfCtgyPurpPrtry) {
        this.pmtTpInfCtgyPurpPrtry = pmtTpInfCtgyPurpPrtry;
    }

    public String getPmtTpInfSvcLvlCd() {
        return pmtTpInfSvcLvlCd;
    }

    public void setPmtTpInfSvcLvlCd(String pmtTpInfSvcLvlCd) {
        this.pmtTpInfSvcLvlCd = pmtTpInfSvcLvlCd;
    }

    public String getPmtTpInfId() {
        return pmtTpInfId;
    }

    public void setPmtTpInfId(String pmtTpInfId) {
        this.pmtTpInfId = pmtTpInfId;
    }

    public Date getReqdExctnDt() {
        return reqdExctnDt;
    }

    public void setReqdExctnDt(Date reqdExctnDt) {
        this.reqdExctnDt = reqdExctnDt;
    }

    public List<AuszahlungDetail> getAuszahlungDetails() {
        return this.auszahlungDetails;
    }

    public void setAuszahlungDetails(List<AuszahlungDetail> auszahlungDetails) {
        this.auszahlungDetails = auszahlungDetails;
    }

    public AuszahlungDetail addAuszahlungDetail(AuszahlungDetail auszahlungDetail) {
        getAuszahlungDetails().add(auszahlungDetail);
        auszahlungDetail.setAuszahlungKopf(this);

        return auszahlungDetail;
    }

    public AuszahlungDetail removeAuszahlungDetail(AuszahlungDetail auszahlungDetail) {
        getAuszahlungDetails().remove(auszahlungDetail);
        auszahlungDetail.setAuszahlungKopf(null);

        return auszahlungDetail;
    }

    private void setDefaultValues() {
        this.pmtMtd = "TRF";
        this.pmtTpInfSvcLvlCd = "SEPA";
        this.chrgBr = "SLEV";
    }
}