package com.spatineo.tls.mock.server;

import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;

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
     * <p>Starts and joins a previously defined server</p>
     */
    public void start() {
        try {
            SERVER.start();
            System.out.println("SSL test server started");
            SERVER.join();
        } catch(Exception e) {
            System.out.println(e);
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
        SERVER.setHandler(new CustomRequestHandler());
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
