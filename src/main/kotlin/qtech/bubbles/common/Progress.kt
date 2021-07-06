package qtech.bubbles.common

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.ToString

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
open class Progress(var progress: Int, val max: Int) {

    constructor(max: Int) : this(0, max)

    fun increment() {
        if (progress + 1 <= max) {
            progress++
        } else {
            throw IllegalStateException("Progress increment at end: " + (progress + 1) + ", max: " + max)
        }
    }
}