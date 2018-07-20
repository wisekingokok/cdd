package com.chewuwuyou.app.tools;

import java.util.List;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zengys on 2016/5/6.
 */
public class ZysSwipeMenuView extends LinearLayout implements
		View.OnClickListener {

	@SuppressWarnings("unused")
	private ZysSwipeMenuListView mListView;

	private ZysSwipeMenuLayout mLayout;
	private ZysSwipeMenu mMenu;
	private OnSwipeItemClickListener onItemClickListener;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ZysSwipeMenuView(ZysSwipeMenu menu, ZysSwipeMenuListView listView) {
		super(menu.getContext());
		mListView = listView;
		mMenu = menu;
		List<ZysSwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (ZysSwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	@SuppressWarnings("deprecation")
	private void addItem(ZysSwipeMenuItem item, int id) {
		LayoutParams params = new LayoutParams(item.getWidth(),
				LayoutParams.MATCH_PARENT);
		LinearLayout parent = new LinearLayout(getContext());
		parent.setId(id);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setLayoutParams(params);
		parent.setBackgroundDrawable(item.getBackground());
		parent.setOnClickListener(this);
		addView(parent);

		if (item.getIcon() != null) {
			parent.addView(createIcon(item));
		}
		if (!TextUtils.isEmpty(item.getTitle())) {
			parent.addView(createTitle(item));
		}

	}

	private ImageView createIcon(ZysSwipeMenuItem item) {
		ImageView iv = new ImageView(getContext());
		iv.setImageDrawable(item.getIcon());
		return iv;
	}

	private TextView createTitle(ZysSwipeMenuItem item) {
		TextView tv = new TextView(getContext());
		tv.setText(item.getTitle());
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(item.getTitleSize());
		tv.setTextColor(item.getTitleColor());
		return tv;
	}

	@Override
	public void onClick(View v) {
		if (onItemClickListener != null && mLayout.isOpen()) {
			onItemClickListener.onItemClick(this, mMenu, v.getId());
		}
		if (onExpandableSwipeItemClickListener != null && mLayout.isOpen()) {
			onExpandableSwipeItemClickListener.onExpandableItemClick(this,
					mMenu, v.getId());
		}
	}

	/***********
	 * 实现ExpandableListView左划功能 start
	 ***************/
	@SuppressWarnings("unused")
	private ZysSwipeMenuExpandableListView mExpandableListView;

	public ZysSwipeMenuView(ZysSwipeMenu menu,
			ZysSwipeMenuExpandableListView expandableListView) {
		super(menu.getContext());
		mExpandableListView = expandableListView;
		mMenu = menu;
		List<ZysSwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (ZysSwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	private OnExpandableSwipeItemClickListener onExpandableSwipeItemClickListener;

	public OnExpandableSwipeItemClickListener getOnExpandableSwipeItemClickListener() {
		return onExpandableSwipeItemClickListener;
	}

	public void setOnExpandableSwipeItemClickListener(
			OnExpandableSwipeItemClickListener onExpandableSwipeItemClickListener) {
		this.onExpandableSwipeItemClickListener = onExpandableSwipeItemClickListener;
	}

	public static interface OnExpandableSwipeItemClickListener {
		void onExpandableItemClick(ZysSwipeMenuView view, ZysSwipeMenu menu,
				int index);
	}

	private int groupPosition;
	private int childPosition;

	public int getGroupPostion() {
		return groupPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}

	public void setPositions(int groupPosition, int childPosition) {
		this.groupPosition = groupPosition;
		this.childPosition = childPosition;
	}

	/***************************
	 * end
	 *******************************/

	public OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	public void setOnSwipeItemClickListener(
			OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setLayout(ZysSwipeMenuLayout mLayout) {
		this.mLayout = mLayout;
	}

	public static interface OnSwipeItemClickListener {
		void onItemClick(ZysSwipeMenuView view, ZysSwipeMenu menu, int index);

	}
}
