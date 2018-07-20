package com.chewuwuyou.app.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.WeiZhangDetailsAdapter;
import com.chewuwuyou.app.bean.Vehicle;
import com.chewuwuyou.app.bean.WeizhangGroupItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;

@SuppressLint("ValidFragment")
public class WeiZhangFragment extends BaseFragment implements
		View.OnClickListener {

	private TextView mWeizhangGroupCityTV;
	private TextView mWeizhangGroupInfoTV;
	private TextView mWeizhangEmptyTV;

	private ListView mAnimatedExpandableListView;

	// private List<WeizhangGroupItem> mData = new
	// ArrayList<WeizhangGroupItem>();
	private WeiZhangDetailsAdapter mAdapter;
	public static final int RESULT_FROM_CHOOSE_CITY = 2;
	public static final int RESULT_FROM_EDIT_VEHICLE = 1;
	private Vehicle mVehicle;

	private Button mHandleBtn;// 处理违章
	private Activity mActivity;
	private View mContentView;

	public WeiZhangFragment() {
		super();
	}

	public static WeiZhangFragment newInstance(Activity activity,
			Vehicle vehicle) {
		WeiZhangFragment fragment = new WeiZhangFragment(activity, vehicle);
		return fragment;
	}

	public WeiZhangFragment(Activity activity, Vehicle vehicle) {
		MyLog.d("xuhan",
				"--------------------------------------TieFragment(Activity activity,  String type, String region)");
		this.mActivity = activity;
		this.mVehicle = vehicle;
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.d("xuhan", "--------------------------------------onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onCreateView");
		this.mContentView = LayoutInflater.from(mActivity).inflate(
				R.layout.wei_zhang_info_layout, null);

		initView();
		initData();
		initEvent();
		return mContentView;
	}

	@Override
	protected void initView() {
		mWeizhangGroupCityTV = (TextView) mContentView
				.findViewById(R.id.weizhang_group_city_tv);
		mWeizhangGroupInfoTV = (TextView) mContentView
				.findViewById(R.id.weizhang_group_info_tv);
		mAnimatedExpandableListView = (ListView) mContentView
				.findViewById(R.id.weizhang_list);
		mWeizhangEmptyTV = (TextView) mContentView
				.findViewById(R.id.weizhang_empty_tv);
		// mWeizhangGroupRefreshIBtn = (ImageButton)
		// findViewById(R.id.weizhang_group_refresh_ibtn);
		mHandleBtn = (Button) mContentView.findViewById(R.id.handle_btn);

	}

	@Override
	protected void initData() {
		if (mVehicle.getWeiZhangGroups() == null) {
			mVehicle.setWeiZhangGroups(new ArrayList<WeizhangGroupItem>());
		}
		if (mVehicle.getWeiZhangGroups().size() > 0) {
			if (mVehicle.getWeiZhangGroups().get(0).getHistorys().size() < 1) {
				mWeizhangEmptyTV.setVisibility(View.VISIBLE);
			}

		}
		if (mVehicle.getWeiZhangGroups() != null
				|| mVehicle.getWeiZhangGroups().size() == 0) {
			WeizhangGroupItem weizhangGroupItem = new WeizhangGroupItem();
			weizhangGroupItem.setCount(0);
			weizhangGroupItem.setStatus(0);
			weizhangGroupItem.setTotal_money(0.0);
			weizhangGroupItem.setTotal_score(0);
			// WeizhangChildItem weizhangChildItem=new WeizhangChildItem();
			// weizhangGroupItem.getHistorys().add(weizhangChildItem);
			mVehicle.getWeiZhangGroups().add(weizhangGroupItem);

		}
		WeizhangGroupItem item = mVehicle.getWeiZhangGroups().get(0);
		mWeizhangGroupCityTV.setText(mVehicle.getVehicleAdd());
		String content = new StringBuilder().append("共")
				.append(item.getCount()).append("条违章,").append("罚款")
				.append(item.getTotal_money()).append("元,").append("扣分")
				.append(item.getTotal_score()).append("分").toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(content);
		ssb.setSpan(new AbsoluteSizeSpan(18, true), 1,
				String.valueOf(item.getCount()).length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mWeizhangGroupInfoTV.setText(ssb.toString());

		if (mAdapter == null) {

			mAdapter = new WeiZhangDetailsAdapter(mActivity,
					mVehicle.getWeiZhangGroups());

			// mAnimatedExpandableListView.setOnGroupClickListener(new
			// OnGroupClickListener() {
			//
			// @Override
			// public boolean onGroupClick(ExpandableListView parent, View
			// v, int groupPosition, long id) {
			// // We call collapseGroupWithAnimation(int) and
			// // expandGroupWithAnimation(int) to animate group
			// // expansion/collapse.
			// if
			// (mAnimatedExpandableListView.isGroupExpanded(groupPosition))
			// {
			// mAnimatedExpandableListView.collapseGroupWithAnimation(groupPosition);
			// } else {
			// mAnimatedExpandableListView.expandGroupWithAnimation(groupPosition);
			// }
			// return true;
			// }
			//
			// });
		}
		mAnimatedExpandableListView.setAdapter(mAdapter);
		/* mAdapter.notifyDataSetChanged(); */
		mAnimatedExpandableListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(mActivity,
								WeizhangItemDetailsActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("weizhangChildItem",
								mVehicle.getWeiZhangGroups().get(0)
										.getHistorys().get(arg2));
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

	}

	@Override
	protected void initEvent() {
		mHandleBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.handle_btn:
			// List<WeizhangChildItem> mWeizChildItems = new
			// ArrayList<WeizhangChildItem>();
			// for (int i = 0; i < mAdapter.mChecked.size(); i++) {
			// if (mAdapter.mChecked.get(i)) {
			// mWeizChildItems.add(mVehicle.getWeiZhangGroups().get(0)
			// .getHistorys().get(i));
			// }
			// }
			// if (mTag == 1) {
			// Intent handlerWZIntent = new Intent(mActivity,
			// VehicleServiceActivity.class);
			// Bundle childItemsbundle = new Bundle();
			// childItemsbundle.putSerializable("weizhangChildItems",
			// (Serializable) mWeizChildItems);
			// childItemsbundle
			// .putSerializable(Constant.VEHICLE_SER, mVehicle);
			// handlerWZIntent.putExtras(childItemsbundle);
			// handlerWZIntent.putExtra("handlerWZ", 1);
			// handlerWZIntent.putExtra("serviceType",
			// Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
			// startActivity(handlerWZIntent);
			//
			// } else {
			// Intent chooseIllIntent = new Intent();
			// Bundle childItemsbundle = new Bundle();
			// childItemsbundle.putSerializable("weizhangChildItems",
			// (Serializable) mWeizChildItems);
			// chooseIllIntent.putExtras(childItemsbundle);
			// mActivity.setResult(Constant.CHOOSE_ILLEGAL_SERVICE,
			// chooseIllIntent);
			// }
			Intent intent = new Intent(getActivity(), ServiceTypeActivity.class);
			intent.putExtra("serviceType",
					Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
			startActivity(intent);
			mActivity.finish();

			break;
		default:
			break;
		}
	}

	// private void getIllegal(int city_id, String city_name) {
	// CarInfo car = new CarInfo();
	// car.setChepai_no(mVehicle.getPlateNumber());
	// // car.setChepai_no("川AW2W94");
	//
	// // car.setChejia_no("152930");
	// car.setChejia_no(mVehicle.getFrameNumber());
	// car.setEngine_no(mVehicle.getEngineNumber());
	// // car.setEngine_no("214064514");
	// car.setRegister_no("");
	// // car.setCity_id(659);
	// car.setCity_id(city_id);
	// WeizhangResponseJson weizhangInfo = WeizhangClient.getWeizhang(car);
	// WeizhangGroupItem item = WeizhangGroupItem.parse(weizhangInfo.toJson());
	// item.setCity_name(city_name);
	// mVehicle.getWeiZhangGroups().add(item);
	// if (mAdapter == null) {
	// mAdapter = new WeiZhangDetailsAdapter(mActivity,
	// mVehicle.getWeiZhangGroups());
	//
	// // mAnimatedExpandableListView.setOnGroupClickListener(new
	// // OnGroupClickListener() {
	// //
	// // @Override
	// // public boolean onGroupClick(ExpandableListView parent, View v,
	// // int groupPosition, long id) {
	// // // We call collapseGroupWithAnimation(int) and
	// // // expandGroupWithAnimation(int) to animate group
	// // // expansion/collapse.
	// // if (mAnimatedExpandableListView.isGroupExpanded(groupPosition)) {
	// // mAnimatedExpandableListView.collapseGroupWithAnimation(groupPosition);
	// // } else {
	// // mAnimatedExpandableListView.expandGroupWithAnimation(groupPosition);
	// // }
	// // return true;
	// // }
	// //
	// // });
	// }
	// mAnimatedExpandableListView.setAdapter(mAdapter);
	// mAdapter.notifyDataSetChanged();
	// }

}
