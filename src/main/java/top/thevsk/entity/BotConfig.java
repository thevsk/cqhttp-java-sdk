package top.thevsk.entity;

public class BotConfig {

    private String apiVersion;

    private Long selfId;

    private String accessToken;

    private String secret;

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

    public BotConfig(String apiVersion, Long selfId, String accessToken, String secret) {
        this.apiVersion = apiVersion;
        this.selfId = selfId;
        this.accessToken = accessToken;
        this.secret = secret;
    }

    public BotConfig(String accessToken, String secret) {
        this.accessToken = accessToken;
        this.secret = secret;
    }

    public BotConfig(String secret) {
        this.secret = secret;
    }
}
