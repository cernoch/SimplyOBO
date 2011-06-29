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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Checks that a {@link OBOSyntaxListener} receives all expected events.
 * 
 * <p>Typically, the test procedure firstly adds tag-value pairs expected
 * to be received from the parser (see {@link #expectHeader(String,String)} and
 * {@link #expectStanza(String,String,String)}). Then the parser executes.
 * Finally the test procedure checks the {@link #isEmpty()} method whether
 * all expected tags were received</p>.
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class SyntaxChecker implements LineByLineListener {
    
    private Set<TagValuePair> header = new HashSet<TagValuePair>();
    private Map<String, Set<TagValuePair>> stanzas
            = new HashMap<String, Set<TagValuePair>>();
    
    public void expectHeader(String tag, String val) {
        header.add(new TagValuePair(tag, val));
    }
    
    public void expectStanza(String stanza, String tag, String val) {
        if (stanzas.get(stanza) == null)
            stanzas.put(stanza, new HashSet<TagValuePair>());

        stanzas.get(stanza).add(new TagValuePair(tag, val));
    }

    String currStanza = null;
    
    @Override
    public void onStanza(String stanza) {
        currStanza = stanza;
    }

    @Override
    public void onTagValue(String tag, String value) {
        Set<TagValuePair> toRemove = header;
        
        if (currStanza != null)
            toRemove = stanzas.get(currStanza);

        if (toRemove == null)
            return;

        TagValuePair tagval = new TagValuePair(tag, value);
        if (!toRemove.remove(tagval))
            System.err.println("Unexpected to parse: " + tagval);
    }
    
    public boolean isEmpty() {
        if (!header.isEmpty()) return false;
        
        for (String stanza : stanzas.keySet())
            if (!stanzas.get(stanza).isEmpty())
                return false;
        
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (header.isEmpty())
            sb.append("Header ");
            for (TagValuePair tv : header)
                sb.append(" ").append(tv);

        for (String stanza : stanzas.keySet()) {
            sb.append("; [").append(stanza).append("]");
            for (TagValuePair tv : stanzas.get(stanza))
                sb.append(" ").append(tv);
        }
        
        return sb.toString();
    }
}
