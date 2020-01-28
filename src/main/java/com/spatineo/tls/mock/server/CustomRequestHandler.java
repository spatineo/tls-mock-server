package com.spatineo.tls.mock.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomRequestHandler extends AbstractHandler {
    private String contentType;
    public CustomRequestHandler() {
        contentType = Const.CONTENT_TYPE_HTML;
    }

    public CustomRequestHandler(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType(contentType);
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.println(getResponseMessage(Integer.toString(request.getServerPort()), request.getContextPath()));

        System.out.println("Response: ");
        System.out.println(response.toString());

        baseRequest.setHandled(true);
    }

    private String getResponseMessage(String port, String contextpath) {
        String message;
        if(contentType.equals(Const.CONTENT_TYPE_HTML)) {
            message = "<h1>" + Const.SERVER_GREETING + " " + port + " " + contextpath + "</h1>";
        } else if(contentType.equals(Const.CONTENT_TYPE_JSON)) {
            message = "{\"message\":\"" + Const.SERVER_GREETING + "\", \"port\":\"" + port + "\",\"path\":\"" + contextpath + "\"}";
        } else if(contentType.equals(Const.CONTENT_TYPE_XML)) {
            message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><message>" + Const.SERVER_GREETING + "</message><port>" + port + "</port><path>" + contextpath + "</path></response>";
        }
         else {
            message = "Hello World!";
        }
        return message;
    }
}
