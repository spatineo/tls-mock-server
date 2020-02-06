package com.spatineo.tls.mock.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomRequestHandler extends AbstractHandler {
    private String contentType;
    private String DEFAULT_RESPONSE;
    private String CUSTOM_RESPONSE;
    private File CUSTOM_RESPONSE_FILE;

    public CustomRequestHandler(String contentType) {
        this.contentType = contentType;
    }

    public CustomRequestHandler(int port, String contextPath, String customResponse, String customResponseFilePath) {
        DEFAULT_RESPONSE = Const.SERVER_GREETING + " " + port + " " + contextPath;
        CUSTOM_RESPONSE = customResponse;

        if(customResponseFilePath != null) {
            File customResponseFile = new File(customResponseFilePath);
            CUSTOM_RESPONSE_FILE = (customResponseFile.exists()) ? customResponseFile : null;
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String mimeType;
        if(CUSTOM_RESPONSE_FILE == null) {
            mimeType = request.getHeader("Accept");
            mimeType = (mimeType != null) ? mimeType : "text/plain";

            PrintWriter out = response.getWriter();
            out.println((CUSTOM_RESPONSE != null && CUSTOM_RESPONSE.trim() != "") ? CUSTOM_RESPONSE : DEFAULT_RESPONSE);
        } else {
            mimeType = Const.CONTENT_TYPE_PLAIN;
            response.setHeader("Content-Disposition", "attachment; filename=" + CUSTOM_RESPONSE_FILE.getName());
        }

        response.setContentType(mimeType);
        response.setStatus(HttpServletResponse.SC_OK);

        System.out.println("Response: ");
        System.out.println(response.toString());

        baseRequest.setHandled(true);
    }
}
