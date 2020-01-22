package com.spatineo.tls.mock.server;

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
        System.out.println("Request: ");
        System.out.println(request.toString());
        response.setContentType(Const.RESPONSE_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        System.out.println("Response: ");
        System.out.println(response.toString());
        out.println("<h1>" + Const.SERVER_GREETING + request.getServerPort() + " " + target + "</h1>");

        baseRequest.setHandled(true);
    }

}
