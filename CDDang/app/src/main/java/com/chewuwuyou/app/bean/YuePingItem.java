package com.chewuwuyou.app.bean;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class YuePingItem {
	private int id;// 6,
	private String url;// "/upload/20150206144153349.png",
	private String noteName;// "\u8001\u599e",
	private String nickName;// ""
	private String content;// "werefkdsjkfdjfkdsjfkdsjkfjsdkfdskfjdsfksjdkf",
	private String publishTime;// 评论时间
	private String friendId; // 评论的朋友 -- 可以是自己 3460
	private YuePingReplyItem toWho;// 针对那一条评论回复的
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNoteName() {
		return noteName;
	}
	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public YuePingReplyItem getToWho() {
		return toWho;
	}
	public void setToWho(YuePingReplyItem toWho) {
		this.toWho = toWho;
	}
	public static YuePingItem parse(String pingJson) {
		YuePingItem ping = null;
        if (pingJson != null) {
            Gson g = new Gson();
            ping = g.fromJson(pingJson, YuePingItem.class);
        }
        return ping;
    }
	
	public static List<YuePingItem> parseList(String json) {
        Gson g = new Gson();
        List<YuePingItem> list = g.fromJson(json,
                new TypeToken<List<YuePingItem>>() {
                }.getType());
        return list;
    }
}
