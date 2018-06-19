package top.thevsk.core;

import top.thevsk.entity.BotRequest;

public interface BotController {

    boolean before(BotRequest request);

    Object onMessage(BotRequest request);

    Object onEvent(BotRequest request);

    Object onRequest(BotRequest request);
}