package com.chewuwuyou.app.utils;

import java.util.HashMap;
import java.util.Map;

import android.widget.TextView;

/**
 * @describe:根据编码识别车的类型
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-10-29下午3:44:34
 */
public class VehicleTypeUtil {

	/**
	 * 根据获取的编码更新UI上车辆类型的更新
	 * 
	 * @param code
	 * @param mText
	 */
	public static void codeToType(String code, TextView mText) {
		Map<String, String> vehicleTypeMap = new HashMap<String, String>();
		vehicleTypeMap.put("02", "小型汽车");
		vehicleTypeMap.put("01", "大型汽车");
		vehicleTypeMap.put("03", "使馆汽车");
		vehicleTypeMap.put("04", "领馆汽车");
		vehicleTypeMap.put("05", "境外汽车");
		vehicleTypeMap.put("06", "外籍汽车");
		vehicleTypeMap.put("07", "两三轮摩托车");
		vehicleTypeMap.put("08", "轻便摩托车");
		vehicleTypeMap.put("09", "使馆摩托车");
		vehicleTypeMap.put("10", "领馆摩托车");
		vehicleTypeMap.put("11", "境外摩托车");
		vehicleTypeMap.put("12", "外籍摩托车");
		vehicleTypeMap.put("13", "低速车");
		vehicleTypeMap.put("14", "拖拉机");
		vehicleTypeMap.put("15", "挂车");
		vehicleTypeMap.put("16", "教练汽车");
		vehicleTypeMap.put("17", "教练摩托车");
		vehicleTypeMap.put("18", "试验汽车");
		vehicleTypeMap.put("19", "试验摩托车");
		vehicleTypeMap.put("20", "临时入境汽车");
		vehicleTypeMap.put("21", "临时入境摩托车");
		vehicleTypeMap.put("22", "临时行驶车");
		vehicleTypeMap.put("23", "警用汽车");
		vehicleTypeMap.put("24", "警用摩托");
		vehicleTypeMap.put("25", "原农机号牌");
		vehicleTypeMap.put("26", "香港入出境车");
		vehicleTypeMap.put("27", "澳门入出境车");
		vehicleTypeMap.put("31", "武警号牌");
		vehicleTypeMap.put("32", "军队号牌");
		vehicleTypeMap.put("41", "无号牌");
		vehicleTypeMap.put("42", "假号牌");
		vehicleTypeMap.put("43", "挪用号牌");
		vehicleTypeMap.put("99", "其他号牌");
		mText.setText(vehicleTypeMap.get(code));
	}

	public static String vehicleTypeToCode(String vehicleType) {
		Map<String, String> vehicleTypeMap = new HashMap<String, String>();
		vehicleTypeMap.put("小型汽车", "02");
		vehicleTypeMap.put("大型汽车", "01");
		vehicleTypeMap.put("外籍汽车", "06");
		vehicleTypeMap.put("两三轮摩托车", "07");
		vehicleTypeMap.put("轻便摩托车", "08");
		vehicleTypeMap.put("挂车", "15");
		vehicleTypeMap.put("教练车", "16");
		// vehicleTypeMap.put("使馆汽车", "03");
		// vehicleTypeMap.put("领馆汽车", "04");
		// vehicleTypeMap.put("境外汽车", "05");
		// vehicleTypeMap.put("使馆摩托车", "09");
		// vehicleTypeMap.put("领馆摩托车", "10");
		// vehicleTypeMap.put("境外摩托车", "11");
		// vehicleTypeMap.put("外籍摩托车", "12");
		// vehicleTypeMap.put("低速车", "13");
		// vehicleTypeMap.put("拖拉机", "14");
		// vehicleTypeMap.put("教练摩托车", "17");
		// vehicleTypeMap.put("试验汽车", "18");
		// vehicleTypeMap.put("试验摩托车", "19");
		// vehicleTypeMap.put("临时入境汽车", "20");
		// vehicleTypeMap.put("临时入境摩托车", "21");
		// vehicleTypeMap.put("临时行驶车", "22");
		// vehicleTypeMap.put("警用汽车", "23");
		// vehicleTypeMap.put("警用摩托", "24");
		// vehicleTypeMap.put("原农机号牌", "25");
		// vehicleTypeMap.put("香港入出境车", "26");
		// vehicleTypeMap.put("澳门入出境车", "27");
		// vehicleTypeMap.put("武警号牌", "31");
		// vehicleTypeMap.put("军队号牌", "32");
		// vehicleTypeMap.put("无号牌", "41");
		// vehicleTypeMap.put("假号牌", "42");
		// vehicleTypeMap.put("挪用号牌", "43");
		// vehicleTypeMap.put("其他号牌", "99");
		return vehicleTypeMap.get(vehicleType.trim());
	}
}
