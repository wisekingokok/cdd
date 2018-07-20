package com.chewuwuyou.eim.model;

import java.util.Date;

import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.util.DateUtil;

import android.os.Parcel;
import android.os.Parcelable;

// 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：  
// android.os.BadParcelableException:  
// Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person  
// 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用  
// 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；  
// 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
// 5.反序列化对象  
public class IMMessage implements Parcelable, Comparable<IMMessage> {
	public static final String IMMESSAGE_KEY = "immessage.key";
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	private int _id;
	private int type;
	private String content;
	private String time;
	/**
	 * 存在本地，表示与谁聊天
	 */
	private String fromSubJid;
	/**
	 * 存在本地，表示和哪个聊天室有关，默认空字符串
	 */
	private String fromRoom = "";
	/**
	 * 0:接受 1：发送
	 */
	private int msgType = 0;
	private String fileName;
	private String msgTo;// 从哪里发送

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Parcelable.Creator<IMMessage> CREATOR = new Creator() {

		@Override
		public IMMessage createFromParcel(Parcel source) {
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			IMMessage msg = new IMMessage();
			msg.set_id(source.readInt());
			msg.setType(source.readInt());
			msg.setContent(source.readString());
			msg.setTime(source.readString());
			msg.setFromSubJid(source.readString());
			msg.setFromRoom(source.readString());
			msg.setMsgType(source.readInt());
			msg.setFileName(source.readString());
			msg.setMsgTo(source.readString());
			return msg;
		}

		@Override
		public IMMessage[] newArray(int size) {
			return new IMMessage[size];
		}
	};

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public IMMessage() {
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

	/**
	 * 0:接受 1：发送
	 */
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMsgTo() {
		return msgTo;
	}

	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeInt(type);
		dest.writeString(content);
		dest.writeString(time);
		dest.writeString(fromSubJid);
		dest.writeInt(msgType);
		dest.writeString(fileName);
		dest.writeString(fromRoom);
		dest.writeString(msgTo);
	}

	/**
	 * 新消息的构造方法.
	 * 
	 * @param content
	 * @param time
	 */
	public IMMessage(String content, String time, String withSb, int msgType) {
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
	public int compareTo(IMMessage oth) {
		if (null == this.getTime() || null == oth.getTime()) {
			return 0;
		}
		String format = null;
		String time1 = "";
		String time2 = "";
		if (this.getTime().length() == oth.getTime().length() && this.getTime().length() == 23) {
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
