package com.qtech.utilities.python.builtins

import java.math.BigDecimal
import java.math.MathContext
import kotlin.Int

class Float : BigDecimal {
    constructor(`val`: String?) : super(`val`) {}
    constructor(`val`: String?, mc: MathContext?) : super(`val`, mc) {}
    constructor(`val`: Double) : super(`val`) {}
    constructor(`val`: Double, mc: MathContext?) : super(`val`, mc) {}
    constructor(`val`: Int) : super(`val`) {}
    constructor(`val`: Int, mc: MathContext?) : super(`val`, mc) {}
    constructor(`val`: Long) : super(`val`) {}
    constructor(`val`: Long, mc: MathContext?) : super(`val`, mc) {}

    override fun toByte(): Byte {
        return toInt().toByte()
    }

    override fun toChar(): Char {
        return toInt().toChar()
    }

    override fun toShort(): Short {
        return toInt().toShort()
    }
}