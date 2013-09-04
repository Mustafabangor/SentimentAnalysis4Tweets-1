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
package com.dhamacher.sentimentanalysis4tweets.servlet;
import com.dhamacher.sentimentanalysis4tweets.common.LocalTweet;
import com.dhamacher.sentimentanalysis4tweets.database.Connector;
import com.dhamacher.sentimentanalysis4tweets.search.Search;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author daniel, mustafa
 */
public class SentimentController extends HttpServlet {
    
    SentimentScriptEngine jsEngine = new SentimentScriptEngine();
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
        response.setContentType("text/html;charset=UTF-8");
        /* If Compare button pressed use DB to pull scores from sentients table */
        if (request.getParameter("compare") != null) {
            compare(request, response);
            RequestDispatcher dispatch = getServletConfig().getServletContext().getRequestDispatcher("/Comparison.jsp");
            dispatch.forward(request, response);            
        /* Otherwise pull tweets from TwitterAPI and DB, analyze and visualize */
        } else {
            RequestDispatcher dispatch = getServletConfig()
                    .getServletContext().getRequestDispatcher("/Result.jsp");
            String q = request.getParameter("token");
            Search s = new Search(q, 2);
            Results r = new Results(s);
            String result[] = {r.getEntity(), "" + r.getPositive(), "" + r.getNegative(), "" + s.getTweets().size()};
            request.setAttribute("result", result);
            request.setAttribute("timeline", r.getDates());
            request.setAttribute("timelineValues", getTimeLineData(s.getTweets(), s.getQuery().getRequest()));
            dispatch.forward(request, response);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void compare(HttpServletRequest request, HttpServletResponse response) {        
        try {
            /* Get DB connection */
            Connection con = new Connector("twitter_sentiment").getCon();
            /* Set query and execute */
            String query = "SELECT value FROM entity";
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);
            ResultSet rs = (ResultSet) ps.executeQuery();
            /* Mechanism to count the rows in the DB Instance of entity */
            int rowCount;
            int currentRow = rs.getRow();
            rowCount = rs.last() ? rs.getRow() : 0;
            if (currentRow == 0) {
                rs.beforeFirst();
            } else {
                rs.absolute(currentRow);
            }
            /* Copy data to arrays */
            String brands[] = new String[rowCount];
            int i = 0;
            while (rs.next()) {
                brands[i] = rs.getString(1);
                i++;
            }
            String positive[] = new String[brands.length];
            String negative[] = new String[brands.length];
            int j = 0;
            for (String s : brands) {
                Results result = new Results(s);                
                positive[j] = "" + result.getPositive();
                negative[j] = "" + result.getNegative();
                j++;
            }
            /*jsEngine.addBinding("positiveComparison", positive);
            jsEngine.addBinding("negativeComparison", negative);
            jsEngine.addBinding("brandsComparison", positive);*/
            request.setAttribute("positive", positive);
            request.setAttribute("negative", negative);    
            request.setAttribute("brands", brands);
        } catch (SQLException ex) {
            Logger.getLogger(SentimentController.class.getName()).log(Level.SEVERE, null, ex);        
        } 
    }

    /**
     * Method used to generate the data that is used in the timeline chart.
     *
     * @param tweets The collection of tweets pulled.
     * @param query THe search string
     * @return A LinkedList with double values that was generated
     */
    private LinkedList<Double> getTimeLineData(LinkedList<LocalTweet> tweets, String query) {
        LinkedList<Double> values = new LinkedList<Double>();
        double max = 0.0, score = 0.0;
        int interval = Math.max(1, (int) tweets.size() / 100);
        // Extract tweet data:
        for (int i = 0; i < tweets.size(); i++) {
            score += tweets.get(i).getSentimentScore(query);
            // If this score exceeds the max score recorded so far:
            if (Math.abs(score) > max) {
                max = Math.abs(score);
            }
            // Only add values at interval number of tweets:
            if (i % interval == 0) {
                values.add(score);
            }
        }
        // Normalize values.
        for (int i = 0; i < values.size(); i++) {
            values.set(i, 51 + values.get(i) * 100 / (max * 2.1));
        }
        // Create and return a new Data object containing these values:
        return values;
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
