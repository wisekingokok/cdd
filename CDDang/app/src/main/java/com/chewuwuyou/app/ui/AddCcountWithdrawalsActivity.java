package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.tools.EditInputBiaoQing;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:添加支付宝账户
 * @author:liuchun
 */
public class AddCcountWithdrawalsActivity extends CDDBaseActivity implements
		OnClickListener {

	private Button mAddCcount;// 确认
	private TextView mSubHeaderBarTv;// 取消
	private ImageButton mSubHeaderBarLeftIbtn;// 返回
	private EditText mAccountUserName, mAccountNo;// 用户名称，支付宝账户名称
	private RelativeLayout mTitleHeight;// 标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_ccount_withdrawals);
		initView();// 初始化控件
		initData();// 逻辑处理
		initEvent();// 点击事件
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mAccountUserName = (EditText) findViewById(R.id.account_user_name);
		mAccountNo = (EditText) findViewById(R.id.account_name);
		mSubHeaderBarTv = (TextView) findViewById(R.id.sub_header_bar_tv);
		mSubHeaderBarTv.setText("添加支付宝账户");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mAddCcount = (Button) findViewById(R.id.add_ccount);// 确认
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_ccount:// 确认
			String accountName = mAccountUserName.getText().toString().trim();
			String accountNo = mAccountNo.getText().toString().trim();
			if (TextUtils.isEmpty(accountName) || TextUtils.isEmpty(accountNo)) {
				ToastUtil.toastShow(AddCcountWithdrawalsActivity.this,
						"请输入完整的账户信息");
			} else if (!accountNo.matches(RegularUtil.verifyEmail)
					&& !accountNo.matches(RegularUtil.verifyTelephone)) {
				ToastUtil.toastShow(AddCcountWithdrawalsActivity.this,
						"请输入正确的支付宝账户");
			} else {
				AjaxParams params = new AjaxParams();
				params.put("account", accountName);
				params.put("accountNo", accountNo);
				addAccount(params);
			}
			break;
		case R.id.sub_header_bar_left_ibtn:// 取消
			finishActivity();
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
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断

		/* 过滤表情 */

		InputFilter[] filters = { new EditInputBiaoQing(),
				new InputFilter.LengthFilter(10) };
		mAccountUserName.setFilters(filters);

	}

	/**
	 * 点击事件
	 */
	@Override
	protected void initEvent() {
		mAddCcount.setOnClickListener(this);// 确认
		mSubHeaderBarLeftIbtn.setOnClickListener(this);// 取消
	}

	/**
	 * 添加账户
	 */
	public void addAccount(AjaxParams params) {
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						
						if (jo.getString("re").equals("添加账户成功")) {
							if(getIntent().getIntExtra("Identification", 0)==Constant.CLICK_BALANCE_TIXIAN){
								Intent intent = new Intent(AddCcountWithdrawalsActivity.this,BalanceWithdrawalsActivity.class);
								intent.putExtra("amount",CacheTools.getUserData("walletMoney"));
								startActivity(intent);
							}else if(getIntent().getIntExtra("Identification", 0)==Constant.CLICK_TIXIAN_ACCOUNT){
								Intent intent = new Intent(AddCcountWithdrawalsActivity.this,CccountWithdrawalsActivity.class);
								intent.putExtra("amount",CacheTools.getUserData("walletMoney"));
								startActivity(intent);
							}
							finishActivity();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(AddCcountWithdrawalsActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;

				}
			}
		}, params, NetworkUtil.Add_WITHDRAWALS, false, 0);
	}
	
	//支付密码输入错误,你的账户已被冻结

}
