package com.chewuwuyou.app.utils;


/**
 * @describe:关于服务的工具类
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-7上午10:56:57
 */
public class ServiceUtils {

	public static String getProjectName(String projectName) {

		if (projectName.equals("201")) {
			return "车辆年检";
		} else if (projectName.equals("202")) {
			return "车辆过户";
		} else if (projectName.equals("203")) {
			return "车辆上牌";
		} else if (projectName.equals("204")) {
			return "提档转籍";
		} else if (projectName.equals("205")) {
			return "补办登记证";
		} else if (projectName.equals("206")) {
			return "补办行驶证";
		} else if (projectName.equals("207")) {
			return "补办牌照";
		} else if (projectName.equals("208")) {
			return "代开委托书";
		} else if (projectName.equals("209")) {
			return "代领环保标志";
		} else if (projectName.equals("210")) {
			return "重打车架号";
		} else if (projectName.equals("301")) {
			return "驾证年审";
		} else if (projectName.equals("302")) {
			return "驾证注销";
		} else if (projectName.equals("303")) {
			return "补办驾证";
		} else if (projectName.equals("304")) {
			return "到期换证";
		} else if (projectName.equals("305")) {
			return "异地转入";
		} else if (projectName.equals("306")) {
			return "驾证违章";
		} else if (projectName.equals("307")) {
			return "上岗证代办";
		} else if (projectName.equals("213")) {
			return "进京证";
		} else if (projectName.equals("101")) {
			return "车辆违章代缴";
		} else if (projectName.equals("102")) {
			return "驾证违章代缴";
		} else if (projectName.equals("103")) {
			return "高速违章";
		} else if (projectName.equals("104")) {
			return "城管违章";
		} else {
			return "上门服务";
		}

	}

}
