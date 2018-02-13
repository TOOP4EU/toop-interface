package eu.toop.iface.servlet;

import eu.toop.iface.ITOOPInterfaceDC;
import eu.toop.iface.ToopInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToopInterfaceServletDC extends HttpServlet {

    private Map<String, ITOOPInterfaceDC> endpoints = new HashMap<String, ITOOPInterfaceDC>();

    public void addEndpoint(String pathInfo, ITOOPInterfaceDC endpoint) {
        endpoints.put(pathInfo, endpoint);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        ToopInterface.getInterfaceDC().doGet(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        ToopInterface.getInterfaceDC().doPost(req, resp);
    }
}
