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

import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.database.Operator;
import com.dhamacher.sentimentanalysis4tweets.search.Search;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Perform text analysis.
 *
 * This will perform the actual semantic, lexical and sentimental analysis of
 * tweet content. Through extraction of words, assigning sentiment and relating
 * all this to the original search query, the sentimental value of each tweet
 * with respect to a given query is determined.
 *
 * @author daniel, mustafa
 */
public class Analysis {

    private Search parent = null;
    private Tokenizer tokenizer = null;
    private Lexicon lexicon = null;
    private double[] gaussian = new double[6];

    public Analysis(Search parent) throws Exception {
        this.parent = parent;
        this.tokenizer = new Tokenizer();
        this.lexicon = new Lexicon();
        double sentiment = 0.0;
        double score = 0.0;
        this.gaussian = CalculateDistance();
        LinkedList<LocalTweet> tweets = parent.getTweets();
        System.out.println("Analyzing sentiment for " + tweets.size() + " tweets.");
        for (LocalTweet tweet : tweets) {
            if (tweet.isFromDB()) {
                sentiment += tweet.getSentimentScore(parent.getQuery().getRequest());
            } else {
                score = getSentiment(tweet);
                sentiment += score;
                tweet.addSentiment(parent.getQuery().getRequest(), score);
            }
        }
        System.out.println("Total sentiment score: " + sentiment);
    }

    /**
     * Retrieve the sentiment value of a tweet.
     *
     * @param tweet
     * @return
     */
    private double getSentiment(LocalTweet tweet) throws IOException {
        LinkedList<String> tokens = tokenizer.getTokens(tweet);
        Double score = 0.0;
        Double negation = 1.0;
        Double modifier = 1.0;
        int range = 0;
        Double gauss = 1.0;
        int i = 0;
        for (String token : tokens) {
            gauss = getDistance(parent, token, tokens, i, this.gaussian);
            score += (negation) * (modifier) * (gauss) * lexicon.get(token);

            // Deal with negations
            if (isNegation(token)) {
                negation = -1.0;
                range = 2;
            } else if (range > 1) {
                range--;
            } else {
                negation = 1.0;
            }            
            modifier = getModifierValue(token, negation);
            i++;
        }
        return score;
    }

    private static boolean isNegation(String token) {
        HashMap<String, Double> negations = Operator.getInstance().getNegations();
        return negations.containsKey(token);
    }

    private static double getModifierValue(String token, Double negation) {
        HashMap<String, Double> modifiers = Operator.getInstance().getModifiers();
        double m;
        if (modifiers.containsKey(token)) {
            m = modifiers.get(token);
            if ((negation == -1.0) && (m == 2)) {
                m = 0.8;
            }
        } else {
            m = 1.0;
        }
        return m;
    }

    /*
     * The distance is calculated thanks to a gaussian distribution with :
     * variance sigma = 1 
     * mean i equals to the position of the token
     * height is equal to the value of the token in the lexicon
     * 
     * If the distance between the entity and the token is 1, the 
     * value of gaussian is equal to the sentiment score of the token.
     * When the distance increases, the value of gaussian decreases.
     * 
     */
    public double[] CalculateDistance() {
        double sigma = 1.0;
        double gaussian_max = Math.exp(-(1 / (sigma * sigma * 2))) / (sigma * Math.sqrt(2 * Math.PI));
        for (int i = 0; i <= 5; i++) {
            gaussian[i] = (1 / gaussian_max) * Math.exp(-(((i) / sigma) * ((i) / sigma)) / 2) / (sigma * Math.sqrt(2 * Math.PI));
        }
        return gaussian;
    }

    private double getDistance(Search parent, String token, LinkedList<String> tokens, int i, double gaussian[]) {
        String entity = parent.getQuery().getRequest();
        int x = Math.abs(tokens.indexOf(entity));
        double height = Math.abs(lexicon.get(token));
        if (Math.abs(x - i) <= 5) {
            return height * gaussian[Math.abs(x - i)];
        } else {
            return 0.0;
        }
    }
}