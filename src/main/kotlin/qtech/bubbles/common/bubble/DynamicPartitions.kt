package qtech.bubbles.common.bubble

import qtech.bubbles.common.runnables.Applier
import qtech.bubbles.core.exceptions.ValueExistsException
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.math3.exception.OutOfRangeException
import java.util.concurrent.CopyOnWriteArrayList

/**
 * This class is used for dynamically change ranges or get values from an index (based of all ranges merged).
 * One problem: it can cause performance issues. But, so far currently known is this the fastest method.
 *
 * @param <T> the type to use for the partition value.
</T> */
class DynamicPartitions<T> {
    private var sizes: MutableList<Double> = CopyOnWriteArrayList()
    val values: MutableList<T> = CopyOnWriteArrayList()

    @get:Synchronized
    var totalSize = 0.0

    /**
     * Adds a partition along with the size and value.
     *
     * @param size  the size.
     * @param value the value.
     * @return the partition index of the new partition.
     * @throws ValueExistsException as the exception it says: if the value already exists.
     */
    @Throws(ValueExistsException::class)
    fun add(size: Double, value: T): Int {
        if (values.contains(value)) throw ValueExistsException()
        sizes.add(size)
        values.add(value)
        totalSize += size
        return sizes.lastIndexOf(size)
    }

    /**
     * Clears all partitions.
     *
     * *In case of emergency.*
     */
    fun clear() {
        sizes.clear()
        values.clear()
        totalSize = 0.0
    }

    /**
     * Inserts a partition at the given index along with the size and value.
     *
     * @param index the partition index.
     * @param size  the size.
     * @param value the value.
     * @return the index.
     */
    fun insert(index: Int, size: Double, value: T): Int {
        sizes.add(index, size)
        values.add(index, value)
        totalSize += size
        return index
    }

    /**
     * Returns the size of the partition at the given index.
     *
     * @param index the partition index.
     * @return the size.
     */
    fun getSize(index: Int): Double {
        return sizes[index]
    }

    /**
     * Removes the partition at the given index.
     *
     * @param index the partition index.
     */
    fun remove(index: Int) {
        totalSize -= sizes[index]
        sizes.removeAt(index)
        values.removeAt(index)
    }

    /**
     * Returns a range from the ‘partition’ index.
     *
     * @param index the index.
     * @return the range at the given index.
     * @throws NullPointerException if the index is out of range.
     */
    fun getRange(index: Int): Range {
        var range: Range? = null
        var currentSize = 0.0
        for (i in sizes.indices) {
            val newSize = currentSize + sizes[i]
            if (i == index) {
                range = Range(currentSize, newSize)
            }
            currentSize = newSize
        }
        if (range == null) {
            throw NullPointerException()
        }
        return range
    }

    /**
     * Returns value based on the item index from all partitions merged..
     *
     * @param drIndex the index based on all ranges.
     * @return the value.
     */
    fun getValue(drIndex: Double): T? {
        if (!(0.0 <= drIndex && totalSize > drIndex)) {
            throw OutOfRangeException(drIndex, 0, totalSize)
        }

//        System.out.println(sizes);
//        System.out.println(values);
        var value: T? = null
        var currentSize = -1.0
        for (i in sizes.indices) {
            val newSize = currentSize + sizes[i]
            //            System.out.println("004_A: " + newSize);
//            System.out.println("004_B: " + currentSize);
            if (currentSize < drIndex && newSize >= drIndex) {
                value = values[i]
            }
            currentSize = newSize

//            System.out.println("005_A: " + newSize);
//            System.out.println("005_B: " + currentSize);
        }
        return value
    }

    /**
     * Change the size for a partition.
     *
     * @param value the value to change.
     * @param size  the size for the partition to set.
     * @return the new size.
     */
    fun edit(value: T, size: Double): Double {
        val index = indexOf(value)
        if (index >= sizes.size) throw OutOfRangeException(index, 0, sizes.size)
        totalSize = totalSize - sizes[index] + size
        sizes[index] = size
        return sizes[index]
    }

    /**
     * Change the size and value of a partition.
     *
     * @param value    the value to change.
     * @param size     the partition size/
     * @param newValue the value.
     * @return the new size.
     */
    fun edit(value: T, size: Double, newValue: T): Double {
        val index = indexOf(value)
        if (index >= sizes.size) throw OutOfRangeException(index, 0, sizes.size)
        totalSize = totalSize - sizes[index] + size
        sizes[index] = size
        values[index] = newValue
        return sizes[index]
    }

    /**
     * Returns ranges of all partitions.
     *
     * @return the ranges of all partitions.
     */
    val ranges: Array<Range?>
        get() {
            var ranges = arrayOf<Range?>()
            var currentSize = 0.0
            for (size in sizes) {
                val newSize = currentSize + size
                ranges = ArrayUtils.add(ranges, Range(currentSize, newSize))
                currentSize = newSize
            }
            return ranges
        }

    /**
     * Returns the index based of the value.
     *
     * @param value the value to get index from.
     * @return the index.
     */
    fun indexOf(value: T): Int {
        return values.indexOf(value)
    }

    /**
     * Returns the range based of the value.
     *
     * @param value the value to get the range from..
     * @return the index.
     */
    fun rangeOf(value: T): Range {
        val index = values.indexOf(value)
        return getRange(index)
    }

    fun editLengths(applier: Applier<T, Double>) {
        var currentSize = 0.0
        val sizes2: MutableList<Double> = ArrayList(sizes)
        for (i in sizes2.indices) {
            val applierSize = applier.apply(values[i])
            val newSize = currentSize + sizes2[i]
            totalSize = totalSize - sizes2[i] + applierSize
            sizes2[i] = applierSize
            currentSize = newSize
        }
        sizes = sizes2
    }
}