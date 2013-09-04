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
        <script src="http://code.highcharts.com/modules/exporting.js"></script>
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
            $(function() {
                var optionValues = new Array();
                optionValues[0] = '<%=result[0]%>';
                optionValues[1] = '<%=result[1]%>';
                optionValues[2] = '<%=result[2]%>';
                optionValues[3] = '<%=result[3]%>';
                var dates = [<%=dates.toString()%>];
                var values = [<%=values.toString()%>];
                var count = values.length;
                var v = new Array(count);
                for (var i = 0; i < count; i++) {
                    v[i] = parseInt(values[i]);
                }
                $('#container').highcharts({
                    chart: {
                        zoomType: 'x',
                        spacingRight: 20
                    },
                    title: {
                        text: 'USD to EUR exchange rate from 2006 through 2008'
                    },
                    subtitle: {
                        text: document.ontouchstart === undefined ?
                                'Click and drag in the plot area to zoom in' :
                                'Drag your finger over the plot to zoom in'
                    }, 
                    xAxis: {
                        type: 'datetime',
                        maxZoom: 14 * 24 * 3600000, // fourteen days
                        title: {
                            text: null
                        }
                    },
                    yAxis: {
                        title: {
                            text: 'Exchange rate'
                        }
                    },
                    tooltip: {
                        shared: true
                    },
                    legend: {
                        enabled: false
                    },
                    plotOptions: {
                        area: {
                            fillColor: {
                                linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
                                stops: [
                                    [0, Highcharts.getOptions().colors[0]],
                                    [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                                ]
                            },
                            lineWidth: 1,
                            marker: {
                                enabled: false
                            },
                            shadow: false,
                            states: {
                                hover: {
                                    lineWidth: 1
                                }
                            },
                            threshold: null
                        }
                    },
                    series: [{
                            type: 'area',
                            name: 'Trend for ' + optionValues[0],
                            pointInterval: 24 * 3600 * 1000,
                            pointStart: Date.UTC(2013, 08, 01),
                            data: v
                        }]
                });
            });















            /*******************************************************************/

            /*$(function() {
             var dates = [<%=dates.toString()%>];
             var values = [<%=values.toString()%>];
             var count = values.length;
             var v = new Array(count);
             for (var i = 0; i < count; i++) {
             v[i] = parseInt(values[i]);                   
             }   
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
             text: 'Polarity Results Of ' + optionValues[0]
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
             data: v
             },]
             });
             });*/
        </script>
        <div id="container" style="width:80%; height:800px; padding: 5em;"></div>    
        <div id="trend" style="width:50%; height:800px; padding: 5em;"></div>   
    </body>
</html>
