package com.chewuwuyou.app.ui;

import java.text.DecimalFormat;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Offer;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.tools.EditInputFilter;
import com.chewuwuyou.app.tools.EditInputFilterThousand;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.OrderStateUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * 接单大厅 1、我要接单 2、报价
 * 
 * @author zengys
 * 
 */
public class NewRobOrderDetailsActivity extends CDDBaseActivity implements OnClickListener {

	private ImageButton mImbtnBack;
	private TextView mTextTitle;
	private TextView mOrderStatus;
	private LinearLayout mLinearOrderContacts;
	private TextView mOrderServe;
	private TextView mOrderPersons;
	private TextView mOrderNum;
	private TextView mOrdercity;
	private TextView mOrderTime;
	private TextView mOrderName;
	private ImageView mImageNews;
	private ImageView mImageTelephone;
	private Button mBtnOffer;
	private Button mBtnJiedan;
	private TextView mEditMoney;
	private Task mTask;// 任务详情
	private List<Offer> mOffers;
	private String mMyOfferId;// 我的报价Id
	private int mOfferNum = 0;// 1标识已有我的报价信息 调用修改报价接口进行修改报价
	private String mTaskId;
	private TextView mTextOrderHint;
	private RelativeLayout mRelativeBaojia;
	private ImageView mHandlerHeadIV;// 对方的头像
	private int mMakeOrderNum;
	DecimalFormat df = new DecimalFormat("######0.00");
	private TextView mBRightTV;
	@ViewInject(id = R.id.order_remarks)
	private TextView mOrderRemarks;
	@ViewInject(id = R.id.order_hide_remarks)
	private LinearLayout mOrderHideRemarks;
	private String maxPrice;//最大确认价格
	private String minPrice;//最小确认价格

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_roborder_details_layout);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mImbtnBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mOrderStatus = (TextView) findViewById(R.id.order_status);
		mLinearOrderContacts = (LinearLayout) findViewById(R.id.order_contacts_contact_mode);
		mOrderNum = (TextView) findViewById(R.id.order_num);
		mOrderServe = (TextView) findViewById(R.id.order_serve);
		mOrdercity = (TextView) findViewById(R.id.order_city);
		mOrderPersons = (TextView) findViewById(R.id.order_persons);
		mOrderTime = (TextView) findViewById(R.id.order_time);
		mOrderName = (TextView) findViewById(R.id.ordear_contact_name);
		mImageNews = (ImageView) findViewById(R.id.order_news);
		mImageTelephone = (ImageView) findViewById(R.id.order_telephone);
		mBtnOffer = (Button) findViewById(R.id.btn_offer);
		mBtnJiedan = (Button) findViewById(R.id.btn_jiedan);
		mEditMoney = (TextView) findViewById(R.id.edit_money);
		mTextOrderHint = (TextView) findViewById(R.id.text_order_hint);
		mRelativeBaojia = (RelativeLayout) findViewById(R.id.relative_baojia);
		mTextTitle.setVisibility(View.VISIBLE);
		mHandlerHeadIV = (ImageView) findViewById(R.id.handler_head_iv);
		mBRightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
	}

	@Override
	protected void initData() {
		mTextTitle.setText("订单详情");
		getData();
	}

	@Override
	protected void initEvent() {

		mImbtnBack.setOnClickListener(this);
		mBtnOffer.setOnClickListener(this);
		mBtnJiedan.setOnClickListener(this);
		mImageTelephone.setOnClickListener(this);
		mImageNews.setOnClickListener(this);
		mBRightTV.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			if (AppManager.isExsitMianActivity(MainActivityEr.class, NewRobOrderDetailsActivity.this)==false) {//判断首页是否在堆栈中
				startActivity(new Intent(NewRobOrderDetailsActivity.this, MainActivityEr.class));
			}
			finishActivity();
			break;
			case R.id.sub_header_bar_right_tv:
				dialog("","是否取消报价","");
				break;

		case R.id.order_telephone:
			DialogUtil.call(mTask.getExactPhone(),
					NewRobOrderDetailsActivity.this);
			break;

		case R.id.order_news:
			AppContext.createChat(NewRobOrderDetailsActivity.this,
					mTask.getUserId());
			// Intent chatIntent = new Intent(NewRobOrderDetailsActivity.this,
			// ChatActivity.class);
			// chatIntent.putExtra("to", mTask.getUserId() + "@iZ232jtyxeoZ");
			// startActivity(chatIntent);
			break;

		case R.id.btn_offer:
			// changeOffer();// 参与报价

			if(mBtnOffer.getText().toString().equals("修改价格")){
				dialog();
			}else{
				if(mTask.getProvideBill().equals("1")){
					ConfirmDialog("车当当提醒你","此单需要您提供相关办理票据", "");
				}else{
					dialog();
				}
			}
			break;

		case R.id.btn_jiedan:
			makeOrder();// 我要接单
			break;
		default:
			break;
		}

	}

	/**
	 * 查询订单详情及我的报价情况
	 */
	private void getData() {

		AjaxParams params = new AjaxParams();
		params.put("taskId", getIntent().getStringExtra("taskId"));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					// mOfferNum = 1;
					MyLog.i("YUY", "任务详情 = " + msg.obj.toString());
					mTask = Task.parse(msg.obj.toString());

					if(!mTask.getStatus().equals("27")&&!mTask.getStatus().equals("28")){
						Intent intent = new Intent(NewRobOrderDetailsActivity.this,OrderActivity.class);
						intent.putExtra("taskId",mTask.getId());
						startActivity(intent);
						finishActivity();
						return;
					}
					//motify start by yuyong 20160919修改确认价格范围
					maxPrice=mTask.getMax();
					minPrice=mTask.getMin();
					//motify end by yuyong 20160919修改确认价格范围
					statusUi(mTask);
					mTaskId = mTask.getId();
					mOrderNum.setText(mTask.getOrderNum());
					if(mTask.getProvideBill().equals("1")){
						mOrderRemarks.setText("需要票据");
					}else{
						mOrderRemarks.setText("不需要票据");
					}
					mOrderServe.setText(ServiceUtils.getProjectName(mTask
							.getProjectName())); // 服务项目

					if(Integer.parseInt(mTask.getStatus())==2){
                        finishActivity();
					}else if (Integer.valueOf(mTask.getStatus()) == 27) {
						OrderStateUtil.orderStatusShow(
								Integer.parseInt(mTask.getStatus()),
								Integer.parseInt(mTask.getFlag()), mOrderStatus);// 订单状态
					} else {
						if (mTask.getOfferCnt() == 0) {
							mOrderStatus.setText("报价中");

						} else {
							OrderStateUtil.orderStatusShow(
									Integer.parseInt(mTask.getStatus()),
									Integer.parseInt(mTask.getFlag()),
									mOrderStatus);// 订单状态
						}
					}
					setTaskLocation(mTask, mOrdercity);// 办理地区
					mOrderTime.setText(mTask.getPubishTime());// 订单创建时间
					mOrderPersons.setText("总共" + mTask.getOrderCnt() + "位");// 接单商家
					ImageUtils.displayImage(mTask.getUrl(), mHandlerHeadIV, 10);
					mOrderName.setText(mTask.getName());



					break;

				default:

					break;
				}
			}

		}, params, NetworkUtil.QUERY_A_TASK, false, 0);
		getOffers();
	}

	/**
	 * 查询所有报价信息
	 */
	private void getOffers() {
		AjaxParams params = new AjaxParams();
		params.put("taskId", getIntent().getStringExtra("taskId"));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mOffers = Offer.parseList(jo.getJSONArray("offers")
								.toString());
						mMakeOrderNum = mOffers.size();
						for (int i = 0; i < mOffers.size(); i++) {
							if (mOffers.get(i).getUserId()
									.equals(CacheTools.getUserData("userId"))) {// 我已报价（我已接单）只能进行
								mOfferNum = 1;
								Offer offer = mOffers.get(i);
								mMyOfferId = String.valueOf(offer.getId());
								mEditMoney.setText("￥  "
										+ df.format(offer.getOffer()));
								if (offer.getOffer() == 0) {
									mBtnOffer.setText("报价");
								} else {

									mBtnOffer.setText("修改价格");
								}

							}
						}

						viewType(mOfferNum);

					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.GET_OFFER, false, 1);

	}

	private void setTaskLocation(Task task, TextView locationTV) {

		if (task.getTaskProvince().equals("") || task.getTaskProvince() == ""
				|| task.getTaskProvince() == null) {
			if (task.getTaskDistrict().equals("")
					|| task.getTaskDistrict() == ""
					|| task.getTaskDistrict() == null) {
				locationTV.setText(task.getTaskLocation());
			} else {
				locationTV.setText(task.getTaskCity() + "," + task.getTaskDistrict());
			}

		} else {
			locationTV.setText(task.getTaskProvince() + "," + task.getTaskCity() + "," + task.getTaskDistrict());
		}

	}

	/**
	 * 我要接单
	 */
	private void makeOrder() {

		if (mMakeOrderNum >= 30) {
			ToastUtil.toastShow(NewRobOrderDetailsActivity.this,
					"此单人数已超限，下次要快点哟");
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("taskId", mTaskId);
		params.put("price", "0");
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					ToastUtil.toastShow(NewRobOrderDetailsActivity.this, "接单成功");
					viewType(1);// 1成功接单后更新UI
					getData();
					getOffers();
					break;
				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(NewRobOrderDetailsActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;
				default:
					ToastUtil
							.toastShow(NewRobOrderDetailsActivity.this, "订单异常");
					break;
				}
			}
		}, params, NetworkUtil.MAKE_OFFER, false, 0);

	}

	/**
	 * 发送报价信息
	 * 
	 */
	private void changeOffer(EditText edit, final Dialog dialog) {
		final String price = edit.getText().toString().trim();
		AjaxParams params = new AjaxParams();
		if (TextUtils.isEmpty(price) || price == null) {
			ToastUtil.toastShow(NewRobOrderDetailsActivity.this, "订单金额不能为空");
			return;
		}

		if (price.equals(".")) {
			ToastUtil.toastShow(NewRobOrderDetailsActivity.this, "请输入正确的报价金额");
			return;
		}
		//motify start by yuyong 20160919 修改金额限制从服务器取
		if (Double.valueOf(price) < Double.valueOf(minPrice)) {
			ToastUtil.toastShow(NewRobOrderDetailsActivity.this, "报价金额不低于"+minPrice+"元");
			return;
		}
		if (Double.valueOf(price) > Double.valueOf(maxPrice)) {
			ToastUtil.toastShow(NewRobOrderDetailsActivity.this, "输入金额不能大于"+maxPrice+"元");
			return;
		}
		//motify start by yuyong 20160919 修改金额限制从服务器取
		MyLog.i("YUY", "myOfferId = " + mMyOfferId + "price = " + price);
		params.put("taskOfferId", mMyOfferId);
		params.put("price", price);
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					if(mBtnOffer.getText().toString().equals("修改价格")){
						ToastUtil.toastShow(NewRobOrderDetailsActivity.this, "修改报价成功");
					}else{
						ToastUtil.toastShow(NewRobOrderDetailsActivity.this, "报价成功");
					}
					mEditMoney
							.setText("￥  " + df.format(Double.valueOf(price)));
					dialog.dismiss();
					getData();
					break;
				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(NewRobOrderDetailsActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.UPDATE_OFFER, false, 0);

	}

	/**
	 * 判断View的显示
	 * 
	 * @param myOffer
	 *            1表示已有我的报价信息（我已接单）
	 */
	private void viewType(int myOffer) {

		if (myOffer == 0) {
			mBtnJiedan.setVisibility(View.VISIBLE);
			mTextOrderHint.setVisibility(View.INVISIBLE);
			mRelativeBaojia.setVisibility(View.INVISIBLE);
			mLinearOrderContacts.setVisibility(View.INVISIBLE);
		} else {
			mBtnJiedan.setVisibility(View.INVISIBLE);
			mTextOrderHint.setVisibility(View.VISIBLE);
			mRelativeBaojia.setVisibility(View.VISIBLE);
			mLinearOrderContacts.setVisibility(View.VISIBLE);
			mBRightTV.setText("取消报价");
			mBRightTV.setVisibility(View.VISIBLE);
		}
	}

	// 弹窗
	private void dialog() {

		final Dialog dialog = new Dialog(NewRobOrderDetailsActivity.this,
				R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater
				.from(NewRobOrderDetailsActivity.this);
		final RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.make_offer_dialog, null);

//		layout.getBackground().setAlpha(100);
		dialog.setContentView(layout);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		((TextView) layout.findViewById(R.id.title_tv)).setText("报价金额");
		final EditText priceET = (EditText) layout.findViewById(R.id.price_et);

		// 取消报价
		Button btnCancel = (Button) layout.findViewById(R.id.cancel_btn);
		// 报价
		Button makeOfferBtn = (Button) layout.findViewById(R.id.make_offer_btn);
		makeOfferBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(priceET.getText().toString().contains(".")){
					String sdfk = priceET.getText().toString().substring(priceET.getText().toString().indexOf(".")+1);
					if(sdfk.length()>2){
						ToastUtil.toastShow(NewRobOrderDetailsActivity.this,"金额保留两位小数");
					}else{
						changeOffer(priceET, dialog);
					}
				}else{
					changeOffer(priceET, dialog);
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		}
		);

	}

	/**
	 * 
	 * 在业务订单详情，如果发单方付款等导致此订单状态有所改变，那么接单方 在业务订单列表进去也应该到相应的UI
	 */

	private void statusUi(Task task) {
		// 4表示订单已付款状态

		if (task.getStatus().equals("4")) {

			Intent intent = new Intent(NewRobOrderDetailsActivity.this,
					OrderActivity.class);
			intent.putExtra("taskId", getIntent().getStringExtra("taskId"));
			startActivity(intent);
			finishActivity();
		}
	}

	/**
	 * 提示用户是否进行操作
	 */
	public void dialog(String title, String context, final String txet) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(NewRobOrderDetailsActivity.this);
		dialog.setTitle(title);
		dialog.setMessage(context);
		dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				cancelOrders(dialog);
			}
		});
		dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		dialog.create().show();
	}
	/**
	 * 取消接单
	 */
	public void cancelOrders(AlertDialog.Builder dialog){
		AjaxParams params = new AjaxParams();
		params.put("taskId", getIntent().getStringExtra("taskId"));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						finishActivity();
						break;
					case Constant.NET_DATA_FAIL:
						ToastUtil.toastShow(NewRobOrderDetailsActivity.this,((DataError) msg.obj).getErrorMessage());
					default:
						break;
				}
			}
		}, params, NetworkUtil.SELECT_DELETE, false, 0);
	}

	/**
	 * 提示用户是否进行操作
	 *
	 */
	public void ConfirmDialog(String title, String context, final String txet) {
		new com.chewuwuyou.app.utils.AlertDialog(this).builder().setTitle(title)
				.setMsg(context)
				.setPositiveButton("不提供", new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

					}
				}).setNegativeButton("提供", new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog();
			}
		}).show();
	}
}
