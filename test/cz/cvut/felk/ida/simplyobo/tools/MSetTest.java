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

import java.util.HashSet;
import org.junit.*;
import static org.junit.Assert.*;

public class MSetTest {
    
    @Test
    public void testAdd() {
        MSet<String,String> x = new MSet<String,String>();
        assertTrue( x.add("ahoj", "lidi"));
        assertTrue( x.add("ahoj", "kamarádi"));
        assertFalse(x.add("ahoj", "kamarádi"));
                
        assertTrue( x.add("jak", "se máte"));

        HashSet<String> exp1 = new HashSet<String>();
        exp1.add("lidi");
        exp1.add("kamarádi");
        
        HashSet<String> exp2 = new HashSet<String>();
        exp2.add("se máte");
        
        assertEquals(exp1, x.get("ahoj"));
        assertEquals(exp2, x.get("jak"));
        assertEquals(null, x.get("blabla"));
    }

    @Test
    public void testRem() {
        MSet<String,String> x = new MSet<String,String>();
        assertTrue( x.add("ahoj", "lidi"));
        assertTrue( x.add("ahoj", "kamarádi"));
        assertFalse(x.add("ahoj", "kamarádi"));
        
        assertTrue( x.rem("ahoj", "lidi"));
        assertFalse(x.rem("ahoj", "xxx"));
                
        HashSet<String> exp = new HashSet<String>();
        exp.add("kamarádi");
        
        assertEquals(exp, x.get("ahoj"));
    }
}
