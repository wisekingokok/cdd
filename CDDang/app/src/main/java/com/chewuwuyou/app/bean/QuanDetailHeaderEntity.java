package com.chewuwuyou.app.bean;
import java.io.Serializable;
import java.util.List;

public class QuanDetailHeaderEntity implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	private QuanInfoEntity quanInfo;
	private int id;//2
	private int userId;// 5,
	private String url;//圈动态发起人的头像地址 "/upload/20150206144153349.png",
	private String nickName;// ""
	private String sex;// 帖子发起人的性别
	private String noteName;// "\u8001\u599e",
	private String ziji;// 是不是自己发的，1是自己发的，0不是自己发的
	private String content;//"s"
	private String publishTime;// 评论时间
	private List<QuanTuItem> tus;// 多张图就是小图
	private int tucnt;// 图数量
	private List<QuanZanItem> quanzan;// 赞列表
	private int quanzancnt;// 帖详情点赞人数
	private String zaned;// 当前用户是否已经赞过,1标识赞过，0表示没赞过
	private int quanpingcnt;// 帖详情点评人数
	private String pinged;// 当前用户是否已经评论过
//	public QuanInfoEntity getQuanInfo() {
//		return quanInfo;
//	}
//
//	public void setQuanInfo(QuanInfoEntity quanInfo) {
//		this.quanInfo = quanInfo;
//	}



	public List<QuanZanItem> getQuanzan() {
		return quanzan;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getNickName() {
		return nickName;
	}



	public void setNickName(String nickName) {
		this.nickName = nickName;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}



	public String getNoteName() {
		return noteName;
	}



	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}



	public String getZiji() {
		return ziji;
	}



	public void setZiji(String ziji) {
		this.ziji = ziji;
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



	public List<QuanTuItem> getTus() {
		return tus;
	}



	public void setTus(List<QuanTuItem> tus) {
		this.tus = tus;
	}



	public int getTucnt() {
		return tucnt;
	}



	public void setTucnt(int tucnt) {
		this.tucnt = tucnt;
	}



	public void setQuanzan(List<QuanZanItem> quanzan) {
		this.quanzan = quanzan;
	}



	public int getQuanzancnt() {
		return quanzancnt;
	}



	public void setQuanzancnt(int quanzancnt) {
		this.quanzancnt = quanzancnt;
	}



	public String getZaned() {
		return zaned;
	}



	public void setZaned(String zaned) {
		this.zaned = zaned;
	}

	public int getQuanpingcnt() {
		return quanpingcnt;
	}



	public void setQuanpingcnt(int quanpingcnt) {
		this.quanpingcnt = quanpingcnt;
	}



	public String getPinged() {
		return pinged;
	}



	public void setPinged(String pinged) {
		this.pinged = pinged;
	}

}
