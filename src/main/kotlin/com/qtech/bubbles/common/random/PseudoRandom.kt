@file:Suppress("unused")

package com.qtech.bubbles.common.random

import org.bson.ByteBuf
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * A pseudo random number generator, which does not
 * produce a series of numbers, but each number determined by
 * some input (and independent of earlier numbers).
 *
 *
 * This is based on the
 * [Blum Blum Shub
 * algorithm](http://en.wikipedia.org/wiki/Blum_Blum_Shub), combined with the SHA-1 message digest to get the
 * right index.
 *
 *
 *
 * Inspired by the question
 * [Algorithm
 * for generating a three dimensional random number space](http://stackoverflow.com/q/6586042/600500) on
 * Stack Overflow, and the answer from woliveirajr.
 */
open class PseudoRandom @JvmOverloads constructor(seed: BigInteger = BigInteger.valueOf(System.currentTimeMillis())) {
    /**
     * Get the current seed of the PseudoRandom.
     *
     * @return the current seed of the PseudoRandom.
     */
    var seed: BigInteger = seed
        set(value) {
            field = initializeSeed(value)
        }

    abstract class Range<T : Number?> {
        abstract fun clip(bigVal: BigInteger): T
    }

    /**
     * An instance of this class represents a range of
     * integer numbers, both endpoints inclusive.
     */
    class IntegerRange
    /**
     * @param min minimum value of the range.
     * @param max maximum value of the range.
     */(val min: Int, val max: Int) : Range<Int>() {
        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an integer between min and max, inclusive.
         */
        override fun clip(bigVal: BigInteger): Int {
            val modulus = BigInteger.valueOf(max + 1L - min)
            return (min + bigVal.mod(modulus).toLong()).toInt()
        }
    }

    /**
     * An instance of this class represents a range of
     * 64 bit integer numbers, both endpoints inclusive.
     */
    class LongRange
    /**
     * @param min minimum value of the range.
     * @param max maximum value of the range.
     */(val min: Long, val max: Long) : Range<Long>() {
        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an 64-bit integer between min and max, inclusive.
         */
        override fun clip(bigVal: BigInteger): Long {
            val modulus = BigInteger.valueOf(max + 1L - min)
            return min + bigVal.mod(modulus).toLong()
        }
    }

    /**
     * An instance of this class represents a range of
     * BigInteger numbers, both endpoints inclusive.
     */
    class BigIntegerRange
    /**
     * @param min minimum value of the range.
     * @param max maximum value of the range.
     */(val min: BigInteger, val max: BigInteger) : Range<BigInteger>() {
        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an BigInteger between min and max, inclusive.
         */
        override fun clip(bigVal: BigInteger): BigInteger {
            val modulus = max.add(BigInteger("1")).subtract(min)
            return min.add(bigVal.mod(modulus))
        }
    }

    /**
     * An instance of this class represents a range of
     * float numbers, both endpoints inclusive.
     */
    class FloatRange
    /**
     * @param min minimum value of the range.
     * @param max maximum value of the range.
     */(val min: Float, val max: Float) : Range<Float>() {
        /**
         * Clips a (positive) BigDecimal to the range represented
         * by this object.
         *
         * @return an float between min and max, inclusive.
         */
        override fun clip(bigVal: BigInteger): Float {
//            System.out.println("max=" + max);
//            System.out.println("min=" + min);
            val modulus = BigDecimal.valueOf(max + 1.0 - min)
            //            System.out.println("mod=" + modulus);
            return (min + mod(bigVal, modulus).toDouble()).toFloat()
        }
    }

    /**
     * An instance of this class represents a range of
     * 64 bit integer numbers, both endpoints inclusive.
     */
    class DoubleRange
    /**
     * @param min minimum value of the range.
     * @param max maximum value of the range.
     */(val min: Double, val max: Double) : Range<Double>() {
        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an 64-bit integer between min and max, inclusive.
         */
        override fun clip(bigVal: BigInteger): Double {
//            System.out.println("max=" + max);
//            System.out.println("min=" + min);
            val modulus = BigDecimal.valueOf(max + 1.0 - min)
            //            System.out.println("mod=" + modulus);
            return min + mod(bigVal, modulus).toDouble()
        }
    }

    /**
     * An instance of this class represents a range of
     * BigDecimal numbers, both endpoints inclusive.
     */
    class BigDecimalRange
    /**
     * @param min minimum value of the range.
     * @param max maximum value of the range.
     */(val min: BigDecimal, val max: BigDecimal) : Range<BigDecimal>() {
        /**
         * Clips a (positive) BigDecimal to the range represented
         * by this object.
         *
         * @return an BigDecimal between min and max, inclusive.
         */
        override fun clip(bigVal: BigInteger): BigDecimal {
            val modulus = max.add(BigDecimal("1")).subtract(min)
            return min.add(mod(bigVal, modulus))
        }
    }

    /**
     * the modular square of the seed value.
     */
    private var s0: BigInteger? = null

    /**
     * The MessageDigest used to convert input data
     * to an index for our PRNG.
     */
    private var md: MessageDigest

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: BigDecimal) : this(seed.unscaledValue())

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: Any) : this(seed.hashCode())

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: Array<Any?>?) : this(Arrays.hashCode(seed))

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: Thread) : this(seed.id)

    /**
     * Creates a new PseudoRandom instance, seeded by the given seed.
     */
    constructor(seed: ByteArray) : this(BigInteger(1, seed))

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: ByteBuf) : this(seed.array())

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: InputStream) : this(seed.readAllBytes())

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: ByteBuffer) : this(seed.array())

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    constructor(seed: CharArray?) : this(String(seed!!))

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a string.
     */
    constructor(seed: String) : this(seed.toByteArray())

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a byte.
     */
    constructor(seed: Byte) : this(BigInteger.valueOf(seed.toLong()))

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a short.
     */
    constructor(seed: Short) : this(BigInteger.valueOf(seed.toLong()))

    /**
     * Creates a new PseudoRandom instance,
     * seeded by an integer.
     */
    constructor(seed: Int) : this(BigInteger.valueOf(seed.toLong()))

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a long.
     */
    constructor(seed: Long) : this(BigInteger.valueOf(seed))

    /**
     * Transforms the initial seed into some value that is
     * usable by the generator. (This is completely deterministic.)
     */
    private fun initializeSeed(proposal: BigInteger): BigInteger {
        // we want our seed be big enough so s^2 > M.
        var s = proposal
        while (s.bitLength() <= M.bitLength() / 2) {
            s = s.shiftLeft(10)
        }
        // we want gcd(s, M) = 1
        while (M.gcd(s) != BigInteger.ONE) {
            s = s.add(BigInteger.ONE)
        }
        // we save s_0 = s^2 mod M
        s0 = s.multiply(s).mod(M)

        return proposal
    }

    /**
     * calculates `x_k = r.clip( s_k )`.
     */
    private fun <T : Number?> calculate(r: Range<T>, k: BigInteger): T {
        val exp = TWO.modPow(k, lambdaM)
        val sk = s0!!.modPow(exp, M)
        return r.clip(sk)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Int, max: Int, input: ByteArray): Int {
        return getNumber(IntegerRange(min, max), input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Int, max: Int, vararg input: Int): Int {
        return getNumber(IntegerRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Int, max: Int, vararg input: Long): Int {
        return getNumber(IntegerRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Int, max: Int, vararg input: BigInteger): Int {
        return getNumber(IntegerRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Long, max: Long, input: ByteArray): Long {
        return getNumber(LongRange(min, max), input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Long, max: Long, vararg input: Int): Long {
        return getNumber(LongRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Long, max: Long, vararg input: Long): Long {
        return getNumber(LongRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Long, max: Long, vararg input: BigInteger): Long {
        return getNumber(LongRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigInteger, max: BigInteger, input: ByteArray): BigInteger {
        return getNumber(BigIntegerRange(min, max), input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigInteger, max: BigInteger, vararg input: Int): BigInteger {
        return getNumber(BigIntegerRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigInteger, max: BigInteger, vararg input: Long): BigInteger {
        return getNumber(BigIntegerRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigInteger, max: BigInteger, vararg input: BigInteger): BigInteger {
        return getNumber(BigIntegerRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Float, max: Float, input: ByteArray): Float {
        return getNumber(FloatRange(min, max), input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Float, max: Float, vararg input: Int): Float {
        return getNumber(FloatRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Float, max: Float, vararg input: Long): Float {
        return getNumber(FloatRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Float, max: Float, vararg input: BigInteger): Float {
        return getNumber(FloatRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Double, max: Double, input: ByteArray): Double {
        return getNumber(DoubleRange(min, max), input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Double, max: Double, vararg input: Int): Double {
        return getNumber(DoubleRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Double, max: Double, vararg input: Long): Double {
        return getNumber(DoubleRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: Double, max: Double, vararg input: BigInteger): Double {
        return getNumber(DoubleRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigDecimal, max: BigDecimal, input: ByteArray): BigDecimal {
        return getNumber(BigDecimalRange(min, max), input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigDecimal, max: BigDecimal, vararg input: Int): BigDecimal {
        return getNumber(BigDecimalRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigDecimal, max: BigDecimal, vararg input: Long): BigDecimal {
        return getNumber(BigDecimalRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun getNumber(min: BigDecimal, max: BigDecimal, vararg input: BigInteger): BigDecimal {
        return getNumber(BigDecimalRange(min, max), *input)
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun <T : Number?> getNumber(r: Range<T>, input: ByteArray): T {
        var dig: ByteArray?
        synchronized(md) {
            md.reset()
            md.update(input)
            dig = md.digest()
        }
        return calculate(r, BigInteger(1, dig))
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun <T : Number?> getNumber(r: Range<T>, vararg input: Int): T {
        var dig: ByteArray?
        synchronized(md) {
            md.reset()
            for (i in input) {
                md.update(
                    byteArrayOf(
                        (i shr 24).toByte(), (i shr 16).toByte(),
                        (i shr 8).toByte(), i.toByte()
                    )
                )
            }
            dig = md.digest()
        }
        return calculate(r, BigInteger(1, dig))
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun <T : Number?> getNumber(r: Range<T>, vararg input: Long): T {
        var dig: ByteArray?
        synchronized(md) {
            md.reset()
            for (i in input) {
                md.update(
                    byteArrayOf(
                        (i shr 56).toByte(), (i shr 48).toByte(), (i shr 40).toByte(), (i shr 32).toByte(), (i shr 24).toByte(), (i shr 16).toByte(),
                        (i shr 8).toByte(), i.toByte()
                    )
                )
            }
            dig = md.digest()
        }
        return calculate(r, BigInteger(1, dig))
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    fun <T : Number?> getNumber(r: Range<T>, vararg input: BigInteger): T {
        var dig: ByteArray?
        synchronized(md) {
            md.reset()
            for (i in input) {
                md.update(i.toByteArray())
            }
            dig = md.digest()
        }
        return calculate(r, BigInteger(1, dig))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: BigDecimal) {
        this.setSeed(seed.unscaledValue())
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: Any) {
        this.setSeed(seed.hashCode())
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: Array<Any?>?) {
        this.setSeed(Arrays.hashCode(seed))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: Thread) {
        this.setSeed(seed.id)
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: ByteArray) {
        this.setSeed(BigInteger(1, seed))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: ByteBuf) {
        this.setSeed(seed.array())
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    @Throws(IOException::class)
    fun setSeed(seed: InputStream) {
        this.setSeed(seed.readAllBytes())
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: ByteBuffer) {
        this.setSeed(seed.array())
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: CharArray?) {
        this.setSeed(String(seed!!))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: String) {
        this.setSeed(seed.toByteArray())
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: Byte) {
        this.setSeed(BigInteger.valueOf(seed.toLong()))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: Short) {
        this.setSeed(BigInteger.valueOf(seed.toLong()))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: Int) {
        this.setSeed(BigInteger.valueOf(seed.toLong()))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun setSeed(seed: Long) {
        this.setSeed(BigInteger.valueOf(seed))
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    fun resetSeed() {
        this.setSeed(BigInteger.valueOf(System.currentTimeMillis()))
    }

    companion object {
        private fun mod(a: BigDecimal, m: BigDecimal): BigDecimal {
            val result = a.remainder(m)
            return if (result.signum() >= 0) result else result.add(m)
        }

        private fun mod(a: BigInteger, m: BigDecimal): BigDecimal {
//        System.out.println(new BigDecimal(a));
//        System.out.println(m);
            val result = BigDecimal(a).remainder(m)
            //        System.out.println(result);
            return if (result.signum() >= 0) result else result.add(m)
        }

        /**
         * M = p * q =
         * 510458987753305598818664158496165644577818051165198667838943583049282929852810917684801057127 * 1776854827630587786961501611493551956300146782768206322414884019587349631246969724030273647
         *
         *
         * A big number, composed of two large primes.
         */
        private val M = BigInteger(
            "90701151669688414188903413878244126959941449657" +
                    "82009133495922185615411523457607691918744187485" +
                    "10492533485214517262505932675573506751182663319" +
                    "285975046876611245165890299147416689632169"
        )

        /**
         * λ(M) = lcm(p-1, q-1)
         *
         *
         * The value of λ(M), where λ is the Carmichael function.
         * This is the lowest common multiple of the predecessors of
         * the two factors of M.
         */
        private val lambdaM = BigInteger(
            "53505758348442070944517069391220634799707248289" +
                    "10045667479610928077057617288038459593720911813" +
                    "73249762745139558184229125081884863164923576762" +
                    "05906844204771187443203120630003929150698"
        )

        /**
         * The number 2 as a BigInteger, for use in the calculations.
         */
        private val TWO = BigInteger.valueOf(2)

        /**
         * Test method.
         */
        @JvmStatic
        fun main(test: Array<String>) {
            val pr = PseudoRandom("Hallo Welt".toByteArray())
            val r = IntegerRange(10, 30)
            for (i in 0..9) {
                println("x(" + i + ") = " + pr.getNumber(r, i))
            }
            for (i in 0..4) {
                for (j in 0..4) {
                    println(
                        "x(" + i + ", " + j + ") = " +
                                pr.getNumber(r, i, j)
                    )
                }
            }
            for (i in 0..4) {
                var j = 0
                while (j < 5) {
                    val k = 0
                    while (j < 5) {
                        println(
                            "x(" + i + ", " + j + ", " + k + ") = " +
                                    pr.getNumber(r, i, j, k)
                        )
                        j++
                    }
                    j++
                }
            }
            for (i in 0..4) {
                var j = 0
                while (j < 5) {
                    val k = 0
                    while (j < 5) {
                        val l = 0
                        while (j < 5) {
                            println(
                                "x(" + i + ", " + j + ", " + k + ", " + l + ") = " +
                                        pr.getNumber(r, i, j, k, l)
                            )
                            j++
                        }
                        j++
                    }
                    j++
                }
            }
            // to show that it really is deterministic:
            for (i in 0..9) {
                println("x(" + i + ") = " + pr.getNumber(r, i))
            }
        }
    }
    /**
     * Creates a new PseudoRandom instance, using the given seed.
     */
    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    init {
        try {
            md = MessageDigest.getInstance("SHA-1")
        } catch (ex: NoSuchAlgorithmException) {
            throw RuntimeException(ex)
        }
        initializeSeed(seed)
    }
}