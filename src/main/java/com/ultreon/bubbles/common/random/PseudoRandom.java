package com.ultreon.bubbles.common.random;

import org.bson.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * A pseudo random number generator, which does not
 * produce a series of numbers, but each number determined by
 * some input (and independent of earlier numbers).
 * <p>
 * This is based on the
 * <a href="http://en.wikipedia.org/wiki/Blum_Blum_Shub">Blum Blum Shub
 * algorithm</a>, combined with the SHA-1 message digest to get the
 * right index.
 * </p>
 * <p>
 * Inspired by the question
 * <a href="http://stackoverflow.com/q/6586042/600500">Algorithm
 * for generating a three dimensional random number space</a> on
 * Stack Overflow, and the answer from woliveirajr.
 */
@SuppressWarnings("unused")
public class PseudoRandom {
    private BigInteger seed;

    protected static abstract class Range<T extends Number> {
        abstract T clip(BigInteger bigVal);
    }

    private static BigDecimal mod(BigDecimal a, BigDecimal m) {
        BigDecimal result = a.remainder(m);
        return (result.signum() >= 0 ? result : result.add(m));
    }

    private static BigDecimal mod(BigInteger a, BigDecimal m) {
//        System.out.println(new BigDecimal(a));
//        System.out.println(m);
        BigDecimal result = new BigDecimal(a).remainder(m);
//        System.out.println(result);
        return (result.signum() >= 0 ? result : result.add(m));
    }

    /**
     * An instance of this class represents a range of
     * integer numbers, both endpoints inclusive.
     */
    public static final class IntegerRange extends Range<Integer> {

        public final int min;
        public final int max;

        /**
         * @param min minimum value of the range.
         * @param max maximum value of the range.
         */
        public IntegerRange(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an integer between min and max, inclusive.
         */
        final Integer clip(BigInteger bigVal) {
            BigInteger modulus = BigInteger.valueOf(max + 1L - min);
            return (int) (min + bigVal.mod(modulus).longValue());
        }
    }

    /**
     * An instance of this class represents a range of
     * 64 bit integer numbers, both endpoints inclusive.
     */
    public static final class LongRange extends Range<Long> {

        public final long min;
        public final long max;

        /**
         * @param min minimum value of the range.
         * @param max maximum value of the range.
         */
        public LongRange(long min, long max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an 64-bit integer between min and max, inclusive.
         */
        final Long clip(BigInteger bigVal) {
            BigInteger modulus = BigInteger.valueOf(max + 1L - min);
            return min + bigVal.mod(modulus).longValue();
        }
    }

    /**
     * An instance of this class represents a range of
     * BigInteger numbers, both endpoints inclusive.
     */
    public static final class BigIntegerRange extends Range<BigInteger> {

        public final BigInteger min;
        public final BigInteger max;

        /**
         * @param min minimum value of the range.
         * @param max maximum value of the range.
         */
        public BigIntegerRange(BigInteger min, BigInteger max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an BigInteger between min and max, inclusive.
         */
        final BigInteger clip(BigInteger bigVal) {
            BigInteger modulus = max.add(new BigInteger("1")).subtract(min);
            return min.add(bigVal.mod(modulus));
        }
    }

    /**
     * An instance of this class represents a range of
     * floateger numbers, both endpofloats inclusive.
     */
    public static final class FloatRange extends Range<Float> {

        public final float min;
        public final float max;

        /**
         * @param min minimum value of the range.
         * @param max maximum value of the range.
         */
        public FloatRange(float min, float max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Clips a (positive) BigDecimal to the range represented
         * by this object.
         *
         * @return an floateger between min and max, inclusive.
         */
        final Float clip(BigInteger bigVal) {
//            System.out.println("max=" + max);
//            System.out.println("min=" + min);
            BigDecimal modulus = BigDecimal.valueOf(max + 1d - min);
//            System.out.println("mod=" + modulus);
            return (float) (min + mod(bigVal, modulus).doubleValue());
        }
    }

