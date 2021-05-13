package com.qsoftware.bubbles.common;

import com.qsoftware.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface Translatable {
    String getTranslationId();
}
