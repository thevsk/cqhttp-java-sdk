package top.thevsk.core;

import top.thevsk.aop.BotControllerInterceptor;
import top.thevsk.aop.BotSendInterceptor;
import top.thevsk.entity.BotConfig;
import top.thevsk.entity.BotRequest;
import top.thevsk.log.BotLog;

import java.util.HashSet;
import java.util.Set;

public class Bot {

    private Set<Object> botControllers = null;

    private Set<BotControllerInterceptor> botControllerInterceptors = null;

    private Set<BotSendInterceptor> botSendInterceptors = null;

    private HttpServer httpServer = null;

    private BotConfig botConfig = null;

    public Bot(int port, BotConfig botConfig) {
        botControllerInterceptors = new HashSet<>();
        botControllers = new HashSet<>();
        botSendInterceptors = new HashSet<>();
        httpServer = new HttpServer();
        httpServer.setPort(port);
        this.botConfig = botConfig;
    }

    public Bot addBotController(Object botController) {
        botControllers.add(botController);
        return this;
    }

    public Bot addBotInterceptor(BotControllerInterceptor botControllerInterceptor) {
        botControllerInterceptors.add(botControllerInterceptor);
        return this;
    }

    public Bot addBotSendInterceptor(BotSendInterceptor botSendInterceptor) {
        botSendInterceptors.add(botSendInterceptor);
        return this;
    }

    public void start() {
//        check();
        httpServer.setBot(this);
        httpServer.start();
        BotLog.info("server started...");
    }

    private void check() {
        if (botControllers.size() == 0) {
            throw new RuntimeException("empty controller");
        }
    }

    void onHttp(BotRequest request) {
        System.out.println(request.getBody());
    }
}
