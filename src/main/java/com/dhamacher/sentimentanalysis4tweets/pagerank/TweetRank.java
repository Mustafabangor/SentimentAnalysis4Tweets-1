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
package com.dhamacher.sentimentanalysis4tweets.pagerank;

import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.common.TweetRankInfo;
import com.dhamacher.sentimentanalysis4tweets.common.User;
import com.dhamacher.sentimentanalysis4tweets.twitterapi.TweetOperator;
import java.util.logging.Logger;


/**
 * Contains the relevant information for static quality computation (pagerank)
 * of a {@code LocalTweet} object.
 * @author daniel, mustafa
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
