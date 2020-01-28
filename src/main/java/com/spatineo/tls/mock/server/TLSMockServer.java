package com.spatineo.tls.mock.server;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.File;

public class TLSMockServer {
    private Server SERVER;

    /**
     * <p>Class constructer for custom implementation of jetty server</p>
     * @param port The port at which http requests can be made to
     */
    public TLSMockServer(int port) {
        System.out.println("Setting http port to " + port);
        SERVER = new Server(port);
        System.out.println("Server initialized");
    }

    /**
     * <p>Starts the previously defined server</p>
     */
    public void start() {
        try {
            SERVER.start();
            System.out.println("SSL test server started");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Stops the previously defined server</p>
     */
    public void join() {
        try {
            if(SERVER != null) {
                SERVER.join();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *<p>Initialize server SSL configurations</p>
     * @param securePort The port at which https requests are made to
     * @param ciphers An array of cipher suites accepted by the server
     * @param protocols An array of TLS protocol versions accepted by the server
     */
    public void initTestServer(int securePort, String[] ciphers, String[] protocols) {
        System.out.println("Setting https port to " + securePort);
        HttpConfiguration httpsConfig = createHttpsFactory(securePort);
        HttpConnectionFactory httpsCF = new HttpConnectionFactory(httpsConfig);

        SslConnectionFactory ssl = createSslConnectioFactory(ciphers, protocols);

        ServerConnector httpsConnector = new ServerConnector(SERVER, ssl, httpsCF);
        httpsConnector.setPort(securePort);

        SERVER.addConnector(httpsConnector);

        HandlerList handlers = new HandlerList();

        ContextHandler rootpath = new ContextHandler();
        rootpath.setContextPath("/");
        rootpath.setHandler(new CustomRequestHandler(Const.CONTENT_TYPE_HTML));

        ContextHandler jsonpath = new ContextHandler();
        jsonpath.setContextPath("/json"); //TODO: MIME type as parameter
        jsonpath.setHandler(new CustomRequestHandler(Const.CONTENT_TYPE_JSON));

        ContextHandler xmlpath = new ContextHandler();
        xmlpath.setContextPath("/xml"); //TODO: MIME type as parameter
        xmlpath.setHandler(new CustomRequestHandler(Const.CONTENT_TYPE_XML));

        String filePath = System.getProperty(Const.PROPERTY_RESPONSE_FILE);
        ContextHandler filepath = new ContextHandler();
        filepath.setContextPath("/file");
        filepath.setHandler(new CustomFileRequestHandler((ServerHandler.isEmpty(filePath)) ? "" : filePath));

        handlers.addHandler(filepath);
        handlers.addHandler(jsonpath);
        handlers.addHandler(xmlpath);
        handlers.addHandler(rootpath);

        SERVER.setHandler(handlers);

    }

    private HttpConfiguration createHttpsFactory(int port) {
        HttpConfiguration httpsConfig = new HttpConfiguration();
        httpsConfig.setSecureScheme(Const.SECURE_SCHEME);
        httpsConfig.setSecurePort(port);
        httpsConfig.setSendXPoweredBy(true);
        httpsConfig.setSendServerVersion(true);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        return httpsConfig;
    }

    private SslConnectionFactory createSslConnectioFactory(String[] ciphers, String[] protocols) {
        SslContextFactory.Server sslContext = new SslContextFactory.Server();
        sslContext.setIncludeCipherSuites(ciphers);
        sslContext.setIncludeProtocols(protocols);
        sslContext.setKeyStorePath(System.getProperty(Const.PROPERTY_KEYSTORE));
        sslContext.setKeyStorePassword(System.getProperty(Const.PROPERTY_KEYSTORE_PSWD));
        sslContext.setCipherComparator(HTTP2Cipher.COMPARATOR);
        sslContext.setNeedClientAuth(false);

        return new SslConnectionFactory(sslContext, Const.PROTOCOL);
    }
}
