package com.chewuwuyou.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ActivityAdapter;
import com.chewuwuyou.app.adapter.BusinessHallAdapter;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.bean.TrafficBusinessListBook;
import com.chewuwuyou.app.bean.YueItem;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.callback.PullFragmentCallBack;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.ui.AreaSelectActivity;
import com.chewuwuyou.app.ui.NewRobOrderDetailsActivity;
import com.chewuwuyou.app.ui.ToquoteAcitivity;
import com.chewuwuyou.app.ui.YueDetailActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 违章代缴
 * 刘春
 *
 */
public class IllegalPaymentFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>,View.OnClickListener {

	private View mContentView;
	private PullToRefreshListView mBusinessList;
	private List<Task> mData;// 订单集合
	private BusinessHallAdapter mAdapter;
	private int mOfferNum;
	private int mCurcor;//记录条数，用于分页
	private String mServiceLoc;// 商家的服务地区
	// 省市区ID
	private String mProvinceId;
	private String mCityId;
	private String mDistrictId;
	private boolean mIsRefreshing = false;// 翻页要用
	private CallBackValue callBackValue;//定义回调接口
	private LinearLayout mNetworkRequest;
	private LinearLayout mNetworkAbnormalLayout;//暂无订单信息或网络异常
	private TextView mNetworkAgain;//点击重新加载
	private TextView mBusinessNoOrder;//暂无订单

	private final int WOFABU=10;
	private final int WOJIDAN=20;
	/**
	 * fragment与activity产生关联是  回调这个方法
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//当前fragment从activity重写了回调接口  得到接口的实例化对象
		callBackValue =(CallBackValue) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.illegal_payment_fragment, null);
		initView();
		initData();
		initEvent();
		return mContentView;
	}

	/**
	 * 初始化界面
	 */
	@Override
	protected void initView() {
		mNetworkRequest= (LinearLayout) mContentView.findViewById(R.id.network_request);
		mNetworkAbnormalLayout= (LinearLayout) mContentView.findViewById(R.id.network_abnormal_layout);
		mNetworkAgain = (TextView) mContentView.findViewById(R.id.network_again);
		mBusinessList = (PullToRefreshListView) mContentView.findViewById(R.id.business_list);
		mBusinessNoOrder = (TextView) mContentView.findViewById(R.id.business_no_order);


		mData = new ArrayList<Task>();
		mAdapter = new BusinessHallAdapter(getActivity(),mData);
		mBusinessList.setAdapter(mAdapter);
	}

	/**
	 * 点击事件
	 * @param v
     */
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.network_again://点击重新加载
				mNetworkAbnormalLayout.setVisibility(View.GONE);
				mNetworkRequest.setVisibility(View.VISIBLE);
				getBusinessList();//访问网络，接收违章代缴数据
				break;
		}
	}


	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {

	}

	@Override
	public void onResume() {
		super.onResume();
		mNetworkAbnormalLayout.setVisibility(View.GONE);
		mNetworkRequest.setVisibility(View.VISIBLE);
		getBusinessList();//访问网络，接收违章代缴数据
	}

	@Override
	protected void initEvent() {
		mBusinessList.setOnRefreshListener(this);
		mNetworkAgain.setOnClickListener(this);
		mBusinessList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				if (mData.get(position-1).getUserId().equals(CacheTools.getUserData("userId"))) {// 判断是否由我自己发布
					intent = new Intent(getActivity(),ToquoteAcitivity.class);
				} else {
					intent = new Intent(getActivity(),NewRobOrderDetailsActivity.class);
					intent.putExtra("mOfferNum",String.valueOf(mOfferNum));
				}
				intent.putExtra("taskId", mData.get(position-1).getId());
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取B类商家订单
	 */
	private void getDate(final boolean refresh) {
		AjaxParams params = new AjaxParams();
		if (refresh) {
			mCurcor = 0;
		}
		params.put("start", String.valueOf(mCurcor));
		params.put("taskType", "1");//1代表违章服务
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						try {
							JSONObject jo = new JSONObject(msg.obj.toString());
							List<Task> mRefreshData = Task.parseList(jo.getJSONArray("tasks").toString());

							mIsRefreshing = false;
							mBusinessList.onRefreshComplete();
							if (refresh) {
								mBusinessList.onLoadMore();
								mData.clear();
								if (mRefreshData == null) {
									mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
								}
								if(mRefreshData.size()==0){
									mBusinessNoOrder.setVisibility(View.VISIBLE);
								}
							} else {
								if (mRefreshData == null) {
									ToastUtil.toastShow(getActivity(),
											"没有更多数据了");
									return;
								}
							}

							mData.addAll(mRefreshData);
							mCurcor = mRefreshData.size();
							if (mRefreshData.size() < 1) {
								if (msg.obj.toString().equals("[]")) {
									ToastUtil.toastShow(getActivity(),
											"没有更多数据了");
								}
								mBusinessList.onLoadComplete();
							}
							mAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					case Constant.NET_DATA_FAIL:
						ToastUtil.toastShow(getActivity(), ((DataError) msg.obj).getErrorMessage());
						break;
					default:
						mBusinessList.onRefreshComplete();
						mIsRefreshing = false;
						break;
				}
			}
		}, params, NetworkUtil.GET_NEW_B_TASK, false, 1);
	}

	/**
	 * 下拉加载
	 * @param refreshView
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		if(!mIsRefreshing){
			mIsRefreshing = true;
			getDate(true);//刷新数据
		}else{
			mBusinessList.onRefreshComplete();
		}
	}

	/**
	 * 上啦刷新
	 * @param refreshView
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getDate(false);
		} else {
			mBusinessList.onRefreshComplete();
		}
	}
	/**
	 * 根据城市iD 访问数据
	 *
	 * @param
	 */
	private void getBusinessList() {
		mCurcor = 0;
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));
		params.put("taskType", "1");//1代表违章服务
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:

						mIsRefreshing = false;
						mBusinessList.onRefreshComplete();
						mBusinessList.onLoadMore();
						mNetworkRequest.setVisibility(View.GONE);
						mData.clear();
						try {
							JSONObject jo = new JSONObject(msg.obj.toString());
							List<Task> mRefreshData = Task.parseList(jo.getJSONArray("tasks").toString());
							if(mRefreshData.size()==0){
								mBusinessNoOrder.setVisibility(View.VISIBLE);
							}

							mData.addAll(mRefreshData);
							mCurcor = mRefreshData.size();
							mAdapter.notifyDataSetChanged();

							mServiceLoc = jo.getString("loca");
							mProvinceId = String.valueOf(jo.getInt("province"));
							mCityId = String.valueOf(jo.getInt("city"));
							mDistrictId =String.valueOf(jo.getInt("district"));
							callBackValue.SendMessageValue(mServiceLoc,mProvinceId,mCityId,mDistrictId);
						} catch (JSONException e) {
							e.printStackTrace();
							callBackValue.SendMessageValue(mServiceLoc,mProvinceId,mCityId,mDistrictId);
						}
						break;
					case Constant.NET_DATA_FAIL:
						ToastUtil.toastShow(getActivity(), ((DataError) msg.obj).getErrorMessage());
						mNetworkRequest.setVisibility(View.GONE);
						mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
						break;
					default:
						mNetworkRequest.setVisibility(View.GONE);
						mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
						break;
				}
			}
		}, params, NetworkUtil.GET_NEW_B_TASK, false, 1);
	}

	//定义一个回调接口
	public interface CallBackValue{
		public void SendMessageValue(String mServiceLoc,String mProvinceId,String mCityId,String mDistrictId);
	}
}
