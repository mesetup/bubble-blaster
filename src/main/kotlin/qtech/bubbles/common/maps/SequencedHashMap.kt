@file:Suppress("unused")

package qtech.bubbles.common.maps

import java.io.Externalizable
import java.io.IOException
import java.io.ObjectInput
import java.io.ObjectOutput
import java.util.*

/*
 * $Header: /home/projects/aspectwerkz/scm/aspectwerkz4/src/main/org/codehaus/aspectwerkz/util/SequencedHashMap.java,v 1.3 2004/10/22 12:40:40 avasseur Exp $
 * $Revision: 1.3 $
 * $Date: 2004/10/22 12:40:40 $
 *
 *
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ /**
 * A map of objects whose mapping entries are sequenced based on the order in which they were added. This data structure
 * has fast <I>O(1) </I> search time, deletion time, and insertion time.
 *
 *
 *
 *
 * Although this map is sequenced, it cannot implement [java.util.List]because of incompatible interface
 * definitions. The remove methods in List and Map have different return values (see:
 * [java.util.List.remove]and [java.util.Map.remove]).
 *
 *
 *
 *
 * This class is not thread safe. When a thread safe implementation is required, use [ ][Collections.synchronizedMap] as it is documented, or use explicit synchronization controls.
 *
 * @author [Michael A. Smith ](mailto:mas@apache.org)
 * @author [Daniel Rall ](mailto:dlr@collab.net)
 * @author [Henning P. Schmiedehausen ](mailto:hps@intermeta.de)
 * @author [Quinten E. Jungblut]()
 * @since 1.0.0
 */
class SequencedHashMap<K, V> : HashMap<K, V?>, Cloneable, Externalizable {
    /**
     * Sentinel used to hold the head and tail of the list of entries.
     */
    private var sentinel: Entry<K?, V?>

    /**
     * Map of keys to entries
     */
    private var entries1: HashMap<K?, Entry<K?, V?>?>

    /**
     * Holds the number of modifications that have occurred to the map, excluding modifications made through a
     * collection view's iterator (e.g. entrySet().iterator().remove()). This is used to create a fail-fast behavior
     * with the iterators.
     */
    @Transient
    private var modCount: Long = 0

    /**
     * Construct a new sequenced hash map with default initial size and load factor.
     */
    constructor() {
        sentinel = createSentinel()
        entries1 = HashMap()
    }

    /**
     * Construct a new sequenced hash map with the specified initial size and default load factor.
     *
     * @param initialSize the initial size for the hash table
     * @see HashMap
     */
    constructor(initialSize: Int) {
        sentinel = createSentinel()
        entries1 = HashMap(initialSize)
    }

    /**
     * Construct a new sequenced hash map with the specified initial size and load factor.
     *
     * @param initialSize the initial size for the hash table
     * @param loadFactor  the load factor for the hash table.
     * @see HashMap
     */
    constructor(initialSize: Int, loadFactor: Float) {
        sentinel = createSentinel()
        entries1 = HashMap(initialSize, loadFactor)
    }

    /**
     * Construct a new sequenced hash map and add all the elements in the specified map. The order in which the mappings
     * in the specified map are added is defined by [.putAll].
     */
    constructor(m: Map<K, V?>) : this() {
        putAll(m)
    }

    /**
     * Removes an internal entry from the linked list. This does not remove it from the underlying map.
     */
    private fun removeEntry(entry: Entry<K?, V?>) {
        entry.next!!.prev = entry.prev
        entry.prev!!.next = entry.next
    }

    /**
     * Inserts a new internal entry to the tail of the linked list. This does not add the entry to the underlying map.
     */
    private fun insertEntry(entry: Entry<K?, V?>) {
        entry.next = sentinel
        entry.prev = sentinel.prev
        sentinel.prev!!.next = entry
        sentinel.prev = entry
    }
    // per Map.size()

    /**
     * Implements [MutableMap.size].
     */
    override val size: Int
        get() {
            return entries1.size
        }


    /**
     * Implements [MutableMap.isEmpty].
     */
    override fun isEmpty(): Boolean {
        // for quick check whether the map is entry, we can check the linked list
        // and see if there's anything in it.
        return sentinel.next === sentinel
    }

