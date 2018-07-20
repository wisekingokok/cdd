package com.chewuwuyou.app.widget;

import com.chewuwuyou.app.bean.LogisticsCompany;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<LogisticsCompany> {

	public int compare(LogisticsCompany o1, LogisticsCompany o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
