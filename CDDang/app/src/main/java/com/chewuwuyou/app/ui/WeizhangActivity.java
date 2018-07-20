package com.chewuwuyou.app.ui;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Vehicle;
import com.chewuwuyou.app.fragment.BaoxianNotifyFragment;
import com.chewuwuyou.app.fragment.NianjianNotifyFragment;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.viewpagerindicator.TabPageIndicator;

public class WeizhangActivity extends BaseFragmentActivity implements
		View.OnClickListener {
	// header bar
	private ImageButton mBackBtn;
	private TextView mHeaderTV;
	private ImageButton mHeaderBarRightIBtn;
	// 页标
	private TabPageIndicator mTabPageIndicator;
	// 页卡
	private ViewPager mPager;
	private FragmentStatePagerAdapter mAdapter;
	private Button mEditVehicleBtn;
	private Button mDeleteVehicleBtn;
	private PopupWindow mSettingPopWindow = null;
	private Vehicle mVehicle;
	private View mSettingPopView;
	private static final String[] CONTENT = new String[] { "违章信息", "年检提醒",
			"保险提醒" };
	public static final int RESULT_FROM_CHOOSE_CITY = 2;
	public static final int RESULT_FROM_EDIT_VEHICLE = 1;
	private RelativeLayout mTitleHeight;// 标题布局高度

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weizhang_ac);
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mVehicle = (Vehicle) getIntent().getSerializableExtra(
				Constant.VEHICLE_SER);
		mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(this);

		mHeaderBarRightIBtn = (ImageButton) findViewById(R.id.sub_header_bar_right_ibtn);
		mHeaderBarRightIBtn.setVisibility(View.VISIBLE);
		mHeaderBarRightIBtn.setImageResource(R.drawable.editcar);
		mHeaderBarRightIBtn.setOnClickListener(this);
		mHeaderTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mHeaderTV.setText(!TextUtils.isEmpty(mVehicle.getNoteName()) ? mVehicle
				.getNoteName() : mVehicle.getPlateNumber());

		mPager = (ViewPager) findViewById(R.id.weizhang_pager);
		mTabPageIndicator = (TabPageIndicator) findViewById(R.id.weizhang_pager_tab);
		mAdapter = new GroupAdapter(WeizhangActivity.this);
		mPager.setAdapter(mAdapter);
		mTabPageIndicator.setViewPager(mPager);

		this.mSettingPopView = getLayoutInflater().inflate(
				R.layout.weizhang_pop_window, null);
		this.mEditVehicleBtn = (Button) mSettingPopView
				.findViewById(R.id.item_edit_vehicle_btn);
		this.mDeleteVehicleBtn = (Button) mSettingPopView
				.findViewById(R.id.item_delete_vehicle_btn);

		this.mSettingPopWindow = new PopupWindow(mSettingPopView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.mSettingPopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mSettingPopWindow.setFocusable(false);
		this.mSettingPopWindow.setOutsideTouchable(true);
		this.mEditVehicleBtn.setOnClickListener(this);
		this.mDeleteVehicleBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		// 退出
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.sub_header_bar_right_ibtn:
			// 选择地区
			mSettingPopWindow.showAsDropDown(mHeaderBarRightIBtn, 5, 5);
			break;

		case R.id.item_edit_vehicle_btn:
			intent = new Intent(WeizhangActivity.this,
					UpdateVehicleInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constant.VEHICLE_SER, mVehicle);
			intent.putExtras(bundle);
			startActivityForResult(intent, RESULT_FROM_EDIT_VEHICLE);
			mSettingPopWindow.dismiss();
			break;
		case R.id.item_delete_vehicle_btn:
			StatService.onEvent(WeizhangActivity.this, "clickDeleteVehicleBtn",
					"点击车辆管理删除按钮");
			new AlertDialog.Builder(WeizhangActivity.this)
					.setTitle("删除车辆")
					.setMessage("确定删除" + mVehicle.getPlateNumber() + "车辆吗？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int arg1) {
									dialog.dismiss();
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										final DialogInterface dialog, int arg1) {
									AjaxParams params2 = new AjaxParams();
									params2.put("id", mVehicle.getId());
									requestNet(
											new Handler() {
												@Override
												public void handleMessage(
														Message msg) {
													// method stub
													super.handleMessage(msg);
													switch (msg.what) {
													case Constant.NET_DATA_SUCCESS:
														dialog.dismiss();
														finishActivity();
														break;
													case Constant.NET_DATA_FAIL:
														dialog.dismiss();
														ToastUtil
																.toastShow(
																		WeizhangActivity.this,
																		((DataError) msg.obj)
																				.getErrorMessage());
														break;
													default:
														dialog.dismiss();
														break;
													}
												}
											}, params2,
											NetworkUtil.DELETE_VEHICLE_URL,
											false, 0);
								}
							}).show();

			mSettingPopWindow.dismiss();

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RESULT_FROM_EDIT_VEHICLE:
			if (resultCode == RESULT_OK) {
				if (data.getSerializableExtra(Constant.VEHICLE_SER) != null) {
					mVehicle = (Vehicle) data
							.getSerializableExtra(Constant.VEHICLE_SER);
				}
			}
			break;

		default:
			break;
		}
	}

	class GroupAdapter extends FragmentStatePagerAdapter {

		public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

		public GroupAdapter(FragmentActivity activity) {
			super(activity.getSupportFragmentManager());
			addTab(WeiZhangFragment
					.newInstance(WeizhangActivity.this, mVehicle));
			addTab(NianjianNotifyFragment.newInstance(WeizhangActivity.this,
					mVehicle));
			addTab(BaoxianNotifyFragment.newInstance(WeizhangActivity.this,
					mVehicle));

		}

		public ArrayList<Fragment> getmFragments() {
			return mFragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragments.get(arg0);
		}

		public void addTab(Fragment fragment) {
			mFragments.add(fragment);
			notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length];
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}
}
