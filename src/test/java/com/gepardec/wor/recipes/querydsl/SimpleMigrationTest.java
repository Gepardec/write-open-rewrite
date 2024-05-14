package com.gepardec.wor.recipes.querydsl;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.test.TypeValidation;

import static org.openrewrite.java.Assertions.java;

class SimpleMigrationTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("com.gepardec.wor.recipes.querydsl.UpdateImports")
          .parser(JavaParser.fromJavaVersion().classpath(JavaParser.runtimeClasspath()));
    }

    @DocumentExample
    @Test
    void replacesPackages() {
        rewriteRun(
          //language=java
          java(
            """
              package at.gepardec.wor.recipes.querydsl.testapp;
                                                     
              import at.gepardec.wor.recipes.querydsl.testapp.entities.QTestEntity;
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
              }
              """,
            """
              package at.gepardec.wor.recipes.querydsl.testapp;
                                                     
              import at.gepardec.wor.recipes.querydsl.testapp.entities.QTestEntity;
              import com.querydsl.jpa.impl.JPAQueryFactory;
              
              import javax.enterprise.context.RequestScoped;
              import javax.inject.Inject;
              import javax.persistence.EntityManager;
              
              @RequestScoped
              public class TestDAO {
                  @Inject
                  private EntityManager em;
              
                  public String getName() {
                      return new JPAQueryFactory(em)
                              .select(QTestEntity.testEntity.name)
                              .from(QTestEntity.testEntity)
                              .where(QTestEntity.testEntity.id.eq(1L))
                              .fetchOne();
                  }
              }
              """
          )
        );
    }
}