    /**
     * Implements [MutableMap.containsKey].
     */
    override fun containsKey(key: K): Boolean {
        // pass on to underlying map implementation
        return entries1.containsKey(key)
    }

    /**
     * Implements [MutableMap.containsValue].
     */
    override fun containsValue(value: V?): Boolean {
        // unfortunately, we cannot just pass this call to the underlying map
        // because we are mapping keys to entries, not keys to values. The
        // underlying map doesn't have an efficient implementation anyway, so this
        // isn't a big deal.
        // do null comparison outside loop so we only need to do it once. This
        // provides a tighter, more efficient loop at the expense of slight
        // code duplication.
        if (value == null) {
            var pos = sentinel.next
            while (pos !== sentinel) {
                if (pos!!.value == null) {
                    return true
                }
                pos = pos.next
            }
        } else {
            var pos = sentinel.next
            while (pos !== sentinel) {
                if (value == pos!!.value) {
                    return true
                }
                pos = pos.next
            }
        }
        return false
    }

    /**
     * Implements [MutableMap.get].
     *
     * @param key the key value.
     */
    override operator fun get(key: K): V? {
        // find entry for the specified key object
        val (_, value) = entries1[key] ?: return null
        return value
    }

    // sentinel.next points to the "first" element of the sequence -- the head
    // of the list, which is exactly the entry we need to return. We must test
    // for an empty list though because we don't want to return the sentinel!
    /**
     * Return the entry for the "oldest" mapping. That is, return the Map.Entry for the key-value pair that was first
     * put into the map when compared to all the other pairings in the map. This behavior is equivalent to using
     * `entrySet().iterator().next()`, but this method provides an optimized implementation.
     *
     * @return The first entry in the sequence, or `null` if the map is empty.
     */
    val first: Entry<K?, V?>?
        get() =
            // sentinel.next points to the "first" element of the sequence -- the head
            // of the list, which is exactly the entry we need to return. We must test
            // for an empty list though because we don't want to return the sentinel!
            if (isEmpty()) null else sentinel.next // sentinel.next points to the "first" element of the sequence -- the head
                                                   // of the list -- and the requisite key is returned from it. An empty list
                                                   // does not need to be tested. In cases where the list is empty,
                                                   // sentinel.next will point to the sentinel itself which has a null key,
                                                   // which is exactly what we would want to return if the list is empty (a
                                                   // nice convenient way to avoid test for an empty list)
    /**
     * Return the key for the "oldest" mapping. That is, return the key for the mapping that was first put into the map
     * when compared to all the other objects in the map. This behavior is equivalent to using
     * `getFirst().getKey()`, but this method provides a slightly optimized implementation.
     *
     * @return The first key in the sequence, or `null` if the map is empty.
     */
    val firstKey: K?
        get() =
        // sentinel.next points to the "first" element of the sequence -- the head
            // of the list -- and the requisite key is returned from it. An empty list
            // does not need to be tested. In cases where the list is empty,
            // sentinel.next will point to the sentinel itself which has a null key,
            // which is exactly what we would want to return if the list is empty (a
            // nice convenient way to avoid test for an empty list)
            sentinel.next!!.key // sentinel.next points to the "first" element of the sequence -- the head
                                // of the list -- and the requisite value is returned from it. An empty
                                // list does not need to be tested. In cases where the list is empty,
                                // sentinel.next will point to the sentinel itself which has a null value,
                                // which is exactly what we would want to return if the list is empty (a
                                // nice convenient way to avoid test for an empty list)
    /**
     * Return the value for the "oldest" mapping. That is, return the value for the mapping that was first put into the
     * map when compared to all the other objects in the map. This behavior is equivalent to using
     * `getFirst().getValue()`, but this method provides a slightly optimized implementation.
     *
     * @return The first value in the sequence, or `null` if the map is empty.
     */
    val firstValue: Any?
        get() =
            // sentinel.next points to the "first" element of the sequence -- the head
            // of the list -- and the requisite value is returned from it. An empty
            // list does not need to be tested. In cases where the list is empty,
            // sentinel.next will point to the sentinel itself which has a null value,
            // which is exactly what we would want to return if the list is empty (a
            // nice convenient way to avoid test for an empty list)
            sentinel.next!!.value // sentinel.prev points to the "last" element of the sequence -- the tail
                                  // of the list, which is exactly the entry we need to return. We must test
                                  // for an empty list though because we don't want to return the sentinel!
    /**
     * Return the entry for the "newest" mapping. That is, return the Map.Entry for the key-value pair that was first
     * put into the map when compared to all the other pairings in the map. The behavior is equivalent to:
     *
     * <pre>
     * Object obj = null;
     * Iterator iter = entrySet().iterator();
     * while (iter.hasNext()) {
     * obj = iter.next();
     * }
     * return (Map.Entry) obj; </pre>
     *
     * However, the implementation of this method ensures an O(1) lookup of the last key rather than O(n).
     *
     * @return The last entry in the sequence, or `null` if the map is empty.
     */
    val last: Entry<K?, V?>?
        get() =
            // sentinel.prev points to the "last" element of the sequence -- the tail
            // of the list, which is exactly the entry we need to return. We must test
            // for an empty list though because we don't want to return the sentinel!
            if (isEmpty()) null else sentinel.prev // sentinel.prev points to the "last" element of the sequence -- the tail
                                                   // of the list -- and the requisite key is returned from it. An empty list
                                                   // does not need to be tested. In cases where the list is empty,
                                                   // sentinel.prev will point to the sentinel itself which has a null key,
                                                   // which is exactly what we would want to return if the list is empty (a
                                                   // nice convenient way to avoid test for an empty list)
    /**
     * Return the key for the "newest" mapping. That is, return the key for the mapping that was last put into the map
     * when compared to all the other objects in the map. This behavior is equivalent to using
     * `getLast().getKey()`, but this method provides a slightly optimized implementation.
     *
     * @return The last key in the sequence, or `null` if the map is empty.
     */
    val lastKey: K?
        get() =// sentinel.prev points to the "last" element of the sequence -- the tail
            // of the list -- and the requisite key is returned from it. An empty list
            // does not need to be tested. In cases where the list is empty,
            // sentinel.prev will point to the sentinel itself which has a null key,
            // which is exactly what we would want to return if the list is empty (a
            // nice convenient way to avoid test for an empty list)
            sentinel.prev!!.key // sentinel.prev points to the "last" element of the sequence -- the tail
                                // of the list -- and the requisite value is returned from it. An empty
                                // list does not need to be tested. In cases where the list is empty,
                                // sentinel.prev will point to the sentinel itself which has a null value,
                                // which is exactly what we would want to return if the list is empty (a
                                // nice convenient way to avoid test for an empty list)
    /**
     * Return the value for the "newest" mapping. That is, return the value for the mapping that was last put into the
     * map when compared to all the other objects in the map. This behavior is equivalent to using
     * `getLast().getValue()`, but this method provides a slightly optimized implementation.
     *
     * @return The last value in the sequence, or `null` if the map is empty.
     */
    val lastValue: Any?
        get() =// sentinel.prev points to the "last" element of the sequence -- the tail
        // of the list -- and the requisite value is returned from it. An empty
        // list does not need to be tested. In cases where the list is empty,
        // sentinel.prev will point to the sentinel itself which has a null value,
        // which is exactly what we would want to return if the list is empty (a
            // nice convenient way to avoid test for an empty list)
            sentinel.prev!!.value

