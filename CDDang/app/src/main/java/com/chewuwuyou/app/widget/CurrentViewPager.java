package com.chewuwuyou.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CurrentViewPager extends ViewPager {
	private boolean mNotScroll = false;

	public CurrentViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CurrentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setmNotScroll(boolean notScroll) {
		this.mNotScroll = notScroll;
	}
	
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		// TODO Auto-generated method stub
		if (mNotScroll) {
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

}
