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

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class MMSetTest {
    
    public MMSetTest() {
    }

    @Test
    public void testAdd() {
        MMSet<String,String,String> x = new MMSet<String,String,String>();
        assertTrue( x.add("x","y","w"));
        assertTrue( x.add("x","y","z"));
        assertFalse(x.add("x","y","z"));
                
        assertTrue( x.add("q","a","b"));

        HashSet<String> exp1 = new HashSet<String>();
        exp1.add("w");
        exp1.add("z");
        
        
        HashMap<String,Set<String>> exp2 = new HashMap<String,Set<String>>();
        HashSet<String> set = new HashSet<String>();
        set.add("b");
        exp2.put("a", set);
        
        assertEquals(exp1, x.get("x").get("y"));
        assertEquals(exp2, x.get("q"));
        assertEquals(null, x.get("blabla"));
    }

    @Test
    public void testRem() {
        MMSet<String,String,String> x = new MMSet<String,String,String>();
        assertTrue( x.add("x","y","w"));
        assertTrue( x.add("x","y","z"));
        assertFalse(x.add("x","y","z"));
                
        assertTrue( x.rem("x","y","w"));
        assertFalse(x.rem("x","y","q"));

        HashSet<String> exp1 = new HashSet<String>();
        exp1.add("z");
        
        assertEquals(exp1, x.get("x").get("y"));
        assertEquals(null, x.get("q"));
    }
}
