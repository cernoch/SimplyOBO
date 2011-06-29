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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Line-by-line OBO format parser.
 * 
 * <p>Reads basic syntax (stanzas and tag-value pairs) from an OBO file.</p>
 *
 * @author Radomír Černoch (radomir.cernoch at gmail.com)
 */
public class LineByLineParser {

    private LineByLineListener sink;

    public LineByLineParser(LineByLineListener sink) {
        setListener(sink);
    }
    
    public void setListener(LineByLineListener sink) {
        this.sink = sink;       
    }
    
    public LineByLineListener getListener() {
        return sink;
    }
    
    private static String removeComment(String s) {
        int i = findFirstNonEscaped(s, "!");
        return i == -1 ? s : s.substring(0,i);
    }
    
    private static int findFirstNonEscaped(String haystack, String needle) {
        int i = -1;
        
        do {
            i = haystack.indexOf(needle, i+1);
        } while (i > 0 && haystack.charAt(i-1) == '\\');
        
        return i;
    }
    
    private static final Map<Character,Character> ESCAPES;
               
    static {
        ESCAPES = new HashMap<Character,Character>();
        ESCAPES.put('!', '!');
        ESCAPES.put('n', '\n');
        ESCAPES.put('W', ' ');
        ESCAPES.put('t', '\t');
        ESCAPES.put(':', ':');
        ESCAPES.put(',', ',');
        ESCAPES.put('\'', '\'');
        ESCAPES.put('\\', '\\');
        ESCAPES.put('(', '(');
        ESCAPES.put(')', ')');
        ESCAPES.put('[', '[');
        ESCAPES.put(']', ']');
        ESCAPES.put('{', '{');
        ESCAPES.put('}', '}');
    }
    
    public static String unEscape(String s) {
        StringBuilder output = new StringBuilder(s.length());
        
        for (int i = 0; i < s.length(); i++) {
            Character toAppend = s.charAt(i);
            if (s.charAt(i) == '\\'
                        && (i+1) < s.length()
                        && ESCAPES.get(s.charAt(i+1)) != null) {
                toAppend = ESCAPES.get(s.charAt(i+1));
                i++;
            }
            output.append(toAppend);
        }
        
        return output.toString();
    }
    
    public void parse(Reader oboFile) throws IOException {
        BufferedReader b;
        if (oboFile instanceof BufferedReader)
            b = (BufferedReader) oboFile;
        else
            b = new BufferedReader(oboFile);

        String l; // Line
        while ((l = b.readLine()) != null) {
            l = removeComment(l);
            String t = l.trim();
            
            if (t.startsWith("[") && t.endsWith("]")) {
                sink.onStanza(t.substring(1,t.length()-1));
                continue;
            }
            
            {// Trailing slash means a multi-line:
                String a; // to append
                while (l.endsWith("\\") && (a = b.readLine()) != null) {
                    l = l.substring(0,l.length()-1);
                    l += removeComment(a);
                }
            }
            
            int dci = findFirstNonEscaped(l, ":");
            if (dci >= 0) {
                String tag = l.substring(0,dci);
                tag = unEscape(tag);
                
                do { dci++; } while (dci < l.length() && l.charAt(dci) == ' ');
                String val = l.substring(dci);
                
                sink.onTagValue(tag,val);
                continue;
            }
        }
    }
}
