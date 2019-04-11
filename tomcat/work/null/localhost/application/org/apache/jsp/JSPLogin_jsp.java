package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class JSPLogin_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\r\n");
      out.write("\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("<h1>ECommittee</h1>\r\n");
      out.write("<hr />\r\n");
      out.write("\r\n");
      out.write("<h2>Login</h2>\r\n");
      out.write("\r\n");
      out.write("<form action=\"ServletLogin\" method=\"post\">\r\n");
      out.write("\r\n");
      out.write("<table>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td width=\"54\">\r\n");
      out.write("\t\t\tRole:  \r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td width=\"144\">\r\n");
      out.write("            <div align=\"left\">\r\n");
      out.write("                <select name=\"role\">\r\n");
      out.write("                  <option>Administrator</option>\r\n");
      out.write("                  <option>Author</option>\r\n");
      out.write("                  <option>Chair</option>\r\n");
      out.write("                  <option>Committee Member</option>\r\n");
      out.write("                  <option>Coordinator</option>\r\n");
      out.write("                  <option>Reviewer</option>\r\n");
      out.write("                </select>\r\n");
      out.write("          </div></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td width=\"54\">\r\n");
      out.write("\t\t\tUsuario:  \r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t<input type=\"text\" name=\"user\"> \r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td width=\"54\">\r\n");
      out.write("\t\t\tSenha: \t  \r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t<input type=\"password\" name=\"password\">\r\n");
      out.write("\t\t</td>\t\r\n");
      out.write("\t</tr>\r\n");
      out.write("</table>\r\n");
      out.write("<p>\r\n");
      out.write("\t<input type=\"hidden\" value=\"formLogin\" name=\"formSubmitted\">\r\n");
      out.write("\t<input type=\"submit\" value=\"OK\">\r\n");
      out.write("</p>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<hr />\r\n");
      out.write("\r\n");
      out.write("<h4><i><a href=\"/application/ServletSignAuthor\">Sign Up as Author</a></i></h3>\r\n");
      out.write("\t\r\n");

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
