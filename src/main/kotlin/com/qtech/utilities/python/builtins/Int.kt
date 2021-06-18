package com.qtech.utilities.python.builtins

import java.math.BigInteger
import kotlin.Int

class Int : BigInteger {
    constructor(`val`: String?, radix: Int) : super(`val`, radix) {}
    constructor(`val`: String) : super(`val`) {}

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