package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ViolationDate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnCode;
	private String count;
	private List<Violation> item;
	private String returnMessage;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<Violation> getItem() {
		return item;
	}

	public void setItem(List<Violation> item) {
		this.item = item;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public static ViolationDate parse(String userJson) {
		ViolationDate u = null;
		if (userJson != null) {
			Gson g = new Gson();
			u = g.fromJson(userJson, ViolationDate.class);
		}
		return u;
	}

	public static List<ViolationDate> parseList(String userJson) {
		List<ViolationDate> csts = null;
		if (userJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(userJson, new TypeToken<List<ViolationDate>>() {
			}.getType());
		}
		return csts;
	}

}
