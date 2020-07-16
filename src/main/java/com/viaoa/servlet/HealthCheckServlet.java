// Copied from OATemplate project by OABuilder 07/06/19 06:25 PM
package com.viaoa.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HealthCheckServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String q = req.getParameter("name");
        PrintWriter out = resp.getWriter();

        String s = req.getQueryString();
        s = req.getContextPath();
        s = req.getLocalAddr();
        s = req.getLocalName();
        int x = req.getLocalPort();  // 8082
        s = req.getMethod();
        s = req.getPathInfo();
        s = req.getRemoteAddr();
        s = req.getRemoteHost();
        s = req.getServletPath(); // "/servlet/hello
        
        
        
        out.println("<html>");
        out.println("<body>");
        out.println("Running");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String name = req.getParameter("name");
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<body>");
        out.println("Running");
        out.println("</body>");
        out.println("</html>");
    }
}
