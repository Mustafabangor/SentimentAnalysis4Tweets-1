/* Copyright 2013 Daniel Hamacher, Mustafa Elkhunni
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dhamacher.sentimentanalysis4tweets.sentiment;
import com.dhamacher.sentimentanalysis4tweets.database.Operator;
import java.util.HashMap;


/**
 *
 * @author daniel, mustafa
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