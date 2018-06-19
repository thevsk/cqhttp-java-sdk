package top.thevsk.utils;

public class StrUtils {

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else {
            int len = str.length();
            if (len == 0) {
                return true;
            } else {
                int i = 0;

                while (i < len) {
                    switch (str.charAt(i)) {
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ':
                            ++i;
                            break;
                        default:
                            return false;
                    }
                }

                return true;
            }
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isNotNullObjects(Object... objects) {
        for (Object object : objects) {
            if (object == null) return false;
        }
        return true;
    }

    public static boolean isNullObjects(Object... objects) {
        return !isNotNullObjects(objects);
    }
}
