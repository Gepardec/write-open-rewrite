package com.gepardec.recipes.java.maven;

import com.gepardec.impl.java.maven.addDeclarationsOfUsedTransitiveDependencies.AddDependenciesByTypesVisitor;
import com.gepardec.impl.java.maven.addDeclarationsOfUsedTransitiveDependencies.DependenciesUsedByProject;
import com.gepardec.impl.java.maven.addDeclarationsOfUsedTransitiveDependencies.FindUsedDependenciesVisitor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.ScanningRecipe;
import org.openrewrite.TreeVisitor;

import java.util.LinkedHashSet;
import java.util.Set;

public class AddDeclarationsOfUsedTransitiveDependencies extends ScanningRecipe<Set<DependenciesUsedByProject>> {

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "Add declarations of used transitive dependencies.";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "Dependencies used in the Java-Code of a project are added to it's POM and therefore fixes \"Used undeclared dependency\" warnings of maven.";
    }

    @Override
    public Set<DependenciesUsedByProject> getInitialValue(ExecutionContext ctx) {
        return new LinkedHashSet<>();
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getScanner(Set<DependenciesUsedByProject> acc) {
        return new FindUsedDependenciesVisitor(acc);
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor(Set<DependenciesUsedByProject> acc) {
        return new AddDependenciesByTypesVisitor(acc);
    }
}
