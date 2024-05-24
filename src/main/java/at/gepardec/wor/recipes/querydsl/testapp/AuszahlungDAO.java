package at.gepardec.wor.recipes.querydsl.testapp;

import java.util.Date;
import java.util.List;

/*import at.gepardec.wor.recipes.querydsl.testapp.entities.AuszahlungDetail;
import at.gepardec.wor.recipes.querydsl.testapp.entities.AuszahlungKopf;
import at.gepardec.wor.recipes.querydsl.testapp.entities.QAuszahlungDetail;
import at.gepardec.wor.recipes.querydsl.testapp.entities.QAuszahlungKopf;
import com.mysema.query.types.expr.BooleanExpression;

public class AuszahlungDAO extends at.sozvers.stp.rgkk.domain.dao.BaseDAO {

    private static final QAuszahlungKopf AUSZAHLUNG_KOPF = QAuszahlungKopf.auszahlungKopf;
    private static final QAuszahlungDetail AUSZAHLUNG_DETAIL = QAuszahlungDetail.auszahlungDetail;

    private static final String POSTANWEISUNG = "CPPP";

    public Long findMaxAuszahlungKopfId(Integer traegerId) {
        Long maxId = createQuery(traegerId).from(AUSZAHLUNG_KOPF)
                                           .uniqueResult(AUSZAHLUNG_KOPF.id.max());
        return maxId == null ? Long.valueOf(0L) : maxId;
    }

    public Long getMaxAuszahlungDetailId(Integer traegerId) {
        return createQuery(traegerId).from(AUSZAHLUNG_DETAIL)
                                     .singleResult(AUSZAHLUNG_DETAIL.id.max());
    }

    public List<AuszahlungKopf> findAuszahlungenKoepfe(Integer traegerId, Long minId, Long maxId) {
        return createQuery(traegerId).from(AUSZAHLUNG_KOPF)
                                     .where(AUSZAHLUNG_KOPF.id.gt(minId)
                                                              .and(AUSZAHLUNG_KOPF.id.loe(maxId)))
                                     .list(AUSZAHLUNG_KOPF);
    }

    public List<AuszahlungDetail> findAuszahlungenDetails(Integer traegerId, Long auszahlungKopfId) {
        return createQuery(traegerId).from(AUSZAHLUNG_DETAIL)
                                     .where(AUSZAHLUNG_DETAIL.auszahlungKopf.id.eq(auszahlungKopfId))
                                     .list(AUSZAHLUNG_DETAIL);
    }

    public int getCountAuszahlungsKopfCreatedToday(final Integer traegerId, boolean checkPostanWeisungOnly) {
        BooleanExpression where = AUSZAHLUNG_KOPF.erstelltAm.lt(new Date());

        if(checkPostanWeisungOnly) {
            where = where.and(AUSZAHLUNG_KOPF.pmtTpInfCtgyPurpPrtry.eq(POSTANWEISUNG));
        } else {
            where = where.and(AUSZAHLUNG_KOPF.pmtTpInfCtgyPurpPrtry.isNull());
        }

        return createQuery(traegerId).from(AUSZAHLUNG_KOPF).where(where).count();
    }
}
*/

import at.gepardec.wor.recipes.querydsl.testapp.entities.AuszahlungDetail;
import at.gepardec.wor.recipes.querydsl.testapp.entities.AuszahlungKopf;
import at.gepardec.wor.recipes.querydsl.testapp.entities.QAuszahlungDetail;
import at.gepardec.wor.recipes.querydsl.testapp.entities.QAuszahlungKopf;
import com.querydsl.core.types.dsl.BooleanExpression;

public class AuszahlungDAO extends at.sozvers.stp.rgkk.domain.dao.BaseDAO {

    private static final QAuszahlungKopf AUSZAHLUNG_KOPF = QAuszahlungKopf.auszahlungKopf;
    private static final QAuszahlungDetail AUSZAHLUNG_DETAIL = QAuszahlungDetail.auszahlungDetail;

    private static final String POSTANWEISUNG = "CPPP";

    public Long findMaxAuszahlungKopfId(Integer traegerId) {
        Long maxId = createQuery(traegerId).select(AUSZAHLUNG_KOPF.id.max()).from(AUSZAHLUNG_KOPF)
                .fetchOne();
        return maxId == null ? Long.valueOf(0L) : maxId;
    }

    public Long getMaxAuszahlungDetailId(Integer traegerId) {
        return createQuery(traegerId)
                .select(AUSZAHLUNG_DETAIL.id.max())
                .from(AUSZAHLUNG_DETAIL)
                .fetchFirst();
    }

    public List<AuszahlungKopf> findAuszahlungenKoepfe(Integer traegerId, Long minId, Long maxId) {
        return createQuery(traegerId)
                .select(AUSZAHLUNG_KOPF)
                .from(AUSZAHLUNG_KOPF)
                .where(AUSZAHLUNG_KOPF.id.gt(minId)
                        .and(AUSZAHLUNG_KOPF.id.loe(maxId)))
                .fetch();
    }

    public List<AuszahlungDetail> findAuszahlungenDetails(Integer traegerId, Long auszahlungKopfId) {
        return createQuery(traegerId)
                .select(AUSZAHLUNG_DETAIL)
                .from(AUSZAHLUNG_DETAIL)
                .where(AUSZAHLUNG_DETAIL.auszahlungKopf.id.eq(auszahlungKopfId))
                .fetch();
    }

    public int getCountAuszahlungsKopfCreatedToday(final Integer traegerId, boolean checkPostanWeisungOnly) {
        BooleanExpression where = AUSZAHLUNG_KOPF.erstelltAm.lt(new Date());

        if(checkPostanWeisungOnly) {
            where = where.and(AUSZAHLUNG_KOPF.pmtTpInfCtgyPurpPrtry.eq(POSTANWEISUNG));
        } else {
            where = where.and(AUSZAHLUNG_KOPF.pmtTpInfCtgyPurpPrtry.isNull());
        }

        return createQuery(traegerId).selectFrom(AUSZAHLUNG_KOPF).where(where).fetch().size();
    }
}