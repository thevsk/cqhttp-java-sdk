# cqhttp-java-sdk 

> 感谢 [@richardchien](https://github.com/richardchien) 提供 [richardchien/coolq-http-api](https://github.com/richardchien/coolq-http-api)

基于 [酷Q](https://cqp.cc/) 与 [richardchien/coolq-http-api](https://github.com/richardchien/coolq-http-api) 的 JAVA SDK

## 使用方式

**一个简单的示例：回复私聊**

```java
import top.thevsk.core.Bot;
import top.thevsk.core.BotController;
import top.thevsk.entity.BotConfig;
import top.thevsk.entity.BotRequest;

public class Test {


    public static void main(String[] args) {
        new Bot(7800, new BotConfig().setApiHost("127.0.0.1").setApiPort(5700))
            .addBotController(new Controller())
            .start();
    }

    public static class Controller implements BotController {

        @Override
        public boolean before(BotRequest request) {
            return "private".equals(request.getMessageType());
        }

        @Override
        public Object onMessage(BotRequest request) {
            request.getFastReply().reply("你好");
            return null;
        }

        @Override
        public Object onEvent(BotRequest request) {
            return null;
        }

        @Override
        public Object onRequest(BotRequest request) {
            return null;
        }
    }
}
```

## BUG提交及联络方式

若有BUG请加我QQ或者发邮件

QQ：[2522534416](http://wpa.qq.com/msgrd?v=3&uin=2522534416&site=qq&menu=yes)

QQ：[1916079648](http://wpa.qq.com/msgrd?v=3&uin=1916079648&site=qq&menu=yes)

邮箱：zafkielcn@gmail.com