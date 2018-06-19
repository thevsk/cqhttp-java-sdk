package top.thevsk.entity;

import top.thevsk.send.BotHttpSender;
import top.thevsk.utils.CQUtils;
import top.thevsk.utils.StrUtils;

class BotFastReply {

    private BotHttpSender botHttpSender;

    private BotRequest request;

    BotFastReply(BotHttpSender botHttpSender, BotRequest request) {
        this.botHttpSender = botHttpSender;
        this.request = request;
    }


    /**
     * 回复消息
     *
     * @param message
     * @return
     */
    public BotResponse reply(String message) {
        if (request.getMessageType() != null) {
            switch (request.getMessageType()) {
                case "discuss":
                    return botHttpSender.sendDiscussMsg(request.getDiscussId(), message, false);
                case "group":
                    return botHttpSender.sendGroupMsg(request.getGroupId(), message, false);
                case "private":
                    return botHttpSender.sendPrivateMsg(request.getUserId(), message, false);
                default:
                    return null;
            }
        }
        if (request.getGroupId() != null) {
            return botHttpSender.sendGroupMsg(request.getGroupId(), message, false);
        }
        return null;
    }

    /**
     * 回复消息，并且 @ 消息发送人
     *
     * @param message
     * @return
     */
    public BotResponse replyAt(String message) {
        return reply(CQUtils.at(request.getUserId()) + " " + message);
    }

    /**
     * 回复某人 私人消息
     *
     * @param message
     * @param userId
     * @return
     */
    public BotResponse replyPrivate(String message, Long userId) {
        return botHttpSender.sendPrivateMsg(userId, message, false);
    }

    public BotResponse replyGroup(String message, Long groupId) {
        return botHttpSender.sendGroupMsg(groupId, message, false);
    }

    /**
     * 踢出消息发送人 仅限群
     *
     * @return
     */
    public BotResponse kick() {
        return kick(request.getUserId());
    }


    /**
     * 踢出某人 仅限群
     *
     * @return
     */
    public BotResponse kick(Long userId) {
        if (StrUtils.isNotNullObjects(request.getGroupId(), userId)) {
            return botHttpSender.setGroupKick(request.getGroupId(), userId, false);
        }
        return null;
    }

    /**
     * 禁言消息发送人 仅限群
     *
     * @param time
     * @return
     */
    public BotResponse ban(Long time) {
        if (StrUtils.isNotNullObjects(request.getGroupId(), request.getFlag())) {
            return botHttpSender.setGroupAnonymousBan(request.getGroupId(), request.getFlag(), time);
        }
        return ban(request.getUserId(), time);
    }

    /**
     * 禁言某人 仅限群
     *
     * @param time
     * @return
     */
    public BotResponse ban(Long userId, Long time) {
        if (StrUtils.isNotNullObjects(request.getGroupId(), userId)) {
            return botHttpSender.setGroupBan(request.getGroupId(), userId, time);
        }
        return null;
    }

    /**
     * 登录号退出群 or 讨论组
     *
     * @return
     */
    public BotResponse leave() {
        if (StrUtils.isNotNullObjects(request.getDiscussId())) {
            return botHttpSender.setDiscussLeave(request.getDiscussId());
        }
        if (StrUtils.isNotNullObjects(request.getGroupId())) {
            return botHttpSender.setGroupLeave(request.getGroupId(), false);
        }
        return null;
    }

    /**
     * 设置消息发送人的名片
     *
     * @param card
     * @return
     */
    public BotResponse setCard(String card) {
        return setCard(request.getUserId(), card);
    }

    /**
     * 设置某人的名片
     *
     * @param card
     * @return
     */
    public BotResponse setCard(Long userId, String card) {
        if (StrUtils.isNotNullObjects(request.getGroupId(), userId)) {
            return botHttpSender.setGroupCard(request.getGroupId(), userId, card);
        }
        return null;
    }

    /**
     * 设置消息发送人头衔
     *
     * @param title
     * @return
     */
    public BotResponse setSpecialTitle(String title) {
        return setSpecialTitle(request.getUserId(), title);
    }

    /**
     * 设置某人头衔
     *
     * @param title
     * @return
     */
    public BotResponse setSpecialTitle(Long userId, String title) {
        if (StrUtils.isNotNullObjects(request.getGroupId(), userId)) {
            return botHttpSender.setGroupSpecialTitle(request.getGroupId(), userId, title);
        }
        return null;
    }

    /**
     * 设置消息发送人为管理员 or 取消管理员
     *
     * @param enable
     * @return
     */
    public BotResponse setAdmin(Boolean enable) {
        return setAdmin(request.getUserId(), enable);
    }

    /**
     * 设置某人为管理员 or 取消管理员
     *
     * @param enable
     * @return
     */
    public BotResponse setAdmin(Long userId, Boolean enable) {
        if (StrUtils.isNotNullObjects(request.getGroupId(), userId)) {
            return botHttpSender.setGroupAdmin(request.getGroupId(), userId, enable);
        }
        return null;
    }

    /**
     * 回复加群 or 加好友
     *
     * @param approve
     * @param message
     * @return
     */
    public BotResponse requestAdd(Boolean approve, String message) {
        if (!"request".equals(request.getPostType())) return null;
        if ("friend".equals(request.getRequestType())) {
            return botHttpSender.setFriendAddRequest(request.getFlag(), approve, message);
        } else {
            return botHttpSender.setGroupAddRequest(request.getFlag(), request.getSubType(), approve, message);
        }
    }
}
