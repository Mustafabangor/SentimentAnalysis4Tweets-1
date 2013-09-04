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
package com.dhamacher.sentimentanalysis4tweets.preprocessing;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author daniel, mustafa
 */
public class TextNormalizer {

    static String tweet = "";

    public TextNormalizer(String tweet) {
        this.tweet = tweet;
    }

    public String getTweet() {
        return tweet;
    }

    /**
     * Get tweet normalized without noise
     *
     * @param Tweet
     * @return
     */
    public static String getTweetWithoutUrlsAnnotations(String Tweet) {
        StringTokenizer tokens = new StringTokenizer(Tweet, " ");
        String newTweet = "";
        while (tokens.hasMoreTokens()) {
            String temp = tokens.nextToken();
            if (!temp.contains("@") && !temp.contains("http")) {
                newTweet += temp + " ";
            }
        }
        tweet = "";
        tweet = newTweet;
        return tweet;
    }

    /**
     * Try to detect a smiley at the given tweet and it returns the appropriate
     * score add this score to the general score as well ask MIhai for tokenizer
     *
     * @return
     */
    public static double detectSmiley(String tweet, HashMap<String, Double> smileys) {
        double score = 0;
        StringTokenizer toks = new StringTokenizer(tweet, " ");        
        while (toks.hasMoreTokens()) {
            String token = toks.nextToken();
            if (smileys.containsKey(token)) {
                score = smileys.get(token);
                return score;
            }
        }
        return score;
    }

    public static String removeDuplicates(String s) {
        StringBuilder noDupes = new StringBuilder();
        int pos = 0;
        int len = s.length();
        int curLen = 0;
        while (pos < len) {
            if (pos == 0 || s.charAt(pos) != s.charAt(pos - 1)) {
                curLen = 1;
            } else {
                ++curLen;
            }

            if (curLen < 3) {
                noDupes.append(s.charAt(pos));
            }
            ++pos;
        }
        return noDupes.toString();
    }

    public static String toLowerCase(String tw) {
        return tw.toLowerCase();
    }
}
