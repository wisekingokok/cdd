package com.chewuwuyou.app.ui;

import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.PersonPicGridAdapter;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.bean.TuItem;
import com.chewuwuyou.app.utils.Bimp;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.MyGridView;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.util.StringUtil;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:车友信息:有关车友信息的统一使用该类(自己 将不会有聊天和加好友)
 * @author:yuyong
 * @date:2015-4-10下午3:08:13
 * @version:1.2.1
 */
public class PersonalHomeActivity extends CDDBaseActivity implements View.OnClickListener {

	private static final int UPDATE_DATA = 111;
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private TextView mUserCarDingleNumTV;// id
	private MyGridView mUserPicPullGV;
	private TextView mNickNameTV;// 昵称
	private TextView mUserSexTV;// 性别
	private TextView mUserWorkTV;// 职业
	private TextView mUserAddressTV;// 家乡
	private TextView mUserLoveCarTV;// 爱车
	private ImageView mUserLoveCarIV;// 爱车图片
	private TextView mUserHobbiesTV;// 兴趣爱好
	private TextView mUserAgeTV;// 年龄
	private TextView mUserStarSitTV;// 星座
	private TextView mUserRegsiterTimeTV;
	private TextView mUserMoodTV;// 车友心情
	private ImageButton mUpdatePersonInfoIBtn;// 修改个人信息及查看他人帖子等

	private PopupWindow mMingXiPopWindow = null;
	private View mMingXiPopView;
	private PersonHome mPersonHome;
	private Button mToChatBtn;// 跟他聊天

	private ImageView mPersonPicBgIV;// 背景图片
	private RelativeLayout mContainer;
	private HackyViewPager mExpandedImageViewPager;
	private Button mAddCarFriendBtn;// 添加车友
	private Button mBeizhuBtn;
	private Button mHisQuanBtn;
	private Button mHisTieBtn;
	private Button mHisYueBtn;
	private Button mDeleteFriendBtn;

