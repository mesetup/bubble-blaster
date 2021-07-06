package qtech.bubbles.common

import qtech.bubbles.annotation.FieldsAreNonnullByDefault
import qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import javax.annotation.ParametersAreNonnullByDefault

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
interface Translatable {
    val translationId: String
}