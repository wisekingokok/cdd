package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:用户评价详情
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-1下午3:08:55
 */
public class Comment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "id": 8,
	// "content": "xiatest",
	// "star": 5,
	// "publishTime": "2014-11-29 15:31:52.0",
	// "url":"upload/xx.png",//头像
	// "nickName":"xxx"//昵称
	private int id;
	private String content;
	private int star;
	private String publishTime;
	private String url;
	private String nickName;
	private String hao;
	private String zhong;
	private String cha;
	
	
	public String getHao() {
		return hao;
	}

	public void setHao(String hao) {
		this.hao = hao;
	}

	public String getZhong() {
		return zhong;
	}

	public void setZhong(String zhong) {
		this.zhong = zhong;
	}

	public String getCha() {
		return cha;
	}

	public void setCha(String cha) {
		this.cha = cha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
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

	public static Comment parse(String commentJson) {
		Comment comment = null;
		if (commentJson != null) {
			Gson g = new Gson();
			comment = g.fromJson(commentJson, Comment.class);
		}
		return comment;
	}

	public static List<Comment> parseList(String commentJson) {
		List<Comment> comments = null;
		if (commentJson != null) {
			Gson g = new Gson();
			comments = g.fromJson(commentJson, new TypeToken<List<Comment>>() {
			}.getType());
		}
		return comments;
	}
}
