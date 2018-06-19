import top.thevsk.core.Bot;
import top.thevsk.entity.BotConfig;

public class Test {

    public static void main(String[] args) {
        new Bot(7800, new BotConfig().setSelfId(2421139761L).setApiVersion("4.0.5")).start();
    }
}
