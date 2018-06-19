package top.thevsk.annotation;

import top.thevsk.aop.BotControllerInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BotInterceptor {

    Class<? extends BotControllerInterceptor>[] value();
}
