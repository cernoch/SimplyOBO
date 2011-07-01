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
import java.util.Map;
import java.util.Set;

/**
 * Indexes objects by their ID and allows fast searching.
 * 
 * <p>Objects that implement {@link WithID} have a defined ID, which
 * can be used for finding them in a large collection. Such collection
 * is usually represented by this object.</p>
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class Index<I, T extends WithID<I>> {

    private Map<I,T> index = new HashMap<I,T>();
    
    /**
     * Adds a new object into the index.
     * 
     * @param o the object to be inserted
     * @return previously stored object for the same ID or {@code null}
     * if there was no such object.
     */
    public T put(T o) {
        cache = null;
        return index.put(o.id(), o);
    }
    
    /**
     * Retrieves the object with the given ID.
     * 
     * @param id ID of the object to find
     * @return the object with given ID or {@code null}
     * if there is no such object
     */
    public T get(I id) {
        return index.get(id);
    }
    
    /**
     * Indicates whether the object with given ID is stored in the database.
     * 
     * @param id ID of the queried object
     * @return {@code true} if found, {@code false} if not found
     */
    public boolean contains(I id) {
        return index.containsKey(id);
    }
    
    private Set<T> cache = null;
    
    /**
     * Returns all objects stored in the index.
     * 
     * @return collection of objects in the index.
     */
    public Set<T> all() {
        if (cache == null)
            cache = new HashSet<T>(index.values());
        
        return cache;
    }
}
