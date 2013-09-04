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

import com.dhamacher.sentimentanalysis4tweets.sentiment.Thesaurus;
import java.util.HashMap;

/**
 * Search Query Object
 *
 * A search request is more than a single word: it includes related word,
 * stemming, and other preprocessing steps which must be performed before we can
 * start looking at tweets. Thus, we need a Search Query object.
 *
 * @author daniel, mustafa
 */
public class Query {

    private Thesaurus thesaurus;
    private String request;

    public Query(String request) {
        this.request = request;
        this.thesaurus = new Thesaurus(this);
    }

    public String getRequest() {
        return this.request;
    }

    public String toString() {
        return this.request;
    }

    public HashMap<String, Double> getRelated() {
        return thesaurus.getRelated();
    }
}