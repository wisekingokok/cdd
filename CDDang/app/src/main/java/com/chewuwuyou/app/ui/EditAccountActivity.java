package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.tools.EditTextFilter;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:编辑支付账户信息、包含支付宝、银联
 * @author:yuyong
 * @date:2015-10-26下午5:28:55
 * @version:1.2.1
 */
public class EditAccountActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;

	private int mFlag = 0;
	private EditText mBankAccountNumET;// 银行账户
	private EditText mBankAccountNameET;// 银行账户名
	private EditText mAlipyAccountNameET;// 支付宝帐户名
	private EditText mAlipyAccountNumET;// 支付宝账户
	private EditText mWxAccountNumET;// 微信账户
	private Button mSaveBtn;// 保存账户信息
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mFlag = getIntent().getExtras().getInt("flag");
		if (mFlag == 0) {
			setContentView(R.layout.edit_zfb_ac);
		} else if (mFlag == 1) {
			setContentView(R.layout.edit_yhk_ac);
		} else {
			setContentView(R.layout.edit_wx_ac);
		}
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mSaveBtn = (Button) findViewById(R.id.save_btn);

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if (mFlag == 0) {
			mTitleTV.setText("支付宝账户设置");
			mAlipyAccountNameET = (EditText) findViewById(R.id.zfb_account_name_et);
			mAlipyAccountNumET = (EditText) findViewById(R.id.zfb_account_number_et);
			mAlipyAccountNameET.setText(getIntent().getStringExtra(
					"zfbAccountName"));
			mAlipyAccountNumET
					.setText(getIntent().getStringExtra("zfbAccount"));

			mAlipyAccountNameET.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// String t = mAlipyAccountNameET.getText().toString();
					String editable = mAlipyAccountNameET.getText().toString();
					EditTextFilter edit = new EditTextFilter();
					String str = edit.StringFilter(editable).toString();
					if (!editable.equals(str)) {
						mAlipyAccountNameET.setText(str);
						mAlipyAccountNameET.setSelection(str.length()); // 光标置后
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

		} else if (mFlag == 1) {
			mTitleTV.setText("银联账户设置");
			mBankAccountNameET = (EditText) findViewById(R.id.bank_account_name_et);
			mBankAccountNumET = (EditText) findViewById(R.id.bank_account_number_et);
			bankCardNumAddSpace(mBankAccountNumET);
			mBankAccountNameET.setText(getIntent().getStringExtra(
					"bankAccountName"));
			mBankAccountNumET
					.setText(getIntent().getStringExtra("bankAccount"));

			mBankAccountNameET.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// String t = mBankAccountNameET.getText().toString();
					String banktable = mBankAccountNameET.getText().toString();
					EditTextFilter fiter = new EditTextFilter();
					String str = fiter.StringFilter(banktable.toString());
					if (!banktable.equals(str)) {
						mBankAccountNameET.setText(str);
						mBankAccountNameET.setSelection(str.length()); // 光标置后
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

		} else {
			mTitleTV.setText("微信账户设置");
			mWxAccountNumET = (EditText) findViewById(R.id.wx_account_et);
			mWxAccountNumET.setText(getIntent().getStringExtra("wxAccount"));
		}

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackIBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
	}

	/**
	 * 银行卡四位加空格
	 * 
	 * @param mEditText
	 */
	protected void bankCardNumAddSpace(final EditText mEditText) {
		mBankAccountNumET.setTextSize(20);
		mEditText.addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;

			int location = 0;// 记录光标的位置
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == ' ') {
						konggeNumberB++;
					}
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3
						|| isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isChanged) {
					location = mEditText.getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == ' ') {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}

					index = 0;
					int konggeNumberC = 0;
					while (index < buffer.length()) {
						if ((index == 4 || index == 9 || index == 14 || index == 19)) {
							buffer.insert(index, ' ');
							konggeNumberC++;
						}
						index++;
					}

					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}

					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}

					mEditText.setText(str);
					Editable etable = mEditText.getText();
					Selection.setSelection(etable, location);
					isChanged = false;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.save_btn:
			AjaxParams params = new AjaxParams();
			if (mFlag == 0) {
				String zfbAccountName = mAlipyAccountNameET.getText()
						.toString();
				String zfbAccount = mAlipyAccountNumET.getText().toString();

				if (mAlipyAccountNumET.getText().toString().trim().equals("")) {
					ToastUtil.toastShow(EditAccountActivity.this, "请输入支付宝帐户名");
				} else if (mAlipyAccountNameET.getText().toString().trim()
						.equals("")) {
					ToastUtil.toastShow(EditAccountActivity.this, "请输入支付宝帐户");
				} else if (!mAlipyAccountNameET.getText().toString().trim()
						.equals("")
						&& !mAlipyAccountNumET.getText().toString().trim()
								.equals("")) {
					params.put("zfbName", zfbAccountName);
					params.put("zfb", zfbAccount);
					params(params);
				}
			} else if (mFlag == 1) {
				String upAccountName = mBankAccountNameET.getText().toString();
				String upAccount = mBankAccountNumET.getText().toString()
						.trim();
				if (TextUtils.isEmpty(upAccountName)) {
					ToastUtil.toastShow(EditAccountActivity.this, "请输入账户名");
				} else if (TextUtils.isEmpty(upAccount)) {
					ToastUtil.toastShow(EditAccountActivity.this, "请输入银行卡账号");
				} else {
					params.put("up", upAccount);
					params.put("upName", upAccountName);
					params(params);
				}

			} else {
				String wxAccount = mWxAccountNumET.getText().toString();
				if (TextUtils.isEmpty(wxAccount)) {
					ToastUtil.toastShow(EditAccountActivity.this, "请输入微信账号");
				} else {
					params.put("wxzf", wxAccount);
					params(params);
				}
			}
			break;
		default:
			break;
		}
	}

	public void params(AjaxParams params) {
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					ToastUtil.toastShow(EditAccountActivity.this, "设置账户成功");
					finishActivity();
					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.SETTING_PAY_ACCOUNT, false, 0);
	}
}
