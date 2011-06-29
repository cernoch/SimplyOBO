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

import java.io.InputStreamReader;
import java.io.Reader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests {@link LineByLineParser} by providing example OBO files.
 * 
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class LineByLineParserTest {

    @Test
    public void testParse1() throws Exception {

        Reader oboFile = new InputStreamReader(
                getClass().getResourceAsStream("simple.obo"));
        
        SyntaxChecker sc = new SyntaxChecker();
        sc.expectHeader("format-version", "1.2");
        sc.expectHeader("date", "14:07:2009 13:33");
        sc.expectHeader("auto-generated-by", "OBO-Edit 2.0");
        sc.expectHeader("subsetdef", "test_next_goslim \"Second test set\"");
        sc.expectHeader("subsetdef", "gosubset_prok \"Prokaryotic GO subset\"");
        sc.expectHeader("remark", "cvs version: $Revision: 1.239 $");
        
        sc.expectStanza("Term", "id", "GO:0000001");        
        sc.expectStanza("Term", "name", "test term GO:0000001");
        sc.expectStanza("Term", "def", "\"This is a definition for GO:0000001.\" [db:me]");
        sc.expectStanza("Term", "subset", "test_goslim");
        sc.expectStanza("Term", "subset", "test_next_goslim");
        sc.expectStanza("Term", "is_a", "GO:0000008 ");
        sc.expectStanza("Term", "relationship", "part_of GO:0000008 ");
        sc.expectStanza("Term", "relationship", "regulates GO:0000008 ");
        
        LineByLineParser instance = new LineByLineParser(sc);
        instance.parse(oboFile);
        if (!sc.isEmpty()) fail("Non-parsed items: " + sc.toString());
    }

    @Test
    public void testParse2() throws Exception {

        Reader oboFile = new InputStreamReader(
                getClass().getResourceAsStream("multiline.obo"));
        
        SyntaxChecker sc = new SyntaxChecker();
        sc.expectHeader("date", "14:07:2009 13:33");
        
        LineByLineParser instance = new LineByLineParser(sc);
        instance.parse(oboFile);
        if (!sc.isEmpty()) fail("Non-parsed items: " + sc.toString());
    }
    
    @Test
    public void testParse3() throws Exception {

        Reader oboFile = new InputStreamReader(
                getClass().getResourceAsStream("escape.obo"));
        
        SyntaxChecker sc = new SyntaxChecker();
        sc.expectHeader("unusual:tag:with:special:chars\\x",
                "and its value\\!");
        
        LineByLineParser instance = new LineByLineParser(sc);
        instance.parse(oboFile);
        if (!sc.isEmpty()) fail("Non-parsed items: " + sc.toString());
    }    
}
