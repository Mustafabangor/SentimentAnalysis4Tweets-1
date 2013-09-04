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

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author daniel, mustafa
 */
public class TweetClassifier {

    /**
     * The training data gathered so far.
     */
    private Instances m_Data = null;
    /**
     * The filter used to generate the word counts.
     */
    private StringToWordVector m_Filter = new StringToWordVector();
    /**
     * The actual classifier.
     */
    private Classifier m_Classifier = new NaiveBayes();
    private boolean m_UpToDate;

    /* Create empty training dataset*/
    public TweetClassifier() {
        String nameOfDataset = "MessageClassificationProblem";
        FastVector attributes = new FastVector(2);
        attributes.addElement(new Attribute("content", (FastVector) null));
        FastVector classValues = new FastVector(4);
        classValues.addElement("");
        classValues.addElement("neutral");
        classValues.addElement("negative");
        classValues.addElement("positive");
        attributes.addElement(new Attribute("Class", classValues));       
        m_Data = new Instances(nameOfDataset, attributes, 100);
        m_Data.setClassIndex(m_Data.numAttributes() - 1);
    }

    /**
     * Updates model using the given training message.
     *
     * @param message the message content
     * @param classValue the class label
     */
    public void updateData(String message, String classValue) {
        Instance instance = makeInstance(message, m_Data);
        instance.setClassValue(classValue);
        m_Data.add(instance);
        m_UpToDate = false;
    }

    /**
     * Method that converts a text message into an instance.
     *
     * @param text the message content to convert
     * @param data the header information
     * @return the generated Instance
     */
    private Instance makeInstance(String text, Instances data) {
        Instance instance = new Instance(2);
        Attribute messageAtt = data.attribute("content");
        instance.setValue(messageAtt, messageAtt.addStringValue(text));
        instance.setDataset(data);
        return instance;
    }

    /**
     * Classifies a given message.
     *
     * @param message the message content
     * @throws Exception if classification fails
     */
    public void classifyMessage(String message) throws Exception {

        if (m_Data.numInstances() == 0) {
            throw new Exception("No classifier available.");
        }
        if (!m_UpToDate) {
            m_Filter.setInputFormat(m_Data);
            Instances filteredData = Filter.useFilter(m_Data, m_Filter);
            m_Classifier.buildClassifier(filteredData);
            m_UpToDate = true;
        }
    }
}