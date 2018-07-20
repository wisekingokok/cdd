package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @describe:邮箱绑定
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-8上午11:39:42
 */
public class EmailBindActivity extends BaseActivity {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	@ViewInject(id = R.id.email_address)
	private EditText mEmail;// 绑定邮箱
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_bind_ac);
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mHeaderTV.setText(R.string.email_bind_title);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});
	}

	public void onAction(View v) {

		String email = mEmail.getText().toString();

		// 判断邮箱地址是否为空
		if (TextUtils.isEmpty(email)) {
			ToastUtil.showToast(EmailBindActivity.this,
					R.string.email_is_not_null);
		} else if (!email.matches(RegularUtil.verifyEmail))// 验证邮箱
		{
			ToastUtil.showToast(EmailBindActivity.this,
					R.string.email_geshi_error);
		} else {
			// 发送Email
			isEmailBind(email);

		}

	}

	/**
	 * 判断邮箱是否被占用 {"result":1,"data":{"isUse":true}} //邮箱已经被占用
	 * {"result":1,"data":{"isUse":false}} //邮箱未被占用
	 * 
	 * @param email
	 */
	private void isEmailBind(String email) {
		AjaxParams params = new AjaxParams();
		params.put("email", email);
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						if (jo.getBoolean("isUse") == true) {
							ToastUtil.showToast(EmailBindActivity.this,
									R.string.email_early_use);
						} else {
							sendEmail();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				}
			}

		}, params, NetworkUtil.EMAIL_IS_BIND, false, 0);
	}

	/**
	 * 发送Email
	 */
	private void sendEmail() {

		String email = mEmail.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("email", email);
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					final Dialog dialog = new Dialog(EmailBindActivity.this,
							R.style.myDialogTheme);
					LayoutInflater inflater = LayoutInflater.from(EmailBindActivity.this);
					final RelativeLayout layout = (RelativeLayout) inflater
							.inflate(R.layout.bind_email_dialog, null);
					layout.setAlpha(100);
					dialog.setContentView(layout);
					dialog.show();
					// 确定
					Button btnok = (Button) layout.findViewById(R.id.btnok);
					btnok.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							StatService.onEvent(EmailBindActivity.this,
									"clickEmailBindOkBtn", "点击邮箱绑定确认按钮");
							// 进入邮箱
							ToastUtil.showToast(EmailBindActivity.this,
									R.string.email_send_success);
							dialog.dismiss();
							setResult(RESULT_OK, null);
							finishActivity();
						}
					});

					break;

				default:
					break;
				}
			}

		}, params, NetworkUtil.EMAIL_BIND_URL, false, 0);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(EmailBindActivity.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(EmailBindActivity.this);
	}
}
