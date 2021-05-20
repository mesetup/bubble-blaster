package com.qtech.bubbleblaster.common.versioning;

import com.qtech.bubbleblaster.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DefaultArtifactVersion implements ArtifactVersion {
    private ComparableVersion comparableVersion;
    private String label;
    private boolean unbounded;
    private final VersionRange range;

    public DefaultArtifactVersion(String versionNumber) {
        comparableVersion = new ComparableVersion(versionNumber);
        range = VersionRange.createFromVersion(versionNumber, this);
    }

    public DefaultArtifactVersion(String label, VersionRange range) {
        this.label = label;
        this.range = range;
    }

    public DefaultArtifactVersion(String label, String version) {
        this(version);
        this.label = label;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return ((DefaultArtifactVersion) obj).containsVersion(this);
    }

    @Override
    public int compareTo(ArtifactVersion o) {
        return unbounded ? 0 : this.comparableVersion.compareTo(((DefaultArtifactVersion) o).comparableVersion);
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean containsVersion(ArtifactVersion source) {
        if (source.getLabel() != null && !source.getLabel().equals(getLabel())) {
            return false;
        }
        if (unbounded) {
            return true;
        }
        if (range != null) {
            return range.containsVersion(source);
        } else {
            return false;
        }
    }

    @Override
    public String getVersionString() {
        return comparableVersion == null ? "unknown" : comparableVersion.toString();
    }

    @Override
    public String getRangeString() {
        return range == null ? "any" : range.toString();
    }

    @Override
    public String toString() {
        if (label == null) {
            return getVersionString();
        }
        return label + (unbounded ? "" : "@" + range);
    }

    public VersionRange getRange() {
        return range;
    }
}