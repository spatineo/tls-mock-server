package com.spatineo.tls.mock.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServerHandler.class)
public class ServerHandlerTest {
    ServerHandler serverHandler;
    String[] ARGS = {"TLSv1.2", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "8080,8443"};
    String[] protocols = {"TLSv1.2"};
    String[] ciphers = {"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"};
    Integer[] ports = {8080, 8443};

    @Before
    public void setup() throws Exception {
        serverHandler = PowerMockito.spy(new ServerHandler());
    }

    //@Test
    public void startMain() throws Exception {
        setSystemProperties(true);
        ServerHandler.main(ARGS);
    }

    @Test
    public void initTest() throws Exception {
        setSystemProperties(true);
        PowerMockito.doNothing().when(serverHandler, "startAndJoinServer");

        serverHandler.init(protocols, ciphers, ports);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noSystemProperties() throws Exception{
        setSystemProperties(false);
        System.setProperty(Const.PROPERTY_KEYSTORE, "");
        System.setProperty(Const.PROPERTY_KEYSTORE_PSWD, "");

        serverHandler.init(null, null, null);
    }

    @Test
    public void isEmptyTest() {
        Assert.assertTrue(ServerHandler.isEmpty(null));
        Assert.assertTrue(ServerHandler.isEmpty(""));
        Assert.assertFalse(ServerHandler.isEmpty("not empty"));
    }

    @Test
    public void arrayIsEmptyTest() {
        Assert.assertTrue(ServerHandler.arrayIsEmpty(null));
        Assert.assertTrue(ServerHandler.arrayIsEmpty(new String[]{}));
        Assert.assertFalse(ServerHandler.arrayIsEmpty(new String[]{"val", "val"}));
    }

    private void setSystemProperties(boolean set) throws NullPointerException {
        String keystorePath = getClass().getClassLoader().getResource("keystore").getPath();
        String testResponsePath = getClass().getClassLoader().getResource("Response.xml").getPath();
        System.setProperty(Const.PROPERTY_KEYSTORE, (set) ? keystorePath : "");
        System.setProperty(Const.PROPERTY_KEYSTORE_PSWD, (set) ? "password" : "");
        System.setProperty(Const.PROPERTY_RESPONSE_FILE, (set) ? testResponsePath : "");
    }
}
