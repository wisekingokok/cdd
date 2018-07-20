package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ExamineBook;

/**
 * 商家身份认证
 *
 * @author yuyong
 *
 */
public class IDVerificationActivity extends CDDBaseActivity implements
		OnClickListener {
	private TextView mTitleTV;
	private ImageButton mBackIBtn;

	private RadioGroup mChooseIDVerRG;// 选择认证方式的容器
	private RadioButton mPersonRBtn;// 个人,企业
	private Button mNextBtn;// 下一步提交资料
	private int mVerMent = 0;// 默认认证方式为0 个人认证
	public static IDVerificationActivity idVerificationActivity;
	private RelativeLayout mTitleHeight;// 标题布局高度
	private TextView mXieyiTV;// 点击查看服务协议
	private ExamineBook response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.id_verification_ac);
		idVerificationActivity = this;
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mChooseIDVerRG = (RadioGroup) findViewById(R.id.choose_id_verification_rg);
		mPersonRBtn = (RadioButton) findViewById(R.id.person_rb);
		mNextBtn = (Button) findViewById(R.id.next_btn);
		mXieyiTV = (TextView) findViewById(R.id.xieyi_tv);
		if (getIntent().getSerializableExtra("response") != null) {
			response = (ExamineBook) getIntent().getSerializableExtra("response");
		}
	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mTitleTV.setText("成为商家");
	}

	@Override
	protected void initEvent() {
		mXieyiTV.setOnClickListener(this);
		mBackIBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
		mChooseIDVerRG
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup rg, int checkId) {
						if (checkId == mPersonRBtn.getId()) {
							mVerMent = 0;
						} else {
							mVerMent = 1;
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.sub_header_bar_left_ibtn:
				finishActivity();
				break;
			case R.id.next_btn:// 进入下一步提交资料
				if (mVerMent == 0) {
					intent = new Intent(IDVerificationActivity.this,IDInfoActivity.class);
					intent.putExtra("identityType", mVerMent);
					Bundle bundle = new Bundle();
					bundle.putSerializable("response", response);
					intent.putExtras(bundle);
					startActivity(intent);

				} else {
					intent = new Intent(IDVerificationActivity.this,EnterpriseCheckActivity.class);
					intent.putExtra("identityType", mVerMent);
					Bundle bundle = new Bundle();
					bundle.putSerializable("response", response);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				break;

			case R.id.xieyi_tv:
				intent = new Intent(IDVerificationActivity.this,
						AgreementActivity.class);
				intent.putExtra("type", 1);
				startActivity(intent);
				break;
			default:
				break;
		}
	}
}
