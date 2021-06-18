package com.qtech.bubbles.annotation

import java.lang.annotation.ElementType
import javax.annotation.Nonnull
import javax.annotation.meta.TypeQualifierDefault

@MustBeDocumented
@Nonnull
@TypeQualifierDefault(ElementType.METHOD)
@Retention(AnnotationRetention.RUNTIME)
annotation class MethodsReturnNonnullByDefault 