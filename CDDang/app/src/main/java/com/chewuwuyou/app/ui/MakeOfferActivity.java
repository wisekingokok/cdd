package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;
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
import com.chewuwuyou.app.tools.EditInputFilter;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @author root
 *
 */
/**
 * @author root
 *
 */
public class MakeOfferActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;

	private TextView mProjectNameTV;
	private EditText mProjectPriceET;
	private Button mMakeOfferBtn;// 报价
	private Button mCancelOfferBtn;// 取消报价
	private RelativeLayout mTitleHeight;//标题布局高度
	private String mPostUrl;// 报价和修改报价

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_offer_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mProjectNameTV = (TextView) findViewById(R.id.project_name_tv);
		mProjectPriceET = (EditText) findViewById(R.id.project_price_et);
		mMakeOfferBtn = (Button) findViewById(R.id.make_offer_btn);
		mCancelOfferBtn = (Button) findViewById(R.id.cancel_offer_btn);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		if (getIntent().getIntExtra("isUpdatePrice", 0) == 1) {// 如果是修改报价
			mTitleTV.setText("修改报价");
			String myOffer = getIntent().getStringExtra("myOffer");
			mProjectPriceET.setText(myOffer);
			mPostUrl = NetworkUtil.UPDATE_OFFER;
			mProjectPriceET.requestFocus();
			mProjectPriceET.setSelection(myOffer.length());
		} else {
			mTitleTV.setText("我要报价");
			mPostUrl = NetworkUtil.MAKE_OFFER;
		}
		mProjectNameTV.setText(ServiceUtils.getProjectName(getIntent()
				.getStringExtra("projectName")));

	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mMakeOfferBtn.setOnClickListener(this);
		mCancelOfferBtn.setOnClickListener(this);

		InputFilter[] filters = { new EditInputFilter(5000),new InputFilter.LengthFilter(7)/* 这里限制输入的长度为7个字母 */ };
		mProjectPriceET.setFilters(filters);

		// Tools.inputPrice(mProjectPriceET);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.make_offer_btn:
			String price = mProjectPriceET.getText().toString();

			AjaxParams params = new AjaxParams();
			if (getIntent().getIntExtra("isUpdatePrice", 0) == 1) {
				params.put("taskOfferId",
						getIntent().getStringExtra("myOfferId"));
			} else {
				params.put("taskId", getIntent().getStringExtra("taskId"));
			}
			// params.put("comment", ""); //报价描述，暂时不用
			if (TextUtils.isEmpty(price) || Double.valueOf(price) <= 0) {
				ToastUtil.toastShow(MakeOfferActivity.this, "请输入正确的报价");
			} else {
				params.put("price", price);
				requestNet(new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						switch (msg.what) {
						case Constant.NET_DATA_SUCCESS:
							if (getIntent().getIntExtra("isUpdatePrice", 0) == 1) {
								ToastUtil.toastShow(MakeOfferActivity.this,
										"修改报价成功");
							} else {
								ToastUtil.toastShow(MakeOfferActivity.this,
										"报价成功，请刷新订单");
							}

							finishActivity();
							break;
							case Constant.NET_DATA_FAIL:
								ToastUtil.toastShow(MakeOfferActivity.this,
										((DataError) msg.obj).getErrorMessage());
								break;
						default:
							if (getIntent().getIntExtra("isUpdatePrice", 0) == 1) {
								ToastUtil.toastShow(MakeOfferActivity.this,
										"修改报价失败");
							} else {
								ToastUtil.toastShow(MakeOfferActivity.this,
										"报价失败");
							}

							break;
						}
					}

				}, params, mPostUrl, false, 0);
			}

			break;
		case R.id.cancel_offer_btn:
			finishActivity();
			break;

		default:
			break;
		}
	}

}
