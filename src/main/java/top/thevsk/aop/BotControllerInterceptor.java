package top.thevsk.aop;

public interface BotControllerInterceptor {

    boolean before();

    void after();

    void throwException(Exception exception);
}