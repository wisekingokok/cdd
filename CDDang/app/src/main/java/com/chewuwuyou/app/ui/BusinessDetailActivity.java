package com.chewuwuyou.app.ui;

import java.text.DecimalFormat;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.bean.TrafficBusinessListBook;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * 业务详情页面
 * 
 * @author Administrator
 * 
 */
public class BusinessDetailActivity extends CDDBaseActivity implements
		OnClickListener {
	@ViewInject(id = R.id.scrollView)
	ScrollView scrollView;
	@ViewInject(id = R.id.userName)
	TextView userName;
	@ViewInject(id = R.id.address)
	TextView address;
	@ViewInject(id = R.id.tip)
	TextView tip;
	@ViewInject(id = R.id.type)
	TextView type;
	@ViewInject(id = R.id.fees)
	TextView fees;
	@ViewInject(id = R.id.serverFees)
	TextView serverFees;
	@ViewInject(id = R.id.totalFees)
	TextView totalFees;
	@ViewInject(id = R.id.material)
	TextView material;// 材料
	@ViewInject(id = R.id.evaluateSize)
	TextView evaluateSize;// 评价数量
	@ViewInject(id = R.id.business_name)
	TextView business_name;// 评价人名
	@ViewInject(id = R.id.business_score)
	TextView business_score;// 分数
	@ViewInject(id = R.id.business_context)
	TextView business_context;// 内容
	@ViewInject(id = R.id.business_time)
	TextView business_time;// 时间
	@ViewInject(id = R.id.more)
	TextView more;// 更多
	@ViewInject(id = R.id.cddServe)
	TextView cddServe;// 车当当服务条款
	@ViewInject(id = R.id.cddillegalServe)
	TextView cddillegalServe;// 违章代缴

	@ViewInject(id = R.id.picTotal)
	TextView picTotal;// 预计价格
	@ViewInject(id = R.id.sub)
	TextView sub;// 提交
	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	ImageButton mBackIBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	TextView mTitleTV;

	@ViewInject(id = R.id.feesLL)
	LinearLayout feesLL;
	@ViewInject(id = R.id.logo)
	ImageView logo;
	@ViewInject(id = R.id.checkBox)
	TextView checkBox;// 同意
	@ViewInject(id = R.id.business_comment_portrait)
	ImageView business_comment_portrait;// 头像
	@ViewInject(id = R.id.business_ratingbar)
	RatingBar business_ratingbar;// 星

	@ViewInject(id = R.id.release_option)
	LinearLayout release_option;//提示选择票据
	@ViewInject(id = R.id.release_need)
	RadioButton release_need;// 选择票据
	@ViewInject(id = R.id.release_no_need)
	RadioButton release_no_need;// 不选择票据

	private boolean isSelected = false;
	private boolean isUnchecked = false;


	private TrafficBusinessListBook broker;
	private ServicePro servicePro;
	private int mServiceType;
	private double payMoney;
	// private JSONObject mJsonObject;// 服务项目
	private int mCommentSize = 0;// 总的评论条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_detail);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		evaluateSize.setText("(0)");
		business_ratingbar.setVisibility(View.GONE);
		business_name.setVisibility(View.GONE);
		business_time.setVisibility(View.GONE);
		business_score.setVisibility(View.GONE);
		business_comment_portrait.setVisibility(View.GONE);
		business_context.setVisibility(View.GONE);
		more.setText("暂无更多评价");
		mTitleTV.setText("业务详情");
		DecimalFormat df = new DecimalFormat("#0.00");
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			return;
		broker = (TrafficBusinessListBook) bundle
				.getSerializable(Constant.TRAFFIC_SER);
		servicePro = (ServicePro) bundle.getSerializable("servicedata");
		if (broker != null) {
			userName.setText("商家名称:" + broker.getRealName());
     		address.setText("受理地区:" + broker.getLocation());
			getData(broker.getId() + "");
			fees.setText("￥  " + df.format(broker.getFees()));
			serverFees.setText("￥  " + df.format(broker.getServicePrice()));
		}

		if (servicePro != null) {
			ImageUtils.displayImage(servicePro.getProjectImg(), logo, 0);
			tip.setText(servicePro.getServiceDesc());
			type.setText(servicePro.getProjectName() == null ? "" : servicePro
					.getProjectName());
			mServiceType = servicePro.getType();
			if (mServiceType == 1) {
				feesLL.setVisibility(View.GONE);
				cddillegalServe.setVisibility(View.VISIBLE);
				payMoney = Double.valueOf(broker.getServicePrice());
			} else if (mServiceType == 2) {
				feesLL.setVisibility(View.VISIBLE);
				cddillegalServe.setVisibility(View.GONE);
				payMoney = broker.getFees() + broker.getServicePrice();// 要加上规费.（暂时没有）
			} else if (mServiceType == 3) {
				feesLL.setVisibility(View.VISIBLE);
				cddillegalServe.setVisibility(View.GONE);
				payMoney = broker.getFees() + broker.getServicePrice();// 要加上规费.（暂时没有）
			}
			totalFees.setText("￥  " + df.format(payMoney));
			picTotal.setText("￥  " + df.format(payMoney));
			try {
				JSONArray jsonArray = new JSONArray(
						servicePro.getServiceFolder());
				// 显示所需资料
				StringBuilder strFolder = new StringBuilder("");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = jsonArray.getJSONObject(i);
					strFolder.append(jo.getString("name")).append("\n");
					for (int j = 0; j < jo.getJSONArray("list").length(); j++) {
						try {
							int s = j + 1;
							strFolder.append(" " + s + ".  ")
									.append(jo.getJSONArray("list").get(j))
									.append("\n");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				material.setText(strFolder.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

		}

		// MyLog.i("YUY",
		// "xxxxxxxxxxxxxxxxxxxxxxxx"
		// + getIntent().getStringExtra("servicedata"));
		// try {
		// mJsonObject = new JSONObject(getIntent().getStringExtra(
		// "servicedata"));
		// if (mJsonObject != null) {
		// ImageUtils.displayImage(mJsonObject.getString("projectImg"),
		// logo, 360);
		// tip.setText(mJsonObject.getString("serviceDesc"));
		// type.setText(mJsonObject.getString("projectName") == null ? ""
		// : mJsonObject.getString("projectName"));
		// mServiceType = Integer.parseInt(mJsonObject.getString("type"));
		// StringBuilder str = new StringBuilder("");
		// // 显示所需资料
		// for (int i = 0; i < mJsonObject.getJSONArray("serviceFolder")
		// .length(); i++) {
		// JSONObject jo = mJsonObject.getJSONArray("serviceFolder")
		// .getJSONObject(i);
		// str.append(jo.getString("name")).append("\n");
		// for (int j = 0; j < jo.getJSONArray("list").length(); j++) {
		// try {
		// int s = j + 1;
		// str.append(" " + s + ". ")
		// .append(jo.getJSONArray("list").get(j))
		// .append("\n");
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// material.setText(str.toString());
		// }
		//
		// } catch (JSONException e1) {
		// e1.printStackTrace();
		// }

	}

	@Override
	protected void initEvent() {
		sub.setOnClickListener(this);
		more.setOnClickListener(this);
		cddServe.setOnClickListener(this);
		mBackIBtn.setOnClickListener(this);
		cddillegalServe.setOnClickListener(this);
		release_need.setOnClickListener(this);
		release_no_need.setOnClickListener(this);
		business_ratingbar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.more:// 更多评价
			if (mCommentSize >= 1) {
				intent = new Intent(this, EvaluateListActivity.class);
				intent.putExtra("businessId", broker.getId() + "");
				intent.putExtra("bId", 1);
				startActivity(intent);
			} else {
				ToastUtil.toastShow(this, "暂无评论");
			}
			break;
		case R.id.sub:// 提交
			subOrder();
			break;
		case R.id.cddServe:// 服务条款
			intent = new Intent(this, AgreementActivity.class);
			intent.putExtra("type", 2);
			startActivity(intent);
			break;
		case R.id.cddillegalServe:// 违章代缴
			intent = new Intent(this, AgreementActivity.class);
			intent.putExtra("type", 3);
			startActivity(intent);
			break;

		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.release_need://选择票据
		   isSelected = true;
			isUnchecked = false;
			release_option.setVisibility(View.GONE);
			break;
		case R.id.release_no_need://不选择票据
			isUnchecked = true;
			isSelected = false;
			release_option.setVisibility(View.GONE);
			break;

		}

	}

	/**
	 * 取得评论
	 */
	private void getData(String businessId) {
		if (businessId == null)
			return;
		AjaxParams params = new AjaxParams();
		MyLog.i("YUY", "商家idxxxxxxxxxxxxxx" + businessId);
		params.put("businessId", businessId);
		requestNet(mHandler, params, NetworkUtil.QUERY_COMMENT_BY_BUSINESS,
				false, 0);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:
				MyLog.i("YUY", "商家最近一条评价 ===============" + msg.obj.toString());
				try {
					JSONObject jo = new JSONObject(msg.obj.toString());
					mCommentSize = jo.getInt("hao") + jo.getInt("zhong")
							+ jo.getInt("cha");
					if (mCommentSize == 0) {
						evaluateSize.setText("(0)");
						business_ratingbar.setVisibility(View.GONE);
						business_name.setVisibility(View.GONE);
						business_time.setVisibility(View.GONE);
						business_score.setVisibility(View.GONE);
						business_comment_portrait.setVisibility(View.GONE);
						business_context.setVisibility(View.GONE);
						more.setText("暂无更多评价");
					} else {
						evaluateSize.setText("(" + mCommentSize + ")");
						business_context.setText(jo.getString("content"));
						if (jo.getString("url") != null
								&& !"".equals(jo.getString("url"))) {
							ImageUtils.displayImage(jo.getString("url"),
									business_comment_portrait, 360,
									R.drawable.user_yuan_icon,
									R.drawable.user_yuan_icon);
						}
						int star = jo.getInt("star");
						business_ratingbar.setRating(star);
						business_name.setText(jo.getString("nickName"));
						business_time.setText(jo.getString("publishTime").substring(0,19));
						StringBuffer str = new StringBuffer(star);
						str.append(star > 3 ? "好评" : star < 3 ? "差评" : "中评");
						business_score.setText(str.toString());
						business_ratingbar.setVisibility(View.VISIBLE);
						business_name.setVisibility(View.VISIBLE);
						business_time.setVisibility(View.VISIBLE);
						business_score.setVisibility(View.GONE);
						business_comment_portrait.setVisibility(View.VISIBLE);
						business_context.setVisibility(View.VISIBLE);
						more.setText("查看更多评价");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case Constant.NET_DATA_FAIL:
				ToastUtil.toastShow(BusinessDetailActivity.this,
						((DataError) msg.obj).getErrorMessage());
				evaluateSize.setText("(0)");
				business_ratingbar.setVisibility(View.GONE);
				business_name.setVisibility(View.GONE);
				business_time.setVisibility(View.GONE);
				business_score.setVisibility(View.GONE);
				business_comment_portrait.setVisibility(View.GONE);
				business_context.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}

	};

	private void subOrder() {
		String tipStr = tip.getText().toString();
//		if (TextUtils.isEmpty(tipStr) || broker == null || payMoney < 10
//				|| payMoney > 5000) {
//			ToastUtil.toastShow(this, "订单信息有误");
//			return;
//		}
		if (broker.getUserId().equals(CacheTools.getUserData("userId"))) {
			ToastUtil.toastShow(BusinessDetailActivity.this, "不能给自己下单");
			return;
		}
        if(isSelected!=true&&isUnchecked!=true){
			release_option.setVisibility(View.VISIBLE);
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("userDescription", tipStr);
		params.put("userBId", broker.getUserId() + "");
		params.put("spSta", String.valueOf(1));// 区分交易方式 默认为平台担保支付 1 选择商家支付为2
		params.put("paymentAmount", String.valueOf(payMoney));
		params.put("projectName", servicePro.getProjectNum() + "");
		if(isSelected==true){
			params.put("provideBill", "1");
		}else{
			params.put("provideBill", "0");
		}
		if (mServiceType == Constant.SERVICE_TYPE.CAR_SERVICE) {
			requestNet(mOrderHandler, params, NetworkUtil.VEHICLE_SERVICE_URL,
					false, 0);
		} else if (mServiceType == Constant.SERVICE_TYPE.ILLEGAL_SERVICE) {
			requestNet(mOrderHandler, params,
					NetworkUtil.VIOLATION_SERVICE_URL, false, 0);
		} else if (mServiceType == Constant.SERVICE_TYPE.LICENCE_SERVICE) {
			requestNet(mOrderHandler, params, NetworkUtil.LICENCE_SERVICE_URL,
					false, 0);
		}
	}

	private Handler mOrderHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:
				AlertDialog dialog = new AlertDialog(
						BusinessDetailActivity.this).builder();
				dialog.setMsg("发布订单成功！");
				dialog.setNegativeButton("继续发布", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishActivity();
					}
				});
				if (CacheTools.getUserData("role").contains("2")
						|| CacheTools.getUserData("role").contains("3")) {// 区分是否使用户发布
					dialog.setPositiveButton("进入秘书服务", new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(
									BusinessDetailActivity.this,
									MyWorkActivity.class);
							startActivity(intent);
							finishActivity();
						}
					});
				} else {
					dialog.setPositiveButton("进入订单管理", new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(
									BusinessDetailActivity.this,
									TaskManagerActivity.class);
							startActivity(intent);
							finishActivity();
						}
					});
				}
				dialog.setCancelable(false);
				dialog.show();
				break;
			case Constant.NET_DATA_FAIL:
				ToastUtil.toastShow(BusinessDetailActivity.this,
						((DataError) msg.obj).getErrorMessage());
				break;
			default:
				break;
			}
		}
	};
}
