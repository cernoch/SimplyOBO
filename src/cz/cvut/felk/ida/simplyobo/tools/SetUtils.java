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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilities to work with Sets.
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public final class SetUtils {

    private SetUtils() {}
    
    /**
     * Computes the intersection of two sets.
     * 
     * @param <X> type of the set
     * @param a one set
     * @param b second set
     * @return items, which are contained in both sets
     */
    public static <X> Set<X> intersection(Set<X> a, Set<X> b) {
        Set<X> out = new HashSet<X>();
        for (X x : a) if (b.contains(x)) out.add(x);
        return out;
    }
    
    /**
     * Merges a set with an array.
     * 
     * @param <X> type of the set
     * @param set set to merge 
     * @param arr array to merge
     * @return set containing the {@code set} and {@code arr}
     */
    public static <X> Set<X> merge(Set<X> set, X... arr) {
        Set<X> out = new HashSet<X>(set);
        out.addAll(Arrays.asList(arr));
        return out;
    }
}
