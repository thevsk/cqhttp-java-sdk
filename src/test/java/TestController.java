import top.thevsk.core.BotController;
import top.thevsk.entity.BotRequest;

public class TestController implements BotController {
    @Override
    public boolean before(BotRequest request) {
        return true;
    }

    @Override
    public Object onMessage(BotRequest request) {
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
