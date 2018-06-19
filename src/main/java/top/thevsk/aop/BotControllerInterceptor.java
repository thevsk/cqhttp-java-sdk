package top.thevsk.aop;

import top.thevsk.core.BotController;
import top.thevsk.entity.BotRequest;

public interface BotControllerInterceptor {

    boolean before(BotRequest request);

    void after(BotRequest request, Object controllerReturnValue);

    void throwException(BotRequest request, BotController botController, Exception exception);
}