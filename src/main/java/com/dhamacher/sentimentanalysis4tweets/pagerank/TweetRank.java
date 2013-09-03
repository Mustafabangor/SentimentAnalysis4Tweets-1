package com.dhamacher.sentimentanalysis4tweets.pagerank;

import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.common.TweetRankInfo;
import com.dhamacher.sentimentanalysis4tweets.common.User;
import com.dhamacher.sentimentanalysis4tweets.twitterapi.TweetOperator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Contains the relevant information for static quality computation (pagerank)
 * of a {@code LocalTweet} object.
 * @author j
 */
public class TweetRank {
    
    LocalTweet tweet;
    TweetOperator operator;
    User author;
    TweetRankInfo rankInfo;
    
    public TweetRank(LocalTweet tweet, TweetOperator tweetOperator) {
        this.tweet = tweet;
        this.operator = tweetOperator;
        retrieveRankInfo();
    }

    private void retrieveRankInfo() {
        long authorId = tweet.getFromUserId();
        long tweetId = tweet.getId();
        this.author = operator.getUser(authorId);
        this.rankInfo = operator.getTweetRankInfo(tweetId);
    }
    
    public long getFollowerCount() {
        return author.getFollowersCount();
    }
    
    public long getRetweetCount() {
        return rankInfo.getRetweetCount();
    }

    public LocalTweet getTweet() {
        return tweet;
    }
    
    public User getAuthor() {
        return author;
    }

    public boolean isFavorited() {
        return rankInfo.isFavorited();
    }

}
