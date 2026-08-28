<%@ include file="include/jspHeader.jspf"%>

<%
if (webApp != null) webApp.close();


webApp = new OAWebApp();
webApp.create();
oasession.put("OAWebApp", webApp);

String js = webApp.getJavaScriptForClient();
if (OAStr.isNotEmpty(js)) {
    System.out.println(""+js);
    out.write(js);
}
%>

<%
/*
if ("editPanel".equalsIgnoreCase(request.getParameter("cn"))) {
    HtmlElement he = new HtmlElement("panel.panSchools.panRight.panSchoolList.panEdit" + ".edit.tabSchoolInfo.txtName");
    he.getOAHtmlComponent().setEnabled(false);
    String sx = he.getOAHtmlComponent().getEnabledScript();
    //qqq  sx = "import * as utils from './js/util/utils.js';\n" + sx;
    out.write(sx);
}
*/
out.write("console.log('call to oa-web-app.jsp qs=" + request.getQueryString() + "');");
%>

