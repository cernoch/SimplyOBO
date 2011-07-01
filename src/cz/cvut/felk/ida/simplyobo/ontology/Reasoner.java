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

import cz.cvut.felk.ida.simplyobo.parser.EndOfParsingAware;
import cz.cvut.felk.ida.simplyobo.tools.SetUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides additional inference on top of an ontology.
 *
 * <p>The inference is transparent to the caller.
 * You may use the Reasoner just as an ordinary {@link Ontology}.</p>
 * 
 * <p>The reasoner is implemented using a forward-reasoning technique.
 * Expect a slow initialization, high memory consumption, but a bleedingly
 * fast querying.</p>
 * 
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class Reasoner extends Ontology implements EndOfParsingAware {

    /**
     * Starts the reasoning.
     */
    @Override
    public void parsingEnded() {
        inferRelationTransitiveClosure();
    }
    
    /**
     * Infers transitive axioms
     */
    private void inferRelationTransitiveClosure() {
        
        List<TermImpl> ss = new ArrayList<TermImpl>();
        List<TypeImpl> vs = new ArrayList<TypeImpl>();
        List<TermImpl> os = new ArrayList<TermImpl>();

        do {
            ss.clear(); vs.clear(); os.clear();
            
            for (TypeImpl r : svoIdx.allV())
            for (TypeImpl q : r.trans ? SetUtils.merge(r.trOver, r) : r.trOver)
            for (TermImpl y : SetUtils.intersection(
                    svoIdx.getObyV(r), svoIdx.getSbyV(q)))
                
            for (TermImpl x : svoIdx.getS(r, y))
            for (TermImpl z : svoIdx.getO(y, q))
            if (! svoIdx.contains(x, r, z)) {
                ss.add(x); vs.add(r); os.add(z);
            }

            for (int i = 0; i < ss.size(); i++)
                svoIdx.add(ss.get(i), vs.get(i), os.get(i));
            
        } while (!ss.isEmpty());
    }
}
