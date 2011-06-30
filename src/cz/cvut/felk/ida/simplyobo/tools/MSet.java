/*
 * Copyright (c) 2011 Radomír Černoch (radomir.cernoch at gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package cz.cvut.felk.ida.simplyobo.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Maps keys to set of values.
 * 
 * <p>Works just like a {@link Map} with following differences:<ul>
 * <li>{@link #put(Object,Set)} and {@link #putAll(Map)} are not allowed and
 * throw {@link UnsupportedOperationException}.
 * Use {@link #add(Object,Object)} instead.</li>
 * <li>{@link #remove(Object)} removes all mappings for the given key.</li>
 * <li>{@link #rem(Object,Object)} removes a single mapping key-value pair.</li>
 * </ul>
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class MSet<K,V> extends HashMap<K,Set<V>> implements Iterable<K> {

    /**
     * Inserts the given key-value pair into the map.
     * 
     * @param key the key to be inserted
     * @param value the value to be inserted
     * @return {@code true} if the MSet did not contain the mapping
     */
    public boolean add(K key, V value) {
        Set<V> set = get(key);
        if (set == null) {
            set = new HashSet<V>();
            put(key, set);
        }
        return set.add(value);
    }

    /**
     * Removes the given key-value pair from the map.
     * 
     * <p>If the pair was the last pair for the key, then the key is removed
     * from the map. Therefore {@link #containsKey(Object)} will return
     * {@code false} for the supplied key.</p>
     * 
     * @param key key to be removed
     * @param value value to be removed
     * @return {@code true} if the MSet did contain the mapping
     */
    public boolean rem(K key, V value) {

        boolean out = false;
        Set<V> set = get(key);

        if (set != null) {
            out = set.remove(value);

            if (set.isEmpty())
                remove(key);
        }
        return out;
    }
    
    /**
     * Iterates over all keys.
     */
    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
