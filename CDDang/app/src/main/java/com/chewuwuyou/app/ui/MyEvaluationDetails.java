package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.EvaluateAdapter;
import com.chewuwuyou.app.bean.Comment;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.TrafficBroker;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.RadioGroup;
import com.chewuwuyou.app.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @describe:评价详情
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-8下午9:44:31
 */
public class MyEvaluationDetails extends BaseActivity {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	private TrafficBroker mTrafficBroker;// 经纪人实体

	/**
	 * 服务地区
	 */
	@ViewInject(id = R.id.business_location)
	private TextView mLocation;
	/**
	 * 商家头像
	 */
	@ViewInject(id = R.id.business_head_img)
	private ImageView mHeadImg;
	/**
	 * 商家名称
	 */
	@ViewInject(id = R.id.business_name)
	private TextView mName;
	/**
	 * 商家电话
	 */
	@ViewInject(id = R.id.business_phone)
	private TextView mPhone;// 电话

	/**
	 * 用户评价总数量
	 */

	@ViewInject(id = R.id.user_evaluate)
	private TextView mUserEvaluate;

	/**
	 * 商家等级
	 */
	@ViewInject(id = R.id.rating_bar)
	private RatingBar mRatingBar;

	/**
	 * 好评列表
	 */
	@ViewInject(id = R.id.pl_list)
	private ListView mCattleAgentList;
	/**
	 * 用户名
	 */
	@ViewInject(id = R.id.user_name)
	private TextView mUserName;
	private List<Comment> mComments;
	private List<Comment> mHpComments;
	private List<Comment> mZpComments;
	private List<Comment> mCpComments;
	/**
	 * 进入时默认选择好评列表
	 */
	private String plString = "好评";

	/**
	 * 评论group
	 */
	private RadioGroup mPlGroup;
	/**
	 * 好评、中评、差评
	 */
	private RadioButton mHpButton, mZpButton, mCpButton;
	private RelativeLayout mTitleHeight;//标题布局高度
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:
				mComments = Comment.parseList(msg.obj.toString());
				if (mComments != null) {
					// 按时间顺序降序排列
					Collections.sort(mComments, new Comparator<Comment>() {
						@Override
						public int compare(Comment lhs, Comment rhs) {
							Date date1 = DateTimeUtil.stringToDate(lhs
									.getPublishTime());
							Date date2 = DateTimeUtil.stringToDate(rhs
									.getPublishTime());
							// 对日期字段进行升序，如果欲降序可采用after方法
							if (date1.before(date2)) {
								return 1;
							}
							return -1;
						}
					});
				}
				for (int i = 0, size = mComments.size(); i < size; i++) {
					if (mComments.get(i).getStar() > 3) {
						mHpComments.add(mComments.get(i));
					}
					if (mComments.get(i).getStar() == 3) {
						mZpComments.add(mComments.get(i));
					}
					if (mComments.get(i).getStar() < 3) {
						mCpComments.add(mComments.get(i));
					}
				}
				mHpButton.setText(getString(R.string.good_comment,
						mHpComments.size()));
				mZpButton.setText(getString(R.string.average_comment,
						mZpComments.size()));
				mCpButton.setText(getString(R.string.negative_comment,
						mCpComments.size()));

				listShowByPlString();
				mPlGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						plString = ((RadioButton) findViewById(checkedId))
								.getText().toString();
						listShowByPlString();
					}
				});
				break;
			case Constant.NET_DATA_FAIL:
				ToastUtil.toastShow(MyEvaluationDetails.this,
						((DataError) msg.obj).getErrorMessage());
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_evaluate_details);
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mHeaderTV.setText("客户评价");
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});
		updateUI();
	}

	/**
	 * 获取经纪人详情更新ui
	 */
	private void updateUI() {
		AjaxParams params = new AjaxParams();
		params.put("businessId", getIntent().getStringExtra("businessId"));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", "===我的详情===" + msg.obj);
					mTrafficBroker = TrafficBroker.parse(msg.obj.toString());
					mLocation.setText(mTrafficBroker.getLocation());
					mName.setText(mTrafficBroker.getBusinessName());
					mPhone.setText(mTrafficBroker.getMobile());
//					ImageUtils.showImg(mHeadImg, mTrafficBroker.getUrl(),
//							R.drawable.bg_defaultimg);
					ImageUtils.displayImage(mTrafficBroker.getUrl(), mHeadImg, 10);
					/*mUserEvaluate.setText("总评价 ("
							+ mTrafficBroker.getCommentsize() + ")");*/
					mRatingBar.setRating(mTrafficBroker.getStar());
					AjaxParams params = new AjaxParams();
					params.put("businessId",
							String.valueOf(mTrafficBroker.getId()));
					requestNet(mHandler, params,
							NetworkUtil.QUERY_COMMENT_BY_BUSINESS, false, 1);
					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.QUERY_ME_BUSINESS, false, 0);
		mPlGroup = (RadioGroup) findViewById(R.id.pl_group);
		mHpButton = (RadioButton) findViewById(R.id.hp_btn);
		mZpButton = (RadioButton) findViewById(R.id.zp_btn);
		mCpButton = (RadioButton) findViewById(R.id.cp_btn);
		mHpComments = new ArrayList<Comment>();
		mZpComments = new ArrayList<Comment>();
		mCpComments = new ArrayList<Comment>();
	}

	/**
	 * 根据选择评论的等级更新列表
	 */
	private void listShowByPlString() {
		if (plString.contains("好评")) {
			EvaluateAdapter adapter = new EvaluateAdapter(
					MyEvaluationDetails.this, mHpComments);
			mCattleAgentList.setAdapter(adapter);
		} else if (plString.contains("中评")) {
			EvaluateAdapter adapter = new EvaluateAdapter(
					MyEvaluationDetails.this, mZpComments);
			mCattleAgentList.setAdapter(adapter);
		} else {
			EvaluateAdapter adapter = new EvaluateAdapter(
					MyEvaluationDetails.this, mCpComments);
			mCattleAgentList.setAdapter(adapter);
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(MyEvaluationDetails.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(MyEvaluationDetails.this);
	}
}
