<%--    
    Copyright 2013 Mustafa Elkhunni
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
         http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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
            /* Retireve the data computed from the SentimentController servlet */
            String brands[] = (String[]) request.getAttribute("brands");
            String positive[] = (String[]) request.getAttribute("positive");
            String negative[] = (String[]) request.getAttribute("negative");
            
            /* The string builder object are used to pasre java arrays inside js arrays */
            StringBuilder br = new StringBuilder();
            StringBuilder pos = new StringBuilder();
            StringBuilder neg = new StringBuilder(); 
            
            /* Parsing process */
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
        <h1>Results of the Sentiment Analysis for: <b><%=br.toString()%></b>,</h1>
        <script type="text/javascript">
            $(function() {
                var brands = [<%=br.toString()%>];                
                var count = brands.length;
                var pos = [<%=pos.toString()%>];
                var neg = [<%=neg.toString()%>];
                var p = new Array(count);
                var n = new Array(count);
                for (var i = 0; i < count; i++) {
                    p[i] = parseInt(pos[i]);
                    n[i] = parseInt(neg[i]);
                }                  
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
                            data: p
                        }, {
                            name: 'Negative',
                            data: n
                        }]
                });
            });
        </script>
        <div id="container" style="width:50%; height:800px; padding: 5em;"></div>    
        <div id="trend" style="width:50%; height:800px; padding: 5em;"></div>   
    </body>
</html>
