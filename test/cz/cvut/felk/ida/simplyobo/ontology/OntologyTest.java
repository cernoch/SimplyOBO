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

import cz.cvut.felk.ida.simplyobo.parser.LineByLineParser;
import cz.cvut.felk.ida.simplyobo.parser.StanzaCollector;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OntologyTest {

    private Ontology onto = new Ontology();
    
    @Before
    public void setUp() throws IOException {
        Reader oboFile = new InputStreamReader(
                getClass().getResourceAsStream("gene_ontology.obo"));

        LineByLineParser parser = new LineByLineParser(new StanzaCollector(onto));
        parser.parse(oboFile);
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testNameAndNamespace() {
        Term t = onto.findTerm(1);
        assertNotNull(t);
        assertEquals("mitochondrion inheritance", t.name());
        assertEquals(Namespace.biological_process, t.namespace());
    }
    
    @Test
    public void testISA() {
        Term t = onto.findTerm(1);
        assertNotNull(t);
        assertTrue(t.isA().contains(onto.findTerm(48308)));
        assertTrue(t.isA().contains(onto.findTerm(48311)));
        assertEquals(2, t.isA().size());
    }
    
    public void testRelation() {
        Term s = onto.findTerm(2000784);
        Type v = onto.findType("positively_regulates");
        Term o = onto.findTerm(71964);
        
        assertNotNull(s);
        assertNotNull(v);
        assertNotNull(o);
        
        assertTrue(s.relations().containsKey(v));
        assertTrue(s.relations().get(v).contains(o));
    }
}
