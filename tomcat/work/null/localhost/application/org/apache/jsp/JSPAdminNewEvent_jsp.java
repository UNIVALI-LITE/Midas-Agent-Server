package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class JSPAdminNewEvent_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/_JSPTop.jsp");
    _jspx_dependants.add("/_JSPBottom.jsp");
  }

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
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

      out.write("<html><body>\r\n");
      out.write("\r\n");
      out.write("<table>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td width=\"200\">\r\n");
      out.write("\t\t\t<h1>ECommittee</h1>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t<form action=\"ServletLogin\" method=\"post\">\r\n");
      out.write("\t\t\t\t<input type=\"hidden\" value=\"formLogout\" name=\"formSubmetido\">\r\n");
      out.write("\t\t\t\t<input type=\"submit\" value=\"Logout\">\r\n");
      out.write("\t\t\t</form>\t\r\n");
      out.write("\t  </td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<hr />\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<h2>System Administrator - Create New Event</h2>\r\n");
      out.write("<hr />\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\tFill the data for the creation of a new event. \r\n");
      out.write("\tDates must be in dd/MM/yyyy format.\r\n");
      out.write("\t\r\n");
      out.write("\t<form action=\"ServletAdminNewEvent\">\r\n");
      out.write("\t\r\n");
      out.write("\t<table>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\r\n");
      out.write("\t\t\t\tName:  \r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"name\">\t\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tSemester: \t\t  \t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"semester\">\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\tBeginDate:\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"beginDate\">\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\tFinishDate:\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"finishDate\">\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td>\r\n");
      out.write("\t\t\t\t<br /><strong>Deadlines</strong>\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tSubmission: \t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"submission\">\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\tAllocation:\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"allocation\">\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tRevision:     \t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"revision\">\t\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tResolution:    \t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"resolution\">\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tCamera Ready:\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"cameraReady\">\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td>\r\n");
      out.write("\t\t\t\t<br />\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\tChair: \t\t\t\t  \r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"chair\">\t\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"110\">\t\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\tCoordenador:\t\t  \r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"revisor\">\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t</table>\t\t\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\t<input type=\"hidden\" value=\"formAdminNewEvent\" name=\"formSubmitted\">\r\n");
      out.write("\t\t<input type=\"submit\" value=\"OK\">\r\n");
      out.write("\t</form>\r\n");
      out.write("</p>\r\n");
      out.write("\r\n");

	if (((String)request.getAttribute("message"))!=null)
	{
		out.println("<hr />");
		out.println((String)request.getAttribute("message"));
		request.removeAttribute("message");
	}

      out.write("\r\n");
      out.write("\r\n");
      out.write("</body></html>");
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
