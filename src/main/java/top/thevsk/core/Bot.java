package top.thevsk.core;

import com.alibaba.fastjson.JSON;
import top.thevsk.annotation.BotInterceptor;
import top.thevsk.aop.BotControllerInterceptor;
import top.thevsk.aop.BotSendInterceptor;
import top.thevsk.entity.BotConfig;
import top.thevsk.entity.BotRequest;
import top.thevsk.entity.HttpRequest;
import top.thevsk.log.BotLog;
import top.thevsk.send.BotHttpSender;
import top.thevsk.utils.BeanUtils;
import top.thevsk.utils.HmacSHA1Utils;
import top.thevsk.utils.StrUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author thevsk
 * @ProjectName https://github.com/thevsk/cqhttp-java-sdk
 * @date 2018-06-19 20:56
 */
public class Bot {

    private HashSet<BotController> botControllers = null;

    private HashSet<BotControllerInterceptor> botControllerInterceptors = null;

    private ConcurrentHashMap<String, BotControllerInterceptor> botControllerInterceptorConcurrentHashMap = null;

    private HashSet<BotSendInterceptor> botSendInterceptors = null;

    private ConcurrentHashMap<String, Set<BotControllerInterceptor>> controllerInterceptorMap = null;

    private HttpServer httpServer = null;

    private BotConfig botConfig = null;

    private BotHttpSender botHttpSender = null;

    public BotConfig getBotConfig() {
        return botConfig;
    }

    public BotHttpSender getBotHttpSender() {
        return botHttpSender;
    }

    public Bot(int port, BotConfig botConfig) {
        botControllerInterceptors = new HashSet<>();
        botControllers = new HashSet<>();
        botSendInterceptors = new HashSet<>();
        controllerInterceptorMap = new ConcurrentHashMap<>();
        botControllerInterceptorConcurrentHashMap = new ConcurrentHashMap<>();
        httpServer = new HttpServer();
        httpServer.setPort(port);
        botHttpSender = new BotHttpSender();
        this.botConfig = botConfig;
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
        if (botControllers.size() == 0) {
            throw new RuntimeException("没有任何接收数据的 controller");
        }
        if (StrUtils.isBlank(botConfig.getApiHost())) {
            throw new RuntimeException("http api host 为空！：new BotConfig().setApiHost(\"127.0.0.1\").setApiPort(5700)");
        }
        if (botConfig.getApiPort() == 0) {
            throw new RuntimeException("http api port 为空！：new BotConfig().setApiHost(\"127.0.0.1\").setApiPort(5700)");
        }
        // init botControllerInterceptorConcurrentHashMap
        for (BotControllerInterceptor interceptor : botControllerInterceptors) {
            botControllerInterceptorConcurrentHashMap.put(interceptor.getClass().getName(), interceptor);
        }
        // init controllerInterceptorMap
        for (BotController botController : botControllers) {
            HashSet<BotControllerInterceptor> interceptors = new HashSet<>();
            BeanUtils.copy(botControllerInterceptors, interceptors);
            BotInterceptor controllerInterceptor = botController.getClass().getAnnotation(BotInterceptor.class);
            if (controllerInterceptor != null) {
                Class<? extends BotControllerInterceptor>[] interceptorsClass = controllerInterceptor.value();
                for (Class clazz : interceptorsClass) {
                    if (botControllerInterceptorConcurrentHashMap.get(clazz.getName()) == null) {
                        BotControllerInterceptor botControllerInterceptor = (BotControllerInterceptor) BeanUtils.newInstance(clazz);
                        interceptors.add(botControllerInterceptor);
                        botControllerInterceptorConcurrentHashMap.put(botControllerInterceptor.getClass().getName(), botControllerInterceptor);
                    } else {
                        interceptors.add(botControllerInterceptorConcurrentHashMap.get(clazz.getName()));
                    }
                }
            }
            controllerInterceptorMap.put(botController.getClass().getName(), interceptors);
        }
        // init httpServer
        httpServer.setBot(this);
        // init botHttpSender
        botHttpSender.setBot(this);
        botHttpSender.setBotSendInterceptors(botSendInterceptors);
        httpServer.start();
        BotLog.info("server started...");
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
        call(request.getBody());
    }

    private void call(String json) {
        BotRequest request = new BotRequest(JSON.parseObject(json), botHttpSender);
        for (BotController botController : botControllers) {
            Set<BotControllerInterceptor> interceptors = controllerInterceptorMap.get(botController.getClass().getName());
            if (callBefore(botController, request, interceptors)) {
                callAfter(botController, request, interceptors, callController(botController, request, interceptors));
            }
        }
    }

    private boolean callBefore(BotController botController, BotRequest request, Set<BotControllerInterceptor> interceptors) {
        for (BotControllerInterceptor interceptor : interceptors) {
            if (!interceptor.before(request, botController)) {
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

    private void callAfter(BotController botController, BotRequest request, Set<BotControllerInterceptor> interceptors, Object controllerReturnValue) {
        for (BotControllerInterceptor interceptor : interceptors) {
            interceptor.after(request, botController, controllerReturnValue);
        }
    }
}
