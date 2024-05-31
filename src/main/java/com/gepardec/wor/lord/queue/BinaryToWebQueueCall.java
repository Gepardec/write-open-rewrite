package com.gepardec.wor.lord.queue;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;

@Value
@EqualsAndHashCode(callSuper = false)
public class BinaryToWebQueueCall extends Recipe {

    @Option(displayName = "Fully qualified type of binary DTO",
            description = "The fully qualified type of the binary DTO.",
            example = "com.gepardec.wor.lord.dto.BinaryDto")
    String binaryDtoFullyQualifiedType;

    @Option(displayName = "Fully qualified type of web DTO",
            description = "The fully qualified type of the web DTO.",
            example = "com.gepardec.wor.lord.dto.WebDto")
    String webDtoFullyQualifiedType;

    @Override
    public String getDisplayName() {
        return "Change binary queue calls to web queue calls";
    }

    @Override
    public String getDescription() {
        return "Change binary queue call to web queue call.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new BinaryQueueToWebVisitor(binaryDtoFullyQualifiedType, webDtoFullyQualifiedType);
    }
}
