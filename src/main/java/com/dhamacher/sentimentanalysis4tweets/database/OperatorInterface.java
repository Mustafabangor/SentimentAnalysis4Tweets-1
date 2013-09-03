/*
 * Interface which stores all the functionalities that
 * Operator class is going to implement
 */
package com.dhamacher.sentimentanalysis4tweets.database;

import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import java.util.HashMap;
import java.util.LinkedList;

import twitter4j.Status;
/**
 *
 * @author maryger
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