	private PersonPicGridAdapter adapter;
	private RelativeLayout mBarCodeRL;// 查看个人二维码
	private List<TuItem> mPicList;// 存储照片墙的所有图片,使用bitmap来进行默认存储新增
	// 为显示车型图片
	private List<CarModel> mCarModels;
	private String mCarBrand;
	private String mCarModel;
	private RelativeLayout mTitleHeight;// 标题布局高度
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:
				MyLog.i("YUY", "车友信息--------" + msg.obj.toString());
				try {
					JSONArray jArray = new JSONArray(msg.obj.toString());
					mPersonHome = PersonHome.parse(jArray.get(0).toString());
					if (String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))) {
						mUpdatePersonInfoIBtn.setVisibility(View.VISIBLE);
						mUpdatePersonInfoIBtn.setImageResource(R.drawable.btn_addnew);
						mToChatBtn.setVisibility(View.GONE);
						mAddCarFriendBtn.setVisibility(View.GONE);
					} else if (!String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))
							&& mPersonHome.getFriend().equals("1")
							&& ContacterManager.ifHasUser(getIntent().getExtras().getInt("userId") + "@iz232jtyxeoz")) {
						mUpdatePersonInfoIBtn.setVisibility(View.VISIBLE);
						mUpdatePersonInfoIBtn.setImageResource(R.drawable.top_btn_more);
						mToChatBtn.setVisibility(View.VISIBLE);
						mAddCarFriendBtn.setVisibility(View.GONE);
						mDeleteFriendBtn.setVisibility(View.VISIBLE);

					} else if (!String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))
							&& mPersonHome.getFriend().equals("0")
							|| !ContacterManager
									.ifHasUser(getIntent().getExtras().getInt("userId") + "@iz232jtyxeoz")) {
						mToChatBtn.setVisibility(View.GONE);
						mAddCarFriendBtn.setVisibility(View.VISIBLE);
						mDeleteFriendBtn.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				updateUI();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_home_view_ac);
		initView();
		initData();
		initEvent();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mUpdatePersonInfoIBtn = (ImageButton) findViewById(R.id.sub_header_bar_right_ibtn);
		mUserMoodTV = (TextView) findViewById(R.id.car_friend_mood_tv);
		mNickNameTV = (TextView) findViewById(R.id.user_nickname_tv);
		mUserSexTV = (TextView) findViewById(R.id.user_sex_tv);
		mUserWorkTV = (TextView) findViewById(R.id.user_work_tv);
		mUserAddressTV = (TextView) findViewById(R.id.user_address_tv);
		mUserLoveCarTV = (TextView) findViewById(R.id.user_love_car_tv);
		mUserLoveCarIV = (ImageView) findViewById(R.id.user_love_car_iv);
		mUserHobbiesTV = (TextView) findViewById(R.id.user_hobbies_tv);
		mUserRegsiterTimeTV = (TextView) findViewById(R.id.user_register_time_tv);
		mUserPicPullGV = (MyGridView) findViewById(R.id.person_pic_pull);
		mUserAgeTV = (TextView) findViewById(R.id.user_age_tv);
		mUserStarSitTV = (TextView) findViewById(R.id.user_star_seat_tv);
		mAddCarFriendBtn = (Button) findViewById(R.id.add_car_friend_btn);
		mToChatBtn = (Button) findViewById(R.id.to_chat_btn);
		mPersonPicBgIV = (ImageView) findViewById(R.id.person_pic_pull_iv);
		mUserCarDingleNumTV = (TextView) findViewById(R.id.user_car_dingle_number_tv);
		mExpandedImageViewPager = (HackyViewPager) findViewById(R.id.expanded_image_viewpager);
		mContainer = (RelativeLayout) findViewById(R.id.container);
		mBarCodeRL = (RelativeLayout) findViewById(R.id.barcode_rl);
		mMingXiPopView = getLayoutInflater().inflate(R.layout.ming_xi_pop_window, null);
		this.mMingXiPopWindow = new PopupWindow(mMingXiPopView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.mMingXiPopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mMingXiPopWindow.setFocusable(true);
		this.mMingXiPopWindow.setOutsideTouchable(true);
//		this.mBeizhuBtn = (Button) mMingXiPopView.findViewById(R.id.beizhu_btn);
//		this.mHisQuanBtn = (Button) mMingXiPopView.findViewById(R.id.his_quan_btn);
//		this.mHisTieBtn = (Button) mMingXiPopView.findViewById(R.id.his_tie_btn);
//		this.mHisYueBtn = (Button) mMingXiPopView.findViewById(R.id.his_yue_btn);

		this.mDeleteFriendBtn = (Button) mMingXiPopView.findViewById(R.id.delete_friend_btn);
	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mCarModels = CarModel.parseBrands(getFromAssets("mobile_models"));
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getIntent().getIntExtra("skip", 0) == 1) {
					getDate(Integer.valueOf(CacheTools.getUserData("userId")));
				} else {
					getDate(getIntent().getIntExtra("userId", 0));
				}
			}
		}).start();

	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mUpdatePersonInfoIBtn.setOnClickListener(this);
		mToChatBtn.setOnClickListener(this);
		mAddCarFriendBtn.setOnClickListener(this);
		this.mBeizhuBtn.setOnClickListener(this);
		this.mHisQuanBtn.setOnClickListener(this);
		this.mHisTieBtn.setOnClickListener(this);
		this.mHisYueBtn.setOnClickListener(this);
		this.mDeleteFriendBtn.setOnClickListener(this);
		mBarCodeRL.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.sub_header_bar_right_ibtn:
			if (mPersonHome == null) {
				ToastUtil.toastShow(PersonalHomeActivity.this, "数据获取失败导致无法跳转，请确认网络是否连接");
			} else if (String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))) {
				Intent intent = new Intent(PersonalHomeActivity.this, PersonInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.PERSONINFO_SER, mPersonHome);
				intent.putExtras(bundle);
				startActivityForResult(intent, UPDATE_DATA);
			} else {
				mMingXiPopWindow.showAsDropDown(mUpdatePersonInfoIBtn, 5, 5);
				if (!TextUtils.isEmpty(mPersonHome.getNoteName())) {
					this.mBeizhuBtn.setText("备注:(" + mPersonHome.getNoteName() + ")");
				}
			}

			break;
		case R.id.barcode_rl:
			if (mPersonHome != null) {
				Intent barcodeIntent = new Intent(PersonalHomeActivity.this, PersonMyBarCodeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.PERSONINFO_SER, mPersonHome);
				barcodeIntent.putExtras(bundle);
				startActivity(barcodeIntent);
			} else {
				ToastUtil.toastShow(PersonalHomeActivity.this, "数据获取失败导致无法跳转，请确认网络是否连接");
			}
			break;
		case R.id.to_chat_btn:
			AppContext.createChat(PersonalHomeActivity.this, mPersonHome.getUserId());
