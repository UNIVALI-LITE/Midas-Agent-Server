package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class JSPSignAuthor_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
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
      out.write("<h1>ECommittee</h1>\r\n");
      out.write("<hr />\r\n");
      out.write("\r\n");
      out.write("<h2>Sign Up - Author</h2>\r\n");
      out.write("<hr />\r\n");
      out.write("\r\n");
      out.write("<p>\r\n");
      out.write("\tFill the form to sign as a new author.\r\n");
      out.write("\t\r\n");
      out.write("\t<form action=\"ServletSignAuthor\">\r\n");
      out.write("\t\r\n");
      out.write("\t<table>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"70\">\r\n");
      out.write("\t\t\t\tName:  \r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\t\t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"name\">\t\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"70\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tInstitute: \t\t  \t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"institute\">\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\t\t\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"70\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tInstitute: \t\t  \t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"email\">\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"70\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tUsername: \t\t  \t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\r\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"username\">\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t\t<td width=\"70\">\t\t\t\t\t\r\n");
      out.write("\t\t\t\tPassword: \t\t  \t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t\t<td>  \t\r\n");
      out.write("\t\t\t\t<input type=\"password\" name=\"password\">\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("\t\t</tr>\t\r\n");
      out.write("\t</table>\t\t\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\t<input type=\"hidden\" value=\"formSignAuthor\" name=\"formSubmitted\">\r\n");
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
