package com.chewuwuyou.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.barcode.view.SuperAwesomeCardFragment;
/**
 * @describe:板块Adapter
 * @author:liuchun
 * @version
 * @created:
 */
public class OrderFragmentAdapter extends FragmentPagerAdapter {

	private String[] TITLES;
	private Fragment[] fragments;
	
	
	public OrderFragmentAdapter(FragmentManager fm,String[] TITLES,Fragment[] fragments) {
		super(fm);
		this.TITLES = TITLES;
		this.fragments = fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	@Override
	public int getCount() {
		return TITLES.length;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return fragments[0];
		case 1:
			return fragments[1];
		case 2:
			return fragments[2];
		case 3:
			return fragments[3];
		case 4:
			return fragments[4];
		case 5:
			return fragments[5];
		default:
			return SuperAwesomeCardFragment.newInstance(0);
		}
	}
}
