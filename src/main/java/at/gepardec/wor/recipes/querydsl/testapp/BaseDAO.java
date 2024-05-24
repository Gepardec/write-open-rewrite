package at.sozvers.stp.rgkk.domain.dao;

import at.gepardec.wor.recipes.querydsl.testapp.entities.DatabaseManagedEntity;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLTemplates;

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
    private EntityManager em;

    protected JPAQueryFactory createQuery(Integer traegerId) {
        return new JPAQueryFactory(em);
    }

    protected JPASQLQuery<Void> createJPASQLQuery(Integer traegerId) {
        return new JPASQLQuery<Void>(getEntityManager(traegerId), getSqlTemplates());
    }

    protected JPADeleteClause createDeleteClause(Integer traegerId, EntityPath<?> entity) {
        return new JPADeleteClause(getEntityManager(traegerId), entity);
    }

    protected JPAUpdateClause createUpdateClause(Integer traegerId, EntityPath<?> entity) {
        return new JPAUpdateClause(getEntityManager(traegerId), entity);
    }

    protected EntityManager getEntityManager(Integer traegerId) {
        return em;
    }

    /**
     * Creates a new instance of the entity manager for the given {@code traegerId}
     */
    protected EntityManager getNewEntityManager(Integer traegerId) {
        return em
                .getEntityManagerFactory()
                .createEntityManager();
    }

    public <T> T update(Integer traegerId, T entity) {
        T mergedEntity = updateWithoutFlush(traegerId, entity);
        getEntityManager(traegerId).flush();
        return mergedEntity;
    }

    public void update(Integer traegerId, List<? extends DatabaseManagedEntity> entities) {
        for (DatabaseManagedEntity entity : entities) {
            updateWithoutFlush(traegerId, entity);
        }
        getEntityManager(traegerId).flush();
    }

    public <T> T updateWithoutFlush(Integer traegerId, T entity) {
        return getEntityManager(traegerId).merge(entity);
    }


    public <T extends DatabaseManagedEntity> T persist(Integer traegerId, T entity) {
        getEntityManager(traegerId).persist(entity);
        getEntityManager(traegerId).flush();
        return entity;
    }

    public void persist(Integer traegerId, List<? extends DatabaseManagedEntity> entities) {
        for (DatabaseManagedEntity entity : entities) {
            getEntityManager(traegerId).persist(entity);
        }
        getEntityManager(traegerId).flush();
    }

    public void bulkPersist(Integer traegerId, List<? extends DatabaseManagedEntity> entities) {
        if (entities.size() < MAX_IN_LIST_LENGTH) {
            persist(traegerId, entities);
        }else{

            for (DatabaseManagedEntity entity : entities) {
                    persistWithoutFlush(traegerId, entity);
                }
            }
    }

    public <T extends DatabaseManagedEntity> T persistWithoutFlush(Integer traegerId, T entity) {
        getEntityManager(traegerId).persist(entity);
        return entity;
    }



    /**
     * @param entity database managed entity
     */
    public void detach(Integer traegerId, DatabaseManagedEntity entity) {
        getEntityManager(traegerId).detach(entity);
        getEntityManager(traegerId).flush();
    }

    /**
     * Refresh the state of the instance from the database, overwriting changes made to the entity, if any.
     * 
     * @param entity database managed entity
     */
    public void refresh(Integer traegerId, DatabaseManagedEntity entity) {
        getEntityManager(traegerId).refresh(entity);
    }

    /**
     * Returns found entity or null
     */
    public <T> T findById(Integer traegerId, Object id, Class<? extends T> entityClass) {
        if (id == null) {
            throw new RuntimeException(String.format("Id fuer Instanzen von %s darf nicht null sein", entityClass.getSimpleName()));
        }
        return getEntityManager(traegerId).find(entityClass, id);
    }

    /**
     * Locking finding. Can be used as select for update
     */
    public <T> T lockingFindById(Integer traegerId, Object id, Class<? extends T> entityClass) {
        return getEntityManager(traegerId).find(entityClass, id, LockModeType.PESSIMISTIC_WRITE);
    }

    public <T extends DatabaseManagedEntity> void remove(Integer traegerId, T entity) {
        getEntityManager(traegerId).remove(entity);
        getEntityManager(traegerId).flush();
    }

    public void remove(Integer traegerId, List<? extends DatabaseManagedEntity> entities) {
        entities.forEach(e -> getEntityManager(traegerId).remove(e));
        getEntityManager(traegerId).flush();
    }


    protected <T> BooleanExpression eq(final SimpleExpression<T> path, final T right) {
        return right == null ? path.isNull() : path.eq(right);
    }

    protected <T> BooleanExpression nullOrEq(final SimpleExpression<T> path, final T right) {
        return path.isNull()
                   .or(path.eq(right));
    }

    protected <T extends Number & Comparable<?>> BooleanExpression notNullAndGt(final NumberExpression<T> path, final T right) {
        return path.isNotNull()
                   .and(path.gt(right));
    }

    public static String getTableName(Class<?> clazz) {
        if (clazz.getAnnotation(Table.class) != null) {
            return clazz.getAnnotation(Table.class).name();
        }

        String name = "";
        if (clazz.getAnnotation(Entity.class) != null) {
            name = clazz.getAnnotation(Entity.class).name();
        }

        if (name.isBlank()) {
            return clazz.getSimpleName().toUpperCase();
        }

        return name;
    }

    private static SQLTemplates SQL_TEMPLATES = new OracleTemplates();
    public SQLTemplates getSqlTemplates() {
        return SQL_TEMPLATES;
    }

}