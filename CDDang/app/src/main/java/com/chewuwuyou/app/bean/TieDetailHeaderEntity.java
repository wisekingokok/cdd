package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

public class TieDetailHeaderEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 2
	private int userId;// 5,
	private String url;// 圈动态发起人的头像地址 "/upload/20150206144153349.png",
	private String nickName;// ""
	private String sex;// 帖子发起人的性别
	private String noteName;// "\u8001\u599e",
	private String ziji;// 是不是自己发的，1是自己发的，0不是自己发的
	private String content;// "s"
	private String publishTime;// 评论时间
	private List<TieTuItem> tus;// 多张图就是小图
	private int tucnt;// 图数量
	private List<ZanItem> tiezan;// 赞列表
	private int tiezancnt;// 帖详情点赞人数
	private String zaned;// 当前用户是否已经赞过,1标识赞过，0表示没赞过
	private int tiepingcnt;// 帖详情点评人数
	private String pinged;// 当前用户是否已经评论过
	
	//xiao kang add
	private String isPro; //是否是规则贴
	private String reuse; //帖子类型 如果是0是普通的，是1是速度基情

	public List<ZanItem> getTiezan() {
		return tiezan;
	}

	public void setTiezan(List<ZanItem> tiezan) {
		this.tiezan = tiezan;
	}

	public int getTiezancnt() {
		return tiezancnt;
	}

	public void setTiezancnt(int tiezancnt) {
		this.tiezancnt = tiezancnt;
	}

	public String getZaned() {
		return zaned;
	}

	public void setZaned(String zaned) {
		this.zaned = zaned;
	}

	public int getTiepingcnt() {
		return tiepingcnt;
	}

	public void setTiepingcnt(int tiepingcnt) {
		this.tiepingcnt = tiepingcnt;
	}

	public String getPinged() {
		return pinged;
	}

	public void setPinged(String pinged) {
		this.pinged = pinged;
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

	public List<TieTuItem> getTus() {
		return tus;
	}

	public void setTus(List<TieTuItem> tus) {
		this.tus = tus;
	}

	public int getTucnt() {
		return tucnt;
	}

	public void setTucnt(int tucnt) {
		this.tucnt = tucnt;
	}

	public String getIsPro() {
		return isPro;
	}

	public void setIsPro(String isPro) {
		this.isPro = isPro;
	}

	public String getReuse() {
		return reuse;
	}

	public void setReuse(String reuse) {
		this.reuse = reuse;
	}
	
	

}
