package top.thevsk.entity;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

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

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        this.body = null;
        String[] line = bufferedReader.readLine().split(" ");
        this.method = line[0];
        this.path = line[1];
        this.httpVersion = line[2];
        Headers headers = new Headers(bufferedReader);
        this.headers = headers;
        if (headers.getContentLength() <= 0) {
            return;
        }
        char[] chars = new char[headers.getContentLength()];
        int read = bufferedReader.read(chars, 0, headers.getContentLength());
        if (read == headers.getContentLength()) {
            this.body = new String(chars);
        }
    }

    public class Headers {

        private String host;

        private String accept;

        private String contentType;

        private String userAgent;

        private int contentLength = 0;

        private Long selfId;

        private String signature;

        public Long getSelfId() {
            return selfId;
        }

        public String getSignature() {
            return signature;
        }

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
                    this.selfId = Long.valueOf(line.substring(line.indexOf(":") + 1).trim());

                } else if (line.toLowerCase().contains("x-signature")) {
                    this.signature = line.substring(line.indexOf(":") + 1).trim();

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
