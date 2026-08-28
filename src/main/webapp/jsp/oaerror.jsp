<%@page trimDirectiveWhitespaces="true"%>
<%@page language="java" isErrorPage="true"%>
<%@page import="java.io.*, java.util.*, java.awt.*, java.util.logging.*"%>

<%!static Logger LOG = Logger.getLogger("oajsp");%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>System Error</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div class="center-container">
<h1>Oops!</h1>
<h3>System error on this page.  Tech Support is currently being notified ... Please try again later ...</h3>


<a href="index.html""><strong>Return to home page</strong></a>

</div>
    <PRE style="color: orange;">
        <% 
        exception.printStackTrace(new java.io.PrintWriter(out)); 
        LOG.log(Level.WARNING, "Error", exception);        
        %>
    </PRE>

</body>
</html>
