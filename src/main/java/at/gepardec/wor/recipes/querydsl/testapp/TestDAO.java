package at.gepardec.wor.recipes.querydsl.testapp;

import at.gepardec.wor.recipes.querydsl.testapp.entities.QTestEntity;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@RequestScoped
public class TestDAO {
    @Inject
    private EntityManager em;

    public String getName() {
        return new JPAQuery(em)
                .from(QTestEntity.testEntity)
                .where(QTestEntity.testEntity.id.eq(1L))
                .uniqueResult(QTestEntity.testEntity.name);
    }

    public Tuple getIdAndName() {
        return new JPAQuery(em)
                .from(QTestEntity.testEntity)
                .where(QTestEntity.testEntity.id.eq(1L))
                .uniqueResult(QTestEntity.testEntity.id, QTestEntity.testEntity.name);
    }
}