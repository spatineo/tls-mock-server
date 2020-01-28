package com.spatineo.tls.mock.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class CustomFileRequestHandler extends AbstractHandler {
    File file;

    public CustomFileRequestHandler(String filePath) {
        file = new File(filePath);
    }

    public boolean fileExists() {
        return file != null && file.exists();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        PrintWriter pw = response.getWriter();
        if(fileExists()) {
            response.setContentType(Const.CONTENT_TYPE_PLAIN);
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setStatus(HttpServletResponse.SC_OK);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                pw.println(line);
            }
        } else {
            response.setContentType(Const.CONTENT_TYPE_HTML);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            pw.println("<div calss=\"content\"><h1>404</h1><p>FILE NOT FOUND</p></div>");
        }

        baseRequest.setHandled(true);
    }
}
