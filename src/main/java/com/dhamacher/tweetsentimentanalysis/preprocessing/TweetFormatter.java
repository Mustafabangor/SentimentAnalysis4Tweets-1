/* SentimentAnalysis4Tweets uses Sentiment Analysis on Tweets with SVM.
 * 
 * Copyright (C) 2013  Daniel Hamacher
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dhamacher.tweetsentimentanalysis.preprocessing;

import com.dhamacher.tweetsentimentanalysis.model.Tweet;
import java.util.Scanner;

/**
 *
 * @author Daniel Hamacher
 */
public class TweetFormatter {

    private Tweet tweet;
    private StringBuilder textBuild;
    private Scanner scan;

    public TweetFormatter(Tweet t) {
        tweet = t;
        scan = new Scanner(t.getText());
        textBuild = new StringBuilder();
    }

    public String getModifiedTweetMessage() {
        while (scan.hasNext()) {
            String temp = scan.next();
            if (temp.contains("http")) {
                continue;
            } else if (temp.contains("@")) {
                continue;
            } else {
                textBuild.append(temp);
            }
        }        
        return textBuild.toString();
    }    

    /*public static void main(String[] args) {
        Tweet t = new Tweet();
        t.setText("@hashish http://google.cA BIMBO");
        TweetFormatter tf = new TweetFormatter(t);
        String results = tf.getModifiedTweetMessage();
        System.out.println(results);
    }*/
}