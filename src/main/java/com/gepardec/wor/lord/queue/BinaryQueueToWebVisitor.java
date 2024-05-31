package com.gepardec.wor.lord.queue;

import com.gepardec.wor.lord.dto.visitors.transform.ObjectFactoryCreator;
import com.gepardec.wor.lord.util.LSTUtil;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.AddImport;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.RemoveImport;
import org.openrewrite.java.RemoveUnusedImports;
import org.openrewrite.java.search.FindMissingTypes;
import org.openrewrite.java.tree.J;

// TODO: Parameterize recipe for binary dto type and web dto type
public class BinaryQueueToWebVisitor extends JavaIsoVisitor<ExecutionContext> {

    private static final MethodMatcher SET_DTO_MATCHER = new MethodMatcher("*..Laqaumv4Record setDto(..)");

    private static final String NEW_CODE_TEMPLATE = """
            #{} serviceRequest = objectFactory.create#{}();
            serviceRequest.setArg0(#{});
            XmlRequestWrapper<#{}> xmlRequestWrapper = new XmlRequestWrapper<>(#{}, serviceRequest);
            data = MessageMarshaller.marshallDto(xmlRequestWrapper);
            """;

    private final String binaryDtoFullyQualifiedType;
    private final String webDtoFullyQualifiedType;

    public BinaryQueueToWebVisitor(String binaryDtoFullyQualifiedType, String webDtoFullyQualifiedType) {
        this.binaryDtoFullyQualifiedType = binaryDtoFullyQualifiedType;
        this.webDtoFullyQualifiedType = webDtoFullyQualifiedType;
    }

    @Override
    public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, ExecutionContext executionContext) {
        method = super.visitMethodDeclaration(method, executionContext);

        var dto = LSTUtil.extractStatementsOfType(method.getParameters(), J.VariableDeclarations.class).stream().filter(p -> p.getType().toString().endsWith("Dto")).findFirst();
        var dtoTemplate = LSTUtil.javaTemplateOf("#{} #{}", webDtoFullyQualifiedType);

        if (dto.isPresent()) {
            method = dtoTemplate.apply(updateCursor(method), method.getCoordinates().replaceParameters(),
                    "Laqaumv4",
                    dto.get().getVariables().get(0).getSimpleName());
            doAfterVisit(new RemoveImport<>(binaryDtoFullyQualifiedType, true));
        }

        maybeAddImport(webDtoFullyQualifiedType);

        var declarations = LSTUtil.extractStatementsOfType(method.getBody().getStatements(), J.VariableDeclarations.class);

        var formatter = declarations.stream().filter(d -> d.getType().toString().endsWith("TmMagnaxMessageFormatter")).findFirst();
        if (formatter.isEmpty()) {
            return method;
        }

        var template = LSTUtil.javaTemplateOf(
                NEW_CODE_TEMPLATE,
                "at.sozvers.stp.lgkk.a02.laaaumv4.ExecuteService",
                "com.gepardec.wor.lord.stubs.XmlRequestWrapper"
        );

        // TODO: Replace doAfterVisit with maybeAddImport/maybeRemoveImport
        doAfterVisit(new RemoveImport<>("com.gepardec.wor.lord.stubs.TmMagnaxMessageFormatter", true));
        doAfterVisit(new AddImport<>("com.gepardec.wor.lord.stubs.MessageMarshaller", null, false));
        maybeAddImport("at.sozvers.stp.lgkk.a02.laaaumv4.ExecuteService");
        maybeAddImport("com.gepardec.wor.lord.stubs.XmlRequestWrapper");

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
            doAfterVisit(new RemoveImport<>("com.gepardec.wor.lord.stubs.Laqaumv4Record", true));
            return null;
        }
        return variableDeclarations;
    }

    @Override
    public J.Try visitTry(J.Try _try, ExecutionContext executionContext) {
        _try = super.visitTry(_try, executionContext);
        boolean hasXplFormatException = _try.getCatches().stream().anyMatch(c -> c.getParameter().getType().toString().endsWith("XplFormatException"));
        if (!hasXplFormatException) {
            return _try;
        }

        // Remove imports and the whole try-catch-statement
        doAfterVisit(new RemoveImport<>("com.gepardec.wor.lord.stubs.SystemErrorException", true));
        doAfterVisit(new RemoveImport<>("com.gepardec.wor.lord.stubs.XplFormatException", true));
        return null;
    }
}
