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
package com.dhamacher.sentimentanalysis4tweets.servlet;
import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.database.Connector;
import com.dhamacher.sentimentanalysis4tweets.database.Operator;
import com.dhamacher.sentimentanalysis4tweets.search.Query;
import com.dhamacher.sentimentanalysis4tweets.search.Search;
import com.dhamacher.sentimentanalysis4tweets.twitterapi.TweetOperator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Results
 * 
 * @author daniel, mustafa
 */
public class Results {

    private LinkedList<LocalTweet> results;
    private Query query;
    private Search search;
    private int positive = 0;
    private int negative = 0;
    private int neutral = 0;
    private LinkedList<String> dates;
    Connector con = new Connector("twitter_sentiment");

    public Results(Search search) { 
        this.search = search;       
        this.query = search.getQuery();
        results = this.search.getTweets();        
        showTweetBreakdown();
        showSentimentOverTime();
        showTopTweets();
    }
    
    public Results(String token) {
        Operator op = Operator.getInstance();
        long id = op.getEntityId(token);
        String query = "Select * FROM sentiments where entity_id="+id;
        LinkedList<Double> scores = new LinkedList<Double>();
        try {
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                scores.add(rs.getDouble(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Results.class.getName()).log(Level.SEVERE, null, ex);
        }
        polarity(scores);
    }
    
    public void polarity(LinkedList<Double> scores) {
        resetCounters();
        for (Double d : scores) {
            if (d > 0) 
                positive++;            
            if (d < 0) 
                negative++;            
            if (d == 0) 
                neutral++;            
        }       
    }
    
    private void resetCounters() {
        positive = 0;
        negative = 0;
    }

    /**
     * Show a tweet sentiment breakdown.
     *
     * This generates a pie chart displaying the number of positive, neutral and
     * negative tweets in our result set. No further details are given.
     */
    private void showTweetBreakdown() {         
        for (LocalTweet tweet : results) {
            if (tweet.getSentimentScore(query.getRequest()) > 0) 
                positive++;            
            if (tweet.getSentimentScore(query.getRequest()) < 0) 
                negative++;            
            if (tweet.getSentimentScore(query.getRequest()) == 0) 
                neutral++;            
        }       
    }

    /**
     * Show the cumulative sentiment progress over time.
     *
     * For a time-ordered list of tweets, this will display the progression of
     * sentiment over time; starting from zero, each positive tweet increases
     * the sentiment value, whereas each negative tweet decreases it. Given a
     * long enough period (ie, enough tweets to parse), this can visualize
     * changes in how people think about the search query.
     */
    private void showSentimentOverTime() {
        dates = new LinkedList<String>();
        // Extract some tweet dates:
        for (int i = 0; i < results.size(); i += results.size() / 4) {
            dates.add(results.get(i).getCreatedString("yy-MM-dd"));
        }      
    }

    /**
     * Show the most influential tweets.
     *
     * These are the tweets with the greatest positive or negative net sentiment
     * score (taking into account page rank, if possible).
     */
    private void showTopTweets() {      
        LinkedList<LocalTweet> mostPositive = TweetOperator.getTopPositive(query.getRequest(), 8, results);
        LinkedList<LocalTweet> mostNegative = TweetOperator.getTopNegative(query.getRequest(), 8, results);      
    }   
    
    public int getPositive() {
        return positive;
    }
    
    public int getNegative() {
        return negative;
    }
    
    public String getEntity() {
        return query.getRequest();
    }
    
    public LinkedList<String> getDates() {
        return dates;
    }
}