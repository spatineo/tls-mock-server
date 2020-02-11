import com.spatineo.tls.mock.server.ServerHandlerTest;
import com.spatineo.tls.mock.server.TLSMockServerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TLSMockServerTest.class,
        ServerHandlerTest.class
})
public class TestSuite {
}
