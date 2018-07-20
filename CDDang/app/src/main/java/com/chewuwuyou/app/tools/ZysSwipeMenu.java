package com.chewuwuyou.app.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * Created by zengys on 2016/5/6.
 */
public class ZysSwipeMenu {
	private Context mContext;
	private List<ZysSwipeMenuItem> mItems;
	private int mViewType;

	public ZysSwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<ZysSwipeMenuItem>();
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(ZysSwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(ZysSwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<ZysSwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public ZysSwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}
}
