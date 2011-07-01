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

/**
 * Index which never returns null, but builds new objects on-the-fly.
 *
 * @param <I> type of the index
 * @param <T> indexable object
 * 
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class BIndex<I,T extends WithID<I>> extends Index<I,T> {
    
    /**
     * Builds pristine indexable objects with the given ID.
     * 
     * @param <I> type of the index
     * @param <T> indexable object
     */
    public interface Builder<I,T> {
        
        /**
         * Returns a new pristine indexable object with the given ID.
         * 
         * @param id ID of the new object
         * @return new object with the given ID
         */
        public T build(I id);
        
    }
    
    private final Builder<I,T> builder;

    /**
     * Creates a new BuildingIndex with the given builder.
     * 
     * @param builder creator of new objects
     */
    public BIndex(Builder<I,T> builder) {
        this.builder = builder;
    }

    /**
     * Retrieves the object with the given ID.
     * 
     * <p>If the object is not found, a new one is created.</p>
     * 
     * @param id ID of the object to find
     * @return the object with given ID
     */
    @Override
    public T get(I id) {
        T t = super.get(id);
        if (t == null) {
            t = builder.build(id);
            put(t);
        }
        return t;
    }
}
