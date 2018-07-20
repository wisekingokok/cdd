package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:圈动态的实体
 * @author:XH
 * @version 
 * @created:
 */

public class QuanItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//圈动态id
	private String url;//圈动态发起人的头像地址
	private String nickName;//圈动态发起人的昵称
	private String noteName;//圈动态发起人的备注名
	private String userName;//设定叮当号用户名
	private String sex;//圈动态发起人的性别
	private int userId;//圈动态发起人的Id
	private String content;//圈动态内容
    private String publishTime;//圈动态发布时间
    private int quanzancnt;//圈动态点赞人数
    private int quanpingcnt;//圈动态点评人数
    private String ziji;//是不是自己发的，1是自己发的，0不是自己发的
	private String zaned;//当前用户是否已经赞过,1标识赞过，0表示没赞过
	private String pinged;//当前用户是否已经评论过
	private int tucnt;//图数量
	private List<QuanTuItem> tus;//多张图就是小图
    private List<QuanPingItem> quanping;//评论list
	private List<QuanZanItem> quanzan;//赞列表

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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

	public List<QuanPingItem> getQuanping() {
		return quanping;
	}
	public List<QuanTuItem> getTus() {
		return tus;
	}
	public void setTus(List<QuanTuItem> tus) {
		this.tus = tus;
	}
	public void setQuanping(List<QuanPingItem> quanping) {
		this.quanping = quanping;
	}
	public int getQuanzancnt() {
		return quanzancnt;
	}
	public void setQuanzancnt(int quanzancnt) {
		this.quanzancnt = quanzancnt;
	}
	public List<QuanZanItem> getQuanzan() {
		return quanzan;
	}
	public void setQuanzan(List<QuanZanItem> quanzan) {
		this.quanzan = quanzan;
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
	public int getQuanpingcnt() {
		return quanpingcnt;
	}
	public void setQuanpingcnt(int quanpingcnt) {
		this.quanpingcnt = quanpingcnt;
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
	public static List<QuanItem> parseList(String json) {
        Gson g = new Gson();
        List<QuanItem> dynamics = g.fromJson(json,
                new TypeToken<List<QuanItem>>() {
                }.getType());
        return dynamics;
    }
	
    public static QuanItem parse(String quanJson) {
    	QuanItem QuanItem = null;
        if (quanJson != null) {
            Gson g = new Gson();
            QuanItem = g.fromJson(quanJson, QuanItem.class);
        }
        return QuanItem;
    }
}
