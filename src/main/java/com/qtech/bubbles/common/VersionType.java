package com.qtech.bubbles.common;

import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum VersionType implements Translatable {
    ALPHA("alpha"), BETA("beta"), RELEASE("release"), CANDIDATE("rc");

    private final String name;

    VersionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toRepresentation() {
        return "VersionType{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    @Override
    public String getTranslationId() {
        return "misc.version_type." + name;
    }
}
