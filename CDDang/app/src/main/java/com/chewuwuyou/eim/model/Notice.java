package com.chewuwuyou.eim.model;

import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * 消息实体.
 *
 * @author sxk
 */
public class Notice implements Serializable, Comparable<Notice> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int ADD_FRIEND = 1;// 好友请求
    public static final int SYS_MSG = 2; // 系统消息
    public static final int CHAT_MSG = 3;// 聊天消息
    public static final int READ = 1;
    public static final int UNREAD = 0;//default
    public static final int All = 2;

    public static final int DEFAULT = 0;//默认
    public static final int AGREE = 1;//同意
    public static final int REFUSE = 2;//拒绝


    private String id; // 主键
    private String title; // 标题
    private String content; // 内容
    private Integer status; // 状态 0已读 1未读
    private String from; // 通知来源
    private String room = ""; // 通知来源
    private String to; // 通知去想
    private String noticeTime; // 通知时间
    private Integer noticeType; // 消息类型 1.好友请求 2.系统消息
    //多了一个好友添加状态friend_add_state 整形 0是指默认值,没被任何操作，1是同意，2是拒绝
    private int addState;//整形 0是指默认值,没被任何操作，1是同意，2是拒绝
    /**
     * userId : 23
     * name : tt
     * portraitUri : url
     */

    private String userId;
    private String name;
    private String portraitUri;

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    @Override
    public int compareTo(Notice oth) {
        if (null == this.getNoticeTime() || null == oth.getNoticeTime()) {
            return 0;
        }
        String format = null;
        String time1 = "";
        String time2 = "";
        if (this.getNoticeTime().length() == oth.getNoticeTime().length()
                && this.getNoticeTime().length() == 23) {
            time1 = this.getNoticeTime();
            time2 = oth.getNoticeTime();
            format = Constant.MS_FORMART;
        } else {
            time1 = this.getNoticeTime().substring(0, 19);
            time2 = oth.getNoticeTime().substring(0, 19);
        }
        Date da1 = DateUtil.str2Date(time1, format);
        Date da2 = DateUtil.str2Date(time2, format);
        if (da1.before(da2)) {
            return 1;
        }
        if (da2.before(da1)) {
            return -1;
        }

        return 0;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getAddState() {
        return addState;
    }

    public void setAddState(int addState) {
        this.addState = addState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }


    public static List<Notice> parseList(String s) {
        List<Notice> notices = null;
        if (s != null) {
            Gson g = new Gson();
            notices = g.fromJson(s, new TypeToken<List<Notice>>() {
            }.getType());
        }
        return notices;
    }

}
