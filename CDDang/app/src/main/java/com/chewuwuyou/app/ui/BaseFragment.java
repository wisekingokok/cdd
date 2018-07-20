package com.chewuwuyou.app.ui;

import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.WaitingDialog;

@SuppressLint("InlinedApi")
public abstract class BaseFragment extends Fragment {
	WaitingDialog waitingDialog;
	Activity mContext;
	String lstLoadingMsg = "";
	String doLoadingMsg = "";
	private WaitingDialog mWaitingDialog;
	protected static final DisplayMetrics mOutMetrics = new DisplayMetrics();
	protected int mTuWidth = 0;
	protected int mTuHeight = 0;

	protected int mBgTuWidth = 0;
	protected int mBgTuHeight = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity();
		mWaitingDialog = new WaitingDialog(mContext);

		mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		mContext.getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);
		mTuWidth = (mOutMetrics.widthPixels - 2 * getResources().getDimensionPixelSize(R.dimen.quan_padding)
				- getResources().getDimensionPixelSize(R.dimen.quan_avatar_width)
				- 2 * getResources().getDimensionPixelSize(R.dimen.quan_tu_interval)) / 3;
		mTuHeight = mTuWidth;

		mBgTuWidth = mOutMetrics.widthPixels;
		mBgTuHeight = (mOutMetrics.widthPixels * 2) / 3;

	}

	// 规范代码，所有继承它的activity都得实现这三个方法，让代码整洁
	protected abstract void initView();

	protected abstract void initData();

	protected abstract void initEvent();

	/**
	 * 显示滚动的对话框
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            消息内容
	 * @author huangzhihao
	 */
	public void showProgressDialog(Context context, final String title, final String message) {
		postRunnable(new Runnable() {
			@Override
			public void run() {
				showProgress(title, message);
			}
		});
	}

	/**
	 * 显示滚动的对话框
	 *            消息内容
	 * @author huangzhihao
	 */
	public void showLoadListProgressDialog() {
		postRunnable(new Runnable() {
			@Override
			public void run() {
				showProgress(lstLoadingMsg);
			}
		});
	}

	/**
	 * 显示操作加载
	 */
	public void showLoadDoingProgressDialog() {
		postRunnable(new Runnable() {
			@Override
			public void run() {
				showProgress(doLoadingMsg);
			}
		});
	}

	public void showProgress(String title, String message) {
		if (null == waitingDialog) {
			waitingDialog = new WaitingDialog(mContext);
		}
		if (waitingDialog.isShowing()) {
			waitingDialog.dismiss();
		}
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if (title != null) {
			waitingDialog.setTitle(title);
		}
		if (message != null) {
			waitingDialog.setMessage(message);
		}
		waitingDialog.setIndeterminate(false);
		// waitingDialog.setCancelable(true);
		waitingDialog.show();
	}

	public void showProgress(String message) {
		showProgress(null, message);
	}

	/**
	 * 隐藏滚动的对话框
	 */
	public void dismissProgressDialog() {
		postRunnable(new Runnable() {
			@Override
			public void run() {
				dismissProgressDialog1();
			}
		});
	}

	/**
	 * 隐藏滚动的对话框
	 */
	public void dismissProgressDialog1() {
		if (waitingDialog != null && waitingDialog.isShowing()) {
			waitingDialog.dismiss();
		}
		waitingDialog = null;
	}

	private void postRunnable(Runnable r) {
		if (getActivity() != null && getView() != null) {
			getView().post(r);
		}
	}

	/**
	 * 对子类界面中的提示红点进行提示
	 * 
	 * @param dotType
	 */
	public static final int NO_NEED = -1;

	public void handleHintDot(int dotType, int showType) {
	}

	protected void showWaitingDialog() {
		if (mWaitingDialog != null && !mWaitingDialog.isShowing()) {
			if (getActivity() != null)
				mWaitingDialog.show();
		}
	}

	protected void dismissWaitingDialog() {
		if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
		}
	}

	/**
	 * @param handler
	 *            消息句柄用于通知UI线程更新UI
	 * @param params
	 *            请求参数
	 * @param url
	 *            请求地址
	 * @param isCache
	 *            是否缓存
	 * @param isCricle 是否转圈
	 */
	@SuppressWarnings("static-access")
	public void requestNet(final Handler handler, AjaxParams params,
			final String url, final boolean isCache, final int isCricle) {
		new NetworkUtil().postMulti(url, params, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (isCricle == 0) {
					showWaitingDialog();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				MyLog.i("YUY", "======fail===" + strMsg);
				if (isCricle == 0) {
					dismissWaitingDialog();
				}
				Message msg = new Message();
				msg.what = Constant.NET_REQUEST_FAIL;
				handler.sendMessage(msg);
				if (IsNetworkUtil.isNetworkAvailable(getActivity()) == false) {
					ToastUtil.toastShow(getActivity(),"网络不可用，请检查您的网络是否连接");
				} else {
					ToastUtil.toastShow(getActivity(),"网络异常，请稍后再试");
					// 请求服务器失败，请稍候再试
					/*
					 * Toast toast = Toast.makeText(CDDgetActivity(),
					 * "请求服务器失败，请稍候再试。。", Toast.LENGTH_LONG); // 可以控制toast显示的位置
					 * toast.setGravity(Gravity.BOTTOM, 0, 10); toast.show();
					 */

				}

			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(String t) {
				MyLog.i("YUY", "url =" + url + "=====t = " + t);

				if (isCricle == 0) {
					dismissWaitingDialog();
				}
				super.onSuccess(t);
				try {
					JSONObject response = new JSONObject(t);
					switch (response.getInt("result")) {
					case 0:// 数据有问题的情况
							// json格式:{"result":0,"data":{"errorCode":0,"errorMessage":"用户未登录"}}
						int errorCode = response.getJSONObject("data").getInt(
								"errorCode");
						if (errorCode == ErrorCodeUtil.IndividualCenter.LOGIN_PAST) {
							// 没有登录跳到登录页面
							ToastUtil.toastShow(getActivity(),"您长时间未操作,请重新登录");
							Tools.clearInfo(getActivity());
							Intent intent = new Intent(getActivity(),
									LunchPageActivity.class);
							startActivity(intent);
							getActivity().finish();
						} else if (errorCode == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE
								|| errorCode == ErrorCodeUtil.Business.NOT_BUSINESS) {
							DialogUtil
									.loginInOtherPhoneDialog(getActivity());

						} else {
							// 其他异常处理
							Message msg = new Message();
							msg.obj = DataError.parse(response
									.getString("data"));
							msg.what = Constant.NET_DATA_FAIL;
							handler.sendMessage(msg);
						}
						break;
					case 1:// 成功返回:{"result":1,"data":obj}
						Message msg = new Message();
						if (response.getString("data") != null
								&& !"".equals(response.getString("data"))) {
							if (isCache) {
								CacheTools.saveCacheStr(url,
										response.getString("data"));
							}
							msg.obj = response.getString("data");
							msg.what = Constant.NET_DATA_SUCCESS;
							handler.sendMessage(msg);
						} else {
							msg.what = Constant.NET_DATA_NULL;
							handler.sendMessage(msg);
						}

						break;
					default:
						// handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
						// ToastUtil.toastShow(CDDgetActivity(), "数据异常");
						break;
					}
				} catch (Exception e) {
					// handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
					// ToastUtil.toastShow(CDDgetActivity(), "数据异常");
					e.printStackTrace();
				}
			}
		});
	}

	private InputMethodManager mImm;

	protected void showKeyboard() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 100);
	}

	protected void hideKeyboard() {
		try {
			if (mImm.isActive()) {
				mImm.hideSoftInputFromWindow(mContext.getCurrentFocus().getApplicationWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
		}
	}
}
