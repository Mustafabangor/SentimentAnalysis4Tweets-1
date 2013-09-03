/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhamacher.sentimentanalysis4tweets.twitterapi;

import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.common.LocalTweetComparator;
import com.dhamacher.sentimentanalysis4tweets.common.TweetRankInfo;
import com.dhamacher.sentimentanalysis4tweets.common.User;
import com.dhamacher.sentimentanalysis4tweets.database.Operator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.IDs;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.Configuration;


/**
 *
 * @author dama
 */
public class TweetOperator {

    private Operator db;
    private int mode;
    public static final int DB_AND_ONLINE = 3;
    public static final int DB_ONLY = 1;
    public static final int ONLINE_ONLY = 2;
    private static final String consumerKey = "XwEagf0oSlyPE59Z86fQ";
    private static final String consumerSecret = "8nNHG4Tbz8JH57YecP87vwheX464dWM9wufJnMdRLY";
    private static final String tokenKey = "89405802-ez85Nkr7r2mn2EfFdS0MXPadMsw1E5YCx6oixdbIg";
    private static final String tokenSecret = "2ykkYdU5qiloVML43CRaffb79RF7MIC9Q3mb9Y7o";
    Twitter twitter;

    public TweetOperator(int mode) {
        try {
            this.mode = mode;
            db = Operator.getInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
        if (mode == 1) {
            TwitterFactory twitterFactory = new TwitterFactory(buildConfig());
            twitter = twitterFactory.getInstance();
        }        
    }

    private Configuration buildConfig() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                /**
                 * Go to https://dev.twitter.com/ -> Sign in -> create App ->
                 * use the data given at the end of App creation You need: (1)
                 * Consumer Key (2) Consumer Secret (3) Access Token (4) Access
                 * Token Secret
                 */
                .setOAuthConsumerKey("XwEagf0oSlyPE59Z86fQ")
                .setOAuthConsumerSecret("8nNHG4Tbz8JH57YecP87vwheX464dWM9wufJnMdRLY")
                .setOAuthAccessToken("89405802-ez85Nkr7r2mn2EfFdS0MXPadMsw1E5YCx6oixdbIg")
                .setOAuthAccessTokenSecret("2ykkYdU5qiloVML43CRaffb79RF7MIC9Q3mb9Y7o");
        return cb.build();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected LinkedList<LocalTweet> getTweetsWithAPI(String queryString) throws TwitterException, IOException {
        Query query = new Query(queryString);
        long sinceId = db.getIdOfLatestTweetForThisEntity(queryString);
        System.out.println("Get ids since " + 0);      
        QueryResult result = twitter.search(query);
        LinkedList<LocalTweet> results = new LinkedList<LocalTweet>();       
        for (Status tweet : result.getTweets()) {
            LocalTweet lt = new LocalTweet();
            lt.copyFrom(tweet);
            results.add(lt);
        }        
        return results;
    }

    protected LinkedList<LocalTweet> getTweetsFromDB(String queryString) {
        return db.getLocalTweets(queryString);
    }

    public LinkedList<LocalTweet> getTweets(String queryString) throws TwitterException, IOException {
        LinkedList<LocalTweet> results;
        if (mode == DB_AND_ONLINE) {
            results = getTweetsFromDB(queryString);
            results.addAll(getTweetsWithAPI(queryString));
        } else if (mode == DB_ONLY) {
            results = getTweetsFromDB(queryString);
        } else if (mode == ONLINE_ONLY) {
            results = getTweetsWithAPI(queryString);
        } else {
            results = new LinkedList<LocalTweet>();
        }
        return results;
    }

    /**
     * Retrieve user info related to pageranking.
     *
     * @param id Twitter ID number of the user
     * @return
     */
    public User getUser(long id) {
        User user = null;
        if (mode == DB_AND_ONLINE) {
            user = db.getUser(id);
            if (user == null) {
                user = getUserWithAPI(id);
            } else {
                System.out.println("User info already found in database:" + id);
            }
        } else if (mode == DB_ONLY) {
            user = db.getUser(id);
        } else {
            user = getUserWithAPI(id);
        }
        // Store the user info in DB.
        // NOTE: this is not done in batch manner here, it may be slower this way
        // yet it's easier to get done for now.
        if (user != null) {
            db.storeUser(user);
        }
        return user;
    }

    public TweetRankInfo getTweetRankInfo(long id) {
        TweetRankInfo tweetRankInfo = null;
        if (mode == DB_AND_ONLINE) {
            tweetRankInfo = db.getTweetRankInfo(id);
            if (tweetRankInfo == null) {
                tweetRankInfo = getTweetRankInfoWithAPI(id);
            } else {
                System.out.println("Tweet rank info already found in database:" + id);
            }
        } else if (mode == DB_ONLY) {
            tweetRankInfo = db.getTweetRankInfo(id);
        } else {
            tweetRankInfo = getTweetRankInfoWithAPI(id);
        }
        // Store the user info in DB.
        // NOTE: this is not done in batch manner here, it may be slower this way
        // yet it's easier to get done for now.
        if (tweetRankInfo != null) {
            db.storeTweetRankInfo(tweetRankInfo);
        }
        return tweetRankInfo;
    }

    private TweetRankInfo getTweetRankInfoWithAPI(long id) {
        boolean favorited = isFavorited(id);
        long retweetCount = getRetweetCount(id);
        TweetRankInfo tweetRankInfo = new TweetRankInfo(id, retweetCount, favorited);
        System.out.println(tweetRankInfo);
        return tweetRankInfo;
    }

    /**
     * Retrieve a Twitter user (with relevant info about pagerank) via Twitter
     * API and create a common.User object.
     *
     * @param id Twitter ID number of the user
     * @return
     */
    private User getUserWithAPI(long id) {
        int followersCount = getFollowersCount(id);
        int friendsCount = getFriendsCount(id);
        // number of tweets is not used now in pageranking, so don't waste a
        // query for it now.
        int statusesCount = 0;
        //int statusesCount = getTweetCount(id);
        return new User(id, followersCount, friendsCount, statusesCount);
    }

    public int getFollowersCount(long userID) {
        try {
            return twitter.showUser(userID).getFollowersCount();


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, "Problem when getting followers count", ex);

            return 0;
        }
    }

