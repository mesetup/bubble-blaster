package com.qtech.bubbles.common

import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import javax.annotation.ParametersAreNonnullByDefault

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
enum class VersionType(val text: String) : Translatable {
    ALPHA("alpha"), BETA("beta"), RELEASE("release"), CANDIDATE("rc");

    override fun toString(): String {
        return text
    }

    fun toRepresentation(): String {
        return "VersionType{" +
                "name='" + text + '\'' +
                '}'
    }

    override val translationId: String
        get() = "misc.version_type.$text"
}