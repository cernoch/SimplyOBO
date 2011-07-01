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
import java.util.Iterator;
import java.util.Map;

/**
 * Maps two keys to set of values.
 * 
 * <p>Works just like a {@link Map} with following differences:
 * <ul>
 * <li>{@link #put(Object,Object)} and {@link #putAll(Map)} are not allowed and
 * throw {@link UnsupportedOperationException}.
 * Use {@link #add(Object,Object,Object)} instead.</li>
 * <li>{@link #remove(Object)} removes mappings for the given primary key.</li>
 * <li>{@link #rem(Object,Object,Object)} removes a single key-key-value
 * mapping triple.</li>
 * </ul>
 * 
 * @param <X> primary key
 * @param <Y> secondary key
 * @param <Z> value
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class MMSet<X,Y,Z> extends HashMap<X,MSet<Y,Z>> implements Iterable<X> {

    /**
     * Inserts the given key-key-value triple into the map.
     * 
     * @param x the primary key to be inserted
     * @param y the secondary key to be inserted
     * @param z the value to be inserted
     * @return {@code true} if the MMSet did not contain the triple
     */
    public boolean add(X x, Y y, Z z) {
        MSet<Y,Z> set = get(x);
        if (set == null) {
            set = new MSet<Y,Z>();
            put(x, set);
        }
        return set.add(y,z);
    }

    /**
     * Removes the given key-key-value triple from the map.
     * 
     * <p>If the pair was the last pair for the primary key, then the primary
     * key is removed from the map. Therefore {@link #containsKey(Object)}
     * will return {@code false} for the supplied primary key.</p>
     * 
     * @param x the primary key to be inserted
     * @param y the secondary key to be inserted
     * @param z the value to be inserted
     * @return {@code true} if the MSet did contain the mapping
     */
    public boolean rem(X x, Y y, Z z) {

        boolean out = false;
        MSet<Y,Z> set = get(x);

        if (set != null) {
            out = set.rem(y,z);

            if (set.isEmpty())
                remove(x);
        }
        return out;
    }

    /**
     * Iterates over primary keys.
     */
    @Override
    public Iterator<X> iterator() {
        return keySet().iterator();
    }
}
