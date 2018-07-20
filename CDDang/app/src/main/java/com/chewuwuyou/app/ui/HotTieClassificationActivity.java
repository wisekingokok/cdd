package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BanKuaiItem;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener;
import com.chewuwuyou.app.widget.PullToRefreshListView;

public class HotTieClassificationActivity extends BaseActivity implements OnRefreshListener<ListView> {

	// header bar
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackIBtn;// 左键,返回按钮
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;// 标题

	private List<BanKuaiItem> mData;// 数据
	private HotTieClassificationAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;

	private boolean mIsRefreshing = false;// 翻页要用
	private boolean mIsSetEmptyTV = false;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hot_tie_classification_ac);
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.classification_list);

		mHeaderTV.setText("汽车与生活");
		mData = new ArrayList<BanKuaiItem>();
		mAdapter = new HotTieClassificationAdapter(HotTieClassificationActivity.this, mData);
		mPullToRefreshListView.setAdapter(mAdapter);

		mPullToRefreshListView.setOnRefreshListener(this);
		// mPullToRefreshListView.setOnItemClickListener(new
		// OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		//
		// }});

		getAllHotTieClassification();
	}

	@SuppressWarnings("static-access")
	private void getAllHotTieClassification() {
		// TODO Auto-generated method stub
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();
		params.put("reuse", String.valueOf(1));

		new NetworkUtil().postMulti(NetworkUtil.GET_ALL_BANKUAI, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(HotTieClassificationActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有分组");
					tv.setTextColor(getResources().getColor(R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}
				// TODO Auto-generated method stub

				super.onSuccess(t);
				mIsRefreshing = false;
				mPullToRefreshListView.onRefreshComplete();
				// dismissWaitingDialog();
				try {
					JSONObject jo = new JSONObject(t);
					if (jo.getInt("result") == 1) {
						MyLog.i("YUY", "======车辆信息===" + jo.toString());
						mData = BanKuaiItem.parseList(jo.getString("data"));
						mAdapter = new HotTieClassificationAdapter(HotTieClassificationActivity.this, mData);
						mPullToRefreshListView.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					} else if (jo.getJSONObject("data").getInt("errorCode") == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE) {
						DialogUtil.loginInOtherPhoneDialog(HotTieClassificationActivity.this);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				// dismissWaitingDialog();
				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(HotTieClassificationActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有分组");
					tv.setTextColor(getResources().getColor(R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}
				mPullToRefreshListView.onRefreshComplete();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				// showWaitingDialog();
			}

		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllHotTieClassification();
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	public void onAction(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		default:
			break;
		}
	}
}