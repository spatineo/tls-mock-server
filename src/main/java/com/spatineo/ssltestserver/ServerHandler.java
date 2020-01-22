package com.spatineo.ssltestserver;

public class ServerHandler {
    public static Logger LOG = new Logger();

    public ServerHandler(){}
    private SSLTestServer SERVER = null;

    /**
     *
     * @param args
     *
     */
    public static void main(String[] args) {
        try {
            ServerHandler serverHandler = new ServerHandler();
            serverHandler.init(args);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     *<p>
     *     Initializes the test server with the given arguments
     *
     *     The paramter args is a String array containing three required and one optional value
     *     0: Required value: Comma separated String of Protocols you want to support eg. "TLSv1,TLSv1.2"
     *     1: Required value: Comma separated String of Cipher Suites you want to support eg. "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"
     *     2: Required value: Comma separated String containing 2 port numbers. The first for http port the second for https eg. "8080,8443"
     *     3: Optional value: A String containing the path to the file you wish to log to
     *</p>
     * @param args
     * @throws IllegalArgumentException
     */
    public void init(String[] args) throws IllegalArgumentException {
        if(!isEmpty(System.getProperty(Const.PROPERTY_KEYSTORE)) || !isEmpty(System.getProperty(Const.PROPERTY_KEYSTORE_PSWD))) {
            try {
                if (args.length == 4 && !isEmpty(args[3])) {
                    LOG = new Logger(args[3]);
                } else {
                    LOG = new Logger();
                }
                LOG.write("Protocols: " + args[0]);
                LOG.write("Cipher suites: " + args[1]);
                LOG.write("Unsecured and secured port: " + args[2]);

                String[] protocolList = args[0].split(Const.SEPARATOR);
                String[] cipherList = args[1].split(Const.SEPARATOR);
                String[] ports = args[2].split(Const.SEPARATOR);

                SERVER = new SSLTestServer(Integer.parseInt(ports[0]));
                SERVER.initTestServer(Integer.parseInt(ports[1]), cipherList, protocolList);
                startAndJoinServer();

            } catch (Exception e) {
                LOG.write(e);
            }
        } else {
            throw new IllegalArgumentException(Const.BAD_ARGUMENTS_MESSAGE);
        }
    }


    private void startAndJoinServer() {
        SERVER.start();
    }

    /**
     *
     * @param string
     * @return true if the trimmed value of the parameter is empty or null
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }
}
