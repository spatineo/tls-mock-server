package com.spatineo.tls.mock.server;

public class TestUtil {
    public static final String[] DEFAULT_PROTOCOLS = {"TLSv1.2"};
    public static final String[] DEFAULT_CIPHERS = {"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"};
    public static final Integer[] DEFAULT_PORTS = {8080, 8443};

    public static void setSystemProperties(boolean set) throws NullPointerException {
        String keystorePath = TestUtil.class.getClassLoader().getResource("keystore").getPath();
        String testResponsePath = TestUtil.class.getClassLoader().getResource("Response.xml").getPath();
        System.setProperty(Const.PROPERTY_KEYSTORE, (set) ? keystorePath : "");
        System.setProperty(Const.PROPERTY_KEYSTORE_PSWD, (set) ? "password" : "");
        System.setProperty(Const.PROPERTY_RESPONSE_FILE, (set) ? testResponsePath : "");
    }
}
