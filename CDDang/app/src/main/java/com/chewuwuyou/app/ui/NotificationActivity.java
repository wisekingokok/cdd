package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.NotificationAdapter;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener;
import com.chewuwuyou.app.widget.PullToRefreshListView;

/**
 * 通知列表
 * 
 * @author Administrator
 */
public class NotificationActivity extends CDDBaseActivity implements
		OnRefreshListener<ListView>, View.OnClickListener {

	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	private PullToRefreshListView mPullToRefreshListView;// 通知列表
	private NotificationAdapter mAdapter;
	private Task mTask;// 任务实体
	private RelativeLayout mTitleHeight;// 标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_ac);
		initView();
		initData();
		initEvent();

		skipToByNotificationType();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.notification_list);
	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mHeaderTV.setText(R.string.notification_title);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackBtn.setOnClickListener(this);
		mPullToRefreshListView.setOnRefreshListener(this);
	}

	/**
	 * 通过通知传过来的typeid查询任务
	 */
	private void queryTaskById(int typeId) {

		AjaxParams params = new AjaxParams();
		params.put("taskId", String.valueOf(typeId));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mTask = Task.parse(msg.obj.toString());
					if (mTask != null) {
						Intent intent = new Intent(NotificationActivity.this,
								OrderActivity.class);
						intent.putExtra("taskId", mTask.getId());
						startActivity(intent);
					}

					break;
				case Constant.NET_DATA_FAIL:

					break;
				}
				super.handleMessage(msg);
			}

		}, params, NetworkUtil.GETTASK_BYTASKID, false, 1);

	}

	/**
	 * 修改通知状态
	 */
	private void updateNotificationStatus(int id) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					// requestNet(mHandler, null,
					// NetworkUtil.GET_NOTIFICATION_LIST, false, 0);
					skipToByNotificationType();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}

		}, params, NetworkUtil.UPDATE_NOTIFICATION_STATUS, false, 0);
	}

	@SuppressWarnings("static-access")
	private void skipToByNotificationType() {
		new NetworkUtil().postMulti(NetworkUtil.GET_NOTIFICATION_LIST, null,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						dismissWaitingDialog();
						MyLog.i("YUY", "通知======" + t.toString());
						try {
							JSONObject jo = new JSONObject(t.toString());
							final JSONArray jArray = new JSONArray(jo
									.getString("data"));
							if (jArray != null && jArray.length() > 0) {
								mAdapter = new NotificationAdapter(
										NotificationActivity.this, jArray);
								mPullToRefreshListView.setAdapter(mAdapter);
								mPullToRefreshListView
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> arg0,
													View arg1, int arg2,
													long arg3) {

												try {
													int id = jArray
															.getJSONObject(
																	arg2 - 1)
															.getInt("id");
													int status = jArray
															.getJSONObject(
																	arg2 - 1)
															.getInt("status");
													int typeId = jArray
															.getJSONObject(
																	arg2 - 1)
															.getInt("typeid");
													MyLog.i("YUY", "通知类型====="
															+ typeId);
													if (jArray.getJSONObject(
															arg2 - 1).getInt(
															"type") == 4) {
														Intent intent = new Intent(
																NotificationActivity.this,
																SystemRemindActivity.class);
														intent.putExtra(
																"content",
																jArray.getJSONObject(
																		arg2 - 1)
																		.getJSONObject(
																				"content")
																		.toString());
														startActivity(intent);
													} else {
														queryTaskById(typeId);
													}

													if (status == 0) {
														updateNotificationStatus(id);
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}

											}
										});
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						dismissWaitingDialog();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						showWaitingDialog();
					}

				});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(NotificationActivity.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(NotificationActivity.this);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		// requestNet(mHandler, null, NetworkUtil.GET_NOTIFICATION_LIST, false,
		// 1);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		default:
			break;
		}
	}
}
