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

import java.util.ArrayList;
import java.util.List;

/**
 * Buffer for tag-value pairs and stanza names from SyntaxParser.
 *
 * <p>Typical usage looks like:
 * <pre>
 * Reader source = new FileReader("gene-ontology.obo");
 * StanzaListener sink = new MyStanzaListener();
 * new LineByLineParser(new StanzaCollector(sink)).parse(source);
 * </pre>
 * </p>
 * 
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class StanzaCollector implements LineByLineListener, DocBegEndAware {

    private final StanzaListener sink;

    /**
     * Creates a new buffer, which will send events to the {@code sink}
     * 
     * @param sink receiver of all new stanzas found
     */
    public StanzaCollector(StanzaListener sink) {
        this.sink = sink;
    }

    private String stanza = null;
    private List<TagValuePair> tagVals = new ArrayList<TagValuePair>();
    
    @Override
    public void onStanza(String stanza) {
        
        if (this.stanza == null)
            sink.onHeader(tagVals);
        else 
            sink.onStanza(this.stanza, tagVals);
        
        tagVals = new ArrayList<TagValuePair>();
        this.stanza = stanza;
    }

    @Override
    public void onTagValue(String tag, String value) {
        tagVals.add(new TagValuePair(tag, value));
    }

    @Override
    public void parsingBegun() {
        if (sink instanceof DocBegEndAware)
            ((DocBegEndAware) sink).parsingBegun();
    }

    @Override
    public void parsingEnded() {
        onStanza(null);
        
        if (sink instanceof DocBegEndAware)
            ((DocBegEndAware) sink).parsingEnded();
    }
}
