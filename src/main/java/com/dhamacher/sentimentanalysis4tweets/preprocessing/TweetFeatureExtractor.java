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
package com.dhamacher.sentimentanalysis4tweets.preprocessing;

import com.csvreader.CsvReader;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author daniel, mustafa
 */
public class TweetFeatureExtractor {

    /**
     * Method which contructs the arff file for weka with the training data
     */
    public static void constructModel() {
        Instances instdata = null;
        try {
            FastVector atts;
            atts = new FastVector();
            atts.addElement(new Attribute("content", (FastVector) null));
            FastVector fvClassVal = new FastVector(4);
            fvClassVal.addElement("");
            fvClassVal.addElement("neutral");
            fvClassVal.addElement("negative");
            fvClassVal.addElement("positive");
            Attribute ClassAttribute = new Attribute("Class", fvClassVal);
            atts.addElement(ClassAttribute);           
            instdata = new Instances("tweetData", atts, 0);
            CsvReader data = new CsvReader("../classified data/traindata.csv");
            int i = 0;
            while (data.readRecord()) {
                double[] vals = new double[instdata.numAttributes()];
                String class_id = data.get(0);
                switch (Integer.parseInt(class_id)) {
                    case 0:
                        class_id = "negative";
                        break;
                    case 2:
                        class_id = "neutral";
                        break;
                    case 4:
                        class_id = "positive";
                        break;
                }
                String tweet_content = data.get(5);
                Instance iInst = new Instance(2);
                iInst.setValue((Attribute) atts.elementAt(0), tweet_content);
                iInst.setValue((Attribute) atts.elementAt(1), class_id);
                instdata.add(iInst);
                System.out.println("[" + i + "] " + class_id + ":" + tweet_content);
                i++;
            }
            data.close();
            StringToWordVector filter = new StringToWordVector();
            instdata.setClassIndex(instdata.numAttributes() - 1);
            filter.setInputFormat(instdata);
            Instances newdata = Filter.useFilter(instdata, filter);
            ArffSaver saver = new ArffSaver();
            saver.setInstances(newdata);
            saver.setFile(new File("./data/train2data.arff"));
            saver.writeBatch();
        } catch (Exception ex) {
            Logger.getLogger(TweetFeatureExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        constructModel();
    }
}