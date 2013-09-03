/* SentimentAnalysis4Tweets uses Sentiment Analysis on Tweets with SVM. * 
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

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;

/**
 *
 * @author Daniel Hamacher
 */
public class TweetClassification {
    
    Instances data;
    FastVector nominalAttributeValues;
    FastVector classAttributesValues;
    FastVector attributes;    
    private final String nameOfSet = "Tweet Analysis";
    private boolean isUpdated;
    
    public TweetClassification() {
        /* Initialize attributes that holds tweet messages */
        attributes = new FastVector(2);
        attributes.addElement(new Attribute("tweet_text", (FastVector) null));
        
        /* Initialize class attributes with values */
        classAttributesValues = new FastVector(4);
        classAttributesValues.addElement("");
        classAttributesValues.addElement("positive");
        classAttributesValues.addElement("neutral");
        classAttributesValues.addElement("negative");
        
        attributes.addElement(new Attribute("class_values"
                , classAttributesValues));
        data = new Instances(nameOfSet, attributes, 500);  
        data.setClassIndex(data.numAttributes()-1);
    }
}
  /*  
    public void refreshData(String msg, String classValues) {
        // Make message into instance.
        Instance instance = makeInstance(msg, data);
// Set class value for instance.
        instance.setClassValue(classValue);
// Add instance to training data.
        m_Data.add(instance);
        m_UpToDate = false;

    }
    
     private Instance makeInstance(String text, Instances data)
    {
// Create instance of length two.
        Instance instance = new Instance(2);
// Set value for message attribute
        Attribute messageAtt = data.attribute("content");
        instance.setValue(messageAtt, messageAtt.addStringValue(text));
// Give instance access to attribute information from the dataset.
        instance.setDataset(data);
        return instance;
    }
     
     
}  /**
     * Classifies a given message.
     *
     * @param message the message content
     * @throws Exception if classification fails
     *
    public void classifyMessage(String message) throws Exception
    {
// Check whether classifier has been built.
        if (m_Data.numInstances() == 0)
        {
            throw new Exception("No classifier available.");
        }
// Check whether classifier and filter are up to date.
        if (!m_UpToDate)
        {
// Initialize filter and tell it about the input format.
            m_Filter.setInputFormat(m_Data);
// Generate word counts from the training data.
            Instances filteredData = Filter.useFilter(m_Data, m_Filter);
// Rebuild classifier.
            m_Classifier.buildClassifier(filteredData);
            m_UpToDate = true;
        }

    }
*/