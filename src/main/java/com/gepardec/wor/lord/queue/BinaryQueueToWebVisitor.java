package com.gepardec.wor.lord.queue;

import com.gepardec.wor.lord.dto.visitors.transform.ObjectFactoryCreator;
import com.gepardec.wor.lord.util.LSTUtil;
import org.openrewrite.Cursor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;

public class BinaryQueueToWebVisitor extends JavaIsoVisitor<ExecutionContext> {

    private static final MethodMatcher SET_DTO_MATCHER = new MethodMatcher("*..Laqaumv4Record setDto(..)");

    private static final String NEW_CODE_TEMPLATE = """
            #{} serviceRequest = objectFactory.create#{}();
            serviceRequest.setArg0(#{});
            XmlRequestWrapper<#{}> xmlRequestWrapper = new XmlRequestWrapper<>(#{}, serviceRequest);
            data = marshallDto(xmlRequestWrapper);
            """;

    @Override
    public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, ExecutionContext executionContext) {
        method = super.visitMethodDeclaration(method, executionContext);

        var dto = LSTUtil.extractStatementsOfType(method.getParameters(), J.VariableDeclarations.class).stream().filter(p -> p.getType().toString().endsWith("Dto")).findFirst();
        var dtoTemplate = LSTUtil.javaTemplateOf("#{} #{}", "at.sozvers.stp.lgkk.a02.laaaumv4.Laqaumv4");

        if (dto.isPresent()) {
            method = dtoTemplate.apply(updateCursor(method), method.getCoordinates().replaceParameters(),
                    "Laqaumv4",
                                dto.get().getVariables().get(0).getSimpleName());
        }

        maybeAddImport("at.sozvers.stp.lgkk.a02.laaaumv4.Laqaumv4");

        var declerations = LSTUtil.extractStatementsOfType(method.getBody().getStatements(), J.VariableDeclarations.class);

        var formatter = declerations.stream().filter(d -> d.getType().toString().endsWith("TmMagnaxMessageFormatter")).findFirst();
        if (formatter.isEmpty()) {
            return method;
        }

        var template = LSTUtil.javaTemplateOf(
                NEW_CODE_TEMPLATE,
                "at.sozvers.stp.lgkk.a02.laaaumv4.ExecuteService",
                "com.gepardec.wor.lord.stubs.XmlRequestWrapper"
        );

        maybeAddImport("at.sozvers.stp.lgkk.a02.laaaumv4.ExecuteService");

        System.out.println("Method declaration: " + formatter);
        doAfterVisit(new ObjectFactoryCreator("objectFactory", "at.sozvers.stp.lgkk.a02.laaaumv4"));
        return template.apply(
                updateCursor(method),
                formatter.orElse(null).getCoordinates().replace(),
                "ExecuteService", "ExecuteService", "request", "ExecuteService", "SERVICE_NAME"
        );

    }

    @Override
    public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, ExecutionContext context) {
        method = super.visitMethodInvocation(method, context);
        if (SET_DTO_MATCHER.matches(method)) {
            return null;
        }
        return method;
    }

    @Override
    public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations variableDeclarations, ExecutionContext context) {
        variableDeclarations = super.visitVariableDeclarations(variableDeclarations, context);

        if (variableDeclarations.getTypeExpression().toString().endsWith("Laqaumv4Record")) {
            return null;
        }
        return variableDeclarations;
    }

    @Override
    public J.Try visitTry(J.Try _try, ExecutionContext executionContext) {
        super.visitTry(_try, executionContext);
        return null;
    }
}
