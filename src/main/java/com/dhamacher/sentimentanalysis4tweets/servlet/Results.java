package com.dhamacher.sentimentanalysis4tweets.servlet;

import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.search.Query;
import com.dhamacher.sentimentanalysis4tweets.search.Search;
import com.dhamacher.sentimentanalysis4tweets.twitterapi.TweetOperator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Results
 *
 * This is in essence a JPanel, used to display results. Thus, it requires
 * results to visualize much of anything, although the basic background will
 * always be displayed, courtesy of the TabPanel it extends.
 *
 * External classes should not have to worry about interacting with this class:
 * all that is handled directly by the SWING framework. The only public method
 * is therefore the method that sets the data which these results are based on.
 *
 * @author Matthijs
 */
public class Results {

    private LinkedList<LocalTweet> results;
    private Query query;
    private Search search;
    private int positive = 0;
    private int negative = 0;
    private int neutral = 0;
    private LinkedList<String> dates;

    public Results(Search search) { 
        this.search = search;
        this.query = search.getQuery();
        results = this.search.getTweets();        
        showTweetBreakdown();
        showSentimentOverTime();
        showTopTweets();
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