package top.thevsk.core;

import com.alibaba.fastjson.JSON;
import top.thevsk.annotation.ControllerInterceptor;
import top.thevsk.aop.BotControllerInterceptor;
import top.thevsk.aop.BotSendInterceptor;
import top.thevsk.entity.BotConfig;
import top.thevsk.entity.BotRequest;
import top.thevsk.entity.HttpRequest;
import top.thevsk.log.BotLog;
import top.thevsk.utils.BeanUtils;
import top.thevsk.utils.HmacSHA1Utils;
import top.thevsk.utils.StrUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Bot {

    private HashSet<BotController> botControllers = null;

    private HashSet<BotControllerInterceptor> botControllerInterceptors = null;

    private ConcurrentHashMap<String, BotControllerInterceptor> botControllerInterceptorConcurrentHashMap = null;

    private HashSet<BotSendInterceptor> botSendInterceptors = null;

    private ConcurrentHashMap<String, Set<BotControllerInterceptor>> controllerInterceptorMap = null;

    private HttpServer httpServer = null;

    private BotConfig botConfig = null;

    private void init(int port, BotConfig botConfig) {
        botControllerInterceptors = new HashSet<>();
        botControllers = new HashSet<>();
        botSendInterceptors = new HashSet<>();
        controllerInterceptorMap = new ConcurrentHashMap<>();
        botControllerInterceptorConcurrentHashMap = new ConcurrentHashMap<>();
        httpServer = new HttpServer();
        httpServer.setPort(port);
        this.botConfig = botConfig;
    }

    public Bot(int port, BotConfig botConfig) {
        init(port, botConfig);
    }

    public Bot(int port) {
        init(port, null);
    }

    public Bot addBotController(BotController botController) {
        botControllers.add(botController);
        return this;
    }

    public Bot addBotInterceptor(BotControllerInterceptor botControllerInterceptor) {
        botControllerInterceptors.add(botControllerInterceptor);
        return this;
    }

    public Bot addBotSendInterceptor(BotSendInterceptor botSendInterceptor) {
        botSendInterceptors.add(botSendInterceptor);
        return this;
    }

    public void start() {
        check();
        beforeStart();
        httpServer.setBot(this);
        httpServer.start();
        BotLog.info("server started...");
    }

    private void beforeStart() {
        // init botControllerInterceptorConcurrentHashMap
        for (BotControllerInterceptor interceptor : botControllerInterceptors) {
            botControllerInterceptorConcurrentHashMap.put(interceptor.getClass().getName(), interceptor);
        }
        // init controllerInterceptorMap
        for (BotController botController : botControllers) {
            HashSet<BotControllerInterceptor> interceptors = new HashSet<>();
            BeanUtils.copy(botControllerInterceptors, interceptors);
            Class<? extends BotControllerInterceptor>[] controllerInterceptor = botController.getClass().getAnnotation(ControllerInterceptor.class).value();
            for (Class clazz : controllerInterceptor) {
                interceptors.add(botControllerInterceptorConcurrentHashMap.get(clazz.getName()));
            }
            controllerInterceptorMap.put(botController.getClass().getName(), interceptors);
        }
    }

    private void check() {
        if (botControllers.size() == 0) {
            throw new RuntimeException("empty controller");
        }
    }

    BotConfig getBotConfig() {
        return botConfig;
    }

    void onHttp(HttpRequest request) {
        if (StrUtils.isBlank(request.getMethod()) || !request.getMethod().toUpperCase().equals("POST")) {
            BotLog.warn("[解析请求] 非 Post 请求");
            return;
        }
        if (StrUtils.isBlank(request.getBody())) {
            BotLog.warn("[解析请求] 空 Request Body");
            return;
        }
        if (StrUtils.isBlank(request.getHeaders().getUserAgent()) || !request.getHeaders().getUserAgent().contains("CQHttp")) {
            BotLog.warn("[解析请求] 非 CQHttp 消息");
            return;
        }
        if (botConfig != null) {
            if (StrUtils.isNotBlank(botConfig.getApiVersion()) && !request.getHeaders().getUserAgent().contains(botConfig.getApiVersion())) {
                BotLog.warn("[解析请求] API 版本错误");
                return;
            }
            if (botConfig.getSelfId() != null && !request.getHeaders().getSelfId().equals(botConfig.getSelfId())) {
                BotLog.warn("[解析请求] QQ 号错误");
                return;
            }
            if (StrUtils.isNotBlank(botConfig.getSecret())) {
                String hmacSha1 = HmacSHA1Utils.hmacSha1(request.getBody().getBytes(), botConfig.getSecret().getBytes());
                if (hmacSha1 != null && !request.getHeaders().getSignature().toUpperCase().contains(hmacSha1)) {
                    BotLog.warn("[解析请求] Secret 验证错误");
                    return;
                }
            }
        }
        call(request.getBody());
    }

    private void call(String json) {
        BotRequest request = new BotRequest(JSON.parseObject(json));
        for (BotController botController : botControllers) {
            Set<BotControllerInterceptor> interceptors = controllerInterceptorMap.get(botController.getClass().getName());
            if (callBefore(request, interceptors)) {
                callAfter(request, interceptors, callController(botController, request, interceptors));
            }
        }
    }

    private boolean callBefore(BotRequest request, Set<BotControllerInterceptor> interceptors) {
        for (BotControllerInterceptor interceptor : interceptors) {
            if (!interceptor.before(request)) {
                return false;
            }
        }
        return true;
    }

    private Object callController(BotController botController, BotRequest request, Set<BotControllerInterceptor> interceptors) {
        Object controllerReturnValue = null;
        try {
            if (botController.before(request)) {
                switch (request.getPostType()) {
                    case "message":
                        controllerReturnValue = botController.onMessage(request);
                        break;
                    case "event":
                        controllerReturnValue = botController.onEvent(request);
                        break;
                    case "request":
                        controllerReturnValue = botController.onRequest(request);
                        break;
                    default:
                        BotLog.warn("[调用 Controller ] 收到了未知的消息类型：" + request.getPostType());
                }
            }
        } catch (Exception e) {
            callException(botController, request, interceptors, e);
        }
        return controllerReturnValue;
    }

    private void callException(BotController botController, BotRequest request, Set<BotControllerInterceptor> interceptors, Exception exception) {
        for (BotControllerInterceptor interceptor : interceptors) {
            interceptor.throwException(request, botController, exception);
        }
    }

    private void callAfter(BotRequest request, Set<BotControllerInterceptor> interceptors, Object controllerReturnValue) {
        for (BotControllerInterceptor interceptor : interceptors) {
            interceptor.after(request, controllerReturnValue);
        }
    }
}
