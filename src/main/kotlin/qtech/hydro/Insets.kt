package qtech.hydro

import java.awt.Insets

class Insets
/**
 * Creates and initializes a new `Insets` object with the
 * specified top, left, bottom, and right insets.
 *
 * @param top    the inset from the top.
 * @param left   the inset from the left.
 * @param bottom the inset from the bottom.
 * @param right  the inset from the right.
 */
    (top: Int, left: Int, bottom: Int, right: Int) : Insets(top, left, bottom, right) {
    fun shrink(amount: Int) {
        top -= amount
        left -= amount
        bottom -= amount
        right -= amount
    }
}