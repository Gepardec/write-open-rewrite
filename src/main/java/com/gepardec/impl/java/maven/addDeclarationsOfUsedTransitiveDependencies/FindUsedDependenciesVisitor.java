package com.gepardec.impl.java.maven.addDeclarationsOfUsedTransitiveDependencies;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FindUsedDependenciesVisitor extends JavaIsoVisitor<ExecutionContext> {
    private final Set<DependenciesUsedByProject> acc;

    public FindUsedDependenciesVisitor(Set<DependenciesUsedByProject> acc) {
        this.acc = acc;
    }

    @Override
    public J.CompilationUnit visitCompilationUnit(J.CompilationUnit cu, ExecutionContext executionContext) {
        DependenciesUsedByProject newProject = extractUsedDependencies(cu);

        retrieveMatchingProject(acc, newProject)
                .ifPresentOrElse(
                        project -> mergeProject(project, newProject),
                        () -> acc.add(newProject));

        return super.visitCompilationUnit(cu, executionContext);
    }

    private static boolean mergeProject(DependenciesUsedByProject project, DependenciesUsedByProject usedDependencies) {
        return project.getUsedDependenciesGav().addAll(usedDependencies.getUsedDependenciesGav());
    }

    private static Optional<DependenciesUsedByProject> retrieveMatchingProject(Set<DependenciesUsedByProject> projects, DependenciesUsedByProject project) {
        return projects.stream().filter(project::equals).findFirst();
    }

    private static @NotNull DependenciesUsedByProject extractUsedDependencies(J.CompilationUnit cu) {
        Set<String> usedDependencies = extractUsedDependenciesGav(cu);
        return buildUsedDependenciesByProject(cu, usedDependencies);
    }

    private static @NotNull DependenciesUsedByProject buildUsedDependenciesByProject(J.CompilationUnit cu, Set<String> usedDependencies) {
        JavaProject.Publication publication = cu.getMarkers().findFirst(JavaProject.class).get().getPublication();

        return new DependenciesUsedByProject(
                publication.getArtifactId(),
                publication.getGroupId(),
                usedDependencies);
    }

    private static @NotNull Set<String> extractUsedDependenciesGav(J.CompilationUnit cu) {
        var gavToTypes = extractGavToTypes(cu);
        Set<JavaType> usedTypes = extractUsedTypes(cu);
        return extractDependenciesOfUsedTypes(usedTypes, gavToTypes);
    }

    private static @NotNull Set<String> extractDependenciesOfUsedTypes(Set<JavaType> usedTypes, Map<String, List<JavaType.FullyQualified>> gavToTypes) {
        return usedTypes.stream()
                .map(FindUsedDependenciesVisitor::normalizeType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(t -> getGavFromType(t, gavToTypes))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private static @NotNull Set<JavaType> extractUsedTypes(J.CompilationUnit cu) {
        Set<JavaType> usedTypes =  new LinkedHashSet<>(cu.getTypesInUse().getTypesInUse());
        usedTypes.addAll(extractStaticImportedTypes(cu));
        return usedTypes;
    }

    private static @NotNull Map<String, List<JavaType.FullyQualified>> extractGavToTypes(J.CompilationUnit cu) {
        return cu.getMarkers().findFirst(JavaSourceSet.class).get().getGavToTypes();
    }

    private static @NotNull Set<JavaType> extractStaticImportedTypes(J.CompilationUnit cu) {
        return cu.getImports().stream()
                .filter(J.Import::isStatic)
                .map(J.Import::getTypeName)
                .map(JavaType::buildType)
                .collect(Collectors.toSet());
    }

    public static Optional<String> getGavFromType(JavaType type, Map<String, List<JavaType.FullyQualified>> gavToTypes) {
        Set<String> gavSet = gavToTypes.keySet();
        for (String gav : gavSet) {
            List<JavaType.FullyQualified> types = gavToTypes.get(gav);
            if (types.stream().anyMatch(t -> TypeUtils.isOfType(t, type))) {
                return Optional.of(gav);
            }
        }
        return Optional.empty();
    }

    public static Optional<JavaType> normalizeType(JavaType type) {
        if (type instanceof JavaType.Primitive) {
            return Optional.empty();
        }

        if (type instanceof JavaType.Parameterized) {
            JavaType.Parameterized parameterized = (JavaType.Parameterized) type;
            return Optional.of(parameterized.getType());
        }

        return Optional.of(type);
    }
}
