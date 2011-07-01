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
import cz.cvut.felk.ida.simplyobo.parser.StanzaListener;
import cz.cvut.felk.ida.simplyobo.parser.SyntaxError;
import cz.cvut.felk.ida.simplyobo.parser.TagValuePair;
import cz.cvut.felk.ida.simplyobo.tools.BIndex;
import cz.cvut.felk.ida.simplyobo.tools.BIndex.Builder;
import cz.cvut.felk.ida.simplyobo.tools.Index;
import cz.cvut.felk.ida.simplyobo.tools.MSet;
import cz.cvut.felk.ida.simplyobo.tools.SVOidx;
import cz.cvut.felk.ida.simplyobo.tools.WithID;
import java.io.IOException;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Ontology reads to a stanza parser and creates types and terms.
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class Ontology implements StanzaListener {

    public Ontology() {}

    public Ontology(Reader r) throws IOException {
        new LineByLineParser(new StanzaCollector(this)).parse(r);
    }
    
    @Override
    public void onHeader(List<TagValuePair> header) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onStanza(String name, List<TagValuePair> tagVals) {
        if ("Term".equals(name)) {
            createTerm(tagVals);
            return;
        }
        
        if ("Typedef".equals(name)) {
            
            return;
        }
    }

    public Type findType(String typeId) {
        return types.get(typeId);
    }
    
    public Term findTerm(Integer termId) {
        return terms.get(termId);
    }
    
    public Collection<? extends Type> allTypes() {
        return types.all();
    }
    
    public Collection<? extends Term> allTerms() {
        return terms.all();
    }
    
    private void createTerm(List<TagValuePair> tagVals) {
        
        TermImpl term;
        {
            Integer id = null;
            String name = null;
            Namespace namespace = null;

            // Read non-structured information
            for (TagValuePair tvp : tagVals) {

                if ("id".equals(tvp.tag())) {
                    id = Integer.valueOf( tvp.val().substring(3) );
                    continue;
                }

                if ("name".equals(tvp.tag())) {
                    name = tvp.val();
                    continue;
                }

                if ("namespace".equals(tvp.tag())) {
                    namespace = Namespace.valueOf(tvp.val());
                    continue;
                }
            }

            // Create the term
            term = terms.get(id);
            term.name = name;
            term.namespace = namespace;
        }
        
        // Read relations
        for (TagValuePair tvp : tagVals) {
            
            if ("is_a".equals(tvp.tag())) {
                Integer id = goIDtoInt(tvp.val().trim());
                TermImpl other = terms.get(id);
                subTerms.add(term, other);
                supTerms.add(other, term);
                continue;
            }
            
            if ("relationship".equals(tvp.tag())) {
                int space = tvp.val().indexOf(" ");
                if (space == -1) throw new SyntaxError("Relationship tag  must"
                        + " contain a typedef name, a space and a term id");
                
                String typeId = tvp.val().substring(0,space);
                Integer termId = goIDtoInt(
                        tvp.val().substring(space+1).trim());
                
                TypeImpl relType = types.get(typeId);
                TermImpl relTerm = terms.get(termId);
                
                svoIdx.add(term, relType, relTerm);
            }
        }
    }
    
    SVOidx<TermImpl,TypeImpl,TermImpl> svoIdx = new SVOidx<TermImpl,TypeImpl,TermImpl>();
    
    private void createTypedef(List<TagValuePair> tagVals) {
        
        TypeImpl type;
        {
            String id = null;
            String name = null;
            boolean transitive = false;

            // Read non-structured information
            for (TagValuePair tvp : tagVals) {

                if ("id".equals(tvp.tag())) {
                    id = tvp.val();
                    continue;
                }

                if ("name".equals(tvp.tag())) {
                    name = tvp.val();
                    continue;
                }
                
                if ("is_transitive".equals(tvp.tag())) {
                    transitive = Boolean.valueOf(tvp.val());
                    continue;
                }
            }

            // Create the term
            type = types.get(id);
            type.name = name;
            type.transitive = transitive;
        }
        
        // Read relations
        for (TagValuePair tvp : tagVals) {
            
            if ("is_a".equals(tvp.tag())) {
                TypeImpl other = types.get(tvp.val().trim());
                subTypes.add(type, other);
                supTypes.add(other, type);
                continue;
            }
            
            if ("transitive_over".equals(tvp.tag())) {
                TypeImpl transOverType = types.get(tvp.val());
                type.transitiveOver.add(transOverType);
                continue;
            }
        }
    }
    
    private Integer goIDtoInt(String ident) {
        if (ident.startsWith("GO:"))
            return Integer.valueOf(ident.substring(3));
        
        throw new IllegalArgumentException("Not a GO identifier");
    }
    
    private final Index<Integer,TermImpl> terms
           = new BIndex<Integer,TermImpl>(new TermBuilder());
    
    private final Index<String,TypeImpl> types
           = new BIndex<String,TypeImpl>(new TypeBuilder());

    private final MSet<TermImpl,TermImpl> subTerms = new MSet<TermImpl,TermImpl>();
    private final MSet<TermImpl,TermImpl> supTerms = new MSet<TermImpl,TermImpl>();

    private final MSet<TypeImpl,TypeImpl> subTypes = new MSet<TypeImpl,TypeImpl>();
    private final MSet<TypeImpl,TypeImpl> supTypes = new MSet<TypeImpl,TypeImpl>();
    
    private static final NumberFormat ID_FMT;
    static {
        ID_FMT = NumberFormat.getNumberInstance();
        ID_FMT.setMinimumIntegerDigits(7);
    }
    
    private class TermImpl extends WithID<Integer> implements Term {
        
        private final Integer id;
        
        private String name = null;
        
        private Namespace namespace = null;

        public TermImpl(Integer id) {
            if (id == null)
                throw new NullPointerException("ID of a term cannot be null!");
            
            this.id = id;
        }
        
        @Override
        public Integer id() {
            return id;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Namespace namespace() {
            return namespace;
        }
        
        @Override
        public Set<? extends Term> isA () {
            return subTerms.get(this);
        }
        
        @Override
        public Map<? extends Type, ? extends Set<? extends Term>> relations() {
            return svoIdx.getVO(this);
        }
        
        @Override
        public String toString() {
            return "GO:" + ID_FMT.format(id);
        }
    }

    private class TypeImpl extends WithID<String> implements Type {

        private final String id;
        
        private String name = null;

        private boolean transitive = false;
        
        private final Set<TypeImpl> transitiveOver = new HashSet<TypeImpl>();

        public TypeImpl(String id) {
            if (id == null)
                throw new NullPointerException("ID of a type cannot be null!");
            
            this.id = id;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public String name() {
            return name;
        }
        
        @Override
        public Set<? extends Type> isA() {
            return subTypes.get(this);
        }
        
        @Override
        public boolean transitive() {
            return transitive;
        }
        
        @Override
        public Set<? extends Type> transitiveOver() {
            return transitiveOver;
        }
    }

    private class TermBuilder implements Builder<Integer,TermImpl> {
        @Override
        public TermImpl build(Integer id) {
            return new TermImpl(id);
        }
    }

    private class TypeBuilder implements Builder<String,TypeImpl> {
        @Override
        public TypeImpl build(String id) {
            return new TypeImpl(id);
        }
    }
}
