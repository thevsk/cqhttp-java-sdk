package top.thevsk.entity;

public class BotConfig {

    private String apiVersion;

    private Long selfId;

    private String accessToken;

    private String secret;

    private String apiHost;

    private int apiPort = 0;

    public String getApiHost() {
        return apiHost;
    }

    public BotConfig setApiHost(String apiHost) {
        this.apiHost = apiHost;
        return this;
    }

    public int getApiPort() {
        return apiPort;
    }

    public BotConfig setApiPort(int apiPort) {
        this.apiPort = apiPort;
        return this;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public BotConfig setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public Long getSelfId() {
        return selfId;
    }

    public BotConfig setSelfId(Long selfId) {
        this.selfId = selfId;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public BotConfig setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public BotConfig setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public BotConfig() {
    }
}
