package top.thevsk.core;

import top.thevsk.entity.BotRequest;

/**
 * @author thevsk
 * @ProjectName https://github.com/thevsk/cqhttp-java-sdk
 * @date 2018-06-19 20:56
 * <p>
 * 实现这个接口之后你就是我的人了
 */
public interface BotController {

    boolean before(BotRequest request);

    Object onMessage(BotRequest request);

    Object onEvent(BotRequest request);

    Object onRequest(BotRequest request);
}