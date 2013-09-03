<%-- 
    Document   : Comparison
    Created on : Sep 2, 2013, 2:28:08 AM
    Author     : daniel
--%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Result of the Sentiment Analysis</title>
        <!-- HIGHCHART -->        
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
        <script src="http://code.highcharts.com/highcharts.js"></script> 
    </head>
    <body>
        <%
            String brands[] = (String[]) request.getAttribute("brands");
            String positive[] = (String[]) request.getAttribute("positive");
            String negative[] = (String[]) request.getAttribute("negative");
            StringBuilder br = new StringBuilder();
            StringBuilder pos = new StringBuilder();
            StringBuilder neg = new StringBuilder();            
            for (String str : brands) {
                if (br.length() > 0) {
                    br.append(',');
                }
                br.append('"').append(str).append('"');
            }
            for (String str : positive) {
                if (pos.length() > 0) {
                    pos.append(',');
                }
                pos.append('"').append(str).append('"');
            }
            for (String str : negative) {
                if (neg.length() > 0) {
                    neg.append(',');
                }
                neg.append('"').append(str).append('"');
            }          
        %>  
        <h1>Results of the Sentiment Analysis for: <b><%=brands.toString()%></b>,</h1>
        <script type="text/javascript">
            $(function() {
                var brands = ['<%=br.toString()%>'];                
                var pos = ['<%=pos.toString()%>'];
                var neg = ['<%=neg.toString()%>'];             
                $('#container').highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Polarity Results Of '
                    },
                    xAxis: {
                        categories: brands
                    },
                    yAxis: {
                        title: {
                            text: 'Total Tweets Used In Analysis: '
                        }
                    },
                    series: [{
                            name: 'Positive',
                            data: pos
                        }, {
                            name: 'Negative',
                            data: neg
                        }]
                });
            });
        </script>
        <div id="container" style="width:50%; height:800px; padding: 5em;"></div>    
        <div id="trend" style="width:50%; height:800px; padding: 5em;"></div>   
    </body>
</html>
