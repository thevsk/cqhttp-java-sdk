package top.thevsk.utils;

import java.util.Set;

public class BeanUtils {

    public static <T> void copy(Set<T> src, Set<T> dist) {
        dist.addAll(src);
    }
}
