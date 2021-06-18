package com.qtech.bubbles.common.random

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.stream.Collectors

/**
 * RNG Class, for randomizing or get random numbers from arguments.
 *
 * @author Quinten Jungblut
 */
open class Rng
/**
 * RNG Constructor.
 *
 * @param random   the [PseudoRandom] instance.
 * @param index    the RNG-Index.
 * @param subIndex the RNG Sub-index.
 */(
    /**
     * @return get random number generator.
     */
    val random: PseudoRandom,
    /**
     * @return get rng-index.
     */
    val index: Int,
    /**
     * @return get rng sub-index.
     */
    val subIndex: Int
) {

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Int, max: Int, vararg input: Int): Int {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex)
        list.add(0, index)
        return random.getNumber(min, max, *list.stream().mapToInt { i: Int? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Int, max: Int, vararg input: Long): Int {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex.toLong())
        list.add(0, index.toLong())
        return random.getNumber(min, max, *list.stream().mapToLong { i: Long? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Int, max: Int, vararg input: BigInteger): Int {
        val list = mutableListOf(*input)
        list.add(0, BigInteger.valueOf(subIndex.toLong()))
        list.add(0, BigInteger.valueOf(index.toLong()))
        return random.getNumber(min, max, *list.toTypedArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Long, max: Long, vararg input: Int): Long {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex)
        list.add(0, index)
        return random.getNumber(min, max, *list.stream().mapToInt { i: Int? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Long, max: Long, vararg input: Long): Long {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex.toLong())
        list.add(0, index.toLong())
        return random.getNumber(min, max, *list.stream().mapToLong { i: Long? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Long, max: Long, vararg input: BigInteger): Long {
        val list = mutableListOf(*input)
        list.add(0, BigInteger.valueOf(subIndex.toLong()))
        list.add(0, BigInteger.valueOf(index.toLong()))
        return random.getNumber(min, max, *list.toTypedArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: BigInteger, max: BigInteger, vararg input: Int): BigInteger {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex)
        list.add(0, index)
        return random.getNumber(min, max, *list.stream().mapToInt { i: Int? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: BigInteger, max: BigInteger, vararg input: Long): BigInteger {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex.toLong())
        list.add(0, index.toLong())
        return random.getNumber(min, max, *list.stream().mapToLong { i: Long? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: BigInteger, max: BigInteger, vararg input: BigInteger): BigInteger {
        val list = mutableListOf(*input)
        list.add(0, BigInteger.valueOf(subIndex.toLong()))
        list.add(0, BigInteger.valueOf(index.toLong()))
        return random.getNumber(min, max, *list.toTypedArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Float, max: Float, vararg input: Int): Float {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex)
        list.add(0, index)
        return random.getNumber(min, max, *list.stream().mapToInt { i: Int? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Float, max: Float, vararg input: Long): Float {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex.toLong())
        list.add(0, index.toLong())
        return random.getNumber(min, max, *list.stream().mapToLong { i: Long? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Float, max: Float, vararg input: BigInteger): Float {
        val list = mutableListOf(*input)
        list.add(0, BigInteger.valueOf(subIndex.toLong()))
        list.add(0, BigInteger.valueOf(index.toLong()))
        return random.getNumber(min, max, *list.toTypedArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Double, max: Double, vararg input: Int): Double {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex)
        list.add(0, index)
        return random.getNumber(min, max, *list.stream().mapToInt { i: Int? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Double, max: Double, vararg input: Long): Double {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex.toLong())
        list.add(0, index.toLong())
        return random.getNumber(min, max, *list.stream().mapToLong { i: Long? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: Double, max: Double, vararg input: BigInteger): Double {
        val list = mutableListOf(*input)
        list.add(0, BigInteger.valueOf(subIndex.toLong()))
        list.add(0, BigInteger.valueOf(index.toLong()))
        return random.getNumber(min, max, *list.toTypedArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: BigDecimal, max: BigDecimal, vararg input: Int): BigDecimal {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex)
        list.add(0, index)
        return random.getNumber(min, max, *list.stream().mapToInt { i: Int? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: BigDecimal, max: BigDecimal, vararg input: Long): BigDecimal {
        val list = Arrays.stream(input).boxed().collect(Collectors.toList())
        list.add(0, subIndex.toLong())
        list.add(0, index.toLong())
        return random.getNumber(min, max, *list.stream().mapToLong { i: Long? -> i!! }.toArray())
    }

    /**
     * @param min   minimum value.
     * @param max   maximum value.
     * @param input the randomizing arguments.
     * @return the random integer.
     */
    fun getNumber(min: BigDecimal, max: BigDecimal, vararg input: BigInteger): BigDecimal {
        val list = mutableListOf(*input)
        list.add(0, BigInteger.valueOf(subIndex.toLong()))
        list.add(0, BigInteger.valueOf(index.toLong()))
        return random.getNumber(min, max, *list.toTypedArray())
    }
}