<%@ page import="at.fhooe.sail.vis.general.EnvData" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Environment Service</title>
</head>
<body>
    <h2>RMI Server Environment Data</h2>
    <table border="1">
        <tr>
            <th>Timestamp</th>
            <th>Sensor</th>
            <th>Value</th>
        </tr>
        <%
            EnvData[] rmiData = (EnvData[]) request.getAttribute("rmiData");
            for (EnvData data : rmiData) {
        %>
        <tr>
            <td><%= data.getTimestamp() %></td>
            <td><%= data.getType() %></td>
            <td><%= Arrays.toString(data.getValues()) %></td>
        </tr>
        <%
            }
        %>
    </table>

    <h2>Socket Server Environment Data</h2>
    <table border="1">
        <tr>
            <th>Timestamp</th>
            <th>Sensor</th>
            <th>Value</th>
        </tr>
        <%
            EnvData[] socketData = (EnvData[]) request.getAttribute("socketData");
            for (EnvData data : socketData) {
        %>
        <tr>
            <td><%= data.getTimestamp() %></td>
            <td><%= data.getType() %></td>
            <td><%= Arrays.toString(data.getValues()) %></td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
