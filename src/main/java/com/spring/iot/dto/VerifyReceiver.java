//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.spring.iot.dto;



import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.util.Throwables;
import com.sun.net.httpserver.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

@AllArgsConstructor
@Data
public final class VerifyReceiver implements VerificationCodeReceiver {
    private static final String LOCALHOST = "localhost";
    private static final String CALLBACK_PATH = "/Callback";
    private HttpServer server;
    String code;
    String error;
    final Semaphore waitUnlessSignaled;
    private final String host;
    private final String callbackPath;
    private String successLandingPageUrl;
    private String failureLandingPageUrl;

    public VerifyReceiver() {
        this("localhost", "/Callback", (String)null, (String)null);
    }

    VerifyReceiver(String host, String successLandingPageUrl, String failureLandingPageUrl) {
        this(host, "/Callback", successLandingPageUrl, failureLandingPageUrl);
    }

    VerifyReceiver(String host, String callbackPath, String successLandingPageUrl, String failureLandingPageUrl) {
        this.waitUnlessSignaled = new Semaphore(0);
        this.host = host;
        this.callbackPath = callbackPath;
        this.successLandingPageUrl = successLandingPageUrl;
        this.failureLandingPageUrl = failureLandingPageUrl;
    }

    public String getRedirectUri() throws IOException {
        return "https://" + this.getHost() + this.callbackPath;
    }




    public String waitForCode() throws IOException {
        this.waitUnlessSignaled.acquireUninterruptibly();
        if (this.error != null) {
            throw new IOException("User authorization failed (" + this.error + ")");
        } else {
            return this.code;
        }
    }

    public void stop() throws IOException {
        this.waitUnlessSignaled.release();
        if (this.server != null) {
            try {
                this.server.stop(0);
            } catch (Exception var2) {
                Throwables.propagateIfPossible(var2);
                throw new IOException(var2);
            }

            this.server = null;
        }

    }

    public String getHost() {
        return this.host;
    }


    public String getCallbackPath() {
        return this.callbackPath;
    }

    class CallbackHandler implements HttpHandler {
        CallbackHandler() {
        }

        public void handle(HttpExchange httpExchange) throws IOException {
            HttpsExchange httpsExchange = (HttpsExchange) httpExchange;
            if (VerifyReceiver.this.callbackPath.equals(httpsExchange.getRequestURI().getPath())) {
                new StringBuilder();

                try {
                    Map<String, String> parms = this.queryToMap(httpsExchange.getRequestURI().getQuery());
                    VerifyReceiver.this.error = (String)parms.get("error");
                    VerifyReceiver.this.code = (String)parms.get("code");
                    Headers respHeaders = httpsExchange.getResponseHeaders();
                    if (VerifyReceiver.this.error == null && VerifyReceiver.this.successLandingPageUrl != null) {
                        respHeaders.add("Location", VerifyReceiver.this.successLandingPageUrl);
                        httpsExchange.sendResponseHeaders(302, -1L);
                    } else if (VerifyReceiver.this.error != null && VerifyReceiver.this.failureLandingPageUrl != null) {
                        respHeaders.add("Location", VerifyReceiver.this.failureLandingPageUrl);
                        httpsExchange.sendResponseHeaders(302, -1L);
                    } else {
                        this.writeLandingHtml(httpsExchange, respHeaders);
                    }

                    httpsExchange.close();
                } finally {
                    VerifyReceiver.this.waitUnlessSignaled.release();
                }

            }
        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap();
            if (query != null) {
                String[] var3 = query.split("&");
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String param = var3[var5];
                    String[] pair = param.split("=");
                    if (pair.length > 1) {
                        result.put(pair[0], pair[1]);
                    } else {
                        result.put(pair[0], "");
                    }
                }
            }

            return result;
        }

        private void writeLandingHtml(HttpExchange exchange, Headers headers) throws IOException {
            HttpsExchange httpsExchange = (HttpsExchange) exchange;
            OutputStream os = exchange.getResponseBody();
            Throwable var4 = null;

            try {
                httpsExchange.sendResponseHeaders(200, 0L);
                headers.add("ContentType", "text/html");
                OutputStreamWriter doc = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                doc.write("<html>");
                doc.write("<head><title>OAuth 2.0 Authentication Token Received</title></head>");
                doc.write("<body>");
                doc.write("Received verification code. You may now close this window.");
                doc.write("</body>");
                doc.write("</html>\n");
                doc.flush();
            } catch (Throwable var13) {
                var4 = var13;
                throw var13;
            } finally {
                if (os != null) {
                    if (var4 != null) {
                        try {
                            os.close();
                        } catch (Throwable var12) {
                            var4.addSuppressed(var12);
                        }
                    } else {
                        os.close();
                    }
                }

            }

        }
    }

    public static final class Builder {
        private String host = "localhost";
        private int port = -1;
        private String successLandingPageUrl;
        private String failureLandingPageUrl;
        private String callbackPath = "/Callback";

        public Builder() {
        }

        public VerifyReceiver build() {
            return new VerifyReceiver(this.host, this.callbackPath, this.successLandingPageUrl, this.failureLandingPageUrl);
        }

        public String getHost() {
            return this.host;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public int getPort() {
            return this.port;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public String getCallbackPath() {
            return this.callbackPath;
        }

        public Builder setCallbackPath(String callbackPath) {
            this.callbackPath = callbackPath;
            return this;
        }

        public Builder setLandingPages(String successLandingPageUrl, String failureLandingPageUrl) {
            this.successLandingPageUrl = successLandingPageUrl;
            this.failureLandingPageUrl = failureLandingPageUrl;
            return this;
        }
    }
}
