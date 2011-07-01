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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Three-way index, which builds a lot of indexes.
 * 
 * TODO: Documentation of SVOidx
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class SVOidx<S,V,O> {
    
    private final MMSet<S,V,O> SVO = new MMSet<S,V,O>();
    
    private final MMSet<O,V,S> OVS = new MMSet<O,V,S>();
    
    private final MMSet<V,S,O> VSO = new MMSet<V,S,O>();
    
    private final MMSet<V,O,S> VOS = new MMSet<V,O,S>();
    
    public void add(S s, V v, O o) {
        SVO.add(s,v,o);
        OVS.add(o,v,s);
        VSO.add(v,s,o);
        VOS.add(v,o,s);
    }

    public Set<S> allS() {
        return Collections.unmodifiableSet(SVO.keySet());
    }
    
    public Set<V> allV() {
        return Collections.unmodifiableSet(VOS.keySet());
    }
    
    public Set<O> allO() {
        return Collections.unmodifiableSet(OVS.keySet());
    }
    
    public Set<S> getS(V v, O o) {
        MSet<O,S> x = VOS.get(v);
        if (x == null) return Collections.EMPTY_SET;
        
        Set<S> y = x.get(o);
        if (y == null) return Collections.EMPTY_SET;
                
        return Collections.unmodifiableSet(y);
    }
    
    public Set<O> getO(S s, V v) {
        MSet<V,O> x = SVO.get(s);
        if (x == null) return Collections.EMPTY_SET;
        
        Set<O> y = x.get(v);
        if (y == null) return Collections.EMPTY_SET;
                
        return Collections.unmodifiableSet(y);
    }
        
    public Set<V> getVbyS(S s) {
        MSet<V,O> x = SVO.get(s);
        if (x == null) return Collections.EMPTY_SET;                        
        return Collections.unmodifiableSet(x.keySet());
    }
    
    public Set<V> getVbyO(O o) {
        MSet<V,S> x = OVS.get(o);
        if (x == null) return Collections.EMPTY_SET;                        
        return Collections.unmodifiableSet(x.keySet());
    }
    
    public Set<S> getSbyV(V v) {
        MSet<S,O> x = VSO.get(v);
        if (x == null) return Collections.EMPTY_SET;                        
        return Collections.unmodifiableSet(x.keySet());
    }
    
    public Set<O> getObyV(V v) {
        MSet<O,S> x = VOS.get(v);
        if (x == null) return Collections.EMPTY_SET;                        
        return Collections.unmodifiableSet(x.keySet());
    }
    
    @Deprecated
    public Map<V,Set<O>> getVO(S s) {
        return SVO.get(s);
    }

    @Deprecated
    public Map<V,Set<S>> getVS(O o) {
        return OVS.get(o);
    }

    @Deprecated
    public Map<S,Set<O>> getSO(V v) {
        return VSO.get(v);
    }

    @Deprecated
    public Map<O,Set<S>> getOS(V v) {
        return VOS.get(v);
    }
    
    public boolean contains(S s, V v, O o) {
        MSet<V,O> x = SVO.get(s);
        if (x == null) return false;
        
        Set<O> y = x.get(v);     
        if (y == null) return false;
                
        return y.contains(o);
    }
}