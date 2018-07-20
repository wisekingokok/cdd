package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:帖子的实体
 * @author:XH
 * @version
 * @created:
 */

public class TieItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 圈动态id
	private int userId;//用户Id
	private String url;// 圈动态发起人的头像地址
	private String nickName;// 圈动态发起人的昵称
	private String noteName;// 圈动态发起人的备注名
	private String userName;//设定叮当号用户名
	private int sex;// 圈动态发起人的性别
    private String title;// 标题
	private String content;// 圈动态内容
	private String publishTime;// 圈动态发布时间
	private int tiezancnt;// 圈动态点赞人数
	private int tiepingcnt;// 圈动态点评人数
	private String ziji;// 是不是自己发的，1是自己发的，0不是自己发的
	private String zaned;// 当前用户是否已经赞过,1标识赞过，0表示没赞过
	private String pinged;// 当前用户是否已经评论过
	private int tucnt;// 图数量
	private List<TieTuItem> tus;// 多张图就是小图
	
	//xiao kang add
	private String isPro; //是否是规则贴
	private String reuse; //帖子类型 如果是0是普通的，是1是速度基情
	private String location;//地址

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

//	public String getSex() {
//		return sex;
//	}
//
//	public void setSex(String sex) {
//		this.sex = sex;
//	}

	public String getContent() {
		return content;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
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

	public int getTiezancnt() {
		return tiezancnt;
	}

	public void setTiezancnt(int tiezancnt) {
		this.tiezancnt = tiezancnt;
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

	public int getTiepingcnt() {
		return tiepingcnt;
	}

	public void setTiepingcnt(int tiepingcnt) {
		this.tiepingcnt = tiepingcnt;
	}

	public String getZiji() {
		return ziji;
	}

	public void setZiji(String ziji) {
		this.ziji = ziji;
	}

	public String getZaned() {
		return zaned;
	}

	public void setZaned(String zaned) {
		this.zaned = zaned;
	}

	public String getPinged() {
		return pinged;
	}

	public void setPinged(String pinged) {
		this.pinged = pinged;
	}

	public int getTucnt() {
		return tucnt;
	}

	public void setTucnt(int tucnt) {
		this.tucnt = tucnt;
	}

	public static List<TieItem> parseList(String json) {
		Gson g = new Gson();
		List<TieItem> list = g.fromJson(json, new TypeToken<List<TieItem>>() {
		}.getType());
		return list;
	}

	public static TieItem parse(String tieJson) {
		TieItem item = null;
		if (tieJson != null) {
			Gson g = new Gson();
			item = g.fromJson(tieJson, TieItem.class);
		}
		return item;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