    /**
     * Implements [MutableMap.put].
     */
    override fun put(key: K, value: V?): V? {
        modCount++
        var oldValue: V? = null

        // lookup the entry for the specified key
        var e = entries1[key]

        // check to see if it already exists
        if (e != null) {
            // remove from list so the entry gets "moved" to the end of list
            removeEntry(e)

            // tick value in map
            oldValue = e.setValue(value)

            // Note: We do not tick the key here because its unnecessary. We only
            // do comparisons using equals(Object) and we know the specified key and
            // that in the map are equal in that sense. This may cause a problem if
            // someone does not implement their hashCode() and/or equals(Object)
            // method properly and then use it as a key in this map.
        } else {
            // add new entry
            e = Entry(key, value)
            entries1[key] = e
        }

        // assert(entry in map, but not list)
        // add to list
        insertEntry(e)
        return oldValue
    }

    /**
     * Implements [MutableMap.remove].
     *
     * @param key
     */
    override fun remove(key: K): V? {
        val e = removeImpl(key)
        return e?.value
    }

    /**
     * Fully remove an entry from the map, returning the old entry or null if there was no such entry with the specified
     * key.
     */
    private fun removeImpl(key: K?): Entry<K?, V?>? {
        val e = entries1.remove(key) ?: return null
        modCount++
        removeEntry(e)
        return e
    }

