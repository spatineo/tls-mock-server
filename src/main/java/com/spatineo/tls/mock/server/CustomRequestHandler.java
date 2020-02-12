package com.spatineo.tls.mock.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class CustomRequestHandler extends AbstractHandler {
    private String CUSTOM_RESPONSE;
    private File CUSTOM_RESPONSE_FILE;

    public CustomRequestHandler(){}

    public CustomRequestHandler(String customResponse, String customResponseFilePath) {
        CUSTOM_RESPONSE = customResponse;

        if(customResponseFilePath != null) {
            File customResponseFile = new File(customResponseFilePath);
            CUSTOM_RESPONSE_FILE = (customResponseFile.exists()) ? customResponseFile : null;
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String delay = System.getProperty(Const.PROPERTY_RESPONSE_DELAY);
        try {
            if(!ServerHandler.isEmpty(delay)) {
                Thread.sleep(Integer.parseInt(delay));
            }
        } catch (InterruptedException e) {
            throw new ServletException(e.getCause());
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }

        String mimeType;
        PrintWriter out = response.getWriter();
        if(CUSTOM_RESPONSE_FILE == null) {
            mimeType = request.getHeader(Const.HEADER_ACCEPT);
            mimeType = (!ServerHandler.isEmpty(mimeType)) ? mimeType.split(Const.SEPARATOR)[0] : Const.CONTENT_TYPE_PLAIN;

            out.println(!ServerHandler.isEmpty(CUSTOM_RESPONSE) ? CUSTOM_RESPONSE : Const.DEFAULT_RESPONSE);
        } else {
            mimeType = Const.CONTENT_TYPE_PLAIN;
            response.setHeader(Const.HEADER_CONTENT_TYPE, "attachment; filename=" + CUSTOM_RESPONSE_FILE.getName());

            BufferedReader br = new BufferedReader(new FileReader(CUSTOM_RESPONSE_FILE));
            String line;
            while((line = br.readLine()) != null) {
                out.println(line);
            }
        }

        response.setContentType(mimeType);
        response.setStatus(HttpServletResponse.SC_OK);

        baseRequest.setHandled(true);
        System.out.println("Response: ");
        System.out.println(response.toString());

    }
}
