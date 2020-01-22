package com.spatineo.ssltestserver;

public class Const {
    public static final String SEPARATOR = ",";
    public static final String PROTOCOL = "http/1.1";/* Options: http/1.1, ssl, h2 */
    public static final String SECURE_SCHEME = "https";
    public static final String PROPERTY_KEYSTORE = "javax.net.ssl.keyStore";
    public static final String PROPERTY_KEYSTORE_PSWD = "javax.net.ssl.keyStorePassword";

    public static final String SERVER_GREETING = "JETTY SERVER ON ";
    public static final String RESPONSE_CONTENT_TYPE = "text/html; charset=utf-8";

    public static final String REQUIRED_PROPERTIES = "\nThese system properties must be set \nKeystore path: " + PROPERTY_KEYSTORE + "\nKeystore password: " + PROPERTY_KEYSTORE_PSWD;
    public static final String REQUIRED_ARGUMENTS = "Three arguments are required\nArg1: Protocols (comma separated string)\nArg2: Cipher suites (comma separated string)\nArg 3: Two ports (comma separated string)";
    public static final String OPTIONAL_ARGUMENTS = "A fourth optional argument can be provided\nArg4: Path to logging file";
    public static final String BAD_ARGUMENTS_MESSAGE = REQUIRED_PROPERTIES + "\n" + REQUIRED_ARGUMENTS + "\n" + OPTIONAL_ARGUMENTS;

}