    /**
     * Adds all the mappings in the specified map to this map, replacing any mappings that already exist (as per
     * [MutableMap.putAll]). The order in which the entries are added is determined by the iterator returned from
     * [MutableMap.entries] for the specified map.
     *
     * @param from the mappings that should be added to this map.
     * @throws NullPointerException if `t` is `null`
     */
    override fun putAll(from: Map<out K, V?>) {
        for ((key, value) in from) {
            put(key, value)
        }
    }

    /**
     * Implements [MutableMap.clear].
     */
    override fun clear() {
        modCount++

        // remove all from the underlying map
        entries1.clear()

        // and the list
        sentinel.next = sentinel
        sentinel.prev = sentinel
    }

    /**
     * Implements [MutableMap.equals].
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other === this) {
            return true
        }
        return if (other !is Map<*, *>) {
            false
        } else entries1 == other.entries
    }

    /**
     * Implements [MutableMap.hashCode].
     */
    override fun hashCode(): Int {
        return entries1.hashCode()
    }

    /**
     * Provides a string representation of the entries within the map. The format of the returned string may change with
     * different releases, so this method is suitable for debugging purposes only. If a specific format is required, use
     * [.entrySet].[iterator()][Set.iterator]and iterate over the entries in the map formatting them
     * as appropriate.
     */
    override fun toString(): String {
        val buf = StringBuilder()
        buf.append('[')
        var pos = sentinel.next
        while (pos !== sentinel) {
            buf.append(pos!!.key)
            buf.append('=')
            buf.append(pos.value)
            if (pos.next !== sentinel) {
                buf.append(',')
            }
            pos = pos.next
        }
        buf.append(']')
        return buf.toString()
    }

    override val keys: MutableSet<K>
        get() {
            return object : AbstractSet<K>() {
                // required impls
                override fun iterator(): MutableIterator<K?> {
                    return OrderedIterator<K>(KEY)
                }

                override fun remove(element: K): Boolean {
                    val e: Entry<K?, V?>? = removeImpl(element)
                    return e != null
                }

                // more efficient impls than abstract set
                override fun clear() {
                    this@SequencedHashMap.clear()
                }

                override val size: Int
                    get() {
                        return this@SequencedHashMap.size
                    }

                override fun isEmpty(): Boolean {
                    return this@SequencedHashMap.isEmpty()
                }

                override operator fun contains(element: K): Boolean {
                    return this@SequencedHashMap.containsKey(element)
                }
            }
        }

