package top.thevsk.aop;

import top.thevsk.entity.BotResponse;

import java.util.Map;

public interface BotSendInterceptor {

    boolean before(String method, Map<String, Object> param);

    void after(String method, Map<String, Object> param, BotResponse response);

    void throwException(String method, Map<String, Object> param, Exception exception);
}
