package com.ultreon.bubbles.common.random;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The renewed and better version of the internal {@link Random}
 * Just added the ability to get seed.
 *
 * @see java.util.Random
 */
@SuppressWarnings({"unused"})
@Deprecated
public class QBRandom extends Random {

    private static final long multiplier = 0x5DEECE66DL;
    private static final long mask = (1L << 48) - 1;
    private final AtomicLong seed;

    /**
     * Creates a new random number generator. This constructor sets
     * the seed of the random number generator to a value very likely
     * to be distinct from any other invocation of this constructor.
     */
    public QBRandom() {
        this(seedUniquifier() ^ System.nanoTime());
    }

    /**
     * Creates a new random number generator using a single {@code long} seed.
     * The seed is the initial value of the internal state of the pseudorandom
     * number generator which is maintained by method {@link #next}.
     *
     * <p>The invocation {@code new QBRandom(seed)} is equivalent to:
     * <pre> {@code
     * QBRandom rnd = new QBRandom();
     * rnd.setSeed(seed);}</pre>
     *
     * @param seed the initial seed
     * @see #setSeed(long)
     */
    public QBRandom(long seed) {
        if (this.getClass() == QBRandom.class)
            this.seed = new AtomicLong(initialScramble(seed));
        else {
            // subclass might have overriden setSeed
            this.seed = new AtomicLong();
            setSeed(seed);
        }
    }

    private static final AtomicLong seedUniquifier
            = new AtomicLong(8682522807148012L);

    private static long seedUniquifier() {
        // L'Ecuyer, "Tables of Linear Congruential Generators of
        // Different Sizes and Good Lattice Structure", 1999
        for (; ; ) {
            long current = seedUniquifier.get();
            long next = current * 181783497276652981L;
            if (seedUniquifier.compareAndSet(current, next))
                return next;
        }
    }

    private static long initialScramble(long seed) {
        return (seed ^ multiplier) & mask;
    }

    /**
     * Get that Seed!
     * Returns the internal seed.
     *
     * @return The internal seed.
     */
    @SuppressWarnings("unused")
    public long getSeed() {
        return seed.get();
    }

    /**
     * The random integer like in <a href="https://www.python.org">Python</a>
     *
     * @param a The minimum
     * @param b The maximum
     * @return a random value between {@code a} and {@code b}
     * @throws IllegalArgumentException if {@code a} is larger than {@code b}.
     */
    public int randInt(int a, int b) throws IllegalArgumentException {
        if (b < a) {
            throw new IllegalArgumentException("Argument a is larger than b");
        }

        return nextInt(b - a) + a;
    }

    /**
     * The random integer like in <a href="https://www.python.org">Python</a>
     *
     * @param min The minimum
     * @param max The maximum
     * @return a random value between {@code a} and {@code b}
     * @throws IllegalArgumentException if {@code a} is larger than {@code b}.
     */
    public float randRange(float min, float max) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("Argument min is larger than max");
        }

//        return nextDouble() * (b - a) + a;
        Random r = new Random();
        return min + (max - min) * r.nextFloat();
    }

    /**
     * The random integer like in <a href="https://www.python.org">Python</a>
     *
     * @param min The minimum
     * @param max The maximum
     * @return a random value between {@code a} and {@code b}
     * @throws IllegalArgumentException if {@code a} is larger than {@code b}.
     */
    public double randRange(double min, double max) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("Argument min is larger than max");
        }

//        return nextDouble() * (b - a) + a;
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    /**
     * The random integer like in <a href="https://www.python.org">Python</a>
     *
     * @param min The minimum
     * @param max The maximum
     * @return a random value between {@code a} and {@code b}
     * @throws IllegalArgumentException if {@code a} is larger than {@code b}.
     */
    public long randLong(long min, long max) throws IllegalArgumentException {
        if (min > max) {
            throw new IllegalArgumentException("Argument min is larger than max");
        }

//        return nextDouble() * (b - a) + a;
        Random r = new Random();
        return (long) ((double) min + ((double) max - (double) min) * r.nextDouble());
    }

    /**
     * Addend
     * <p>
     * The internal state associated with this pseudorandom number generator.
     * (The specs for the methods in this class describe the ongoing
     * computation of this value.)
     */
    private static final long addend = 0xBL;

    private static final double DOUBLE_UNIT = 0x1.0p-53; // 1.0 / (1L << 53)

    // IllegalArgumentException messages
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";
}
