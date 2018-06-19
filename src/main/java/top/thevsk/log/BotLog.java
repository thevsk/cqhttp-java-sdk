package top.thevsk.log;

public class BotLog {

    public static void info(String message) {
        System.out.println("[BOT-SDK] [INFO] " + message);
    }

    public static void error(String message) {
        System.out.println("[BOT-SDK] [ERROR] " + message);
    }

    public static void debug(String message) {
        System.out.println("[BOT-SDK] [DEBUG] " + message);
    }

    public static void warn(String message) {
        System.out.println("[BOT-SDK] [WARN] " + message);
    }
}
