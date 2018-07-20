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
import com.chewuwuyou.app.ui.InsureComputeAcitivity;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.MyLog;

@SuppressLint("ValidFragment")
public class BaoxianNotifyFragment extends BaseFragment implements
		View.OnClickListener {

	private TextView mBaoxianNotifyDaysTV;
	private Button mBaoxianCountBtn;
	private Activity mActivity;
	private View mContentView;
	private Vehicle mVehicle;
	private int mDays = 0;

	public BaoxianNotifyFragment() {
		super();
	}

	public static BaoxianNotifyFragment newInstance(Activity activity,
			Vehicle vehicle) {
		BaoxianNotifyFragment fragment = new BaoxianNotifyFragment(activity,
				vehicle);
		return fragment;
	}

	public BaoxianNotifyFragment(Activity activity, Vehicle vehicle) {
		this.mActivity = activity;
		this.mVehicle = vehicle;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
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
				R.layout.bao_xian_notify_ac, null);

		initView();
		initData();
		initEvent();
		return mContentView;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mBaoxianNotifyDaysTV = (TextView) mContentView
				.findViewById(R.id.baoxian_notify_days_tv);
		mBaoxianCountBtn = (Button) mContentView
				.findViewById(R.id.baoxian_count_btn);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		// 余勇，你要计算出mDays的值呀

		MyLog.i("yang", mVehicle.getInsuranceTime());
		if (!TextUtils.isEmpty(mVehicle.getInsuranceTime())) {
			mBaoxianNotifyDaysTV.setText(displayPing());
		} else {
			mBaoxianNotifyDaysTV.setText("0天");
		}
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBaoxianCountBtn.setOnClickListener(this);
	}

	private SpannableString displayPing() {
		SpannableString ssb = null;

		mDays = DateTimeUtil.getDateDays(mVehicle.getInsuranceTime(),
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.baoxian_count_btn:
			// 进入计算
			Intent intent = new Intent(mActivity, InsureComputeAcitivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}
}
