<%@ include file="include/jspHeader.jspf"%>


<%
System.out.println("AAAAAAqqqqqqqqqqqqqqqqqqqqqqqqqq");
StringBuilder sb = new StringBuilder();
try (BufferedReader reader = request.getReader()) {
    String line;
    while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
    }
}
catch (Exception exzz) {
  System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqq "+exzz);
 throw exzz;//qqqqqqqqqqq 
}
String json = sb.toString();;

webApp.onClientEvent(json);
String js = webApp.getJavaScriptForClient();
if (OAStr.isNotEmpty(js)) {
    System.out.println(""+js);
    out.write(js);
}
%>

<%
// dont add, it will mess up the async method call created
//out.write("console.log('QQQQQQQQQQQQQ call to oa-web-event.jsp qs=" + request.getQueryString() + "');");
%>

