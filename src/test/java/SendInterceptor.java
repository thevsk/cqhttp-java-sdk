import top.thevsk.aop.BotSendInterceptor;
import top.thevsk.entity.BotResponse;

import java.util.Map;

/**
 * @author thevsk
 * @Title: SendInterceptor
 * @ProjectName cqhttp-java-sdk
 * @date 2018-06-19 21:13
 */
public class SendInterceptor implements BotSendInterceptor {
    @Override
    public boolean before(String method, Map<String, Object> param) {
        System.out.println("进入 send before 过滤器，调用方法 " + method);
        return true;
    }

    @Override
    public void after(String method, Map<String, Object> param, BotResponse response) {
        System.out.println("进入 send after 过滤器");
    }

    @Override
    public void throwException(String method, Map<String, Object> param, Exception exception) {
        System.out.println("进入 send exception 过滤器");
    }
}
