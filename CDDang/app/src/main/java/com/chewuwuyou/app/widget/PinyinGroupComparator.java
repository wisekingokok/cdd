package com.chewuwuyou.app.widget;

import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.bean.LogisticsCompany;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinGroupComparator implements Comparator<GroupSetUpMemberInformationEr> {

	public int compare(GroupSetUpMemberInformationEr o1, GroupSetUpMemberInformationEr o2) {
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
