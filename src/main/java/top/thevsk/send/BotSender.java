package top.thevsk.send;

import top.thevsk.entity.BotResponse;

public interface BotSender {

    String SEND_PRIVATE_MSG = "send_private_msg";
    String SEND_GROUP_MSG = "send_group_msg";
    String SEND_DISCUSS_MSG = "send_discuss_msg";
    String SEND_MSG = "send_msg";
    String DELETE_MSG = "delete_msg";
    String SEND_LIKE = "send_like";
    String SET_GROUP_KICK = "set_group_kick";
    String SET_GROUP_BAN = "set_group_ban";
    String SET_GROUP_ANONYMOUS_BAN = "set_group_anonymous_ban";
    String SET_GROUP_WHOLE_BAN = "set_group_whole_ban";
    String SET_GROUP_ADMIN = "set_group_admin";
    String SET_GROUP_ANONYMOUS = "set_group_anonymous";
    String SET_GROUP_CARD = "set_group_card";
    String SET_GROUP_LEAVE = "set_group_leave";
    String SET_GROUP_SPECIAL_TITLE = "set_group_special_title";
    String SET_DISCUSS_LEAVE = "set_discuss_leave";
    String SET_FRIEND_ADD_REQUEST = "set_friend_add_request";
    String SET_GROUP_ADD_REQUEST = "set_group_add_request";
    String GET_LOGIN_INFO = "get_login_info";
    String GET_STRANGER_INFO = "get_stranger_info";
    String GET_GROUP_LIST = "get_group_list";
    String GET_GROUP_MEMBER_INFO = "get_group_member_info";
    String GET_GROUP_MEMBER_LIST = "get_group_member_list";
    String GET_COOKIES = "get_cookies";
    String GET_CSRF_TOKEN = "get_csrf_token";
    String GET_RECORD = "get_record";
    String GET_STATUS = "get_status";
    String GET_VERSION_INFO = "get_version_info";
    String SET_RESTART = "set_restart";
    String SET_RESTART_PLUGIN = "set_restart_plugin";
    String CLEAN_DATA_DIR = "clean_data_dir";
    String _GET_FRIEND_LIST = "_get_friend_list";
    String _GET_GROUP_INFO = "_get_group_info";

    int timeOut = 3000;


    /**
     * 获取登录号信息
     *
     * @return {
     * user_id : QQ 号
     * nickname : QQ 昵称
     * }
     */
    BotResponse getLoginInfo();

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
    BotResponse getStrangerInfo(Long userId, Boolean noCache);

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
    BotResponse getGroupList();

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
    BotResponse getGroupMemberInfo(Long groupId, Long userId, Boolean noCache);

    /**
     * 获取群成员列表
     *
     * @param groupId 群id
     * @return [{...}]
     */
    BotResponse getGroupMemberList(Long groupId);

    /**
     * 获取cookies
     *
     * @return
     */
    BotResponse getCookies();

    /**
     * 获取 CSRF Token
     *
     * @return
     */
    BotResponse getCsrfToken();

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
    BotResponse _getFriendList();

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
    BotResponse _getGroupInfo(Long group_id);

    /**
     * 发送消息
     *
     * @param messageType 消息类型，支持 private、group、discuss，分别对应私聊、群组、讨论组
     * @param id
     * @param message     要发送的内容
     * @param autoEscape  消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    BotResponse sendMsg(String messageType, Long id, String message, Boolean autoEscape);

    /**
     * 发送私人消息
     *
     * @param userId     对方 QQ 号
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    BotResponse sendPrivateMsg(Long userId, String message, Boolean autoEscape);

    /**
     * 发送群消息
     *
     * @param groupId    群号
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    BotResponse sendGroupMsg(Long groupId, String message, Boolean autoEscape);

    /**
     * 发送讨论组消息
     *
     * @param discussId  讨论组 ID（正常情况下看不到，需要从讨论组消息上报的数据中获得）
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    BotResponse sendDiscussMsg(Long discussId, String message, Boolean autoEscape);

    /**
     * 发送好友赞
     *
     * @param userId 对方 QQ 号
     * @param times  赞的次数，每个好友每天最多 10 次
     * @return
     */
    BotResponse sendLike(Long userId, int times);

