package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;

public class YueDetailEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private YueDetailHeaderEntity header;
	private List<YuePingItem> yueping;// 评论list
	public YueDetailHeaderEntity getHeader() {
		return header;
	}
	public void setHeader(YueDetailHeaderEntity header) {
		this.header = header;
	}
	public List<YuePingItem> getYueping() {
		return yueping;
	}
	public void setYueping(List<YuePingItem> yueping) {
		this.yueping = yueping;
	}
	public static YueDetailEntity parse(String tieJson) {
		YueDetailEntity detail = null;
		if (tieJson != null) {
			Gson g = new Gson();
			detail = g.fromJson(tieJson, YueDetailEntity.class);
		}
		return detail;
	}
}