    /**
     * An instance of this class represents a range of
     * 64 bit integer numbers, both endpoints inclusive.
     */
    public static final class DoubleRange extends Range<Double> {

        public final double min;
        public final double max;

        /**
         * @param min minimum value of the range.
         * @param max maximum value of the range.
         */
        public DoubleRange(double min, double max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Clips a (positive) BigInteger to the range represented
         * by this object.
         *
         * @return an 64-bit integer between min and max, inclusive.
         */
        final Double clip(BigInteger bigVal) {
//            System.out.println("max=" + max);
//            System.out.println("min=" + min);
            BigDecimal modulus = BigDecimal.valueOf(max + 1d - min);
//            System.out.println("mod=" + modulus);
            return min + mod(bigVal, modulus).doubleValue();
        }
    }

    /**
     * An instance of this class represents a range of
     * BigDecimal numbers, both endpoints inclusive.
     */
    public static final class BigDecimalRange extends Range<BigDecimal> {

        public final BigDecimal min;
        public final BigDecimal max;

        /**
         * @param min minimum value of the range.
         * @param max maximum value of the range.
         */
        public BigDecimalRange(BigDecimal min, BigDecimal max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Clips a (positive) BigDecimal to the range represented
         * by this object.
         *
         * @return an BigDecimal between min and max, inclusive.
         */
        final BigDecimal clip(BigInteger bigVal) {
            BigDecimal modulus = max.add(new BigDecimal("1")).subtract(min);
            return min.add(mod(bigVal, modulus));
        }
    }

    /**
     * M = p * q =
     * 510458987753305598818664158496165644577818051165198667838943583049282929852810917684801057127 * 1776854827630587786961501611493551956300146782768206322414884019587349631246969724030273647
     * <p>
     * A big number, composed of two large primes.
     */
    private static final BigInteger M =
            new BigInteger("90701151669688414188903413878244126959941449657" +
                    "82009133495922185615411523457607691918744187485" +
                    "10492533485214517262505932675573506751182663319" +
                    "285975046876611245165890299147416689632169");

    /**
     * ??(M) = lcm(p-1, q-1)
     * <p>
     * The value of ??(M), where ?? is the Carmichael function.
     * This is the lowest common multiple of the predecessors of
     * the two factors of M.
     */
    private static final BigInteger lambdaM =
            new BigInteger("53505758348442070944517069391220634799707248289" +
                    "10045667479610928077057617288038459593720911813" +
                    "73249762745139558184229125081884863164923576762" +
                    "05906844204771187443203120630003929150698");

    /**
     * The number 2 as a BigInteger, for use in the calculations.
     */
    private static final BigInteger TWO = BigInteger.valueOf(2);


    /**
     * the modular square of the seed value.
     */
    private BigInteger s_0;

    /**
     * The MessageDigest used to convert input data
     * to an index for our PRNG.
     */
    private final MessageDigest md;

