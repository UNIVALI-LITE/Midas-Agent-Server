package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class JSPAuthor_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<h2>Autor</h2>\r\n");
      out.write("<hr />\r\n");
      out.write("<h3><i><a href=\"/application/ServletAuthorSubmitArticle\">Submit an Article</a></i></h3>\r\n");
      out.write("<h3><i><a href=\"/application/ServletAuthorSubmitCameraReady\">Submit a Camera Ready</a></i></h3>\r\n");
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
