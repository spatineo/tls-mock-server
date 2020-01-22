import com.spatineo.ssltestserver.Const;
import com.spatineo.ssltestserver.ServerHandler;
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
    String[] argsNoLogging;
    String[] argsWithLogging;

    @Before
    public void setup() throws Exception {
        //serverHandler = spy(new ServerHandler());
        serverHandler = PowerMockito.spy(new ServerHandler());
        String logPath = getClass().getClassLoader().getResource("testLog.txt").toURI().getPath();
        argsNoLogging = new String[]{"TLSv1.2", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "8080,8443"};
        argsWithLogging = new String[]{"TLSv1.2", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "8080,8443", logPath};

    }

    //@Test
    public void startMainWithLogging() throws Exception {
        setSystemProperties(true);
        ServerHandler.main(argsWithLogging);
    }

    @Test
    public void threeArgs() throws Exception {
        setSystemProperties(true);
        String[] args = new String[3];
        args[0] = "TLSv1.2,TLSv1.3";
        args[1] = "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384";
        args[2] = "8080,8443";

        PowerMockito.doNothing().when(serverHandler, "startAndJoinServer");

        serverHandler.init(args);
    }

    @Test
    public void fourArgs() throws Exception {
        setSystemProperties(true);
        String[] args = new String[4];
        args[0] = "TLSv1.2,TLSv1.3";
        args[1] = "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384";
        args[2] = "8080,8443";
        args[3] = System.getProperty("User.dir") + "/log.txt";

        PowerMockito.doNothing().when(serverHandler, "startAndJoinServer");
        serverHandler.init(args);
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
        String keystorePath = getClass().getResource("keystore").toURI().getPath();
        System.setProperty(Const.PROPERTY_KEYSTORE, (set) ? keystorePath : ""); //TODO: create test resource
        System.setProperty(Const.PROPERTY_KEYSTORE_PSWD, (set) ? "password" : "");
    }
}
