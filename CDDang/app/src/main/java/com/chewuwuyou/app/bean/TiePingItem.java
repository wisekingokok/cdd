package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:BBS帖子的评论实体
 * @author:XH
 * @version
 * @created:
 * 
 */
public class TiePingItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 6,
	private String url;// "/upload/20150206144153349.png",
	private String noteName;// "\u8001\u599e",
	private String nickName;// ""
	private String content;// "werefkdsjkfdjfkdsjfkdsjkfjsdkfdskfjdsfksjdkf",
	private String publishTime;// 评论时间
	private String friendId; // 评论的朋友 -- 可以是自己 3460
	private TiePingReplyItem toWho;// 针对那一条评论回复的
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
    public TiePingReplyItem getToWho() {
		return toWho;
	}
	public void setToWho(TiePingReplyItem toWho) {
		this.toWho = toWho;
	}
	public static TiePingItem parse(String pingJson) {
        TiePingItem ping = null;
        if (pingJson != null) {
            Gson g = new Gson();
            ping = g.fromJson(pingJson, TiePingItem.class);
        }
        return ping;
    }
	
	public static List<TiePingItem> parseList(String json) {
        Gson g = new Gson();
        List<TiePingItem> list = g.fromJson(json,
                new TypeToken<List<TiePingItem>>() {
                }.getType());
        return list;
    }
}