    /**
     * 撤回消息
     *
     * @param messageId 消息 ID
     * @return
     */
    BotResponse deleteMsg(Long messageId);

    /**
     * 群组踢人
     *
     * @param groupId          群号
     * @param userId           要踢的 QQ 号
     * @param rejectAddRequest 拒绝此人的加群请求
     * @return
     */
    BotResponse setGroupKick(Long groupId, Long userId, Boolean rejectAddRequest);

    /**
     * 群组单人禁言
     *
     * @param groupId  群号
     * @param userId   要禁言的 QQ 号
     * @param duration 禁言时长，单位秒，0 表示取消禁言
     * @return
     */
    BotResponse setGroupBan(Long groupId, Long userId, Long duration);

    /**
     * 群组匿名用户禁言
     *
     * @param groupId  群号
     * @param flag     要禁言的匿名用户的 flag（需从群消息上报的数据中获得）
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     * @return
     */
    BotResponse setGroupAnonymousBan(Long groupId, String flag, Long duration);

    /**
     * 群组全员禁言
     *
     * @param groupId 群号
     * @param enable  是否禁言
     * @return
     */
    BotResponse setGroupWholeBan(Long groupId, Boolean enable);

    /**
     * 群组设置管理员
     *
     * @param groupId 群号
     * @param userId  要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     * @return
     */
    BotResponse setGroupAdmin(Long groupId, Long userId, Boolean enable);

    /**
     * 群组匿名
     *
     * @param groupId 群号
     * @param enable  是否允许匿名聊天
     * @return
     */
    BotResponse setGroupAnonymous(Long groupId, Boolean enable);

    /**
     * 设置群名片（群备注）
     *
     * @param groupId 群号
     * @param userId  要设置的 QQ 号
     * @param card    群名片内容，不填或空字符串表示删除群名片
     * @return
     */
    BotResponse setGroupCard(Long groupId, Long userId, String card);

    /**
     * 退出群组
     *
     * @param groupId   群号
     * @param isDismiss 是否解散，如果登录号是群主，则仅在此项为 true 时能够解散
     * @return
     */
    BotResponse setGroupLeave(Long groupId, Boolean isDismiss);

    /**
     * 设置群组专属头衔
     *
     * @param groupId      群号
     * @param userId       要设置的 QQ 号
     * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
     * @return
     */
    BotResponse setGroupSpecialTitle(Long groupId, Long userId, String specialTitle);

    /**
     * 退出讨论组
     *
     * @param discussId 讨论组 ID（正常情况下看不到，需要从讨论组消息上报的数据中获得）
     * @return
     */
    BotResponse setDiscussLeave(Long discussId);

    /**
     * 处理加好友请求
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求
     * @param remark  添加后的好友备注（仅在同意时有效）
     * @return
     */
    BotResponse setFriendAddRequest(String flag, Boolean approve, String remark);

    /**
     * 处理加群请求／邀请
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param type    add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param approve 是否同意请求／邀请
     * @param reason  拒绝理由（仅在拒绝时有效）
     * @return
     */
    BotResponse setGroupAddRequest(String flag, String type, Boolean approve, String reason);

    /**
     * 获取语音
     *
     * @param file       收到的语音文件名，如 0B38145AA44505000B38145AA4450500.silk
     * @param out_format 要转换到的格式，目前支持 mp3、amr、wma、m4a、spx、ogg、wav、flac
     * @return 转换后的语音文件名，如 0B38145AA44505000B38145AA4450500.mp3
     */
    BotResponse getRecord(String file, String out_format);

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
    BotResponse getStatus();

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
    BotResponse getVersionInfo();

    /**
     * 重启酷 Q，并以当前登录号自动登录（需勾选快速登录）
     *
     * @return
     */
    BotResponse setRestart();

    /**
     * 重启 HTTP API 插件
     *
     * @return
     */
    BotResponse setRestartPlugin();

    /**
     * 清理数据目录
     *
     * @return
     */
    BotResponse cleanDataDir(String dataDir);
}
