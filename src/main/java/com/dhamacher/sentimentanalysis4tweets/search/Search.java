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
package com.dhamacher.sentimentanalysis4tweets.search;
import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.pagerank.Pagerank;
import com.dhamacher.sentimentanalysis4tweets.sentiment.Analysis;
import com.dhamacher.sentimentanalysis4tweets.twitterapi.TweetOperator;
import java.util.LinkedList;

/**
 * Search handler class.
 *
 * @author daniel, mustafa
 */
public class Search {

    private String request;
    private Query query;
    private LinkedList<LocalTweet> tweets;
    private TweetOperator operator;
    private int option;

    public Search(String request, int option) {
        this.request = request;
        this.option = option;
        operator = new TweetOperator(option);      
    } 
    
    public static Search execute(String searchToken, int option) {
        Search search = new Search(searchToken, option);
        search.execute();
        return search;
    }
   
    public LinkedList<LocalTweet> execute() {
        try {
            query = new Query(this.request);
            loadTweets();
            analyzeTweets();
            operator.saveBatch(tweets, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getTweets();
    }

    /**
     * Added the saving to database here. My logic was the following: People
     * probably take a look at the results for a while so we might as well use
     * that moment for our saving. Feel free to move it if it interferes with
     * something.
     *
     * @Override public void done () {
     * SentimentAnalysisApp.getView().showResults(tweets, query);
     * operator.saveNewTweetsToDB(tweets, query.toString());
    }
     */
    public LinkedList<LocalTweet> getTweets() {
        return tweets;
    }

    public Query getQuery() {
        return this.query;
    }

    /**
     * Load tweets.
     *
     * Attempt to load relevant tweets from both the database as well as the
     * twitter API. Those are returned as a single LinkedList of Tweet objects.
     *
     * @throws Exception
     */
    private void loadTweets() throws Exception {
        //SentimentAnalysisApp.setStatus("Loading tweets.");
        tweets = operator.getTweets(query.toString());
    }

    /**
     * Analyze the tweets just found: assign a relevance score, a sentiment
     * score, and a page rank score.
     */
    private void analyzeTweets() throws Exception {
        //SentimentAnalysisApp.setStatus("Analyzing tweets."); 
        Analysis analysis = new Analysis(this);
        Pagerank pagerank = new Pagerank(tweets, query);
        pagerank.computePageranks();

        for (LocalTweet tweet : tweets) {
            tweet.computeScore();
        }
    }
    
    public int getOption() {
        return option;
    }
}