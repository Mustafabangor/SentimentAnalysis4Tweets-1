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
import java.util.Date;
import java.util.HashMap;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

/**
 *
 * @author daniel, mustafa
 */
public class LocalTweet implements Status {
    private long id;
    private String content;
    private String author;
    private long author_id;
    private Date date;
    private double pagerank;
    private HashMap<String, Sentiment> sentiments;
    private boolean fromDB;
    private HashMap<String, Double> score;
    
    public LocalTweet() {
        sentiments = new HashMap<String, Sentiment>();
        score = new HashMap<String, Double>();
        fromDB = true;
    }
    
    /**
     * @param queryString - this corresponds to a database "entity"
     * @param s 
     */
    public void addSentiment(String queryString, Sentiment s) {
        sentiments.put(queryString, s);
    }

    
    public void addSentiment(String queryString, double score) {
        Sentiment sentiment = new Sentiment();
        sentiment.setEntity(queryString);
        sentiment.setScore(score);
        this.addSentiment(queryString, sentiment);
    }    
    
    public void copyFrom(Status tweet) {
        setId(tweet.getId());
        setContent(tweet.getText());
        setAuthor(tweet.getUser().getName());
        setDate(tweet.getCreatedAt());
        setAuthorId(tweet.getId());
        fromDB = false;
    }
    
    public String getText() {
        return content;
    }

    public long getToUserId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getToUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getFromUser() {
        return author;
    }

    public long getId() {
        return id;
    }

    public long getFromUserId() {
        return author_id;
    }

    public String getIsoLanguageCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getProfileImageUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public String getCreatedString (String format) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);    
        return sdf.format(date);        
    }
    
    public String getCreatedString () {
        return getCreatedString("yyyy-MM-dd HH:mm:ss");
    }
    
    public Date getCreatedAt() {
        return date;
    }   
    
    /**
     * @return The static quality/authoritativeness metric associated with the
     *      tweet. Note that it's irrelevant and not computed if the tweet is
     *      neutral, and should not be taken into account in that case.
     */
    public double getPageRank() {
        return pagerank;
    }
    
    /**
     * 
     * @param queryString - corresponds to a database "entity"
     * @return 
     */
    public Sentiment getSentiment(String queryString) {
        return sentiments.get(queryString);
    }
    
    public double getSentimentScore(String queryString) {
        if (sentiments.containsKey(queryString))
            return sentiments.get(queryString).getScore();
        return 0.0;
    }
    
    public boolean isPositive(String queryString) {
        return getSentimentScore(queryString) > 0;
    }
    
    public boolean isNegative(String queryString) {
        return getSentimentScore(queryString) < 0;
    }
    
    public boolean isNeutral(String queryString) {
        return !isPositive(queryString) && !isNegative(queryString);
    }
    
    public boolean isFromDB() {
        return fromDB;
    }
    
 
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public void setAuthorId(long author_id) {
        this.author_id = author_id;
    }
    
    public void setContent(String content) {
        try {
            this.content = new String(content.getBytes("utf8"));
        } catch (Exception e) {}
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setPageRank(double pagerank) {
        this.pagerank = pagerank;
    }
    
    public void setSentiments(HashMap<String, Sentiment> sentiments) {
        this.sentiments = sentiments;
    }
    
    public boolean isTruncated() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long getInReplyToStatusId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long getInReplyToUserId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getInReplyToScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isFavorited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   

    public boolean isRetweet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Status getRetweetedStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long[] getContributors() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long getRetweetCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isRetweetedByMe() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long getCurrentUserRetweetId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isPossiblySensitive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int compareTo(Status o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   

    public int getAccessLevel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Compute overall score, combining pagerank and sentiment score
     */
    public void computeScore() {
        double combination = 0;
        for (String s:sentiments.keySet()) {
            if (pagerank == 0)
                combination = sentiments.get(s).getScore();
            else
                combination = sentiments.get(s).getScore() * pagerank;
            score.put(s,combination);
        }
    }    

    public User getUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public GeoLocation getGeoLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Place getPlace() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public RateLimitStatus getRateLimitStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UserMentionEntity[] getUserMentionEntities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public URLEntity[] getURLEntities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public HashtagEntity[] getHashtagEntities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public MediaEntity[] getMediaEntities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}