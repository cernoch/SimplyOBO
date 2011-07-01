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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import static java.util.logging.Level.*;

/**
 * Ontology holds Types and Terms; allows loading from a StanzaParser.
 * 
 * <p>The ontology contains only axioms directly stated in the serialized file.
 * If you want to use property axioms (such as transitivity), use
 * {@link Reasoner}.</p>
 * 
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class Ontology implements StanzaListener {
    
    private static final Logger L = Logger.getLogger(Ontology.class.getName());

    /**
     * Creates an empty ontology.
     */
    public Ontology() {}

    /**
     * Creates and ontology and loads objects from the given file.
     * 
     * @param r ontology in OBO format to be loaded
     * @throws IOException if the Reader cannot be read
     */
    public Ontology(Reader r) throws IOException {
        new LineByLineParser(new StanzaCollector(this)).parse(r);
    }
    
    protected Namespace defNS = null;
    
    @Override
    public void onHeader(List<TagValuePair> header) {
        for (TagValuePair tvp : header) {
            if ("format-version".equals(tvp.tag())) {
                if (!"1.2".equals(tvp.val()))
                    L.log(WARNING, "OBO ontology version " + tvp.val()
                            + " not supported. Trying to recover.");
                continue;
            }

            if ("default-namespace".equals(tvp.tag())) {
                L.log(FINE, "Setting default namespace: " + tvp.val());
                try {
                    defNS = Namespace.valueOf(tvp.val());
                    
                } catch(IllegalArgumentException ex) {
                    throw new SyntaxError(
                          "Not a valid namespace: \"" + tvp.val() + "\".", ex);
                }
                continue;
            }
        }
    }

    @Override
    public void onStanza(String name, List<TagValuePair> tagVals) {
        if ("Term".equals(name)) {
            L.log(FINE, "Creating a term from " + tagVals);
            createTerm(tagVals);
            return;
        }
        
        if ("Typedef".equals(name)) {
            L.log(FINE, "Creating a type from " + tagVals);
            createTypedef(tagVals);
            return;
        }
    }

    /**
     * Finds the Type with the given ID in the ontology.
     * 
     * @param typeId ID of the searched Type
     * @return the Type with given ID or {@code null} if no such Type exists
     */
    public Type findType(String typeId) {
        return types.contains(typeId) ? types.get(typeId) : null;
    }
    
    /**
     * Finds the Term with the given ID in the ontology.
     * 
     * @param termId ID of the searched Term
     * @return the Term with given ID or {@code null} if no such Term exists
     */
    public Term findTerm(Integer termId) {
        return terms.contains(termId) ? terms.get(termId) : null;
    }
    
    /**
     * Returns all Types in the ontology.
     */
    public Set<? extends Type> allTypes() {
        return types.all();
    }
    
    /**
     * Returns all Terms in the ontology.
     */
    public Set<? extends Term> allTerms() {
        return terms.all();
    }
    
    /**
     * Creates a new Type from the list of t-v pairs stored into the ontology.
     */
    private void createTerm(List<TagValuePair> tagVals) {
        
        TermImpl term;
        {
            Integer id = null;
            String name = null;
            Namespace namespace = defNS;

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
                    try {
                        namespace = Namespace.valueOf(tvp.val());
                        
                    } catch(IllegalArgumentException ex) {
                        throw new SyntaxError(
                          "Not a valid namespace: \"" + tvp.val() + "\".", ex);
                    }
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
                L.log(FINER, "Parsing 'is_a' tag: " + id);
                TermImpl other = terms.get(id);
                subTerms.add(term, other);
                supTerms.add(other, term);
                continue;
            }
            
            if ("relationship".equals(tvp.tag())) {
                L.log(FINER, "Parsing 'relationship' tag: " + tvp.val());
                
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
    
    protected SVOidx<TermImpl,TypeImpl,TermImpl> svoIdx
        = new SVOidx<TermImpl,TypeImpl,TermImpl>();
    
    /**
     * Creates a new Type from the list of t-v pairs stored into the ontology.
     */
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
            type.trans = transitive;
        }
        
        // Read relations
        for (TagValuePair tvp : tagVals) {
            
            if ("is_a".equals(tvp.tag())) {
                String id = tvp.val().trim();
                L.log(FINER, "Parsing 'is_a' tag: " + id);
                TypeImpl other = types.get(id);
                subTypes.add(type, other);
                supTypes.add(other, type);
                continue;
            }
            
            if ("transitive_over".equals(tvp.tag())) {
                String id = tvp.val().trim();
                L.log(FINER, "Parsing 'transitive_over' tag: " + id);
                TypeImpl transOverType = types.get(id);
                type.trOver.add(transOverType);
                continue;
            }
        }
    }
    
    /**
     * Converts a string GO identified to a integer counterpart.
     * 
     * @param ident string representation of the id "GO:0000013"
     * @return number representation of the id
     * @throws NumberFormatException if the specified ID is not a numbers
     */
    private Integer goIDtoInt(String ident) throws NumberFormatException {
        L.log(FINEST, "Converting GO identifier to number: " + ident);
        
        if (ident.startsWith("GO:"))
            return Integer.valueOf(ident.substring(3));
        
        throw new IllegalArgumentException("Not a GO identifier");
    }
    
    protected final Index<Integer,TermImpl> terms
             = new BIndex<Integer,TermImpl>(new TermBuilder());
    
    protected final Index<String,TypeImpl> types
             = new BIndex<String,TypeImpl>(new TypeBuilder());

    protected final MSet<TermImpl,TermImpl> subTerms = new MSet<TermImpl,TermImpl>();
    protected final MSet<TermImpl,TermImpl> supTerms = new MSet<TermImpl,TermImpl>();

    protected final MSet<TypeImpl,TypeImpl> subTypes = new MSet<TypeImpl,TypeImpl>();
    protected final MSet<TypeImpl,TypeImpl> supTypes = new MSet<TypeImpl,TypeImpl>();
    
    private static final NumberFormat ID_FMT;
    static {
        ID_FMT = NumberFormat.getNumberInstance();
        ID_FMT.setMinimumIntegerDigits(7);
    }
    
    protected class TermImpl extends WithID<Integer> implements Term {
        
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
        @SuppressWarnings("unchecked")
        public Set<? extends Term> isA () {
            Set<? extends Term> isA = subTerms.get(this);
            return isA == null ? Collections.EMPTY_SET : isA;
        }

        @Override
        public Set<? extends Term> relation(Type type) {
            TypeImpl typeImpl = types.get(type.id());
            return svoIdx.getO(this, typeImpl);
        }
        
        @Override
        public String toString() {
            return "GO:" + ID_FMT.format(id);
        }
    }

    protected class TypeImpl extends WithID<String> implements Type {

        protected final String id;
        
        protected String name = null;

        protected boolean trans = false;
        
        protected final Set<TypeImpl> trOver = new HashSet<TypeImpl>();

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
        @SuppressWarnings("unchecked")
        public Set<? extends Type> isA() {
            Set<? extends Type> isA = subTypes.get(this);
            return isA == null ? Collections.EMPTY_SET : isA;
        }
        
        @Override
        public boolean transitive() {
            return trans;
        }
        
        @Override
        public Set<? extends Type> transitiveOver() {
            return trOver;
        }
    }

    private class TermBuilder implements Builder<Integer,TermImpl> {
        @Override
        public TermImpl build(Integer id) {
            L.log(FINER, "Creating a new Term with ID: " + id);
            return new TermImpl(id);
        }
    }

    private class TypeBuilder implements Builder<String,TypeImpl> {
        @Override
        public TypeImpl build(String id) {
            L.log(FINER, "Creating a new Type with ID: " + id);
            return new TypeImpl(id);
        }
    }
}