    override val values: MutableCollection<V?>
        get() {
            return object : AbstractCollection<V>() {
                // required impl
                override fun iterator(): MutableIterator<V?> {
                    return OrderedIterator<V>(VALUE)
                }

                override fun remove(element: V): Boolean {
                    // do null comparison outside loop so we only need to do it once. This
                    // provides a tighter, more efficient loop at the expense of slight
                    // code duplication.
                    if (element == null) {
                        var pos = sentinel.next
                        while (pos !== sentinel) {
                            if (pos!!.value == null) {
                                removeImpl(pos.key)
                                return true
                            }
                            pos = pos.next
                        }
                    } else {
                        var pos = sentinel.next
                        while (pos !== sentinel) {
                            if (element == pos!!.value) {
                                removeImpl(pos.key)
                                return true
                            }
                            pos = pos.next
                        }
                    }
                    return false
                }

                // more efficient impls than abstract collection
                override fun clear() {
                    this@SequencedHashMap.clear()
                }

                override val size: Int
                    get(){
                        return this@SequencedHashMap.size
                    }

                override fun isEmpty(): Boolean {
                    return this@SequencedHashMap.isEmpty()
                }

                override operator fun contains(element: V): Boolean {
                    return this@SequencedHashMap.containsValue(element)
                }
            }
        }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V?>>
        get() {
            return object : MutableSet<MutableMap.MutableEntry<K, V?>> {
                // helper
                private fun findEntry(o: Map.Entry<K?, V?>?): Entry<K?, V?>? {
                    if (o == null) {
                        return null
                    }
                    val entry: Entry<K?, V?>? = entries1[o.key]
                    return if (entry != null && entry == o) {
                        entry
                    } else {
                        null
                    }
                }

                // required impl
                override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V?>> {
                    return OrderedIterator(ENTRY)
                }

                override fun remove(element: MutableMap.MutableEntry<K, V?>): Boolean {
                    val (key) = findEntry(element as Map.Entry<K?, V?>) ?: return false
                    return removeImpl(key) != null
                }

                // more efficient impls than abstract collection
                override fun clear() {
                    this@SequencedHashMap.clear()
                }

                override val size: Int
                    get() {
                        return this@SequencedHashMap.size
                    }

                override fun isEmpty(): Boolean {
                    return this@SequencedHashMap.isEmpty()
                }

                override operator fun contains(element: MutableMap.MutableEntry<K, V?>): Boolean {
                    return findEntry(element as Map.Entry<K?, V?>) != null
                }

                override fun add(element: MutableMap.MutableEntry<K, V?>): Boolean {
                    throw IllegalArgumentException("Set is unmodifiable")
                }

                override fun addAll(elements: Collection<MutableMap.MutableEntry<K, V?>>): Boolean {
                    throw IllegalArgumentException("Set is unmodifiable")
                }

                override fun removeAll(elements: Collection<MutableMap.MutableEntry<K, V?>>): Boolean {
                    throw IllegalArgumentException("Set is unmodifiable")
                }

                override fun retainAll(elements: Collection<MutableMap.MutableEntry<K, V?>>): Boolean {
                    throw IllegalArgumentException("Set is unmodifiable")
                }

                override fun containsAll(elements: Collection<MutableMap.MutableEntry<K, V?>>): Boolean {
                    throw IllegalArgumentException("Set is unmodifiable")
                }
            }
        }

    // APIs maintained from previous version of SequencedHashMap for backwards
    // compatibility
    /**
     * Creates a shallow copy of this object, preserving the internal structure by copying only references. The keys and
     * values themselves are not `clone()` 'd. The cloned object maintains the same sequence.
     *
     * @return A clone of this instance.
     * @throws CloneNotSupportedException if clone is not supported by a subclass.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(CloneNotSupportedException::class)
    override fun clone(): SequencedHashMap<K, V> {
        // yes, calling super.clone() silly since we're just blowing away all
        // the stuff that super might be doing anyway, but for motivations on
        // this, see:
        // http://www.javaworld.com/javaworld/jw-01-1999/jw-01-object.html
        val map = super<HashMap>.clone() as SequencedHashMap<K, V>

        // create new, empty sentinel
        map.sentinel = createSentinel()

        // create a new, empty entry map
        // note: this does not preserve the initial capacity and load factor.
        map.entries1 = HashMap()

        // add all the mappings
        map.putAll(this)

        // Note: We cannot just clone the hashmap and sentinel because we must
        // duplicate our internal structures. Cloning those two will not clone all
        // the other entries they reference, and so the cloned hash map will not be
        // able to maintain internal consistency because there are two objects with
        // the same entries. See discussion in the Entry implementation on why we
        // cannot implement a clone of the Entry (and thus why we need to recreate
        // everything).
        return map
    }

    /**
     * Returns the Map.Entry at the specified index
     *
     * @throws ArrayIndexOutOfBoundsException if the specified index is `< 0` or `>` the
     * size of the map.
     */
    private fun getEntry(index: Int): Entry<K?, V?>? {
        var pos: Entry<K?, V?> = sentinel
        if (index < 0) {
            throw ArrayIndexOutOfBoundsException("$index < 0")
        }

        // loop to one before the position
        var i = -1
        while (i < index - 1 && pos.next !== sentinel) {
            i++
            pos = pos.next!!
        }

        // pos.next is the requested position
        // if sentinel is next, past end of list
        if (pos.next === sentinel) {
            throw ArrayIndexOutOfBoundsException(index.toString() + " >= " + (i + 1))
        }
        return pos.next
    }

    /**
     * Returns the key at the specified index.
     *
     * @throws ArrayIndexOutOfBoundsException if the `index` is `< 0` or `>`
     * the size of the map.
     */
    operator fun get(index: Int): K? {
        return getEntry(index)!!.key
    }