    /**
     * Creates a new PseudoRandom instance, using the given seed.
     */
    public PseudoRandom(BigInteger seed) {
        try {
            this.md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        initializeSeed(seed);
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(BigDecimal seed) {
        this(seed.unscaledValue());
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(Object seed) {
        this(seed.hashCode());
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(Object[] seed) {
        this(Arrays.hashCode(seed));
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(Thread seed) {
        this(seed.getId());
    }

    /**
     * Creates a new PseudoRandom instance, seeded by the given seed.
     */
    public PseudoRandom(byte[] seed) {
        this(new BigInteger(1, seed));
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(ByteBuf seed) {
        this(seed.array());
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(InputStream seed) throws IOException {
        this(seed.readAllBytes());
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(ByteBuffer seed) {
        this(seed.array());
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom(char[] seed) {
        this(new String(seed));
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a string.
     */
    public PseudoRandom(String seed) {
        this(seed.getBytes());
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a byte.
     */
    public PseudoRandom(byte seed) {
        this(BigInteger.valueOf(seed));
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a short.
     */
    public PseudoRandom(short seed) {
        this(BigInteger.valueOf(seed));
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by an integer.
     */
    public PseudoRandom(int seed) {
        this(BigInteger.valueOf(seed));
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by a long.
     */
    public PseudoRandom(long seed) {
        this(BigInteger.valueOf(seed));
    }

    /**
     * Creates a new PseudoRandom instance,
     * seeded by the current system time.
     */
    public PseudoRandom() {
        this(BigInteger.valueOf(System.currentTimeMillis()));
    }

    /**
     * Transforms the initial seed into some value that is
     * usable by the generator. (This is completely deterministic.)
     */
    private void initializeSeed(BigInteger proposal) {
        this.seed = proposal;

        // we want our seed be big enough so s^2 > M.
        BigInteger s = proposal;
        while (s.bitLength() <= M.bitLength() / 2) {
            s = s.shiftLeft(10);
        }
        // we want gcd(s, M) = 1
        while (!M.gcd(s).equals(BigInteger.ONE)) {
            s = s.add(BigInteger.ONE);
        }
        // we save s_0 = s^2 mod M
        this.s_0 = s.multiply(s).mod(M);
    }

    /**
     * calculates {@code x_k = r.clip( s_k )}.
     */
    private <T extends Number> T calculate(Range<T> r, BigInteger k) {
        BigInteger exp = TWO.modPow(k, lambdaM);
        BigInteger s_k = s_0.modPow(exp, M);
        return r.clip(s_k);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public int getNumber(int min, int max, byte[] input) {
        return getNumber(new IntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public int getNumber(int min, int max, int... input) {
        return getNumber(new IntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public int getNumber(int min, int max, long... input) {
        return getNumber(new IntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public int getNumber(int min, int max, BigInteger... input) {
        return getNumber(new IntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public long getNumber(long min, long max, byte[] input) {
        return getNumber(new LongRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public long getNumber(long min, long max, int... input) {
        return getNumber(new LongRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public long getNumber(long min, long max, long... input) {
        return getNumber(new LongRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public long getNumber(long min, long max, BigInteger... input) {
        return getNumber(new LongRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigInteger getNumber(BigInteger min, BigInteger max, byte[] input) {
        return getNumber(new BigIntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigInteger getNumber(BigInteger min, BigInteger max, int... input) {
        return getNumber(new BigIntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigInteger getNumber(BigInteger min, BigInteger max, long... input) {
        return getNumber(new BigIntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigInteger getNumber(BigInteger min, BigInteger max, BigInteger... input) {
        return getNumber(new BigIntegerRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public float getNumber(float min, float max, byte[] input) {
        return getNumber(new FloatRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public float getNumber(float min, float max, int... input) {
        return getNumber(new FloatRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public float getNumber(float min, float max, long... input) {
        return getNumber(new FloatRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public float getNumber(float min, float max, BigInteger... input) {
        return getNumber(new FloatRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public double getNumber(double min, double max, byte[] input) {
        return getNumber(new DoubleRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public double getNumber(double min, double max, int... input) {
        return getNumber(new DoubleRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public double getNumber(double min, double max, long... input) {
        return getNumber(new DoubleRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public double getNumber(double min, double max, BigInteger... input) {
        return getNumber(new DoubleRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigDecimal getNumber(BigDecimal min, BigDecimal max, byte[] input) {
        return getNumber(new BigDecimalRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigDecimal getNumber(BigDecimal min, BigDecimal max, int... input) {
        return getNumber(new BigDecimalRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigDecimal getNumber(BigDecimal min, BigDecimal max, long... input) {
        return getNumber(new BigDecimalRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public BigDecimal getNumber(BigDecimal min, BigDecimal max, BigInteger... input) {
        return getNumber(new BigDecimalRange(min, max), input);
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    @NotNull
    public <T extends Number> T getNumber(Range<T> r, byte[] input) {
        byte[] dig;
        synchronized (md) {
            md.reset();
            md.update(input);
            dig = md.digest();
        }
        return calculate(r, new BigInteger(1, dig));
    }


    /**
     * returns a number given by a range, determined by the given input.
     */
    @NotNull
    public <T extends Number> T getNumber(Range<T> r, int... input) {
        byte[] dig;
        synchronized (md) {
            md.reset();
            for (int i : input) {
                md.update(new byte[]{(byte) (i >> 24), (byte) (i >> 16),
                        (byte) (i >> 8), (byte) (i)});
            }
            dig = md.digest();
        }
        return calculate(r, new BigInteger(1, dig));
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    @NotNull
    public <T extends Number> T getNumber(Range<T> r, long... input) {
        byte[] dig;
        synchronized (md) {
            md.reset();
            for (long i : input) {
                md.update(new byte[]{(byte) (i >> 56), (byte) (i >> 48), (byte) (i >> 40), (byte) (i >> 32), (byte) (i >> 24), (byte) (i >> 16),
                        (byte) (i >> 8), (byte) (i)});
            }
            dig = md.digest();
        }
        return calculate(r, new BigInteger(1, dig));
    }

    /**
     * returns a number given by a range, determined by the given input.
     */
    public <T extends Number> T getNumber(Range<T> r, BigInteger... input) {
        byte[] dig;
        synchronized (md) {
            md.reset();
            for (BigInteger i : input) {
                md.update(i.toByteArray());
            }
            dig = md.digest();
        }
        return calculate(r, new BigInteger(1, dig));
    }

    /**
     * Test method.
     */
    public static void main(String[] test) {
        PseudoRandom pr = new PseudoRandom("Hallo Welt".getBytes());

        IntegerRange r = new IntegerRange(10, 30);
        for (int i = 0; i < 10; i++) {
            System.out.println("x(" + i + ") = " + pr.getNumber(r, i));
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println("x(" + i + ", " + j + ") = " +
                        pr.getNumber(r, i, j));
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; j < 5; j++) {
                    System.out.println("x(" + i + ", " + j + ", " + k + ") = " +
                            pr.getNumber(r, i, j, k));
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; j < 5; j++) {
                    for (int l = 0; j < 5; j++) {
                        System.out.println("x(" + i + ", " + j + ", " + k + ", " + l + ") = " +
                                pr.getNumber(r, i, j, k, l));
                    }
                }
            }
        }
        // to show that it really is deterministic:
        for (int i = 0; i < 10; i++) {
            System.out.println("x(" + i + ") = " + pr.getNumber(r, i));
        }
    }

    public void setSeed(BigInteger seed) {
        initializeSeed(seed);
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(BigDecimal seed) {
        this.setSeed(seed.unscaledValue());
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(Object seed) {
        this.setSeed(seed.hashCode());
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(Object[] seed) {
        this.setSeed(Arrays.hashCode(seed));
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(Thread seed) {
        this.setSeed(seed.getId());
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(byte[] seed) {
        this.setSeed(new BigInteger(1, seed));
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(ByteBuf seed) {
        this.setSeed(seed.array());
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(InputStream seed) throws IOException {
        this.setSeed(seed.readAllBytes());
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(ByteBuffer seed) {
        this.setSeed(seed.array());
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(char[] seed) {
        this.setSeed(new String(seed));
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(String seed) {
        this.setSeed(seed.getBytes());
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(byte seed) {
        this.setSeed(BigInteger.valueOf(seed));
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(short seed) {
        this.setSeed(BigInteger.valueOf(seed));
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(int seed) {
        this.setSeed(BigInteger.valueOf(seed));
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void setSeed(long seed) {
        this.setSeed(BigInteger.valueOf(seed));
    }

    /**
     * Sets the seed of the PseudoRandom
     */
    public void resetSeed() {
        this.setSeed(BigInteger.valueOf(System.currentTimeMillis()));
    }

    /**
     * Get the current seed of the PseudoRandom.
     *
     * @return the current seed of the PseudoRandom.
     */
    public BigInteger getSeed() {
        return seed;
    }
}