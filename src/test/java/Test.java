import top.thevsk.core.Bot;
import top.thevsk.core.BotController;
import top.thevsk.entity.BotConfig;
import top.thevsk.entity.BotRequest;

public class Test {

    public static void main(String[] args) {
        new Bot(7800, new BotConfig().setApiHost("127.0.0.1").setApiPort(5700))
                .addBotController(new Controller())
                .start();
    }

    public static class Controller implements BotController {

        @Override
        public boolean before(BotRequest request) {
            return "private".equals(request.getMessageType());
        }

        @Override
        public Object onMessage(BotRequest request) {
            request.getFastReply().reply("你好");
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
}
