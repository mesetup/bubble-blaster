package com.qtech.bubbles.common

import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import javax.annotation.ParametersAreNonnullByDefault

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
interface Translatable {
    val translationId: String
}