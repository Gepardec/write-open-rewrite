package at.gepardec.wor.recipes.querydsl.testapp;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.EntityPathBase;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Table;
import java.util.List;

@Named("base")
public class BaseDAO {

    public static final Integer MAX_IN_LIST_LENGTH = 900;
    public static final String JAVAX_PERSISTENCE_FETCHGRAPH = "javax.persistence.fetchgraph";

    @Inject
    protected EntityManager em;

    protected JPAQuery createQuery(Integer traegerId) {
        return new JPAQuery(getEntityManager(traegerId));
    }

    protected EntityManager getEntityManager(Integer traegerId) {
        return em;
    }

}