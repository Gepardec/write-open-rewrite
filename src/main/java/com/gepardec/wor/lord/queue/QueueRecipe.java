package com.gepardec.wor.lord.queue;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;

@Value
@EqualsAndHashCode(callSuper = false)
public class QueueRecipe extends Recipe {

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
        return new BinaryQueueToWebVisitor();
    }
}
