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
import com.dhamacher.sentimentanalysis4tweets.preprocessing.TextNormalizer;
import java.util.LinkedList;
import twitter4j.Status;
import java.io.*;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/**
 *  Tokenizer for tweets.
 * 
 *  Version 1 :
 *  Tokens are individual segments of a sentence; primarily words, although
 *  some punctuation can also constitute a token. The basic method for this is
 *  splitting on a string character (at least for English). More care must be
 *  taken to proper tokenize combined words ("New York") and handle punctuation.
 * 
 * Version 2 : Standard Tokenizer of Lucene
 * Splits words at punctuation characters, removing punctuation. 
 * However, a dot that's not followed by whitespace is considered part of a token.
 * Splits words at hyphens, unless there's a number in the token, in which case the whole token is interpreted as a product number and is not split.
 * Recognizes email addresses and internet hostnames as one token. 
 * 
 *  @author daniel, mustafa
 */
public class Tokenizer {
    
    
    /**
     *  Retrieve the tokens in a Tweet. These are returned as a linked list of
     *  strings; each string is a token, and they are ordered as they occur in
     *  the tweet.
     * 
     *  Before the tokenization occurs, some preprocessing is performed on the
     *  string, to properly convert URL's.
     * 
     *  @param  tweet   The tweet to tokenize.
     *  @return         The tokens in the tweets text.
     */
    public LinkedList<String> getTokens (Status tweet) throws IOException {
        String text = TextNormalizer.getTweetWithoutUrlsAnnotations(tweet.getText());
        return getTokens(text);
    }

    
    /**
     *  Retrieve the tokens in a String. Behaves like getTokens, but operates on
     *  a string instead of a tweet object.
     * 
     *  @param  text    The text to tokenize.
     *  @return         The tokens in the text.
     */
    
    // Version 1
    /*public LinkedList<String> getTokens (String text) {
        LinkedList<String> tokens   = new LinkedList();
        String[] words              = text.split(" ");
        tokens.addAll(Arrays.asList(words));
        return tokens;
    }*/
    
    // Version 2
    public static LinkedList<String> getTokens (String text) throws IOException {
        LinkedList<String> tokens   = new LinkedList();
        TokenStream ts = new StandardTokenizer(new StringReader(text));
        TermAttribute termAtt = (TermAttribute) ts.getAttribute(TermAttribute.class);
        while(ts.incrementToken()) {
            tokens.add(termAtt.term());
           //System.out.print(termAtt.term());
       }
        return tokens;
    }

}