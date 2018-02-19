package eu.toop.iface;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.toop.commons.exchange.message.ToopResponseMessage;

/**
 * This interface must be implemented by DC receiving components to retrieve
 * incoming requests. The content of the request is an ASiC archive containing a
 * {@link ToopResponseMessage}.
 *
 * @author Anton
 */
public interface IToopInterfaceDC {
  void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException;
}
