package com.chewuwuyou.app.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Vehicle;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.ServiceTypeActivity;
import com.chewuwuyou.app.ui.VehicleServiceActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.MyLog;

@SuppressLint("ValidFragment")
public class NianjianNotifyFragment extends BaseFragment implements
		View.OnClickListener {

	private TextView mNianjianNotifyDaysTV;
	private Button mNianjianDaibanBtn;
	private Activity mActivity;
	private View mContentView;
	private Vehicle mVehicle;
	private int mDays = 0;

	public NianjianNotifyFragment() {
		super();
	}

	public static NianjianNotifyFragment newInstance(Activity activity,
			Vehicle vehicle) {
		NianjianNotifyFragment fragment = new NianjianNotifyFragment(activity,
				vehicle);
		return fragment;
	}

	public NianjianNotifyFragment(Activity activity, Vehicle vehicle) {
		this.mActivity = activity;
		this.mVehicle = vehicle;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onCreateView");
		this.mContentView = LayoutInflater.from(mActivity).inflate(
				R.layout.nian_jian_notify_ac, null);

		initView();
		initData();
		initEvent();
		return mContentView;
	}

	@Override
	protected void initView() {
		mNianjianNotifyDaysTV = (TextView) mContentView
				.findViewById(R.id.nianjian_notify_days_tv);
		mNianjianDaibanBtn = (Button) mContentView
				.findViewById(R.id.nianjian_daiban_btn);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		// 余勇，你要计算出mDays的值呀
		if (!TextUtils.isEmpty(mVehicle.getAnnualSurveyTime())) {
			mNianjianNotifyDaysTV.setText(displayText());
		} else {
			mNianjianNotifyDaysTV.setText("未设置年检提醒时间");
		}
	}

	@Override
	protected void initEvent() {
		mNianjianDaibanBtn.setOnClickListener(this);
	}

	private SpannableString displayText() {
		SpannableString ssb = null;

		mDays = DateTimeUtil.getDateDays(mVehicle.getAnnualSurveyTime(),
				DateTimeUtil.getSystemTime());

		StringBuffer sb = new StringBuffer().append(mDays).append("天");
		int totalLength = sb.toString().length();
		int dayLength = String.valueOf(mDays).length();
		ssb = new SpannableString(sb.toString());
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#57bff0")), 0,
				dayLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
				dayLength, totalLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nianjian_daiban_btn:
			if (CacheTools.getUserData("role") != null) {
				Intent vehicleServiceIntent = new Intent(mActivity,
						ServiceTypeActivity.class);
				vehicleServiceIntent.putExtra("serviceType",
						Constant.SERVICE_TYPE.CAR_SERVICE);
				startActivity(vehicleServiceIntent);
			} else {
				Intent intent = new Intent(mActivity, LoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}

	}
}
