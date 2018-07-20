package com.chewuwuyou.app.bean;

import java.io.Serializable;

import com.google.gson.Gson;

public class DataError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMessage;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static DataError parse(String json) {
		DataError e = null;
		if (json != null) {
			Gson g = new Gson();
			e = g.fromJson(json, DataError.class);
		}
		return e;
	}
}
