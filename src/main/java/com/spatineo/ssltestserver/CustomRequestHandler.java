package com.spatineo.ssltestserver;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomRequestHandler extends AbstractHandler {
    public CustomRequestHandler() { }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServerHandler.LOG.write("Request: ");
        ServerHandler.LOG.write(request.toString());
        response.setContentType(Const.RESPONSE_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        ServerHandler.LOG.write("Response: ");
        ServerHandler.LOG.write(response.toString());
        out.println("<h1>" + Const.SERVER_GREETING + request.getServerPort() + " " + target + "</h1>");

        baseRequest.setHandled(true);
    }

}
