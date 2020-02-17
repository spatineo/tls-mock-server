package com.spatineo.tls.mock.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
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
        delayResponse();
        //Consider checks for content-type
        response.setContentType((!ServerHandler.isEmpty(request.getHeader(Const.HEADER_ACCEPT))) ? request.getHeader(Const.HEADER_ACCEPT).split(Const.SEPARATOR)[0] : Const.CONTENT_TYPE_PLAIN);

        if (CUSTOM_RESPONSE_FILE == null) {
            response.getWriter().println(!ServerHandler.isEmpty(CUSTOM_RESPONSE) ? CUSTOM_RESPONSE : Const.DEFAULT_RESPONSE);
        } else {
            String[] fileNameParts = CUSTOM_RESPONSE_FILE.getName().split("\\.");
            String fileType = fileNameParts[fileNameParts.length -1];
            response.setHeader(Const.HEADER_CONTENT_TYPE, "attachment; filename=" + CUSTOM_RESPONSE_FILE.getName());

            if(fileType.equals("png") || fileType.equals("jpg")) {
                response.setContentType("image/" + fileType);
                writeImageResponse(response.getOutputStream());
            } else {
                writeFileResponse(response.getWriter());
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);

        baseRequest.setHandled(true);
        System.out.println("Response: ");
        System.out.println(response.toString());

    }

    private void writeFileResponse(PrintWriter out) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(CUSTOM_RESPONSE_FILE));
        String line;
        while ((line = br.readLine()) != null) {
            out.println(line);
        }
    }

    private void writeImageResponse(OutputStream os) throws IOException {
        //OutputStream os = response.getOutputStream();
        InputStream is = new FileInputStream(CUSTOM_RESPONSE_FILE);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
    }

    private void delayResponse() throws ServletException {
        String delay = System.getProperty(Const.PROPERTY_RESPONSE_DELAY);
        try {
            if(!ServerHandler.isEmpty(delay)) {
                Thread.sleep(Integer.parseInt(delay));
            }
        } catch (InterruptedException e) {
            throw new ServletException(e.getCause());
        } catch(NumberFormatException e) {
            throw new ServletException(e.getCause());
        }
    }

}
