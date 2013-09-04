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
import com.dhamacher.sentimentanalysis4tweets.common.Sentiment;
import com.dhamacher.sentimentanalysis4tweets.common.TweetRankInfo;
import com.dhamacher.sentimentanalysis4tweets.common.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
/**
 * Class used to operate the two Database used for the analysis of tweets.
 * 
 * @author daniel, mustafa
 */
public class Operator implements OperatorInterface {   

    /* Connection objects */
    private Connector con;
    private Connector conLex;
    private static Operator instance;

    private Operator() {
        con = new Connector("twitter_sentiment");   // Databse holds complete analysis
        conLex = new Connector("twitter_lexicon");  // Stores words with sentiment scores used for analysis
    }

    /**
     * Singleton Operator instance getter.
     *
     * This prevents multiple connections to the same database, and makes it
     * easier to get a DB operator reference.
     *
     * @return Operator instance.
     */
    public static Operator getInstance() {
        if (instance == null) {
            instance = new Operator();
        }
        assert (instance != null);
        return instance;
    }

   /**
    * Stores tweets inside the database
    * @param tw The tweet to save
    */
    @Override
    public void storeTweet(Status tw) {
        /* Extract the date of the tweet in a certain format */
        Date date = (Date) tw.getCreatedAt();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(date);
        //Pagerank pgtw = new Pagerank(author);
        try {
            /* Prepare query to execute to the database */
            String query = "INSERT INTO tweets(id, content, author, date) VALUES(?,?,?,?)";
            PreparedStatement stmt = (PreparedStatement) con.getCon().prepareStatement(query);
            stmt.clearParameters();
            /* Set the record attribuuttes using the values from the tweet instance */
            stmt.setLong(1, tw.getId());
            stmt.setString(2, tw.getText().replaceAll("'", ""));
            stmt.setString(3, tw.getUser().getName());
            stmt.setString(4, currentTime);
            stmt.addBatch();
            /* Execute query */
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the entityId from the database. If the entity is missing insert it
     * and return the new id
     *
     * @return The id of the entity i.e the search word used for pulling the
     * tweets
     */
    public long getEntityId(String entity) {
        long entityId = 0;
        try {
            /* Execute query to database */
            String query = "SELECT id FROM entity "
                    + "WHERE value = ?";
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ps.setString(1, entity);
            ResultSet rs = (ResultSet) ps.executeQuery();
            /* Match record with 'where' clause credentials */
            if (rs.next()) {
                entityId = rs.getLong(1);
                System.out.println("The entity id is: " + entityId);
                /* Record is non-existend and inserted into the database */
            } else {
                System.out.println("N-are");
                query = "INSERT INTO entity(value, score) VALUES(?, ?)";
                ps = con.getCon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity);
                ps.setDouble(2, 0);
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                con.getCon().commit();
                /* Return id recently inserted from above */
                if (rs.next()) {
                    entityId = rs.getLong(1);
                    System.out.println("The entity id is: " + entityId);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entityId;
    }

    /**
     * Store a number of tweets. Inserting more tweets at once is less costly
     * than doing it for each in the list
     *
     * @param list
     */
    @Override
    public void storeTweetBatch(LinkedList<LocalTweet> list, String entity) {
        String query = "INSERT IGNORE INTO  `tweets`"
                + " (id, content, author, date, pagerank)"
                + " VALUES (?, ?, ?, ?, ?)";
        try {
            /* Count the tweet instances to be inserted in the Databse */
            int count = 0;
            PreparedStatement statement = con.getCon().prepareStatement(query);
            /* Retrieve tweets from local structure and set record attributes */
            for (LocalTweet tweet : list) {
                ++count;
                statement.setLong(1, tweet.getId());
                statement.setString(2, tweet.getText());
                statement.setString(3, tweet.getFromUser());
                statement.setString(4, tweet.getCreatedString());
                statement.setDouble(5, tweet.getPageRank());
                statement.addBatch();
                /* If the number of tweets counts one thousand, execute the batch process */
                if (count % 1000 == 0) {
                    count = 0;
                    statement.executeBatch();
                }
            }
            /* Execute the tweets pulled from the structure */
            statement.executeBatch();
            /* Insert record  into the sentiment database */
            long entityId = getEntityId(entity);
            query = "INSERT INTO sentiments(tweet_id, entity_id, score) VALUES(?,?,?)";
            statement = con.getCon().prepareStatement(query);
            /* Batch process to either insert one thousand at a time or whatever the size of the list is */
            count = 0;
            for (LocalTweet tw : list) {
                ++count;
                long id = tw.getId();
                double sentimentScore = tw.getSentimentScore(entity);
                System.out.println(id + " " + entityId + " " + sentimentScore);
                /* Prepare query and set attributes */
                statement.setLong(1, id);
                statement.setLong(2, entityId);
                statement.setDouble(3, sentimentScore);
                statement.addBatch();
                if (count % 1000 == 0) {
                    count = 0;
                    statement.executeBatch();
                }
            }
            /* Execute batch and commit to the database */
            statement.executeBatch();
            con.getCon().commit();
        } catch (SQLException s) {
            System.out.println("It's not saving because the following error occurs ");           
        }
    }
    
    /**
     * Get all the tweets that currently reside inside the database and match a
     * certain key.
     *
     * @param key The search token
     * @return A list of the tweets that meet the credentials of the key
     */
    @Override
    public LinkedList<String> getTweets(String key) {
        LinkedList<String> list = new LinkedList<String>();
        try {
            String query = "SELECT content FROM tweets "
                    + "WHERE MATCH (content)  AGAINST ('" + key + "' IN BOOLEAN MODE);";
            con.getCon().setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Get the tweets that resides locally on the databse
     *
     * @param key THe search token
     * @return A list of all the tweets matching the key attribute
     */
    @Override
    public LinkedList<LocalTweet> getLocalTweets(String key) {
        LinkedList<LocalTweet> list = new LinkedList<LocalTweet>();
        try {
            String query =
                    "SELECT tweets.id, author, content, date, pagerank, "
                    + "       entity.value, sentiments.score, sentiments.id "
                    + "FROM tweets "
                    + "  JOIN sentiments ON (tweets.id = sentiments.tweet_id) "
                    + "  JOIN entity ON (sentiments.entity_id = entity.id) "
                    + "WHERE MATCH (content)  AGAINST ('" + key + "' IN BOOLEAN MODE) ORDER BY tweets.id;";
            System.err.println(query);
            con.getCon().setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                Sentiment s = new Sentiment();
                s.setEntity(rs.getString(6));
                s.setScore(rs.getDouble(7));
                s.setId(rs.getLong(8));
                if (!list.isEmpty() && list.get(list.size() - 1).getId() == rs.getLong(1)) {
                    list.get(list.size() - 1).addSentiment(key, s);
                } else {
                    LocalTweet tweet = new LocalTweet();
                    tweet.setId(rs.getLong(1));
                    tweet.setAuthor(rs.getString(2));
                    tweet.setContent(rs.getString(3));
                    try {
                        tweet.setDate(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString(4)));
                    } catch (ParseException ex) {
                        Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tweet.setPageRank(rs.getLong(5));
                    tweet.addSentiment(key, s);
                    list.add(tweet);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Stores the sentiment record
     * @param tweet_id The tweet id
     * @param entity_id THe search token id
     * @param sentiment The sentiment score
     * @param score THe overall score
     */
    @Override
    public void storeSentiment(long tweet_id, long entity_id, double sentiment, double score) {
        try {
            String query = "INSERT INTO sentiments(tweet_id, entity_id, score) VALUES(?,?,?)";
            PreparedStatement stmt = (PreparedStatement) con.getCon().prepareStatement(query);
            stmt.clearParameters();
            stmt.setLong(1, tweet_id);
            stmt.setLong(2, entity_id);
            stmt.setDouble(3, score);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Insert a user object into the database
     * @param user User object
     */
    public void storeUser(User user) {
        try {
            // Don't add a user more than once.
            if (getUser(user.getId()) != null) {
                return;
            }
            String query = "INSERT INTO users(id, followers_count,"
                    + "friends_count, statuses_count) VALUES(?,?,?,?)";
            PreparedStatement statement = (PreparedStatement) con.getCon().prepareStatement(query);
            statement.clearParameters();
            statement.setLong(1, user.getId());
            statement.setInt(2, user.getFollowersCount());
            statement.setInt(3, user.getFriendsCount());
            statement.setInt(4, user.getStatusesCount());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE,
                    "Error while storing user info", ex);
        }
    }

    /**
     * Get the twitter user from a tweet
     * @param id THe tweet id
     * @return THe user object
     */
    public User getUser(long id) {
        User user = null;
        try {
            String query = "SELECT followers_count,friends_count,statuses_count FROM users "
                    + "WHERE id = ?";
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                user = new User(id, rs.getInt(1), rs.getInt(2), rs.getInt(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE,
                    "Error while retrieving user with id " + id, ex);
        }
        return user;
    }

    /**
     * Retrieve the tweet rank info object
     * @param id id of the tweet
     * @return THe rank info object
     */
    public TweetRankInfo getTweetRankInfo(long id) {
        TweetRankInfo tweetRankInfo = null;
        try {
            String query = "SELECT retweet_count,is_favorited FROM tweet_rank_info "
                    + "WHERE id = ?";
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                tweetRankInfo = new TweetRankInfo(id, rs.getLong(1), rs.getBoolean(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE,
                    "Error while retrieving tweet rank info with id " + id, ex);
        }
        return tweetRankInfo;
    }

    /**
     * Store the information of the tweet on how it is ranked.
     * @param tweetRankInfo Rank info object
     */
    public void storeTweetRankInfo(TweetRankInfo tweetRankInfo) {
        try {
            if (getTweetRankInfo(tweetRankInfo.getId()) != null) {
                return;
            }
            String query = "INSERT INTO tweet_rank_info(id, retweet_count,"
                    + "is_favorited) VALUES(?,?,?)";
            PreparedStatement statement = (PreparedStatement) con.getCon().prepareStatement(query);
            statement.clearParameters();
            statement.setLong(1, tweetRankInfo.getId());
            statement.setLong(2, tweetRankInfo.getRetweetCount());
            statement.setBoolean(3, tweetRankInfo.isFavorited());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE,
                    "Error while storing retweet rank info", ex);
        }
    }

    /**
     * It retrieves a sentiment based on a keyword(tweet id)
     *
     * @param con
     * @param key tweet_id
     * @return LinkedList with sentiments (not sure about the format, check it
     * later) //TODO CHECK LATER FUNCTIONALITY WHEN THE OTHERS PRODUCE DATA
     */
    @Override
    public double getSentiment(long key) {
        double score = 0.0;
        try {
            String query = "SELECT score FROM sentiments "
                    + "WHERE tweet_id = ?";
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ps.setLong(1, key);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                score = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return score;
    }

    /* Used to create the lexicon database, please use Dump files inside the project folder. */
    @Override
    public void fillLexicon() {
        /*
        try {
            String filename1 = "C://Projects//Java//trunk//documentation//lexicon//mpqa.lex";
            String filename2 = "C://Projects//Java//trunk//documentation//lexicon//inquirer.lex";
            String filename3 = "C://Projects//Java//trunk//documentation//lexicon//modifiers.txt";
            String tablename = "lexicon";
            String tablename2 = "modifiers";
            Statement stmt = (Statement) conLex.getCon().createStatement();
            stmt.executeUpdate("LOAD DATA INFILE '" + filename1 + "' INTO TABLE "
                    + tablename + " FIELDS TERMINATED BY ':' LINES TERMINATED BY '\\r\\n' (word, sentiment)");
            stmt.executeUpdate("LOAD DATA INFILE '" + filename2 + "' INTO TABLE " + tablename + " FIELDS TERMINATED BY ':' LINES TERMINATED BY '\\r\\n' (word, sentiment)");
            stmt.executeUpdate("LOAD DATA INFILE '" + filename3 + "' INTO TABLE "
                    + tablename2 + " FIELDS TERMINATED BY ':' LINES TERMINATED BY '\\r\\n' (modifier, value)");
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
   

    /**
     * Retrieve the entire lexicon from the database.
     *
     * These are key-value pairs of words and their sentimental value.
     */
    public HashMap<String, Double> getLexicon() throws SQLException {
        HashMap<String, Double> lexicon = new HashMap<String, Double>();
        String query = "SELECT `word`, `sentiment` FROM `lexicon`";
        PreparedStatement statement = conLex.getCon().prepareStatement(query);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            lexicon.put(result.getString(1), result.getDouble(2));
        }
        return lexicon;
    }

    /**
     * Insert an entity record into the entity database
     * @param value THe search token i.e 'Apple' or 'Blackberry' etc.
     * @param score The sentiment score
     */
    @Override
    public void storeEntity(String value, double score) {

        try {
            String query = "INSERT INTO entity(value,score) VALUES(?,?)";           
            PreparedStatement stmt = (PreparedStatement) con.getCon().prepareStatement(query);            
            stmt.clearParameters();          
            stmt.setString(1, value);
            stmt.setDouble(2, score);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * It returns the score of a single entity based on the key
     *
     * @param key (the entity value)
     * @return the score of this entity (e.g. apple has value 2) //TODO CHECK
     * HOW THE key IS GONNA BE SEARCHED
     */
    public double getEntityScore(String key) {        
        return 0.0;
    }

    /**
     * It stores an entity and all the related words
     *
     * @param entity The search token
     * @param related_word WOrd that relates to the search token
     * @param score The sentiment score
     */
    @Override
    public void storeEntityRelatedWord(String entity, String related_word, double score) {
        try {
            String query = "INSERT INTO entity_relatedword(entity, related_word, score) VALUES(?,?,?)";            
            PreparedStatement stmt = (PreparedStatement) con.getCon().prepareStatement(query);            
            stmt.clearParameters();          
            stmt.setString(1, entity);
            stmt.setString(2, related_word);
            stmt.setDouble(3, score);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * It returns a list of related words with the entity
     *
     * @param key_entity
     * @return a List of the related words and the relational score
     */
    @Override
    public HashMap<String, Double> getRelatedWord(String key_entity) {
        HashMap<String, Double> list = new HashMap<String, Double>();
        try {
            String query = "SELECT related_word,relation_score FROM entity_relatedword "
                    + "WHERE MATCH (entity)  AGAINST ('" + key_entity + "' IN BOOLEAN MODE);";
            con.getCon().setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                list.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Store the lexicon database inside a hash map with the word as key and the 
     * sentiment score as value.
     * @param key The English word
     * @return The hash map of the lexicon database
     */
    public HashMap<String, Double> getLexiconEntries(String key) {
        HashMap<String, Double> list = new HashMap<String, Double>();
        try {
            String query = "SELECT word,sentiment FROM entity_relatedword "
                    + "WHERE MATCH (word)  AGAINST ('" + key + "' IN BOOLEAN MODE);";
            con.getCon().setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                list.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Close the database connection 'twitter_sentiment'.
     */
    public void closeCon() {
        con.closeCon();
    }

    /**
     * Closes the lexicon database connection 'twitter_lexicon'
     */
    public void closeConLex() {
        conLex.closeCon();
    }

    /**
     * Retrieve the smiley symbols and assign them to a hash map
     * @return The hash map with smileys mapping to a sentiment score 
     */
    @Override
    public HashMap<String, Double> getSmileys() {
        HashMap<String, Double> list = new HashMap<String, Double>();
        try {
            String query = "SELECT smiley,value FROM smileys;";
            con.getCon().setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) conLex.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                list.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Retrieve and store the values from the modifier table into an hash map
     * @return The hash map that consist of the modifiers and the sentiment score
     */
    @Override
    public HashMap<String, Double> getModifiers() {
        HashMap<String, Double> list = new HashMap<String, Double>();
        try {
            String query = "SELECT modifier,value FROM modifiers;";
            con.getCon().setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) conLex.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                list.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Retrieve and store the negation table inside a hash map
     * @return The hash map that maps negations to a sentiment score
     */
    @Override
    public HashMap<String, Double> getNegations() {
        HashMap<String, Double> list = new HashMap<String, Double>();
        try {
            String query = "SELECT negation,value FROM negations;";
            con.getCon().setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) conLex.getCon().prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                list.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Retrieves the id of the last tweet that has been inserted to the database.
     * This method is used to continue the inserts after the last tweet.
     * @param entity The search token
     * @return The id of the last tweet id
     */    
    @Override
    public long getIdOfLatestTweetForThisEntity(String entity) {
        long result = 0;
        try {
            String query = "SELECT MAX(tweet_id) FROM sentiments WHERE entity_id = ?";
            PreparedStatement ps = (PreparedStatement) con.getCon().prepareStatement(query);
            long entityId = getEntityId(entity);
            ps.setLong(1, entityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}///:~