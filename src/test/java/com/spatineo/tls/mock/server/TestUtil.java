package com.spatineo.tls.mock.server;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.ServerSocket;

public class TestUtil {
    public static final String PROPERTY_RESPONSE_DELAY = "tls.mock.server.response.delay";
    public static final String HTTP_LOCALHOST = "http://127.0.0.1:";
    public static final String HTTPS_LOCALHOST = "https://127.0.0.1:";
    public static final String ENDPOINT = "/get";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String[] DEFAULT_PROTOCOLS = {"TLSv1.2"};
    public static final String[] DEFAULT_CIPHERS = {"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"};
    public static final Integer[] DEFAULT_PORTS = {8080, 8443};

    public static void setSystemProperties(boolean set, int responseDelay) throws NullPointerException {
        String keystorePath = TestUtil.class.getClassLoader().getResource("keystore").getPath();
        System.setProperty(Const.PROPERTY_KEYSTORE, (set) ? keystorePath : "");
        System.setProperty(Const.PROPERTY_KEYSTORE_PSWD, (set) ? "password" : "");
        if(responseDelay >= 0) {
            System.setProperty(PROPERTY_RESPONSE_DELAY, Integer.toString(responseDelay));
        }
    }

    public static TLSMockServer initAndStartServerWithDefaults(String customResponse, String customFileResponsePath) throws IOException {
        TLSMockServer server =  new TLSMockServer(getAvailablePort());
        server.initTestServer(getAvailablePort(), TestUtil.DEFAULT_CIPHERS, TestUtil.DEFAULT_PROTOCOLS, customResponse, customFileResponsePath);
        server.start();
        return server;
    }

    public static boolean headerExists(Header[] headerArray, String headerName, String headerValue) {
        for(int i = 0; i < headerArray.length; i++) {
            if(headerArray[i].getName().equals(headerName)) {
                return headerArray[i].getValue().startsWith(headerValue);
            }
        }
        return false;
    }

    public static int getAvailablePort() throws IOException {
        ServerSocket s = new ServerSocket(0);
        try {
            s.setReuseAddress(true);
            return s.getLocalPort();
        } finally {
            s.close();
        }
    }

    public static CloseableHttpResponse tryHttpURL(String url, String accept) throws IOException {
        CloseableHttpClient client = null;
        HttpGet request = null;
        try {
            client = HttpClients.createDefault();
            request = new HttpGet(url);
            if(accept != null && accept.trim() != "") {
                request.setHeader(new BasicHeader(HEADER_ACCEPT, accept));
            }
            RequestConfig.Builder requestConfig = RequestConfig.custom();
            requestConfig.setConnectTimeout(5000);
            requestConfig.setConnectionRequestTimeout(5000);
            requestConfig.setSocketTimeout(5000);
            request.setConfig(requestConfig.build());

            return client.execute(request);
        } catch(IOException e) {
            throw e;
        } finally {
            try {
                request.releaseConnection();
                client.close();
            } catch(IOException e) {
                throw e;
            }
        }
    }
}
