package com.gepardec.impl.java.maven.addDeclarationsOfUsedTransitiveDependencies;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.maven.tree.GroupArtifact;
import org.openrewrite.maven.tree.GroupArtifactVersion;
import org.openrewrite.maven.tree.ResolvedPom;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AddDependenciesByTypesVisitor extends MavenIsoVisitor<ExecutionContext> {
    private static final XPathMatcher DEPENDENCIES_MATCHER = new XPathMatcher("/project/dependencies");
    private static final String DEPENDENCY_TEMPLATE = "<dependency>\n" +
            "            <groupId>%s</groupId>\n" +
            "            <artifactId>%s</artifactId>\n" +
            "            <version>%s</version>\n" +
            "        </dependency>";

    private final Set<DependenciesUsedByProject> acc;

    public AddDependenciesByTypesVisitor(Set<DependenciesUsedByProject> acc) {
        this.acc = acc;
    }

    @Override
    public Xml.Document visitDocument(Xml.Document document, ExecutionContext executionContext) {
        if (getDependenciesUsedByProject().isEmpty()) {
            return document;
        }

        if (document.getRoot().getChild("dependencies").isEmpty()) {
            Xml.Tag root = document.getRoot();
            Xml.Tag dependencies = Xml.Tag.build("<dependencies>\n\t</dependencies>").withPrefix("\n\t");
            List<Xml.Tag> children = ListUtils.concat(root.getChildren(), dependencies);
            document = document.withRoot(root.withContent(children));
        }
        return super.visitDocument(document, executionContext);
    }

    @Override
    public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
        Xml.Tag dependencies = super.visitTag(tag, executionContext);

        if (!DEPENDENCIES_MATCHER.matches(getCursor())) {
            return dependencies;
        }

        return getDependenciesUsedByProject()
                .map(usedDependencies -> addMissingDependencies(dependencies, usedDependencies))
                .orElse(dependencies);
    }

    private static Xml.Tag addMissingDependencies(Xml.Tag dependencies, DependenciesUsedByProject dependenciesUsedByProject) {
        for (String usedDependency : dependenciesUsedByProject.getUsedDependenciesGav()) {;
            dependencies = ensureDependencyExists(dependencies, usedDependency);
        }
        return dependencies;
    }

    private static Xml.@NotNull Tag ensureDependencyExists(Xml.Tag dependencies, String usedDependency) {
        GroupArtifactVersion gav = buildGavObject(usedDependency);
        if (hasDependency(dependencies, gav)) {
            return addDependency(dependencies, createDependencyTag(gav));
        }
        return dependencies;
    }

    private static Xml.@NotNull Tag addDependency(Xml.Tag dependencies, Xml.Tag dependencyTag) {
        return dependencies.withContent(ListUtils.concat(dependencies.getChildren(), dependencyTag));
    }

    private static GroupArtifactVersion buildGavObject(String gav) {
        String[] gavParts = gav.split(":");
        return new GroupArtifactVersion(gavParts[0], gavParts[1], gavParts[2]);
    }

    private static boolean hasDependency(Xml.Tag dependencies, GroupArtifactVersion gav) {
        return dependencies.getChildren().stream()
                .filter(dependency -> isDependencyGroupArtifact(dependency, gav.asGroupArtifact()))
                .findFirst().isEmpty();
    }

    private static boolean isDependencyGroupArtifact(Xml.Tag dependency, GroupArtifact ga) {
        return dependency.getChildValue("groupId").map(ga.getGroupId()::equals).orElse(false) &&
                dependency.getChildValue("artifactId").map(ga.getArtifactId()::equals).orElse(false);
    }

    private static Xml.@NotNull Tag createDependencyTag(GroupArtifactVersion gav) {
        return Xml.Tag
                .build(createDependencyTagSource(gav))
                .withPrefix("\n\t\t");
    }

    private static @NotNull String createDependencyTagSource(GroupArtifactVersion gav) {
        return DEPENDENCY_TEMPLATE.formatted(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }


    private @NotNull Optional<DependenciesUsedByProject> getDependenciesUsedByProject() {
        ResolvedPom pom = getResolutionResult().getPom();
        return acc.stream()
                .filter(d -> d.getGroupId().equals(pom.getGroupId()))
                .filter(d -> d.getArtifactId().equals(pom.getArtifactId()))
                .findFirst();
    }

}
