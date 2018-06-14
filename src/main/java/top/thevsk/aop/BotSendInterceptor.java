package top.thevsk.aop;

public interface BotSendInterceptor {

    boolean before();

    void after();

    void throwException(Exception exception);
}