    /**
     * Returns the value at the specified index.
     *
     * @throws ArrayIndexOutOfBoundsException if the `index` is `< 0` or `>`
     * the size of the map.
     */
    fun getValue(index: Int): Any? {
        return getEntry(index)!!.value
    }

    /**
     * Returns the index of the specified key.
     */
    fun indexOf(key: K): Int {
        var e = entries1[key]
        var pos = 0
        while (e!!.prev !== sentinel) {
            pos++
            e = e!!.prev
        }
        return pos
    }

    /**
     * Returns a key iterator.
     */
    operator fun iterator(): MutableIterator<K?> {
        return keys.iterator()
    }

    /**
     * Returns the last index of the specified key.
     */
    fun lastIndexOf(key: K): Int {
        // keys in a map are guaranteed to be unique
        return indexOf(key)
    }

    /**
     * Returns a List view of the keys rather than a set view. The returned list is unmodifiable. This is required
     * because changes to the values of the list (using [java.util.ListIterator.set]) will effectively
     * remove the value from the list and reinsert that value at the end of the list, which is an unexpected side effect
     * of changing the value of a list. This occurs because changing the key, changes when the mapping is added to the
     * map and thus where it appears in the list.
     *
     *
     *
     *
     * An alternative to this method is to use [.keySet]
     *
     * @return The ordered list of keys.
     * @see .keySet
     */
    fun sequence(): List<K?> {
        val l: MutableList<K?> = ArrayList(size)
        l.addAll(keys.asIterable())
        return Collections.unmodifiableList(l)
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index The index of the object to remove.
     * @return The previous value corresponding the `key`, or `null` if none existed.
     * @throws ArrayIndexOutOfBoundsException if the `index` is `< 0` or `>`
     * the size of the map.
     */
    fun remove(index: Int): Any? {
        return remove(get(index))
    }
    // per Externalizable.readExternal(ObjectInput)
    /**
     * Deserializes this map from the given stream.
     *
     * @param in the stream to deserialize from
     * @throws IOException            if the stream raises it
     * @throws ClassNotFoundException if the stream raises it
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class, ClassCastException::class)
    override fun readExternal(`in`: ObjectInput) {
        val size = `in`.readInt()
        for (i in 0 until size) {
            val key = `in`.readObject() as K
            val value = `in`.readObject() as V
            put(key, value)
        }
    }

    /**
     * Serializes this map to the given stream.
     *
     * @param out the stream to serialize to
     * @throws IOException if the stream raises it
     */
    @Throws(IOException::class)
    override fun writeExternal(out: ObjectOutput) {
        out.writeInt(size)
        var pos = sentinel.next
        while (pos !== sentinel) {
            out.writeObject(pos!!.key)
            out.writeObject(pos.value)
            pos = pos.next
        }
    }

    /**
     * [java.util.Map.Entry]that doubles as a node in the linked list of sequenced mappings.
     */
    class Entry<K, V>(
        // Note: This class cannot easily be made cloneable. While the actual
        // implementation of a clone would be simple, defining the semantics is
        // difficult. If a shallow clone is implemented, then entry.next.prev !=
        // entry, which is unintuitive and probably breaks all sorts of assumptions
        // in code that uses this implementation. If a deep clone is
        // implemented, then what happens when the linked list is cyclical (as is
        // the case with SequencedHashMap)? It's impossible to know in the clone
        // when to stop cloning, and thus you end up in a recursive loop,
        // continuously cloning the "next" in the list.
        override var key: K, override var value: V
    ) : MutableMap.MutableEntry<K, V> {

        // package private to allow the SequencedHashMap to access and manipulate
        // them.
        var next: Entry<K, V>? = null
        var prev: Entry<K, V>? = null

        override fun hashCode(): Int {
            // implemented per api docs for Map.Entry.hashCode()
            return (key?.hashCode() ?: 0) xor
                    if (value == null) 0 else value.hashCode()
        }

        @Suppress("CovariantEquals")
        fun equals(obj: Entry<K?, V?>?): Boolean {
            if (obj == null) {
                return false
            }
            return if (obj === this) {
                true
            } else (if (key == null) obj.key == null else key == obj.key) && if (value ==
                null
            ) obj.value ==
                    null else value == obj.value

            // implemented per api docs for Map.Entry.equals(Object)
        }

        override fun toString(): String {
            return "[$key=$value]"
        }

        override fun setValue(newValue: V): V {
            return newValue.also { value = it }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Entry<*, *>

            if (key != other.key) return false
            if (value != other.value) return false

            return true
        }
    }

    private inner class OrderedIterator<T>(returnType: Int) : MutableIterator<T> {
        /**
         * Holds the type that should be returned from the iterator. The value should be either [.KEY],
         * {@link#VALUE}, or [.ENTRY]. To save a tiny bit of memory, this field is also used as a marker for
         * when remove has been called on the current object to prevent a second remove on the same element.
         * Essentially, if this value is negative (i.e. the bit specified by [.REMOVED_MASK]is set), the current
         * position has been removed. If positive, remove can still be called.
         */
        private var returnType: Int

        /**
         * Holds the "current" position in the iterator. When pos.next is the sentinel, we've reached the end of the
         * list.
         */
        private var pos: Entry<K?, V?> = sentinel

        /**
         * Holds the expected modification count. If the actual modification count of the map differs from this value,
         * then a concurrent modification has occurred.
         */
        @Transient
        private var expectedModCount = modCount

        /**
         * Returns whether there is any additional elements in the iterator to be returned.
         *
         * @return `true` if there are more elements left to be returned from the iterator;
         * `false` otherwise.
         */
        override fun hasNext(): Boolean {
            return pos.next !== sentinel
        }

        /**
         * Returns the next element from the iterator.
         *
         * @return the next element from the iterator.
         * @throws NoSuchElementException          if there are no more elements in the iterator.
         * @throws ConcurrentModificationException if a modification occurs in the underlying map.
         */
        @Suppress("UNCHECKED_CAST")
        override fun next(): T {
            if (modCount != expectedModCount) {
                throw ConcurrentModificationException()
            }
            if (pos.next === sentinel) {
                throw NoSuchElementException()
            }

            // clear the "removed" flag
            returnType = returnType and REMOVED_MASK.inv()
            pos = pos.next!!
            return when (returnType) {
                KEY -> pos.key as T
                VALUE -> pos.value as T
                ENTRY -> pos as T
                else -> throw Error("bad iterator type: $returnType")
            }
        }

        /**
         * Removes the last element returned from the [.next]method from the sequenced map.
         *
         * @throws IllegalStateException           if there isn't a "last element" to be removed. That is, if [.next]has
         * never been called, or if [.remove]was already called on the element.
         * @throws ConcurrentModificationException if a modification occurs in the underlying map.
         */
        override fun remove() {
            check(returnType and REMOVED_MASK == 0) { "remove() must follow next()" }
            if (modCount != expectedModCount) {
                throw ConcurrentModificationException()
            }
            removeImpl(pos.key)

            // tick the expected mod count for the remove operation
            expectedModCount++

            // set the removed flag
            returnType = returnType or REMOVED_MASK
        }

        /**
         * Construct an iterator over the sequenced elements in the order in which they were added. The [.next]
         * method returns the type specified by `returnType` which must be either [.KEY],
         * {@link#VALUE}, or [.ENTRY].
         */
        init {
            //// Since this is a private inner class, nothing else should have
            //// access to the constructor. Since we know the rest of the outer
            //// class uses the iterator correctly, we can leave of the following
            //// check:
            //if(returnType >= 0 && returnType <= 2) {
            //  throw new IllegalArgumentException("Invalid iterator type");
            //}
            // Set the "removed" bit so that the iterator starts in a state where
            // "next" must be called before "remove" will succeed.
            this.returnType = returnType or REMOVED_MASK
        }
    }

    companion object {
        // constants to define what the iterator should return on "next"
        private const val KEY = 0
        private const val VALUE = 1
        private const val ENTRY = 2
        private const val REMOVED_MASK = -0x80000000

        // add a serial version uid, so that if we change things in the future
        // without changing the format, we can still deserialize properly.
        private const val serialVersionUID = 3380552487888102930L

        /**
         * Construct an empty sentinel used to hold the head (sentinel.next) and the tail (sentinel.prev) of the list. The
         * sentinel has a `null` key and value.
         */
        private fun <K, V> createSentinel(): Entry<K?, V?> {
            val s = Entry<K?, V?>(null, null)
            s.prev = s
            s.next = s
            return s
        }
    }
}