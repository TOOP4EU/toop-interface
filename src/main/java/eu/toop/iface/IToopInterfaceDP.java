package eu.toop.iface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IToopInterfaceDP {
  void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException;
}
