package eu.toop.iface.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToopInterfaceServlet extends HttpServlet
{
  @Override
  protected void doGet (final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
                                                                                      IOException
  {
    final PrintWriter aPW = resp.getWriter ();
    aPW.write ("<html><body><h1>ToopInterfaceServlet</h1><div>" + req.getParameterMap () + "</div></body></html>");
    aPW.flush ();
    // Don't close aPW!
  }
}
