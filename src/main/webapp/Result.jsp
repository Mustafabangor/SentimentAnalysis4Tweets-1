<%-- 
    Document   : Result
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
            String result[] = (String[]) request.getAttribute("result");
            LinkedList<String> d = (LinkedList<String>) request.getAttribute("timeline");
            LinkedList<Double> dv = (LinkedList<Double>) request.getAttribute("timelineValues");
            StringBuilder dates = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (String s : d) {
                if (dates.length() > 0) {
                    dates.append(',');
                }
                dates.append('"').append(s).append('"');
            }
            for (Double v : dv) {
                if (values.length() > 0) {
                    values.append(',');
                }
                values.append(v);
            }          
        %>     
        
        <h1>Results of the Sentiment Analysis for: <b><%=result[0]%></b></h1>
        
        <script type="text/javascript">
           /* function getQueryVariable(variable) {
                var query = window.location.search.substring(1);
                var vars = query.split('&');
                for (var i = 0; i < vars.length; i++) {
                    var pair = vars[i].split('=');
                    if (decodeURIComponent(pair[0]) == variable) {
                        return decodeURIComponent(pair[1]);
                    }
                }
                alert('Query variable %s not found' + variable);
            }; */          

            $(function() {
                var dates = [<%=dates.toString()%>];
                var values = [<%=values.toString()%>];
                var optionValues = new Array();
                optionValues[0] = '<%=result[0]%>';
                optionValues[1] = '<%=result[1]%>';
                optionValues[2] = '<%=result[2]%>';   
                optionValues[3] = '<%=result[3]%>';
                alert('Values: ' + optionValues[0] + ' ' + optionValues[1] + ' ' + optionValues[2]);
                $('#container').highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Polarity Results Of '+ optionValues[0]
                    },
                    xAxis: {
                        categories: [optionValues[0]]
                    },
                    yAxis: {
                        title: {
                            text: 'Total Tweets Used In Analysis: ' + optionValues[3]
                        }
                    },
                    series: [{
                            name: 'Positive',
                            data: [parseInt(optionValues[1])]
                        }, {
                            name: 'Negative',
                            data: [parseInt(optionValues[2])]
                        }]
                });
                $('#trend').highcharts({
                    chart: {
                        type: 'line'
                    },
                    title: {
                        text: 'Trend Over Time for ' + optionValues[0]
                    },                                
                    series: [{
                            name: 'Trend',
                            data: values
                        },]
                });
            });
        </script>
        <div id="container" style="width:50%; height:800px; padding: 5em;"></div>    
        <div id="trend" style="width:50%; height:800px; padding: 5em;"></div>   
    </body>
</html>