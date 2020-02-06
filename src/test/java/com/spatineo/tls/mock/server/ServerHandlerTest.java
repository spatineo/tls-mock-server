package com.spatineo.tls.mock.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServerHandler.class)
public class ServerHandlerTest {
    ServerHandler serverHandler;
    String[] ARGS = {"TLSv1.2", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "8080,8443"};

    @Before
    public void setup() throws Exception {
        serverHandler = PowerMockito.spy(new ServerHandler());
    }

    //@Test
    public void startMain() throws Exception {
        TestUtil.setSystemProperties(true);
        ServerHandler.main(ARGS);
    }

    @Test
    public void initTest() throws Exception {
        TestUtil.setSystemProperties(true);
        PowerMockito.doNothing().when(serverHandler, "startAndJoinServer");

        serverHandler.init(TestUtil.DEFAULT_PROTOCOLS, TestUtil.DEFAULT_CIPHERS, TestUtil.DEFAULT_PORTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noSystemProperties() throws Exception{
        TestUtil.setSystemProperties(false);
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


}
