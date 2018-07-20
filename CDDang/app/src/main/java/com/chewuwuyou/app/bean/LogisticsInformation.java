package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * @describe:物流信息
 * @author:liuchun
 */
public class LogisticsInformation implements Serializable {

	private String EBusinessID;
	private String ShipperCode;
	private String Success;
	private String LogisticCode;
	private String State;
	private List<TracesEntity> Traces;

	public String getEBusinessID() {
		return EBusinessID;
	}

	public void setEBusinessID(String EBusinessID) {
		this.EBusinessID = EBusinessID;
	}

	public String getShipperCode() {
		return ShipperCode;
	}

	public void setShipperCode(String shipperCode) {
		ShipperCode = shipperCode;
	}

	public String getSuccess() {
		return Success;
	}

	public void setSuccess(String success) {
		Success = success;
	}

	public String getLogisticCode() {
		return LogisticCode;
	}

	public void setLogisticCode(String logisticCode) {
		LogisticCode = logisticCode;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public List<TracesEntity> getTraces() {
		return Traces;
	}

	public void setTraces(List<TracesEntity> traces) {
		Traces = traces;
	}

	public static LogisticsInformation parse(String pingJson) {
		LogisticsInformation ping = null;
		if (pingJson != null) {
			Gson g = new Gson();
			ping = g.fromJson(pingJson, LogisticsInformation.class);
		}
		return ping;
	}

	public static List<LogisticsInformation> parseList(String json) {
		Gson g = new Gson();
		List<LogisticsInformation> list = g.fromJson(json,
				new TypeToken<List<LogisticsInformation>>() {
				}.getType());
		return list;
	}
}
