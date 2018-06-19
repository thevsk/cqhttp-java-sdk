import top.thevsk.core.BotController;
import top.thevsk.entity.BotRequest;

public class TestController implements BotController {
    @Override
    public boolean before(BotRequest request) {
        return true;
    }

    @Override
    public Object onMessage(BotRequest request) {
        System.out.println("收到消息：" + request.getMessage());
        request.getFastReply().replyAt("success");
        return null;
    }

    @Override
    public Object onEvent(BotRequest request) {
        return null;
    }

    @Override
    public Object onRequest(BotRequest request) {
        return null;
    }
}
