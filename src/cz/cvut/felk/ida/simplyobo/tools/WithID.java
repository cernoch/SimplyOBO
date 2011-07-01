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
 * Object with a uniquely determining ID
 * 
 * <p>This is a superclass of all objects having a defined ID. The ID uniquely
 * characterizes the object and hence both {@link #equals(Object)} and
 * {@link #hashCode()} use the ID as their input.</p>
 *
 * @param <I> type of the ID
 * 
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public abstract class WithID<I> {

    /**
     * Unique ID of this object.
     */
    public abstract I id();

    /**
     * Gives the hash-code of the ID.
     */
    @Override
    public final int hashCode() {
        return id().hashCode();
    }

    /**
     * Compares the ID to determine whether both objects are equal.
     * 
     * @param obj the other object to compare
     * @return {@code true} if the IDs do match; {@code false} otherwise
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        
        @SuppressWarnings(value="unchecked")
        final WithID<I> other = (WithID<I>) obj;
        
        if (this.id() != other.id() && (this.id() == null
                || !this.id().equals(other.id()))) return false;
        
        return true;
    }
    
    /**
     * This class name and the ID in square brackets.
     */
    @Override
    public String toString() {
        return getClass().getName() + "[" + id().toString() + "]";
    }
}
