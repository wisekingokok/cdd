package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class YueItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 约约编号
	private String title;// 标题
	private String content;// 描述内容
	private int chargeType;// 付费方式：0：请客，1：AA
	private String type; // 活动组织类型 -- 旅游，观影，美食等

	private int chiefId;// 主办者其他信息
	private String chief;// 主办者nick name
	private String url;// 主办者图像
	private String noteName;// 设定备注名
	private String userName;// 设定叮当号用户名
	private String ziji;// 是不是自己发的，1是自己发的，0不是自己发的
	private int sex; // 限制参与性别类型 -- 0表示男，1表示女，9表示不限

	private String avaliableDate;// 展示到期时间 -- 到这个时间之后失效
	private String publishTime;// 评论时间

	private int involve;// 当前人数
	private String chiefsex;// 性别,"0"表示男，"1"表示女 ,"9"不详

	private List<YueTuItem> tus;// 多张图就是小图
	private int tucnt;// 图数量

	private String zaned;// 当前用户是否已经赞过,1标识赞过，0表示没赞过
	// private String pinged;// 当前用户是否已经评论过 小康又墨不要了

	private int yuezancnt;// 活动点赞人数
	private int yuepingcnt;// 活动点评人数
	private String location;

	public static List<YueItem> parseList(String json) {
		Gson g = new Gson();
		List<YueItem> yues = g.fromJson(json, new TypeToken<List<YueItem>>() {
		}.getType());
		return yues;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getChargeType() {
		return chargeType;
	}

	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}

	public int getInvolve() {
		return involve;
	}

	public void setInvolve(int involve) {
		this.involve = involve;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAvaliableDate() {
		return avaliableDate;
	}

	public void setAvaliableDate(String avaliableDate) {
		this.avaliableDate = avaliableDate;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public int getChiefId() {
		return chiefId;
	}

	public void setChiefId(int chiefId) {
		this.chiefId = chiefId;
	}

	public String getChief() {
		return chief;
	}

	public void setChief(String chief) {
		this.chief = chief;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getZiji() {
		return ziji;
	}

	public void setZiji(String ziji) {
		this.ziji = ziji;
	}

	public List<YueTuItem> getTus() {
		return tus;
	}

	public void setTus(List<YueTuItem> tus) {
		this.tus = tus;
	}

	public int getTucnt() {
		return tucnt;
	}

	public void setTucnt(int tucnt) {
		this.tucnt = tucnt;
	}

	public String getZaned() {
		return zaned;
	}

	public void setZaned(String zaned) {
		this.zaned = zaned;
	}

	public int getYuezancnt() {
		return yuezancnt;
	}

	public void setYuezancnt(int yuezancnt) {
		this.yuezancnt = yuezancnt;
	}

	public int getYuepingcnt() {
		return yuepingcnt;
	}

	public void setYuepingcnt(int yuepingcnt) {
		this.yuepingcnt = yuepingcnt;
	}

	public String getChiefsex() {
		return chiefsex;
	}

	public void setChiefsex(String chiefsex) {
		this.chiefsex = chiefsex;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
