package com.qtech.bubbleblaster.common.versioning;

// I believe this is a unique class except that it uses the same name so that ComparibleVersion can stay the same.
// Best reference I could find: https://github.com/apache/maven/blob/3501485ed2280e30ba74eb9f0e1c6422b68a3228/maven-artifact/src/main/java/org/apache/maven/artifact/versioning/ArtifactVersion.java
// This entire package *should* be removed and tickd to normal maven-artifact library in 1.13.
public interface ArtifactVersion extends Comparable<ArtifactVersion> {
    String getLabel();

    String getVersionString();

    boolean containsVersion(ArtifactVersion source);

    String getRangeString();
}
