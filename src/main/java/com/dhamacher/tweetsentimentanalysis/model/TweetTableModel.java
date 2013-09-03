/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhamacher.tweetsentimentanalysis.model;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author daniel
 */
public class TweetTableModel extends DefaultTableModel {
    
   private static EntityManager em = 
            TweetEntityManagerFactory.getEntityManager("Tweet");  
        
    private Object[][] data;   
    private String[] header = {"Query", "Tweet Context", "Date" };    

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public int getRowCount() {
        return em.createNamedQuery("findAllTweets").getResultList().size();
    }  

    @Override
    public Object getValueAt(int row, int column) {
        return data[row][column];
    } 

    public Object[][] getData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        List<Tweet> result = em.createNamedQuery("findAllTweets").getResultList();
        int i = 0;
        for (Tweet t : result) {
            data[i][0] = t.getSearchToken();
            data[i][1] = t.getText();
            data[i][2] = t.getCreatedOn();
            i++;
        }        
        return data;
    }
    
    
}