package com.spatineo.tls.mock.server;

public class ServerHandler {
    private TLSMockServer SERVER = null;

    public ServerHandler(){}

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
     * @param args Arguments required to start tls-mock-server
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
     *     This method is in place for easier testing and is passed the argument list directly from the main method. All applicable argument descriptors can be found there.
     *</p>
     * @param args initialization args
     * @throws IllegalArgumentException If incorrect arguments are passed to this method  an IllegalArgumentException is thrown. Argument descriptors can be found in the main method javadoc.
     */
    public void init(String[] args) throws IllegalArgumentException {
        if(!isEmpty(System.getProperty(Const.PROPERTY_KEYSTORE)) || !isEmpty(System.getProperty(Const.PROPERTY_KEYSTORE_PSWD))) {
                System.out.println("Protocols: " + args[0]);
                System.out.println("Cipher suites: " + args[1]);
                System.out.println("Unsecured and secured port: " + args[2]);

                String[] protocolList = args[0].split(Const.SEPARATOR);
                String[] cipherList = args[1].split(Const.SEPARATOR);
                String[] ports = args[2].split(Const.SEPARATOR);

                SERVER = new TLSMockServer(Integer.parseInt(ports[0]));
                SERVER.initTestServer(Integer.parseInt(ports[1]), cipherList, protocolList);
                startAndJoinServer();
        } else {
            throw new IllegalArgumentException(Const.BAD_ARGUMENTS_MESSAGE);
        }
    }


    private void startAndJoinServer() {
        SERVER.start();
    }

    /**
     * <p>A method for checking if a trimmed string is empty or null</p>
     * @param string String that is to be checked
     * @return true if the trimmed value of the parameter is empty or null
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }
}
