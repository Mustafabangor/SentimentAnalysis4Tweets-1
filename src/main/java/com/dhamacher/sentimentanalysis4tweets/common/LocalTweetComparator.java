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
package com.dhamacher.sentimentanalysis4tweets.common;
import java.util.Comparator;

/**
 *
 * @author daniel, mustafa
 */
public class LocalTweetComparator implements Comparator<LocalTweet>{

    private String query;
    
    public LocalTweetComparator(String query) {
        super();
        this.query = query;
    }
    
    public int compare(LocalTweet o1, LocalTweet o2) {
        double dif = o1.getSentimentScore(query) - o2.getSentimentScore(query);
        if (dif < 0)
            return -1;
        return 1;
    }    
}