package com.chewuwuyou.eim.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.util.DateUtil;

/**
 * 群消息实体
 * 
 * @author yy
 * 
 */
public class IMRoomMessage implements Parcelable, Comparable<IMRoomMessage> {
	public static final int SUCCESS = 0;
	private int type;
	private String content;// 消息内容
	private String time;// 发送消息时间
	private String fromSubJid;// 存在本地，表示与谁聊天
	private String fromRoom = "";// 存在本地，表示和哪个聊天室有关，默认空字符串
	private int msgType = 0;// 0:接收 1：发送
	private String msgTo;// 从哪里发送

	public IMRoomMessage() {
		this.type = SUCCESS;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFromSubJid() {
		return fromSubJid;
	}

	public void setFromSubJid(String fromSubJid) {
		this.fromSubJid = fromSubJid;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getMsgTo() {
		return msgTo;
	}

	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeString(content);
		dest.writeString(time);
		dest.writeString(fromSubJid);
		dest.writeInt(msgType);
		dest.writeString(fromRoom);
		dest.writeString(msgTo);
	}

	/**
	 * 新消息的构造方法.
	 * 
	 * @param content
	 * @param time
	 */
	public IMRoomMessage(String content, String time, String withSb, int msgType) {
		super();
		this.content = content;
		this.time = time;
		this.msgType = msgType;
		this.fromSubJid = withSb;

	}

	/**
	 * shimiso 按时间降序排列
	 */
	@Override
	public int compareTo(IMRoomMessage oth) {
		if (null == this.getTime() || null == oth.getTime()) {
			return 0;
		}
		String format = null;
		String time1 = "";
		String time2 = "";
		if (this.getTime().length() == oth.getTime().length()
				&& this.getTime().length() == 23) {
			time1 = this.getTime();
			time2 = oth.getTime();
			format = Constant.MS_FORMART;
		} else {
			time1 = this.getTime().substring(0, 19);
			time2 = oth.getTime().substring(0, 19);
		}
		Date da1 = DateUtil.str2Date(time1, format);
		Date da2 = DateUtil.str2Date(time2, format);
		if (da1.before(da2)) {
			return -1;
		}
		if (da2.before(da1)) {
			return 1;
		}

		return 0;
	}

	public String getFromRoom() {
		return fromRoom;
	}

	public void setFromRoom(String fromRoom) {
		this.fromRoom = fromRoom;
	}
}
