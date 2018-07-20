package com.barcode.view;

import java.util.Comparator;

import com.chewuwuyou.app.bean.CityYI;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<CityYI> {

	public int compare(CityYI o1, CityYI o2) {
		if (o1.getCityPinYin().equals("@")
				|| o2.getCityPinYin().equals("#")) {
			return -1;
		} else if (o1.getCityPinYin().equals("#")
				|| o2.getCityPinYin().equals("@")) {
			return 1;
		} else {
			return o1.getCityPinYin().compareTo(o2.getCityPinYin());
		}
	}

}
