/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhamacher.tweetsentimentanalysis.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Daniel Hamacher
 */
public class TweetEntityManagerFactory {   
    
    public static EntityManager getEntityManager(String context) {
        EntityManagerFactory emf = 
                Persistence.createEntityManagerFactory(context);
        return emf.createEntityManager();        
    }   
}
