package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;

public final class _005fJSPError_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    Throwable exception = org.apache.jasper.runtime.JspRuntimeLibrary.getThrowable(request);
    if (exception != null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html><body>\r\n");
      out.write("\r\n");
      out.write("<p><h1>Application Error...</h1></p>\r\n");
      out.write("\r\n");
      out.write("<p><h2>Relevant Information</h2></p>\r\n");
      out.write("\r\n");
      out.write("<table summary=\"Error Info\" border=\"1\">\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td>Status Code: </td>\r\n");
      out.write("\t<td>");
      out.print( request.getAttribute("javax.servlet.error.status_code") );
      out.write(" </td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td>Message: </td>\r\n");
      out.write("\t<td>");
      out.print( request.getAttribute("javax.servlet.error.message") );
      out.write(" </td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td>Request URI: </td>\r\n");
      out.write("\t<td>");
      out.print( request.getAttribute("javax.servlet.error.request_uri") );
      out.write(" </td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td>Servlet Name: </td>\r\n");
      out.write("\t<td>");
      out.print( request.getAttribute("javax.servlet.error.servlet_name") );
      out.write(" </td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td>Exception Type: </td>\r\n");
      out.write("\t<td>");
      out.print( request.getAttribute("javax.servlet.error.exception_type").toString().substring(6) );
      out.write(" </td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr>\r\n");
      out.write("\t<td colspan=\"2\">\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t");
 	
			Throwable cause = exception.getCause();
			Throwable excep = exception;
		
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t\t<strong>\r\n");
      out.write("\t\t<font color=\"red\">\r\n");
      out.write("\t\t");

			out.println(excep.getClass().toString().substring(6));
		
      out.write("\r\n");
      out.write("\t\t</font>\r\n");
      out.write("\t\t");

			out.println(": "+excep.getMessage());
		
      out.write("\t\r\n");
      out.write("\t\t");

			while (cause != null)
			{
		
      out.write("\r\n");
      out.write("\t\t<font color=\"red\"><br />\r\n");
      out.write("\t\t");

				out.println(" Caused By "+cause.getClass().toString().substring(6));
		
      out.write("\r\n");
      out.write("\t\t</font>\r\n");
      out.write("\t\t");

				out.println(": "+cause.getMessage());

				excep = cause;
				cause = excep.getCause();
			}
		
      out.write("\r\n");
      out.write("\t\t<br />\r\n");
      out.write("\t\t");

			StackTraceElement[] stack = excep.getStackTrace();
			StackTraceElement item;
						
			for (int i=0;i<stack.length;i++)
			{
		
      out.write("\r\n");
      out.write("\t\t<br /><i>\r\n");
      out.write("\t\t");
		
				item = stack[i];
				out.println("at "+item.toString());
			}	
		
      out.write("\r\n");
      out.write("\t\t</i>\r\n");
      out.write("\t\t\r\n");
      out.write("\t</td>\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("</body></html>\r\n");
      out.write("\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
