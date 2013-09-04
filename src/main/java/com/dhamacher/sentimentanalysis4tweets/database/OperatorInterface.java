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
package com.dhamacher.sentimentanalysis4tweets.database;
import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import java.util.HashMap;
import java.util.LinkedList;
import twitter4j.Status;
/**
 * Interface for the Operator class
 * @author daniel, mustafa
 */
public interface OperatorInterface
{
    
    public void storeTweet(Status tw);
    public void storeTweetBatch(LinkedList<LocalTweet> list, String entity);
    public long getIdOfLatestTweetForThisEntity(String entity);
    public LinkedList<String> getTweets(String key);
    public HashMap<String,Double> getSmileys();
    public HashMap<String, Double> getModifiers();
    public HashMap<String,Double> getNegations();
    public LinkedList<LocalTweet> getLocalTweets(String key);
    public void storeSentiment(long tweet_id, long entity_id, double sentiment ,double score );
    public double getSentiment(long key);
    public void fillLexicon();
    public void storeEntity(String value,double score);
    public void storeEntityRelatedWord(String entity,String related_word,double score);
    public HashMap<String,Double> getRelatedWord(String key_entity);
    public double getEntityScore(String key);    
}