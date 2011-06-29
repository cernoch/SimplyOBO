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
package cz.cvut.felk.ida.simplyobo.parser;

/**
 * Encapsulates a tag-value pair in an OBO file.
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class TagValuePair {
    
    private final String tag;
    private final String val;

    public TagValuePair(String tag, String val) {
        this.tag = tag;
        this.val = val;
    }

    public String tag() {
        return tag;
    }

    public String val() {
        return val;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;

        final TagValuePair other = (TagValuePair) obj;
        if (this.tag == null ? other.tag != null
                             : !this.tag.equals(other.tag))
            return false;

        if (this.val == null ? other.val != null
                             : !this.val.equals(other.val))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.tag != null ? this.tag.hashCode() : 0);
        hash = 53 * hash + (this.val != null ? this.val.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
