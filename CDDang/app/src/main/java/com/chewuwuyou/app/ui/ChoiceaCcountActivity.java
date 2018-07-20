package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ChoiceWithdrawalsAdapter;
import com.chewuwuyou.app.bean.ChoiceWithdrawal;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:选择账户
 * @author:liuchun
 */
public class ChoiceaCcountActivity extends CDDBaseActivity implements
		OnClickListener {

	private ListView mChoiceaCcountList;
	private ChoiceWithdrawalsAdapter mAdapter;
	private List<ChoiceWithdrawal> mAccountList;// 账户集合
	private TextView mTitel;// 标题
	private ImageButton mSubHeaderBarLeftIbtn;// 返回上一页
	private ChoiceWithdrawal mChoiceWithdrawal;
	private TextView mAddAccountTV;// 添加账户
	private RelativeLayout mTitleHeight;//标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_account);
		initView();// 初始化控件
		initData();
		initEvent();
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mChoiceaCcountList = (ListView) findViewById(R.id.choicea_ccount_lv);
		mTitel = (TextView) findViewById(R.id.sub_header_bar_tv);
		mTitel.setText("选择账户");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mAddAccountTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mAddAccountTV.setVisibility(View.VISIBLE);// 添加支付宝账户
		mAddAccountTV.setText("添加");
	}

	/**
	 * 点击事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;
		case R.id.sub_header_bar_right_tv:
			Intent intent = new Intent(ChoiceaCcountActivity.this,
					AddCcountWithdrawalsActivity.class);
			startActivity(intent);

			break;
		default:
			break;
		}

	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mAccountList = new ArrayList<ChoiceWithdrawal>();
		mAdapter = new ChoiceWithdrawalsAdapter(ChoiceaCcountActivity.this,
				mAccountList);
		mChoiceaCcountList.setAdapter(mAdapter);

	}

	/**
	 * 点击事件
	 */
	@Override
	protected void initEvent() {
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mAddAccountTV.setOnClickListener(this);
		mChoiceaCcountList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				for (int i = 0; i < mAccountList.size(); i++) {
					if (position == i) {
						mAccountList.get(i).setRadio(true);
					} else {
						mAccountList.get(i).setRadio(false);
					}

				}
				mAdapter.notifyDataSetChanged();// 刷新适配器
				Intent intent = new Intent();
				intent.putExtra("name", mAccountList.get(position)
						.getAccountName());
				intent.putExtra("phone", mAccountList.get(position)
						.getAccountPhone());
				setResult(RESULT_OK, intent);
				finishActivity();
			}

		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mAccountList.clear();
					try {
						JSONArray jArray = new JSONArray(msg.obj.toString());
						if (jArray.length() == 0) {
							ToastUtil.toastShow(ChoiceaCcountActivity.this,
									"暂无账户信息");
							return;
						}
						for (int i = 0; i < jArray.length(); i++) {
							if (getIntent().getStringExtra("accountNo").equals(
									jArray.getJSONObject(i).getString("no"))) {
								mChoiceWithdrawal = new ChoiceWithdrawal(
										jArray.getJSONObject(i).getString(
												"name"),
										jArray.getJSONObject(i).getString("no"),
										true);
							} else {
								mChoiceWithdrawal = new ChoiceWithdrawal(
										jArray.getJSONObject(i).getString(
												"name"),
										jArray.getJSONObject(i).getString("no"),
										false);
							}
							mAccountList.add(mChoiceWithdrawal);
						}
						mAdapter.notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				case Constant.NET_DATA_FAIL:

					break;

				default:
					break;
				}
			}
		}, null, NetworkUtil.GET_ALL_WITHDRAWALS, false, 0);
	}

}
