# tls-mock-server

A general purpose HTTPS server for mocking connection establishment with various TLS/SSL version/cipher combinations


## Usage
tls-mock-server starts a jetty service that utilizes selected ports and supports selected protocols and cipher suites.

```sh
java -Djavax.net.ssl.keyStore=PathToKeystore\
-Djavax.net.ssl.keyStorePassword=KeystorePassword\
-jar tls-mock-server-1.0.0.jar\
"TLSv1.2,TLSv1.3"\
"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA"\
"8080,8443"

```
System properties javax.net.ssl.keyStorePassword and javax.net.ssl.keyStorePassword should be set and the parameters protocol, cipher suites and ports should be passed in as comma separated lists.

## License
[GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)
