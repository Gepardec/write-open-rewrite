package com.gepardec.wor.querydsl;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.TypeUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RefactorTestDAOVisitor extends JavaIsoVisitor<ExecutionContext> {
    Map<String, List<Expression>> argsOfUniqueResultCalls = new HashMap<>();
    private final static String CONSTRUCTOR_AND_SELECT_TEMPLATE = "new JPAQueryFactory(#{any(javax.persistence.EntityManager)})\n.select(%s)\n.from(#{any()})";

    @Override
    public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, ExecutionContext executionContext) {
        // Change method invocations from uniqueResult to fetchOne
        extractParamFromUniqueResultIfRequired(method);
        method = replaceUniqueResultWithFetchOne(method);
        method = replaceCallOfFromMethodWithTemplate(method);
        return super.visitMethodInvocation(method, executionContext);
    }

    private J.@NotNull MethodInvocation replaceCallOfFromMethodWithTemplate(J.MethodInvocation method) {
        if (method.getSimpleName().equals("from") &&
                TypeUtils.isOfClassType(method.getSelect().getType(), "com.querydsl.jpa.impl.JPAQueryFactory") && argsOfUniqueResultCalls.containsKey(method.getId().toString())) {

            //add so many #{any()} as there are arguments in the uniqueResult call
            List<Expression> args = argsOfUniqueResultCalls.get(method.getId().toString());
            String stringOfAny = IntStream.range(0, args.size())
                    .mapToObj(i -> "#{any()}")
                    .collect(Collectors.joining(", "));

            //fill arguments to match all placeholders in the template
            // Create an array of Expression with the required size
            Object[] resultArray = new Expression[args.size() + 2];

            // Set the first element - this is the argument of the constructor
            resultArray[0] = ((J.NewClass) method.getSelect()).getArguments().get(0);

            // Copy all elements from args to the result array starting at index 1
            for (int i = 0; i < args.size(); i++) {
                resultArray[i + 1] = args.get(i);
            }

            // Set the last element - orginally from "from" call
            resultArray[resultArray.length - 1] = method.getArguments().get(0);

            JavaTemplate javaTemplate = JavaTemplate.builder(String.format(CONSTRUCTOR_AND_SELECT_TEMPLATE, stringOfAny))
                    .contextSensitive()
                    .build();

            method = javaTemplate.apply(
                    updateCursor(method),
                    method.getCoordinates().replace(),
                    resultArray
            );
        }
        return method;
    }

    private void extractParamFromUniqueResultIfRequired(J.MethodInvocation method) {
        if (method.getSimpleName().equals("uniqueResult")) {
            // find the if of the call of the "from" method that is called before the uniqueResult in the same call chain. This id is used as key in the map, such that arguments can be later user there
            String id = null;
            J.MethodInvocation current = method;
            while (current.getSelect() instanceof J.MethodInvocation) {
                J.MethodInvocation parent = (J.MethodInvocation) current.getSelect();
                if (parent.getSimpleName().equals("from")) {
                    id = parent.getId().toString();
                    break;
                }
                current = parent;
            }
            if (id != null) {
                argsOfUniqueResultCalls.put(id, method.getArguments());
            }
        }
    }

    private static J.@NotNull MethodInvocation replaceUniqueResultWithFetchOne(J.MethodInvocation method) {
        if (method.getSimpleName().equals("uniqueResult") &&
                TypeUtils.isOfClassType(method.getSelect().getType(), "com.querydsl.jpa.impl.JPAQueryFactory")) {
            method = method.withName(method.getName().withSimpleName("fetchOne")).withArguments(Collections.emptyList());
        }
        return method;
    }
}

