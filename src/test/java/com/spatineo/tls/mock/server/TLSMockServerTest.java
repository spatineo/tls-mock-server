package com.spatineo.tls.mock.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TLSMockServerTest {
    private TLSMockServer SERVER;
    private final int PORT_HTTP = TestUtil.DEFAULT_PORTS[0];
    private final int PORT_HTTPS = TestUtil.DEFAULT_PORTS[1];

    @Before
    public void setup() {
        TestUtil.setSystemProperties(true);
    }

    @Test
    public void lifecycleTest() {
        SERVER = new TLSMockServer(PORT_HTTP);
        Assert.assertFalse(SERVER.isAlive());
        SERVER.initTestServer(PORT_HTTPS, TestUtil.DEFAULT_CIPHERS, TestUtil.DEFAULT_PROTOCOLS,null,null);
        Assert.assertFalse(SERVER.isAlive());
        SERVER.start();
        Assert.assertTrue(SERVER.isAlive());
        SERVER.stop();
        Assert.assertFalse(SERVER.isAlive());
    }
}
