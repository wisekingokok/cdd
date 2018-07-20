package com.chewuwuyou.app.ui;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.MyVoucherAdapter;
import com.chewuwuyou.app.bean.Voucher;
import com.chewuwuyou.app.utils.NetworkUtil;

/**
 * @describe:我的抵用券
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-26上午11:11:31
 */
public class MyVoucherActivity extends BaseActivity {

	private List<Voucher> mVouchers;// 抵用券使用详情集合

	@ViewInject(id = R.id.voucher_use_detail_list)
	private ListView mVoucherUseList;// 抵用券使用详情
	@ViewInject(id = R.id.my_voucher_sum_tv)
	private TextView mVoucherSumTV;// 抵用券余额
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_voucher_ac);
		initView();
	}

	@SuppressWarnings("static-access")
	private void initView() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mVoucherSumTV.setText("￥ " + getIntent().getStringExtra("voucher"));
		((TextView) findViewById(R.id.sub_header_bar_tv)).setText("我的抵用券");
		findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finishActivity();
					}
				});
		new NetworkUtil().postMulti(NetworkUtil.GET_VOUCHER_LIST, null,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						dismissWaitingDialog();
						try {
							JSONObject jo = new JSONObject(t.toString());
							if (jo.getInt("result") == 1) {
								mVouchers = Voucher.parseList(jo
										.getString("data"));
								if (mVouchers != null) {
									MyVoucherAdapter adapter = new MyVoucherAdapter(
											MyVoucherActivity.this, mVouchers);
									mVoucherUseList.setAdapter(adapter);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						dismissWaitingDialog();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						showWaitingDialog();
					}
				});
	}

}