//			Intent chatIntent = new Intent(PersonalHomeActivity.this, ChatActivity.class);
//			chatIntent.putExtra("to", String.valueOf(mPersonHome.getUserId()) + "@iz232jtyxeoz");
//			startActivity(chatIntent);
			break;
		case R.id.add_car_friend_btn:
			AjaxParams params = new AjaxParams();
			params.put("userId", mPersonHome.getUserId() + "");
			requestNet(new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						MyLog.i("YUY", "加好友发送推送成功");
						try {
							createSubscriber(StringUtil.getJidByName(String.valueOf(mPersonHome.getUserId())), null,
									null);
							mToChatBtn.setVisibility(View.VISIBLE);
							mAddCarFriendBtn.setVisibility(View.GONE);
							mDeleteFriendBtn.setVisibility(View.VISIBLE);
							mUpdatePersonInfoIBtn.setVisibility(View.VISIBLE);
						} catch (XMPPException e) {
							e.printStackTrace();
						}
						break;

					default:
						break;
					}
				}
			}, params, NetworkUtil.ADD_FRIEND_SEND, false, 1);
			break;

//		case R.id.beizhu_btn:// 设置备注
//			setNotename(getIntent().getExtras().getInt("userId") + "@iz232jtyxeoz");
//			mMingXiPopWindow.dismiss();
//			break;
//		case R.id.his_quan_btn://圈子
//
//			Intent intent = new Intent(PersonalHomeActivity.this, QuanActivity.class);
//			intent.setAction("com.chewuwuyou.app.other_quan_wen");
//			intent.putExtra("other_id", String.valueOf(getIntent().getIntExtra("userId", 0)));
//			intent.putExtra("other_name", CarFriendQuanUtils.showCarFriendName(mPersonHome.getNoteName(),
//					mPersonHome.getNick(), mPersonHome.getUserName()));
//			startActivity(intent);
//			mMingXiPopWindow.dismiss();
//			break;
//		case R.id.his_tie_btn://贴子
//			Intent intent2 = new Intent(PersonalHomeActivity.this, TieActivity.class);
//			intent2.putExtra("other_id", String.valueOf(getIntent().getIntExtra("userId", 0)));
//			intent2.putExtra("other_name", CarFriendQuanUtils.showCarFriendName(mPersonHome.getNoteName(),
//					mPersonHome.getNick(), mPersonHome.getUserName()));
//			startActivity(intent2);
//			mMingXiPopWindow.dismiss();
//			break;
//		case R.id.his_yue_btn://活动
//			Intent intent3 = new Intent(PersonalHomeActivity.this, OtherYueActivity.class);
//			intent3.setAction("com.chewuwuyou.app.other_yue");
//			intent3.putExtra("other_id", String.valueOf(getIntent().getIntExtra("userId", 0)));
//			intent3.putExtra("other_name", CarFriendQuanUtils.showCarFriendName(mPersonHome.getNoteName(),
//					mPersonHome.getNick(), mPersonHome.getUserName()));
//			startActivity(intent3);
//			mMingXiPopWindow.dismiss();
//			break;
		case R.id.delete_friend_btn:// 删除好友
			if (getIntent().getIntExtra("userId", 0) != 0) {
				showDeleteDialog(getIntent().getExtras().getInt("userId") + "@iz232jtyxeoz");
				mMingXiPopWindow.dismiss();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 获取车友信息数据
	 */
	private void getDate(int userId) {
		AjaxParams params = new AjaxParams();
		params.put("ids", String.valueOf(userId));
		requestNet(mHandler, params, NetworkUtil.GET_USER_INFO, false, 0);
	}

	/**
	 * 添加一个联系人
	 * 
	 * @param userJid
	 *            联系人JID
	 * @param nickname
	 *            联系人昵称
	 * @param groups
	 *            联系人添加到哪些组
	 * @throws XMPPException
	 */
	protected void createSubscriber(String userJid, String nickname, String[] groups) throws XMPPException {
		try {
			XmppConnectionManager.getInstance().getConnection().getRoster().createEntry(userJid, nickname, groups);
		} catch (NotLoggedInException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Bimp.tempSelectBitmap.clear();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getIntent().getIntExtra("skip", 0) == 1) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					getDate(Integer.valueOf(CacheTools.getUserData("userId")));
				}
			}).start();
			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mUpdatePersonInfoIBtn.setVisibility(View.VISIBLE);
					mUpdatePersonInfoIBtn.setImageResource(R.drawable.btn_addnew);
				}
			});

		}
	}

	/**
	 * 删除用户
	 * 
	 * @param jid
	 */
	private void showDeleteDialog(final String jid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.delete_user_confim)).setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// api删除
						try {
							ContacterManager.deleteUser(jid);
							mToChatBtn.setVisibility(View.GONE);
							mAddCarFriendBtn.setVisibility(View.VISIBLE);
							mDeleteFriendBtn.setVisibility(View.GONE);
							mUpdatePersonInfoIBtn.setVisibility(View.GONE);
							// 删除数据库
							NoticeManager.getInstance(PersonalHomeActivity.this).delNoticeHisWithSb(jid);
							MessageManager.getInstance(PersonalHomeActivity.this).delChatHisWithSb(jid);

						} catch (XMPPException e) {
							MyLog.e("YUY", e.toString());
						}
						dialog.dismiss();

					}
				}).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();

					}
				});
		builder.show();

	}

	/**
	 * 修改备注
	 * 
	 * @param
	 */
	// private void setNotename(final String jid) {
	// final EditText name_input = new EditText(PersonalHomeActivity.this);
	// name_input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// name_input.setHint("输入昵称");
	// new
	// AlertDialog.Builder(PersonalHomeActivity.this).setTitle("修改昵称").setView(name_input)
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// String name = name_input.getText().toString();
	// if (!"".equals(name)) {
	// ContacterManager.setNickname(jid, name,
	// XmppConnectionManager.getInstance().getConnection());
	// }
	//
	// }
	// }).setNegativeButton("取消", null).show();
	// }

	private void updateUI() {
		if (!TextUtils.isEmpty(mPersonHome.getImageBack())) {
			// ImageUtils.displayImage(mPersonHome.getImageBack(),
			// mPersonPicBgIV, 0);
			ImageUtils.displayImage(mPersonHome.getImageBack(), mPersonPicBgIV, 0, mOutMetrics.widthPixels,
					mOutMetrics.widthPixels, ScalingLogic.CROP);
		} else {
			mPersonPicBgIV.setImageResource(R.drawable.cddbg1);
		}
		mPicList = mPersonHome.getUrls();
		if (mPersonHome.getUrls().size() > 8) {
			mPicList = mPersonHome.getUrls().subList(0, 8);
		} else {
			mPicList = mPersonHome.getUrls();
		}
		adapter = new PersonPicGridAdapter(PersonalHomeActivity.this, mPicList, mUserPicPullGV, mExpandedImageViewPager,
				mContainer);
		mUserPicPullGV.setAdapter(adapter);
		mTitleTV.setText(CarFriendQuanUtils.showCarFriendName(mPersonHome.getNoteName(), mPersonHome.getNick(),
				mPersonHome.getUserName()));
		if (!TextUtils.isEmpty(mPersonHome.getSign())) {
			mUserMoodTV.setText(mPersonHome.getSign());
		} else {
			mUserMoodTV.setHint("...");
		}
		mUserCarDingleNumTV.setText(mPersonHome.getUserId());
		mNickNameTV.setText(mPersonHome.getNick());
		if (mPersonHome.getSex().equals("0")) {
			mUserSexTV.setText("男");
		} else if (mPersonHome.getSex().equals("1")) {
			mUserSexTV.setText("女");
		} else {
			mUserSexTV.setText("不详");
		}
		mUserWorkTV.setText(mPersonHome.getPro());
		mUserAddressTV.setText(mPersonHome.getLocation());
		mUserLoveCarTV.setText(mPersonHome.getCarBrand());
		mUserHobbiesTV.setText(mPersonHome.getHobby());

		String time = mPersonHome.getRegistertime().substring(0, 10);
		mUserRegsiterTimeTV.setText(time);

		mUserAgeTV.setText(mPersonHome.getAge());
		mUserStarSitTV.setText(mPersonHome.getStarsit());
		if (!TextUtils.isEmpty(mPersonHome.getCarBrand()) && !mPersonHome.getCarBrand().equals("未设置")
				&& mPersonHome.getCarBrand().contains("/")) {
			mCarBrand = mPersonHome.getCarBrand().split("/")[0];

			mCarModel = mPersonHome.getCarBrand().split("/")[1];
			// 设置车型图片
			setImage(mUserLoveCarIV, getVehicleImage(mCarBrand, mCarModel, mCarModels));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case UPDATE_DATA:
			if (resultCode == RESULT_OK) {
				getDate(Integer.valueOf(CacheTools.getUserData("userId")));
			}
			break;
		default:
			break;
		}
	}

}
