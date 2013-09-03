/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhamacher.tweetsentimentanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author daniel
 */
public class TweetArff {
    
    private File csv;
    private PrintWriter writer;
    private boolean isClosed;
    private String path;
    
    
    public TweetArff(File f, PrintWriter w) {
        csv = f;
        writer = w;
        isClosed = false;
    }
    
    public static TweetArff getTweetArffInstance(String path) 
            throws FileNotFoundException {
        
        File f = new File(path);
        PrintWriter w = new PrintWriter(f);
        
        return new TweetArff(f, w);        
    }
    
    public void addRelationAnnotation(String relation) {
        writer.write("@RELATION " + relation + "\n");
    }
    
    public void addAttributeAnnotation(String attributeName, String datatype) {
        datatype.toUpperCase();
        writer.write("@ATTRIBUTE " + attributeName + " " + datatype + "\n");
    }
    
    public void addDataAnnotation() {
        writer.write("@DATA\n");
    }
    
    public void closeTweetArff() {        
        writer.close();
        isClosed = true;
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public String getPath() {
        return path;
    }
    
    
    
    
    
}
