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

import java.util.List;

/**
 * Callback for stanza-buffered reading of an OBO file.
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public interface StanzaListener {
    
    /**
     * Called when the parser reads the whole header of the OBO file.
     * 
     * @param header list of tag-value pairs in the header
     */
    public void onHeader(List<TagValuePair> header);
    
    /**
     * Called when the parser reads the whole stanza.
     * 
     * @param name stanza-name (appears within square brackets)
     * @param tagVals list of tag-value pairs associated with the stanza
     */
    public void onStanza(String name, List<TagValuePair> tagVals);
    
}
