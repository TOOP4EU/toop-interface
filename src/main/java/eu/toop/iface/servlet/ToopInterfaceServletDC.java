package eu.toop.iface.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.toop.commons.exchange.message.ToopResponseMessage;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.iface.ToopInterfaceManager;

/**
 * This servlet can be included in Java DC implementations to receive messages
 * from the MP. It should read the received ASiC container and extract the
 * {@link ToopResponseMessage} object. This is than forwarded to the
 * {@link IToopInterfaceDC} implementation registered in
 * {@link ToopInterfaceManager}.
 *
 * @author Philip Helger
 */
public class ToopInterfaceServletDC extends HttpServlet {
  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    ToopInterfaceManager.getInterfaceDC().doPost(req, resp);
  }
}
