package com.chewuwuyou.app.alipay;

import java.net.URLEncoder;

import android.util.Log;

import com.chewuwuyou.app.utils.NetworkUtil;

public class AlipayUtil {
	private static final String TAG = "AlipayUtil";

	@SuppressWarnings("deprecation")
	private static String getNewOrderInfo(String tradeNo, String subject,
			String body, String price) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(tradeNo);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(NetworkUtil.ALIPAY_NOTIFY_URL));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private static String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@SuppressWarnings("deprecation")
	public static String getOrderInfo(String tradeNo, String subject,
			String body, String price) {
		String info = getNewOrderInfo(tradeNo, subject, body, price);
		String sign = Rsa.sign(info, Keys.PRIVATE);
		sign = URLEncoder.encode(sign);
		info += "&sign=\"" + sign + "\"&" + getSignType();
		Log.i(TAG, "start pay");
		// start the pay.
		Log.i(TAG, "info = " + info);
		return info;
	}

}
