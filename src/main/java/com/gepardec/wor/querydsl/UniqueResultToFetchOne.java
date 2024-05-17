package com.gepardec.wor.querydsl;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;

import java.util.List;

public class UniqueResultToFetchOne extends Recipe {
    @Override
    public String getDisplayName() {
        // language=markdown
        return "Change call-chain ending with .uniqueResult() by .fetchOne(...)";
    }

    @Override
    public String getDescription() {
        return "Change call-chain ending with .uniqueResult() by .fetchOne(...).";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            MethodMatcher matcher = new MethodMatcher("com.mysema.query.Projectable uniqueResult(..)", true);
            JavaTemplate template = JavaTemplate.builder("select(#{any()})").build();

            @Override
            public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, ExecutionContext executionContext) {
                System.out.println("method invocation: " + method);
                if (matcher.matches(method)) {
                    System.out.println("--> GOTCHA!");
                    List<Expression> oldArgs = method.getArguments();
                    return method.withName(method.getName().withSimpleName("fetchOne"))
                            .withArguments(List.of());
//                    List<Expression> args = method.getArguments();
//                    return template.apply(
//                            updateCursor(method),
//                            method.getCoordinates().replace(),
//                            args.get(0));
                }
                return super.visitMethodInvocation(method, executionContext);
            }

//            @Override
//            public Expression visitExpression(Expression expression, ExecutionContext executionContext) {
//                System.out.println("expression: " + expression);
//                return super.visitExpression(expression, executionContext);
//            }
        };

    }
}
