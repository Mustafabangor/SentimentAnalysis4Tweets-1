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

import java.io.File;
import java.io.IOException;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Daniel Hamacher
 */
public class ARFFConverter {

    public static void convertToArff(String csvInPath, String arffOutPath) 
            throws IOException {
        CSVLoader csvData = new CSVLoader();
        csvData.setSource(new File(csvInPath));
        Instances rawData = csvData.getDataSet();       
        ArffSaver saver = new ArffSaver();
        saver.setInstances(rawData);
        saver.setFile(new File(arffOutPath));
        saver.setDestination(new File(arffOutPath));
        saver.writeBatch();
    }
}

