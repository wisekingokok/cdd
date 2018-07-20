package com.chewuwuyou.app.wxapi;

import net.sourceforge.simcpux.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.ChoiceCompleteActivity;
import com.chewuwuyou.app.ui.ChoicePresentMannerActivity;
import com.chewuwuyou.app.ui.CommonPayActivity;
import com.chewuwuyou.app.ui.WXPayActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		// 0 支付成功 -1 不成功 -2 取消支付。
		if (resp.errCode == 0) {
			if (Constant.PAY_INTO == 1) {// 支付订单
				ToastUtil.toastShow(WXPayEntryActivity.this, "支付成功");
				Intent intent = new Intent();
				intent.putExtra("wxpayResult", 1);
				setResult(RESULT_OK, intent);
				CommonPayActivity.mInstance.finishActivity();
				WXPayActivity.mActivity.finishActivity();
			} else if (Constant.PAY_INTO == 2) {// 充值订单
				Intent intent = new Intent(WXPayEntryActivity.this,
						ChoiceCompleteActivity.class);
				intent.putExtra("czResult", 1);
				intent.putExtra("payMoney", Constant.PAY_MONEY);
				startActivity(intent);
				ChoicePresentMannerActivity.mChoicePresentMannerActivity.finishActivity();
			}
			finish();

		} else if (resp.errCode == -2) {
			if (Constant.PAY_INTO == 1) {// 支付订单
				ToastUtil.toastShow(WXPayEntryActivity.this, "取消支付");
			}else if(Constant.PAY_INTO == 2){
				ChoicePresentMannerActivity.mChoicePresentMannerActivity.finishActivity();
				ToastUtil.toastShow(WXPayEntryActivity.this, "取消充值");
			}
			finish();
		} else {
			if (Constant.PAY_INTO == 1) {// 支付订单
				ToastUtil.toastShow(WXPayEntryActivity.this, "支付结果:" + resp.errStr);
			}else if(Constant.PAY_INTO == 2){
				ChoicePresentMannerActivity.mChoicePresentMannerActivity.finishActivity();
				ToastUtil.toastShow(WXPayEntryActivity.this, "支付结果:" + resp.errStr);
			}
			finish();
		}
	}

}