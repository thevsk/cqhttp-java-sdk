import top.thevsk.core.Bot;
import top.thevsk.entity.BotConfig;

public class Test {

    public static void main(String[] args) {
        Bot bot = new Bot(7800, new BotConfig().setApiHost("127.0.0.1").setApiPort(5700));
        bot
                .addBotController(new TestController())
                .addBotSendInterceptor(new SendInterceptor())
                .addBotInterceptor(new ControllerInterceptor())
                .start();
    }
}
