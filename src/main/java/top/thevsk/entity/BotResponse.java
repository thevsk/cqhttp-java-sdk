package top.thevsk.entity;

import java.util.Map;

public class BotResponse {

    private int retcode;

    private String status;

    private Map<String, Object> data;

    public int getRetcode() {
        return retcode;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public BotResponse(int retcode, String status, Map<String, Object> data) {
        this.retcode = retcode;
        this.status = status;
        this.data = data;
    }
}
