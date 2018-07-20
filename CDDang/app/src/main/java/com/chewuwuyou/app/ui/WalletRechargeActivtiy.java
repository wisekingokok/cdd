package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.WalletRechanrgeMoneyAdapter;
import com.chewuwuyou.app.bean.WalletRechargeMoney;
import com.chewuwuyou.app.tools.EditInputFilterThousand;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:钱包充值
 * @author:liuchun
 */
public class WalletRechargeActivtiy extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTextTV, mSubHeaderBarRightTV, mMoneyPrompt;// 标题,充值说明,金额提示
	private ImageButton mSubHeaderLeft;// 左边返回
	private EditText mRechargeMoney;// 金额
	private GridView mRechargeGrid;// 选择金额
	private List<WalletRechargeMoney> mWalletRechang;// 集合
	private WalletRechanrgeMoneyAdapter mWallet;// 适配器
	private Button mBalanceRecharge;
	public static WalletRechargeActivtiy mRechargeActivtiy;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (TextUtils.isEmpty(msg.obj.toString())) {
					mMoneyPrompt.setText("每笔充值金额100元起");
					mMoneyPrompt.setTextColor(getResources().getColor(
							R.color.maoneny));
				} else if (Float.parseFloat(msg.obj.toString()) >= 100) {
					mMoneyPrompt.setText("每笔充值金额100元起");
					mMoneyPrompt.setTextColor(getResources().getColor(
							R.color.maoneny));
				} else if (Float.parseFloat(msg.obj.toString()) < 100) {
					mMoneyPrompt.setText("金额不足100元，无法充值");
					mMoneyPrompt.setTextColor(getResources().getColor(
							R.color.red));
				} else if (Float.parseFloat(msg.obj.toString()) >= 50000) {
					mMoneyPrompt.setText("提现金额不能大于50000");
					mMoneyPrompt.setTextColor(getResources().getColor(
							R.color.red));
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallet_recharge);
		mRechargeActivtiy = this;
		initView();
		initData();
		initEvent();

	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTextTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mMoneyPrompt = (TextView) findViewById(R.id.money_prompt);
		mSubHeaderLeft = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mSubHeaderBarRightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mRechargeMoney = (EditText) findViewById(R.id.recharge_money);
		mRechargeGrid = (GridView) findViewById(R.id.recharge_grid);
		mBalanceRecharge = (Button) findViewById(R.id.btn_balance_recharge);

		mWalletRechang = new ArrayList<WalletRechargeMoney>();
		mWalletRechang.add(new WalletRechargeMoney("100", false));
		mWalletRechang.add(new WalletRechargeMoney("200", false));
		mWalletRechang.add(new WalletRechargeMoney("300", false));
		mWalletRechang.add(new WalletRechargeMoney("500", false));
		mWalletRechang.add(new WalletRechargeMoney("1000", false));
		mWallet = new WalletRechanrgeMoneyAdapter(WalletRechargeActivtiy.this,
				mWalletRechang);
		mRechargeGrid.setAdapter(mWallet);
	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		mTextTV.setText("钱包充值");

		mSubHeaderBarRightTV.setText("充值说明");
		mSubHeaderBarRightTV.setVisibility(View.VISIBLE);

		InputFilter[] filters = { new EditInputFilterThousand(50000, 2),new InputFilter.LengthFilter(8) };
		mRechargeMoney.setFilters(filters);
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mSubHeaderLeft.setOnClickListener(this);
		mSubHeaderBarRightTV.setOnClickListener(this);
		mBalanceRecharge.setOnClickListener(this);

		/**
		 * 
		 * 监听输入框输入的情况
		 */
		mRechargeMoney.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				if(s.toString().equals(".")){
					
				}else{
					Message message = new Message();
					message.obj = mRechargeMoney.getText().toString();
					message.what = 1;
					handler.sendMessage(message);
					for (int i = 0; i < mWalletRechang.size(); i++) {
						mWalletRechang.get(i).setSelected(false);
					}
					mWallet.notifyDataSetChanged();
				}
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mRechargeGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				double dValue = Double.valueOf(mWalletRechang.get(position)
						.getMoney());
				String strValue = String.format("%.2f", dValue);
				mRechargeMoney.setText(strValue);
				for (int i = 0; i < mWalletRechang.size(); i++) {
					if (position == i) {
						mWalletRechang.get(position).setSelected(true);
					} else {
						mWalletRechang.get(i).setSelected(false);
					}
				}

				mWallet.notifyDataSetChanged();
			}

		});
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;
		case R.id.sub_header_bar_right_tv:// 充值说明
			intent = new Intent(this, RechargeExplainActivity.class);
			intent.putExtra("type", 0);// 充值说明
			startActivity(intent);
			break;
		case R.id.btn_balance_recharge:// 充值说明
			if (TextUtils.isEmpty(mRechargeMoney.getText().toString())) {
				ToastUtil.toastShow(this, "充值金额不能为空");
			} else if (Float.parseFloat(mRechargeMoney.getText().toString()) < 100) {
				ToastUtil.toastShow(this, "充值金额不能小于100");
			} else if (Float.parseFloat(mRechargeMoney.getText().toString()) > 50000) {
				ToastUtil.toastShow(this, "充值金额不能大于50000");
			} else {
				intent = new Intent(WalletRechargeActivtiy.this,
						ChoicePresentMannerActivity.class);
				intent.putExtra("money", mRechargeMoney.getText().toString());
				// TODO 充值测试
				// intent.putExtra("money", "1");
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

}