    public int getFollowersCount(String user) {
        try {
            return twitter.showUser(user).getFollowersCount();


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, "Problem when getting followers count", ex);

            return 0;
        }
    }

    public int getFriendsCount(long userID) {
        try {
            return twitter.showUser(userID).getFriendsCount();


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, "Problem when getting friends(followed by me) count", ex);

            return 0;
        }
    }

    public int getFriendsCount(String user) {
        try {
            return twitter.showUser(user).getFriendsCount();


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, "Problem when getting friends(followed by me) count", ex);

            return 0;
        }
    }

    public LinkedList<Long> getFollowerIds(String user) {
        try {
            long cursor = -1;
            LinkedList<Long> res = new LinkedList<Long>();
            IDs ids;
            do {
                ids = twitter.getFollowersIDs(user, cursor);
                for (long id : ids.getIDs()) {
                    res.add(id);
                }
            } while ((cursor = ids.getNextCursor()) != 0);
            return res;


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, null, ex);
            return new LinkedList<Long>();
        }
    }

    public LinkedList<Long> getFollowerIds(long userId) {
        try {
            long cursor = -1;
            LinkedList<Long> res = new LinkedList<Long>();
            IDs ids;
            do {
                ids = twitter.getFollowersIDs(userId, cursor);
                for (long id : ids.getIDs()) {
                    res.add(id);
                }
            } while ((cursor = ids.getNextCursor()) != 0);
            return res;


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, null, ex);
            return new LinkedList<Long>();
        }
    }

    public int getFollowersCount(LocalTweet tweet) {
        return getFollowersCount(tweet.getFromUser());
    }

    /**
     *
     * @param tweet
     * @return a list of user ids that follow this user
     */
    public LinkedList<Long> getFollowerIds(LocalTweet tweet) {
        return getFollowerIds(tweet.getFromUser());
    }

    public LinkedList<Long> getRetweetedByIds(long id) {
        int page = 1;
        IDs ids;
        LinkedList<Long> res = new LinkedList<Long>();
        try {
            do {
                ids = twitter.getIncomingFriendships(id);/*getRetweetedByIDs(id, new Paging(page, 100));*/
                for (long this_id : ids.getIDs()) {
                    res.add(this_id);
                }
                ++page;
            } while (ids.getIDs().length != 0);


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     *
     * @param tweet
     * @return a list of user ids who've retweeted this
     */
    public LinkedList<Long> getRetweetedByIds(LocalTweet tweet) {
        return getRetweetedByIds(tweet.getId());
    }

    public long getRetweetCount(LocalTweet tweet) {
        try {
            return twitter.showStatus(tweet.getId()).getRetweetCount();


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, null, ex);

            return 0;
        }
    }

    public long getRetweetCount(long id) {
        try {
            return twitter.showStatus(id).getRetweetCount();


        } catch (TwitterException ex) {
            Logger.getLogger(TweetOperator.class
                    .getName()).log(Level.SEVERE, null, ex);

            return 0;
        }
    }

    public int getTweetCount(long userId) {
        try {
            return twitter.showUser(userId).getStatusesCount();
        } catch (TwitterException tex) {
        }
        return 0;
    }

    public int getTweetCount(LocalTweet tweet) {
        return getTweetCount(tweet.getFromUserId());
    }

    public static LinkedList<LocalTweet> getTopPositive(String queryString, int N, LinkedList<LocalTweet> tweets) {
        LinkedList<LocalTweet> toSort = new LinkedList<LocalTweet>();
        LinkedList<LocalTweet> result = new LinkedList<LocalTweet>();
        for (LocalTweet tweet : tweets) {
            if (tweet.getSentimentScore(queryString) > 0.1) {
                toSort.add(tweet);
            }
        }

        Collections.sort(toSort, new LocalTweetComparator(queryString));
        int sz = toSort.size();
        N = Math.min(N, sz);
        for (int i = 0; i < N; ++i) {
            result.add(toSort.get(sz - i - 1));
        }
        return result;
    }

    public static LinkedList<LocalTweet> getTopNegative(String queryString, int N, LinkedList<LocalTweet> tweets) {
        LinkedList<LocalTweet> toSort = new LinkedList<LocalTweet>();
        LinkedList<LocalTweet> result = new LinkedList<LocalTweet>();
        for (LocalTweet tweet : tweets) {
            if (tweet.getSentimentScore(queryString) < -0.1) {
                toSort.add(tweet);
            }
        }
        Collections.sort(toSort, new LocalTweetComparator(queryString));
        int sz = toSort.size();
        N = Math.min(N, sz);
        for (int i = 0; i < N; ++i) {
            result.add(toSort.get(i));
        }
        return result;
    }

    public boolean isFavorited(long id) {
        try {
            return twitter.showStatus(id).isFavorited();
        } catch (TwitterException tex) {
        }
        return false;
    }

    public boolean isFavorited(LocalTweet tweet) {
        return isFavorited(tweet.getId());
    }

    public void saveToDB(LocalTweet tweet) {
        db.storeTweet(tweet);
    }

    public void saveBatch(LinkedList<LocalTweet> list, String entity) {
        System.out.println("Storing tweets to database.");
        db.storeTweetBatch(list, entity);
    }

    public void saveNewTweetsToDB(LinkedList<LocalTweet> tweets, String query) {
        int sz = tweets.size() - 1;
        LinkedList<LocalTweet> list = new LinkedList<LocalTweet>();
        for (int i = sz; i >= 0 && !tweets.get(i).isFromDB(); --i) {
            list.add(tweets.get(i));
        }

        saveBatch(list, query);
    }
}
