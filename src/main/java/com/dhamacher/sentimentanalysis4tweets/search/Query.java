package com.dhamacher.sentimentanalysis4tweets.search;

import com.dhamacher.sentimentanalysis4tweets.sentiment.Thesaurus;
import java.util.HashMap;


/**
 *  Search Query Object
 * 
 *  A search request is more than a single word: it includes related word, stemming,
 *  and other preprocessing steps which must be performed before we can start 
 *  looking at tweets. Thus, we need a Search Query object.
 * 
 *  @author Matthijs
 */
public class Query {
    
    
    private Thesaurus   thesaurus;
    private String      request;
    
    
    public Query (String request) {        
        this.request    = request;
        this.thesaurus  = new Thesaurus(this);
    }
    
    
    public String getRequest () {
        return this.request;
    }
    
    public String toString () {
        return this.request;
    }
    
    
    public HashMap<String,Double> getRelated () {
        return thesaurus.getRelated();
    }
}