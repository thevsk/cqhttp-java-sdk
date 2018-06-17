package top.thevsk.entity;

import java.io.BufferedReader;
import java.io.IOException;

public class BotRequest {

    private String method;

    private String httpVersion;

    private String path;

    private Headers headers;

    private String body;

    public String getMethod() {
        return method;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getPath() {
        return path;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public BotRequest(String method, String httpVersion, String path, Headers headers, String body) {
        this.method = method;
        this.httpVersion = httpVersion;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public static class Headers {

        private String host;

        private String accept;

        private String contentType;

        private String userAgent;

        private String xSelfId;

        private int contentLength = 0;

        public String getHost() {
            return host;
        }

        public String getAccept() {
            return accept;
        }

        public String getContentType() {
            return contentType;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public String getxSelfId() {
            return xSelfId;
        }

        public int getContentLength() {
            return contentLength;
        }

        public Headers(BufferedReader bufferedReader) throws IOException {
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
                if (line.toLowerCase().contains("host")) {
                    this.host = line.substring(line.indexOf(":") + 1).trim();

                } else if (line.toLowerCase().contains("accept")) {
                    this.accept = line.substring(line.indexOf(":") + 1).trim();

                } else if (line.toLowerCase().contains("content-type")) {
                    this.contentType = line.substring(line.indexOf(":") + 1).trim();

                } else if (line.toLowerCase().contains("user-agent")) {
                    this.userAgent = line.substring(line.indexOf(":") + 1).trim();

                } else if (line.toLowerCase().contains("x-self-id")) {
                    this.xSelfId = line.substring(line.indexOf(":") + 1).trim();

                } else if (line.toLowerCase().contains("content-length")) {
                    try {
                        this.contentLength = Integer.valueOf(line.substring(line.indexOf(":") + 1).trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
