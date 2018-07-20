package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;

public class TieDetailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TieDetailHeaderEntity header;
	private List<TiePingItem> tieping;// 评论list
	
	public TieDetailHeaderEntity getHeader() {
		return header;
	}

	public void setHeader(TieDetailHeaderEntity header) {
		this.header = header;
	}
	
	public List<TiePingItem> getTieping() {
		return tieping;
	}

	public void setTieping(List<TiePingItem> tieping) {
		this.tieping = tieping;
	}

	public static TieDetailEntity parse(String tieJson) {
		TieDetailEntity detail = null;
		if (tieJson != null) {
			Gson g = new Gson();
			detail = g.fromJson(tieJson, TieDetailEntity.class);
		}
		return detail;
	}

}
