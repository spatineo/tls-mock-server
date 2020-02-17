package com.spatineo.tls.mock.server;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.*;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLHandshakeException;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import static  com.spatineo.tls.mock.server.TestUtil.*;
import static com.spatineo.tls.mock.server.TestUtil.initAndStartServerWithDefaults;


public class TLSMockServerTest {
    private TLSMockServer SERVER;

    @Before
    public void setup() throws IOException {
        TestUtil.setSystemProperties(true, 0);
        SERVER = initAndStartServerWithDefaults(null,null);
    }

    @After
    public void cleanup() {
        SERVER.stop();
    }

    @Test
    public void lifecycleTest() throws IOException{
        TestUtil.setSystemProperties(true, 0);
        TLSMockServer server =  new TLSMockServer(getAvailablePort());
        server.initTestServer(getAvailablePort(), new String[]{}, new String[]{},null,null);

        Assert.assertFalse(server.isAlive());
        server.start();
        Assert.assertTrue(server.isAlive());
        server.stop();
        Assert.assertFalse(server.isAlive());
    }

    @Test
    public void HTTPMockServerCustomResponseTest() throws IOException {
        TestUtil.setSystemProperties(true, 0);
        final String customResponse = "Test response set for testing";
        TLSMockServer server =  initAndStartServerWithDefaults(customResponse, null);

        CloseableHttpResponse response = tryHttpURL(HTTP_LOCALHOST + server.getHttpPort() + TestUtil.ENDPOINT, null);
        String responseBody = EntityUtils.toString(response.getEntity());

        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertEquals( customResponse + "\n", responseBody);
        server.stop();
    }

    @Test
    public void HTTPMockServerSuccess() throws IOException {
        TestUtil.setSystemProperties(true, 0);
        CloseableHttpResponse response = tryHttpURL(HTTP_LOCALHOST + SERVER.getHttpPort() + ENDPOINT, null);
        String responseBody = EntityUtils.toString(response.getEntity());

        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertTrue( responseBody.equals("Jetty server says hello!\n"));
    }

    @Test
    public void CustomContentTypeTest() throws IOException {
        TestUtil.setSystemProperties(true, 0);
        final String contentType_text = "text/plain";
        final String contentType_html = "text/html";
        final String contentType_json = "application/json";
        final String contentType_xml = "application/xml";

        //ContentType: text/plain
        CloseableHttpResponse response = tryHttpURL(HTTP_LOCALHOST + SERVER.getHttpPort() + ENDPOINT, contentType_text);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertTrue(headerExists(response.getAllHeaders(), HEADER_CONTENT_TYPE, contentType_text));

        //ContentType: text/html
        response = tryHttpURL(HTTP_LOCALHOST + SERVER.getHttpPort() + ENDPOINT, contentType_html);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertTrue(headerExists(response.getAllHeaders(), HEADER_CONTENT_TYPE, contentType_html));

        //ContentType: application/json
        response = tryHttpURL(HTTP_LOCALHOST + SERVER.getHttpPort() + ENDPOINT, contentType_json);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertTrue(headerExists(response.getAllHeaders(), HEADER_CONTENT_TYPE, contentType_json));

        //ContentType: application/xml
        response = tryHttpURL(HTTP_LOCALHOST + SERVER.getHttpPort() + ENDPOINT, contentType_xml);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertTrue(headerExists(response.getAllHeaders(), HEADER_CONTENT_TYPE, contentType_xml));
    }

    @Test
    public void HTTPMockServer404() throws IOException {
        TestUtil.setSystemProperties(true, 0);
        CloseableHttpResponse response = tryHttpURL(HTTP_LOCALHOST + SERVER.getHttpPort(), null);

        Assert.assertEquals(404, response.getStatusLine().getStatusCode());
    }

    @Test(expected = SSLHandshakeException.class)
    public void HTTPMockServerSSLPort() throws IOException {
        TestUtil.setSystemProperties(true, 0);
        CloseableHttpResponse response = tryHttpURL(HTTPS_LOCALHOST + SERVER.getHttpsPort(), null);
    }

    @Test
    public void fileResponseTest() throws IOException {
        TestUtil.setSystemProperties(true, 0);
        String filePath = getClass().getClassLoader().getResource("Response.xml").getPath();
        int c;
        StringBuilder sb = new StringBuilder();

        TLSMockServer server =  initAndStartServerWithDefaults(null, filePath);

        try {
            CloseableHttpResponse response = tryHttpURL(HTTP_LOCALHOST + server.getHttpPort() + ENDPOINT, null);
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            Assert.assertTrue(headerExists(response.getAllHeaders(), HEADER_CONTENT_DISPOSITION, "attachment"));

            InputStream is = response.getEntity().getContent();
            while ((c = is.read()) > 0) {
                sb.append((char) c);
            }
            Assert.assertTrue(sb.toString().contains("<message>Hello World!</message>"));
        } finally {
            server.stop();
        }
    }

    @Test
    public void imageResponseTest() throws IOException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        TestUtil.setSystemProperties(true, 0);
        String filePath = getClass().getClassLoader().getResource("test.png").getPath();

        TLSMockServer server =  initAndStartServerWithDefaults(null, filePath);
        try {
            CloseableHttpResponse response = tryHttpURL(HTTP_LOCALHOST + server.getHttpPort() + ENDPOINT, null);
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            Assert.assertTrue(headerExists(response.getAllHeaders(), HEADER_CONTENT_DISPOSITION, "attachment"));
            Assert.assertTrue(headerExists(response.getAllHeaders(), HEADER_CONTENT_TYPE, "image"));

            BufferedImage bi = ImageIO.read(response.getEntity().getContent());
            Assert.assertEquals(300, bi.getHeight());
            Assert.assertEquals(300, bi.getWidth());

            ImageIO.write(bi, "png", tmp);
            Assert.assertEquals(4591, tmp.size());
        } finally {
            tmp.close();
            server.stop();
        }
    }

    @Test(expected = SocketTimeoutException.class)
    public void responseDelayTest() throws IOException {
        TestUtil.setSystemProperties(true, 6000);
        TLSMockServer server =  initAndStartServerWithDefaults(null, null);
        try {
            tryHttpURL(HTTP_LOCALHOST + server.getHttpPort() + ENDPOINT, null);
        } finally {
            server.stop();
        }
    }
}
