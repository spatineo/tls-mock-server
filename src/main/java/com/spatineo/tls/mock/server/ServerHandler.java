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
            if(args == null || args.length < 3) {
                throw new IllegalArgumentException(Const.BAD_ARGUMENTS_MESSAGE);
            }
            ServerHandler serverHandler = new ServerHandler();

            System.out.println("Protocols: " + args[0]);
            String[] protocolList = args[0].split(Const.SEPARATOR);

            System.out.println("Cipher suites: " + args[1]);
            String[] cipherList = args[1].split(Const.SEPARATOR);

            System.out.println("Unsecured and secured port: " + args[2]);
            Integer[] ports = stringToIntArray(args[2]);

            String customResponseString = System.getProperty(Const.PROPERTY_RESPONSE_STRING);
            System.out.println("Custom response string (tls.mock.server.custom.response.string): " + customResponseString);

            String customResponseFilePath = System.getProperty(Const.PROPERTY_RESPONSE_FILE_PATH);
            System.out.println("Custom response file path (tls.mock.server.custom.response.file.path): " + customResponseFilePath);

            serverHandler.init(protocolList, cipherList, ports,customResponseString, customResponseFilePath);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static Integer[] stringToIntArray(String string) {
        String[] stringArray = string.trim().split(Const.SEPARATOR);
        Integer[] intArray = new Integer[stringArray.length];
        for(int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i].trim());
        }
        return intArray;
    }

    /**
     *<p>
     *     This method is in place for easier testing and is passed the argument list directly from the main method. All applicable argument descriptors can be found there.
     *</p>
     * @param protocols string array of TLS protocols that the SSL port should respond to
     * @param ciphers string array of java supported cipher suites that the SSL port should respond to
     * @param ports integer array containing two ports [1] http port and [2] https port
     * @throws IllegalArgumentException If incorrect arguments are passed to this method  an IllegalArgumentException is thrown. Argument descriptors can be found in the main method javadoc.
     */
    public void init(String[] protocols, String[] ciphers, Integer[] ports, String customResponseString, String customResponseFilePath) throws IllegalArgumentException {//TODO: Add custom responses
        if(isEmpty(System.getProperty(Const.PROPERTY_KEYSTORE)) || isEmpty(System.getProperty(Const.PROPERTY_KEYSTORE_PSWD))) {
            throw new IllegalArgumentException(Const.BAD_ARGUMENTS_MESSAGE);
        }
        if(arrayIsEmpty(protocols) || arrayIsEmpty(ciphers) || arrayIsEmpty(ports)) {
            throw new IllegalArgumentException(Const.BAD_ARGUMENTS_MESSAGE);
        }
        SERVER = new TLSMockServer(ports[0]);
        SERVER.initTestServer(ports[1], ciphers, protocols, customResponseString, customResponseFilePath);
        startAndJoinServer();
    }


    private void startAndJoinServer() {
        SERVER.start();
        SERVER.join();
    }

    /**
     * <p>A method for checking if a trimmed string is empty or null</p>
     * @param string String that is to be checked
     * @return true if the trimmed value of the parameter is empty or null
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }
    public static boolean arrayIsEmpty(Object[] array) {
        return array == null || array.length < 1;
    }
}
