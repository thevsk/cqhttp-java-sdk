package top.thevsk.entity;

import top.thevsk.aop.BotControllerInterceptor;
import top.thevsk.aop.BotSendInterceptor;

import java.util.Set;

public class BotConfig {

    private Set<Object> botControllers = null;

    private Set<BotControllerInterceptor> botControllerInterceptors = null;

    private Set<BotSendInterceptor> botSendInterceptors = null;

    public BotConfig() {
    }

    public Set<Object> getBotControllers() {
        return botControllers;
    }

    public Set<BotControllerInterceptor> getBotControllerInterceptors() {
        return botControllerInterceptors;
    }

    public Set<BotSendInterceptor> getBotSendInterceptors() {
        return botSendInterceptors;
    }

    public void setBotControllers(Set<Object> botControllers) {
        this.botControllers = botControllers;
    }

    public void setBotControllerInterceptors(Set<BotControllerInterceptor> botControllerInterceptors) {
        this.botControllerInterceptors = botControllerInterceptors;
    }

    public void setBotSendInterceptors(Set<BotSendInterceptor> botSendInterceptors) {
        this.botSendInterceptors = botSendInterceptors;
    }
}