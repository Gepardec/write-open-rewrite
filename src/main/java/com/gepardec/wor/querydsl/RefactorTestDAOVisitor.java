package com.gepardec.wor.querydsl;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Tree;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.*;
import org.openrewrite.marker.Markers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RefactorTestDAOVisitor extends JavaIsoVisitor<ExecutionContext> {
    Map<String, List<Expression>> argsOfUniqueResultCalls = new HashMap<>();
    Set<String> idsOfCountCalls = new HashSet<>();
    private static final Map<String, String> FORCE_IMPORT_REPLACEMENTS = Map.of(
            "com.mysema.query.types.expr.BooleanExpression", "com.querydsl.core.types.dsl.BooleanExpression"
    );
    private final static String SELECT_THEN_FROM_TEMPLATE = "createQuery(#{any()})\n.select(%s)\n.from(#{any()})";
    private final static String SELECT_WITH_FROM_TEMPLATE = "createQuery(#{any()})\n.selectFrom(#{any()})";

    @Override
    public J.Import visitImport(J.Import _import, ExecutionContext executionContext) {
        String importClassString = _import.getQualid().toString();
        if (FORCE_IMPORT_REPLACEMENTS.containsKey(importClassString)) {
            _import = new J.Import(Tree.randomId(),
                    Space.format("\n"),
                    Markers.EMPTY,
                    new JLeftPadded(Space.EMPTY, false, Markers.EMPTY),
                    TypeTree.build(FORCE_IMPORT_REPLACEMENTS.get(importClassString)).withPrefix(Space.SINGLE_SPACE),
                    null);
        }
        return _import;
    }

    @Override
    public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, ExecutionContext executionContext) {
        // Change method invocations from uniqueResult to fetchOne
        extractParamFromSelectsAndMarkCorrespondingFromMethods(method);
        method = replaceOldMethodsCallsWithFetch(method);
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
            resultArray[0] = ((J.MethodInvocation) method.getSelect()).getArguments().get(0);

            // Copy all elements from args to the result array starting at index 1
            for (int i = 0; i < args.size(); i++) {
                resultArray[i + 1] = args.get(i);
            }

            // Set the last element - orginally from "from" call
            resultArray[resultArray.length - 1] = method.getArguments().get(0);

            JavaTemplate javaTemplate = JavaTemplate.builder(String.format(SELECT_THEN_FROM_TEMPLATE, stringOfAny))
                    .contextSensitive()
                    .build();

            method = javaTemplate.apply(
                    updateCursor(method),
                    method.getCoordinates().replace(),
                    resultArray
            );
        }

        if (method.getSimpleName().equals("from") &&
                TypeUtils.isOfClassType(method.getSelect().getType(), "com.querydsl.jpa.impl.JPAQueryFactory") && idsOfCountCalls.contains(method.getId().toString())) {

            JavaTemplate javaTemplate = JavaTemplate.builder(SELECT_WITH_FROM_TEMPLATE)
                    .contextSensitive()
                    .build();

            method = javaTemplate.apply(
                    updateCursor(method),
                    method.getCoordinates().replace(),
                    ((J.MethodInvocation) method.getSelect()).getArguments().get(0),
                    method.getArguments().get(0)
            );
        }

        return method;
    }

    private void extractParamFromSelectsAndMarkCorrespondingFromMethods(J.MethodInvocation method) {
        boolean isSelectMethod = method.getSimpleName().equals("uniqueResult") || method.getSimpleName().equals("singleResult") || method.getSimpleName().equals("list");
        boolean isCountMethod = method.getSimpleName().equals("count");
        if (isSelectMethod || isCountMethod) {
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
            if (id != null && isSelectMethod) {
                argsOfUniqueResultCalls.put(id, method.getArguments());
            }
            if (id != null && isCountMethod) {
                idsOfCountCalls.add(id);
            }
        }
    }

    private static J.@NotNull MethodInvocation replaceOldMethodsCallsWithFetch(J.MethodInvocation method) {
        if (method.getSimpleName().equals("uniqueResult")) {
            method = method.withName(method.getName().withSimpleName("fetchOne")).withArguments(Collections.emptyList());
        }
        if (method.getSimpleName().equals("singleResult")) {
            method = method.withName(method.getName().withSimpleName("fetchFirst")).withArguments(Collections.emptyList());
        }
        if (method.getSimpleName().equals("list")) {
            method = method.withName(method.getName().withSimpleName("fetch")).withArguments(Collections.emptyList());
        }
        if (method.getSimpleName().equals("count")) {
            method = method.withName(method.getName().withSimpleName("fetch().size")).withArguments(Collections.emptyList());
        }
        return method;
    }
}

