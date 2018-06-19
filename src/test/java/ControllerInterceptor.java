import top.thevsk.aop.BotControllerInterceptor;
import top.thevsk.core.BotController;
import top.thevsk.entity.BotRequest;

/**
 * @author thevsk
 * @Title: ControllerInterceptor
 * @ProjectName cqhttp-java-sdk
 * @date 2018-06-19 21:18
 */
public class ControllerInterceptor implements BotControllerInterceptor {
    @Override
    public boolean before(BotRequest request, BotController botController) {
        System.out.println("进入 controller before 过滤器，消息是：" + request.toString());
        return true;
    }

    @Override
    public void after(BotRequest request, BotController botController, Object controllerReturnValue) {
        System.out.println("进入 controller after 过滤器");
    }

    @Override
    public void throwException(BotRequest request, BotController botController, Exception exception) {
        System.out.println("进入 controller exception 过滤器");
    }
}
