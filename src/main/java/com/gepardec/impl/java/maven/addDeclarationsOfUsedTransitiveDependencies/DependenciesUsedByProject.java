package com.gepardec.impl.java.maven.addDeclarationsOfUsedTransitiveDependencies;

import java.util.Objects;
import java.util.Set;

public class DependenciesUsedByProject {
    private final String artifactId;

    private final String groupId;

    private final Set<String> usedDependenciesGav;

    public DependenciesUsedByProject(String artifactId, String groupId, Set<String> usedDependenciesGav) {
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.usedDependenciesGav = usedDependenciesGav;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public Set<String> getUsedDependenciesGav() {
        return usedDependenciesGav;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependenciesUsedByProject that = (DependenciesUsedByProject) o;
        return Objects.equals(artifactId, that.artifactId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactId, groupId);
    }
}
