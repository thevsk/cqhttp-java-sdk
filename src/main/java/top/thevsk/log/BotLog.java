package top.thevsk.log;

public class BotLog {

    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }
}
