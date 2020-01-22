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
    String[] ARGS;

    @Before
    public void setup() throws Exception {
        serverHandler = PowerMockito.spy(new ServerHandler());
        ARGS = new String[]{"TLSv1.2", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "8080,8443"};
    }

    //@Test
    public void startMain() throws Exception {
        setSystemProperties(true);
        ServerHandler.main(ARGS);
    }

    @Test
    public void threeArgs() throws Exception {
        setSystemProperties(true);

        PowerMockito.doNothing().when(serverHandler, "startAndJoinServer");

        serverHandler.init(ARGS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noSystemProperties() throws Exception{
        setSystemProperties(false);
        System.setProperty(Const.PROPERTY_KEYSTORE, "");
        System.setProperty(Const.PROPERTY_KEYSTORE_PSWD, "");

        serverHandler.init(new String[] {});
    }

    @Test
    public void isEmptyTest() {
        Assert.assertTrue(ServerHandler.isEmpty(null));
        Assert.assertTrue(ServerHandler.isEmpty(""));
        Assert.assertFalse(ServerHandler.isEmpty("not empty"));
    }

    private void setSystemProperties(boolean set) throws Exception {
        String keystorePath = System.getProperty("user.dir") + "/src/test/resources/keystore";
        System.setProperty(Const.PROPERTY_KEYSTORE, (set) ? keystorePath : "");
        System.setProperty(Const.PROPERTY_KEYSTORE_PSWD, (set) ? "password" : "");
    }
}
