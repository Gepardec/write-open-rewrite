package com.gepardec.wor.lord.queue;

import com.gepardec.wor.lord.util.LSTUtil;
import org.openrewrite.Cursor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;

public class BinaryQueueToWebVisitor extends JavaIsoVisitor<ExecutionContext> {

    //language=java
    private static final String NEW_CODE_TEMPLATE = """
            #{} serviceRequest = objectFactory.create#{}();
            serviceRequest.setArg0(#{});
            XmlRequestWrapper<#{}> xmlRequestWrapper = new XmlRequestWrapper<>(#{}, serviceRequest);
            data = marshallDto(xmlRequestWrapper);
            """;

    private static final String DTO_TEMPLATE = "#{}";

    @Override
    public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, ExecutionContext executionContext) {
        method = super.visitMethodDeclaration(method, executionContext);

        var dto = LSTUtil.extractStatementsOfType(method.getParameters(), J.VariableDeclarations.class).stream().filter(p -> p.getType().toString().endsWith("Dto")).findFirst();
        var dtoTemplate = LSTUtil.javaTemplateOf("#{} request", "at.sozvers.stp.lgkk.a02.laaaumv4.Laqaumv4");

        if (dto.isPresent()) {
            method = dtoTemplate.apply(updateCursor(method), dto.orElse(null).getCoordinates().replace(), "Laqaumv4 request");
        }

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

        System.out.println("Method declaration: " + formatter);

        return template.apply(
                updateCursor(method),
                formatter.orElse(null).getCoordinates().replace(),
                "ExecuteService", "ExecuteService", "request", "ExecuteService", "SERVICE_NAME"
        );
    }



    @Override
    public J.Try visitTry(J.Try _try, ExecutionContext executionContext) {
        super.visitTry(_try, executionContext);
        return null;
    }
}
