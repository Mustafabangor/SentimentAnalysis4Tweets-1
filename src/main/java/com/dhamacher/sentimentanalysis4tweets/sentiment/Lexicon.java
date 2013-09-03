package com.dhamacher.sentimentanalysis4tweets.sentiment;

import com.dhamacher.sentimentanalysis4tweets.database.Operator;
import java.util.HashMap;


/**
 *
 * @author Matthijs
 */
public class Lexicon {
 
    private static HashMap<String, Double> lexicon;
    
    
    public Lexicon () throws Exception {
        if (lexicon == null)
            lexicon = Operator.getInstance().getLexicon();
    }
    
    
    public Double get (String word) {
        if (lexicon.containsKey(word))
            return lexicon.get(word);
        return 0.0;
    }
}
