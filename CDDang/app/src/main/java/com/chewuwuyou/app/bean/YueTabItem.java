package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:板块实体
 * @author:XH
 * @version 
 * @created:
 */
public class YueTabItem implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;//类型
    private int iconResId;
	private String type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}

	public static List<BanKuaiItem> parseList(String json) {
        Gson g = new Gson();
        List<BanKuaiItem> list = g.fromJson(json,
                new TypeToken<List<BanKuaiItem>>() {
                }.getType());
        return list;
    }
}
