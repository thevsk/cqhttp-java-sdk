package top.thevsk.aop;

import top.thevsk.core.BotController;
import top.thevsk.entity.BotRequest;

public interface BotControllerInterceptor {

    boolean before(BotRequest request, BotController botController);

    void after(BotRequest request, BotController botController, Object controllerReturnValue);

    void throwException(BotRequest request, BotController botController, Exception exception);
}