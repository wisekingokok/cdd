package com.chewuwuyou.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * @describe:商家中心
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-10上午10:24:35
 */
public class BusinessCenterActivity extends BaseActivity {

	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	@ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
	private TextView mHeaderRightTV;// 添加车辆
	private RelativeLayout mTitleHeight;// 标题布局高度
	/**
	 * 商家服务地区
	 */
	@ViewInject(id = R.id.business_service_location)
	private TextView mBusinessServiceLocation;
	@ViewInject(id = R.id.business_ratingbar)
	RatingBar business_ratingbar;
	@ViewInject(id = R.id.address)
	TextView address;

	/**
	 * 商家名片头像
	 */
	// @ViewInject(id = R.id.business_head_img, click = "onAction")
	@ViewInject(id = R.id.business_head_img)
	private ImageView mBusinessHeadImg;
	/**
	 * 商家名称
	 */
	@ViewInject(id = R.id.business_name)
	private TextView mBusinessName;
	@ViewInject(id = R.id.business_score)
	TextView business_score;
	/**
	 * 商家电话
	 */
	@ViewInject(id = R.id.business_phone)
	private EditText mBusinessPhone;
	@ViewInject(id = R.id.type)
	TextView type;
	@ViewInject(id = R.id.img_top)
	ImageView img_top;
	/**
	 * 上传的图像drawable
	 */
	// private Drawable d;
	/**
	 * 上传商家图片地址
	 */
	// private String businessHeadImg;
	/**
	 * 商家信息实体
	 */
	// private MyBusinessInfo myBusinessInfo;
	// /**
	// * 判断商家是否过期
	// */
	// @ViewInject(id = R.id.is_gq)
	// private TextView isGuoq;
	// @ViewInject(id = R.id.service_times_tv)
	// private TextView mServiceTimesTV;// 服务剩余次数
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:

				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					mBusinessName.setText(jsonObject.getString("realName"));// 名称
					mBusinessPhone.setText(jsonObject.getString("mobile"));// 电话
					type.setText(jsonObject.getString("type"));
					business_ratingbar.setRating(Integer.parseInt(jsonObject.getString("star")));
					business_score.setText(Float.parseFloat(jsonObject.getString("star")) + "分");
					address.setText(jsonObject.getString("address"));// 地址
					StringBuffer buffer = new StringBuffer();
					String provice = jsonObject.getString("province");
					if (provice.indexOf("市") <= 0)
						buffer.append(provice).append(jsonObject.getString("city"))
								.append(jsonObject.getString("district"));
					else
						buffer.append(jsonObject.getString("city")).append(jsonObject.getString("district"));
					mBusinessServiceLocation.setText(buffer.toString());
					String url;
					if (jsonObject.get("headImageUrl") instanceof String) {
						url = jsonObject.getString("headImageUrl");
					} else {
						url = jsonObject.getJSONObject("headImageUrl").getString("url");
					}
					ImageUtils.displayImage(url, mBusinessHeadImg, 10, R.drawable.user_fang_icon,
							R.drawable.user_fang_icon);

					if (jsonObject.getJSONArray("images").length() > 0) {
						ImageUtils.displayImage(jsonObject.getJSONArray("images").getString(0), img_top, 10,
								R.drawable.cdd_default_icon, R.drawable.cdd_default_icon);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case Constant.NET_DATA_FAIL:
				ToastUtil.toastShow(BusinessCenterActivity.this, ((DataError) msg.obj).getErrorMessage());
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_center_ac);
		initView();
	}

	private void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mHeaderTV.setText(R.string.business_center_title);
		requestNet(mHandler, null, NetworkUtil.QUERY_ME_BUSINESS, false, 0);
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

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(BusinessCenterActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(BusinessCenterActivity.this);
	}

}
