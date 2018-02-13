package eu.toop.iface.servlet;

import eu.toop.iface.ToopInterface;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ToopInterfaceServletDP extends HttpServlet {
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        ToopInterface.getInterfaceDP().doPost(req, resp);
    }
}
