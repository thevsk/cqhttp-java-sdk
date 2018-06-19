package top.thevsk.utils;

import java.util.Set;

public class BeanUtils {

    public static <T> void copy(Set<T> src, Set<T> dist) {
        dist.addAll(src);
    }

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
