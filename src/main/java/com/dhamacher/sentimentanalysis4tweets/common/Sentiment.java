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
package com.dhamacher.sentimentanalysis4tweets.common;

/**
 *
 * @author daniel, mustafa
 */
public class Sentiment {
    private long id;
    private String entity;
    private double score;
    
    public long getId() {
        return id;
    }
    
    public String getEntity() {
        return entity;
    }
    
    public double getScore() {
        return score;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setEntity(String entity) {
        this.entity = entity;
    }
    
    public void setScore(double score) {
        this.score = score;
    }   
}