package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * 消息界面历史消息实体
 * 
 * @author yuyong
 * 
 */
public class UserChatHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;// 区分群聊、客服（小当弟、小当妹）（后续客服系统会升级）
	private String url;// 聊天头像
	private String name;// 名称
	private String message;// 消息
	private String time;// 发送消息的时间
	private int messageNum;// 消息条数
	private String from;// 和谁聊天
	private int noticeSum;// 未读消息的条数
	private String room;// 群聊的ID
	private String noteName;
	private String nickName;
	private String userName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getMessageNum() {
		return messageNum;
	}

	public void setMessageNum(int messageNum) {
		this.messageNum = messageNum;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public int getNoticeSum() {
		return noticeSum;
	}

	public void setNoticeSum(int noticeSum) {
		this.noticeSum = noticeSum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserChatHistory [url=" + url + ", name=" + name + ", message="
				+ message + ", time=" + time + ", messageNum=" + messageNum
				+ "]";
	}

}
