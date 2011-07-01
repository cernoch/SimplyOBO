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
package cz.cvut.felk.ida.simplyobo.ontology;

import org.junit.*;
import static org.junit.Assert.*;

public class ReasonerTest extends OntologyTest {

    public ReasonerTest() {
        onto = new Reasoner();
    }

    @Test
    public void testTransitivity() {
        Type partOf = onto.findType("part_of");
        
        Term te0022 = onto.findTerm(22);
        Term te7052 = onto.findTerm(7052);
        Term te0278 = onto.findTerm(278);
        
        assertNotNull(partOf);
        assertNotNull(te0022);
        assertNotNull(te7052);
        assertNotNull(te0278);
        
        assertTrue(te0022.relation(partOf).contains(te7052)); // stated
        assertTrue(te0022.relation(partOf).contains(te0278)); // inferred
    }
}
