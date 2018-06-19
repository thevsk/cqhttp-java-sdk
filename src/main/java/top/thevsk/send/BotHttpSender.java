package top.thevsk.send;

import com.alibaba.fastjson.JSON;
import top.thevsk.aop.BotSendInterceptor;
import top.thevsk.core.Bot;
import top.thevsk.entity.BotResponse;
import top.thevsk.utils.StrUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BotHttpSender implements BotSender {

    private Bot bot;

    private HashSet<BotSendInterceptor> botSendInterceptors;

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public void setBotSendInterceptors(HashSet<BotSendInterceptor> botSendInterceptors) {
        this.botSendInterceptors = botSendInterceptors;
    }

    private BotResponse baseSend(String method, Map<String, Object> param) {
        for (BotSendInterceptor interceptor : botSendInterceptors) {
            if (!interceptor.before(method, param)) return null;
        }
        try {
            BotResponse botResponse = _baseSend(method, param == null ? "{}" : JSON.toJSONString(param));
            for (BotSendInterceptor interceptor : botSendInterceptors) {
                interceptor.after(method, param, botResponse);
            }
            return botResponse;
        } catch (IOException e) {
            for (BotSendInterceptor interceptor : botSendInterceptors) {
                interceptor.throwException(method, param, e);
            }
            e.printStackTrace();
        }
        return null;
    }

    private BotResponse _baseSend(String method, String param) throws IOException {
        URL url = new URL("http://" + bot.getBotConfig().getApiHost() + ":" + bot.getBotConfig().getApiPort() + "/" + method);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setConnectTimeout(timeOut);
        conn.setReadTimeout(timeOut);

        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("User-Agent", "thevsk-bot-sdk/1.0.0");
        if (StrUtils.isNotBlank(bot.getBotConfig().getAccessToken())) {
            conn.setRequestProperty("Token", " " + bot.getBotConfig().getAccessToken());
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        if (param == null) {
            bufferedWriter.write("{}");
        } else {
            bufferedWriter.write(param);
        }
        bufferedWriter.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
            json.append(line);
        }
        bufferedReader.close();
        bufferedWriter.close();
        return JSON.toJavaObject(JSON.parseObject(json.toString()), BotResponse.class);
    }


    /**
     * 获取登录号信息
     *
     * @return {
     * user_id : QQ 号
     * nickname : QQ 昵称
     * }
     */
    public BotResponse getLoginInfo() {
        return baseSend(GET_LOGIN_INFO, null);
    }

    /**
     * 获取陌生人信息
     *
     * @param userId  qq
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return {
     * user_id : QQ 号
     * nickname : QQ 昵称
     * sex : 性别，male 或 female 或 unknown
     * age : 年龄
     * }
     */
    public BotResponse getStrangerInfo(Long userId, Boolean noCache) {
        return baseSend(GET_STRANGER_INFO, new HashMap<String, Object>() {
            {
                put("user_id", userId);
                put("no_cache", noCache);
            }
        });
    }

    /**
     * 获取群列表
     *
     * @return [
     * {
     * group_id : 群号
     * group_name : 群名
     * }
     * ]
     */
    public BotResponse getGroupList() {
        return baseSend(GET_GROUP_LIST, null);
    }

    /**
     * 获取群成员信息
     *
     * @param groupId 群id
     * @param userId  qq
     * @param noCache 缓存
     * @return {
     * group_id : 群号
     * user_id : QQ 号
     * nickname : 昵称
     * card : 群名片／备注
     * sex : 性别，male 或 female 或 unknown
     * age : 年龄
     * area : 地区
     * join_time : 加群时间戳
     * last_sent_time : 最后发言时间戳
     * level : 成员等级
     * role : 角色，owner 或 admin 或 member
     * unfriendly : 是否不良记录成员
     * title : 专属头衔
     * title_expire_time : 专属头衔过期时间戳
     * card_changeable : 是否允许修改群名片
     * }
     */
    public BotResponse getGroupMemberInfo(Long groupId, Long userId, Boolean noCache) {
        return baseSend(GET_GROUP_MEMBER_INFO, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("user_id", userId);
                put("no_cache", noCache);
            }
        });
    }

    /**
     * 获取群成员列表
     *
     * @param groupId 群id
     * @return [{...}]
     */
    public BotResponse getGroupMemberList(Long groupId) {
        return baseSend(GET_GROUP_MEMBER_LIST, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
            }
        });
    }

    /**
     * 获取cookies
     *
     * @return {}
     */
    public BotResponse getCookies() {
        return baseSend(GET_COOKIES, null);
    }

    /**
     * 获取 CSRF Token
     *
     * @return {}
     */
    public BotResponse getCsrfToken() {
        return baseSend(GET_CSRF_TOKEN, null);
    }

    /**
     * 获取好友列表 不稳定接口
     *
     * @return [
     * {
     * friend_group_id : 好友分组 ID
     * friend_group_name : 好友分组名称
     * friends : [
     * {
     * nickname : 好友昵称
     * remark : 好友备注
     * user_id : 好友 QQ 号
     * }
     * ]
     * }
     * ]
     */
    public BotResponse _getFriendList() {
        return baseSend(_GET_GROUP_INFO, null);
    }

    /**
     * 获取群信息 不稳定接口
     *
     * @param group_id 群id
     * @return {
     * group_id : 群号
     * group_name : 群名称
     * create_time : 创建时间
     * category : 群分类，具体这个 ID 对应的名称暂时没有
     * member_count : 成员数
     * introduction : 群介绍
     * admins : [ 群主和管理员列表
     * {
     * user_id : QQ 号
     * nickname : 昵称
     * role : 角色，owner 表示群主、admin 表示管理员
     * }
     * ]
     * }
     */
    @Override
    public BotResponse _getGroupInfo(Long group_id) {
        return baseSend(_GET_FRIEND_LIST, new HashMap<String, Object>() {
            {
                put("group_id", group_id);
            }
        });
    }

    /**
     * 发送消息
     *
     * @param messageType 消息类型，支持 private、group、discuss，分别对应私聊、群组、讨论组
     * @param id
     * @param message     要发送的内容
     * @param autoEscape  消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public BotResponse sendMsg(String messageType, Long id, String message, Boolean autoEscape) {
        return baseSend(SEND_MSG, new HashMap<String, Object>() {
            {
                put("message_type", messageType);
                switch (messageType) {
                    case "private":
                        put("user_id", id);
                        break;
                    case "group":
                        put("group_id", id);
                        break;
                    case "discuss":
                        put("discuss_id", id);
                        break;
                    default:
                        throw new RuntimeException();
                }
                put("message", message);
                put("auto_escape", autoEscape);
            }
        });
    }

    /**
     * 发送私人消息
     *
     * @param userId     对方 QQ 号
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public BotResponse sendPrivateMsg(Long userId, String message, Boolean autoEscape) {
        return sendMsg("private", userId, message, autoEscape);
    }

    /**
     * 发送群消息
     *
     * @param groupId    群号
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public BotResponse sendGroupMsg(Long groupId, String message, Boolean autoEscape) {
        return sendMsg("group", groupId, message, autoEscape);
    }

    /**
     * 发送讨论组消息
     *
     * @param discussId  讨论组 ID（正常情况下看不到，需要从讨论组消息上报的数据中获得）
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public BotResponse sendDiscussMsg(Long discussId, String message, Boolean autoEscape) {
        return sendMsg("discuss", discussId, message, autoEscape);
    }

    /**
     * 发送好友赞
     *
     * @param userId 对方 QQ 号
     * @param times  赞的次数，每个好友每天最多 10 次
     * @return
     */
    public BotResponse sendLike(Long userId, int times) {
        return baseSend(SEND_LIKE, new HashMap<String, Object>() {
            {
                put("user_id", userId);
                put("times", times);
            }
        });
    }

    /**
     * 撤回消息
     *
     * @param messageId 消息 ID
     * @return
     */
    public BotResponse deleteMsg(Long messageId) {
        return baseSend(DELETE_MSG, new HashMap<String, Object>() {
            {
                put("message_id", messageId);
            }
        });
    }

    /**
     * 群组踢人
     *
     * @param groupId          群号
     * @param userId           要踢的 QQ 号
     * @param rejectAddRequest 拒绝此人的加群请求
     * @return
     */
    public BotResponse setGroupKick(Long groupId, Long userId, Boolean rejectAddRequest) {
        return baseSend(SET_GROUP_KICK, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("user_id", userId);
                put("reject_add_request", rejectAddRequest);
            }
        });
    }

    /**
     * 群组单人禁言
     *
     * @param groupId  群号
     * @param userId   要禁言的 QQ 号
     * @param duration 禁言时长，单位秒，0 表示取消禁言
     * @return
     */
    public BotResponse setGroupBan(Long groupId, Long userId, Long duration) {
        return baseSend(SET_GROUP_BAN, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("user_id", userId);
                put("duration", duration);
            }
        });
    }

    /**
     * 群组匿名用户禁言
     *
     * @param groupId  群号
     * @param flag     要禁言的匿名用户的 flag（需从群消息上报的数据中获得）
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     * @return
     */
    public BotResponse setGroupAnonymousBan(Long groupId, String flag, Long duration) {
        return baseSend(SET_GROUP_ANONYMOUS_BAN, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("flag", flag);
                put("duration", duration);
            }
        });
    }

    /**
     * 群组全员禁言
     *
     * @param groupId 群号
     * @param enable  是否禁言
     * @return
     */
    public BotResponse setGroupWholeBan(Long groupId, Boolean enable) {
        return baseSend(SET_GROUP_WHOLE_BAN, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("enable", enable);
            }
        });
    }

    /**
     * 群组设置管理员
     *
     * @param groupId 群号
     * @param userId  要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     * @return
     */
    public BotResponse setGroupAdmin(Long groupId, Long userId, Boolean enable) {
        return baseSend(SET_GROUP_ADMIN, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("user_id", userId);
                put("enable", enable);
            }
        });
    }

    /**
     * 群组匿名
     *
     * @param groupId 群号
     * @param enable  是否允许匿名聊天
     * @return
     */
    public BotResponse setGroupAnonymous(Long groupId, Boolean enable) {
        return baseSend(SET_GROUP_ANONYMOUS, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("enable", enable);
            }
        });
    }

    /**
     * 设置群名片（群备注）
     *
     * @param groupId 群号
     * @param userId  要设置的 QQ 号
     * @param card    群名片内容，不填或空字符串表示删除群名片
     * @return
     */
    public BotResponse setGroupCard(Long groupId, Long userId, String card) {
        return baseSend(SET_GROUP_CARD, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("user_id", userId);
                put("card", card);
            }
        });
    }

    /**
     * 退出群组
     *
     * @param groupId   群号
     * @param isDismiss 是否解散，如果登录号是群主，则仅在此项为 true 时能够解散
     * @return
     */
    public BotResponse setGroupLeave(Long groupId, Boolean isDismiss) {
        return baseSend(SET_GROUP_LEAVE, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("is_dismiss", isDismiss);
            }
        });
    }

    /**
     * 设置群组专属头衔
     *
     * @param groupId      群号
     * @param userId       要设置的 QQ 号
     * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
     * @return
     */
    public BotResponse setGroupSpecialTitle(Long groupId, Long userId, String specialTitle) {
        return baseSend(SET_GROUP_SPECIAL_TITLE, new HashMap<String, Object>() {
            {
                put("group_id", groupId);
                put("user_id", userId);
                put("special_title", specialTitle);
                put("duration", -1L);
            }
        });
    }

    /**
     * 退出讨论组
     *
     * @param discussId 讨论组 ID（正常情况下看不到，需要从讨论组消息上报的数据中获得）
     * @return
     */
    public BotResponse setDiscussLeave(Long discussId) {
        return baseSend(SET_DISCUSS_LEAVE, new HashMap<String, Object>() {
            {
                put("discuss_id", discussId);
            }
        });
    }

    /**
     * 处理加好友请求
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求
     * @param remark  添加后的好友备注（仅在同意时有效）
     * @return
     */
    public BotResponse setFriendAddRequest(String flag, Boolean approve, String remark) {
        return baseSend(SET_FRIEND_ADD_REQUEST, new HashMap<String, Object>() {
            {
                put("flag", flag);
                put("approve", approve);
                if (StrUtils.isNotBlank(remark))
                    put("remark", remark);
            }
        });
    }

    /**
     * 处理加群请求／邀请
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param type    add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param approve 是否同意请求／邀请
     * @param reason  拒绝理由（仅在拒绝时有效）
     * @return
     */
    public BotResponse setGroupAddRequest(String flag, String type, Boolean approve, String reason) {
        return baseSend(SET_GROUP_ADD_REQUEST, new HashMap<String, Object>() {
            {
                put("flag", flag);
                put("type", type);
                put("approve", approve);
                if (StrUtils.isNotBlank(reason))
                    put("reason", reason);
            }
        });
    }

    public BotResponse getRecord(String file, String out_format) {
        return baseSend(GET_RECORD, new HashMap<String, Object>() {
            {
                put("file", file);
                put("outFormat", out_format);
            }
        });
    }

    /**
     * 获取插件运行状态
     *
     * @return {
     * good : 插件状态符合预期，意味着插件已初始化，需要启动的服务都在正常运行，且 QQ 在线
     * app_initialized : 插件已初始化
     * app_enabled : 插件已启用
     * online : 当前 QQ 在线
     * http_service_good : use_http 配置项为 yes 时有此字段，表示 HTTP 服务正常运行
     * ws_service_good : use_ws 配置项为 yes 时有此字段，表示 WebSocket 服务正常运行
     * ws_reverse_service_good : use_ws_reverse 配置项为 yes 时有此字段，表示反向 WebSocket 服务正常运行
     * }
     */
    public BotResponse getStatus() {
        return baseSend(GET_STATUS, null);
    }

    /**
     * 获取酷 Q 及 HTTP API 插件的版本信息
     *
     * @return {
     * coolq_directory : 酷 Q 根目录路径
     * coolq_edition : 酷 Q 版本，air 或 pro
     * plugin_version : HTTP API 插件版本，例如 2.1.3
     * plugin_build_number : HTTP API 插件 build 号
     * plugin_build_configuration : HTTP API 插件编译配置，debug 或 release
     * }
     */
    public BotResponse getVersionInfo() {
        return baseSend(GET_VERSION_INFO, null);
    }

    /**
     * 重启酷 Q，并以当前登录号自动登录（需勾选快速登录）
     *
     * @return
     */
    public BotResponse setRestart() {
        return baseSend(SET_RESTART, null);
    }

    /**
     * 重启 HTTP API 插件
     *
     * @return
     */
    public BotResponse setRestartPlugin() {
        return baseSend(SET_RESTART_PLUGIN, null);
    }

    /**
     * 清理数据目录
     *
     * @return
     */
    public BotResponse cleanDataDir(String dataDir) {
        return baseSend(CLEAN_DATA_DIR, new HashMap<String, Object>() {
            {
                put("data_dir", dataDir);
            }
        });
    }
}
