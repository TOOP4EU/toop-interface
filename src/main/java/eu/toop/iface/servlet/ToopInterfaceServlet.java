package eu.toop.iface.servlet;

import eu.toop.iface.IEndpoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToopInterfaceServlet extends HttpServlet {

    private Map<String, IEndpoint> endpoints = new HashMap<String, IEndpoint>();

    public void addEndpoint(String pathInfo, IEndpoint endpoint) {
        endpoints.put(pathInfo, endpoint);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        IEndpoint endpoint = endpoints.get(pathInfo);
        if (endpoint != null) {
            endpoint.doGet(req, resp);
        } else {
            final PrintWriter aPW = resp.getWriter();
            aPW.write("<html><body><h1>ToopInterfaceServlet (doGet)</h1>");
            aPW.write("<div>No endpoint registered for pathInfo " + pathInfo + "</div>");
            aPW.write("</body></html>");
            aPW.flush();
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        IEndpoint endpoint = endpoints.get(pathInfo);
        if (endpoint != null) {
            endpoint.doPost(req, resp);
        } else {
            final PrintWriter aPW = resp.getWriter();
            aPW.write("<html><body><h1>ToopInterfaceServlet (doPost)</h1>");
            aPW.write("<div>No endpoint registered for pathInfo " + pathInfo + "</div>");
            aPW.write("</body></html>");
            aPW.flush();
        }
    }
}
