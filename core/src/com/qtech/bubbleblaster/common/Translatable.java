package com.qtech.bubbleblaster.common;

import com.qtech.bubbleblaster.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbleblaster.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface Translatable {
    String getTranslationId();
}
