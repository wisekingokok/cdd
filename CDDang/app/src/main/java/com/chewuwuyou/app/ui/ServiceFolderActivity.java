package com.chewuwuyou.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:服务所需资料
 * @author:yuyong
 * @date:2015-8-9下午6:40:12
 * @version:1.2.1
 */
public class ServiceFolderActivity extends CDDBaseActivity {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	// private LinearLayout mServiceCLFWLL;// 显示车辆服务的资料
	// private LinearLayout mServiceJZFWLL;// 显示驾证服务资料
	// private LinearLayout mServiceWZFWLL;// 显示违章服务资料
	private String[] mGroupStrs;// 分组集合
	private String[] mChildStrs;// 子项集合
	private RelativeLayout mTitleHeight;//标题布局高度
	private ExpandableListView mExpandableListView;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_folder_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
		mExpandableListView.setGroupIndicator(null);  
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// getExpandableListView().setCacheColorHint(0); // 设置拖动列表的时候防止出现黑色背景
		// mServiceCLFWLL = (LinearLayout)
		// findViewById(R.id.service_clfw_zl_ll);
		// mServiceJZFWLL = (LinearLayout)
		// findViewById(R.id.service_jzfw_zl_ll);
		// mServiceWZFWLL = (LinearLayout)
		// findViewById(R.id.service_wzfw_zl_ll);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		int serviceType = getIntent().getExtras().getInt("serviceType");
		mTitleTV.setText("服务须知");
		if (serviceType == 1) {
			mGroupStrs = getResources().getStringArray(
					R.array.illegal_service_group_arr);
			mChildStrs = getResources().getStringArray(
					R.array.illegal_service_child_arr);
			// mServiceWZFWLL.setVisibility(View.VISIBLE);
		} else if (serviceType == 2) {
			mGroupStrs = getResources().getStringArray(
					R.array.vehicle_service_group_arr);
			mChildStrs = getResources().getStringArray(
					R.array.vehicle_service_child_arr);
			// mServiceCLFWLL.setVisibility(View.VISIBLE);
		} else {
			mGroupStrs = getResources().getStringArray(
					R.array.license_service_group_arr);
			mChildStrs = getResources().getStringArray(
					R.array.license_service_child_arr);
			// mServiceJZFWLL.setVisibility(View.VISIBLE);

		}
		mExpandableListView.setAdapter(new ContactsInfoAdapter());

	}

	class ContactsInfoAdapter extends BaseExpandableListAdapter {

		// -----------------Child----------------//
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return mChildStrs[groupPosition];
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChoildHolder childHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.service_folder_child_view, null);
				childHolder = new ChoildHolder();
				childHolder.childTV = (TextView) convertView
						.findViewById(R.id.child_tv);
				convertView.setTag(childHolder);
			} else {
				childHolder = (ChoildHolder) convertView.getTag();
			}
			childHolder.childTV.setText(mChildStrs[groupPosition]);

			return convertView;
		}

		// ----------------Group----------------//
		@Override
		public Object getGroup(int groupPosition) {
			return mGroupStrs[groupPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			return mGroupStrs.length;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder groupHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.service_folder_group_view, null);
				groupHolder = new GroupHolder();
				groupHolder.groupTV = (TextView) convertView
						.findViewById(R.id.group_tv);
				groupHolder.isTurnOffIV = (ImageView) convertView
						.findViewById(R.id.is_turn_off_iv);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}
			groupHolder.groupTV.setText(mGroupStrs[groupPosition]);
			if (isExpanded) {
				groupHolder.isTurnOffIV
						.setBackgroundResource(R.drawable.icon_arrow1);
			} else {
				groupHolder.isTurnOffIV
						.setBackgroundResource(R.drawable.icon_arrow0);
			}

			return convertView;
		}

//		// 创建组/子视图
//		public TextView getGenericView(String s) {
//			// Layout parameters for the ExpandableListView
//			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//					ViewGroup.LayoutParams.FILL_PARENT, 40);
//
//			TextView text = new TextView(ServiceFolderActivity.this);
//			text.setLayoutParams(lp);
//			// Center the text vertically
//			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//			// Set the text starting position
//			text.setPadding(36, 0, 0, 0);
//
//			text.setText(s);
//			return text;
//		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		class GroupHolder {
			TextView groupTV;
			ImageView isTurnOffIV;
		}

		class ChoildHolder {
			TextView childTV;
		}

	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});
	}

}
