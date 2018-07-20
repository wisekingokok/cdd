package com.chewuwuyou.app.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ActivityAdapter;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.bean.YueItem;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.callback.PullFragmentCallBack;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.ui.YueDetailActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;

/**
 * 活动
 * @author 向
 *
 */
public class ActivityFragment extends BaseFragment implements FragmentCallBackBuilder, PullFragmentCallBack {
	private View mContentView;
	private MyListView listView;
	private ImageView nullCon;
	private TextView nullConTV;
	private FragmentCallBack callback;
	private PersonHome mPersonHome;

	private ActivityAdapter adapter;
	private int mCurcor = 0;// 当前有几条
	private List<YueItem> mData = new ArrayList<YueItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_invitation, null);
		initView();
		initData();
		initEvent();

		return mContentView;
	}

	@Override
	protected void initView() {
		listView = (MyListView) mContentView.findViewById(R.id.listView);
		nullCon = (ImageView) mContentView.findViewById(R.id.nullCon);
		nullConTV = (TextView) mContentView.findViewById(R.id.nullConTV);
	}

	@Override
	protected void initData() {
		adapter = new ActivityAdapter(getActivity(), mData);
		listView.setAdapter(adapter);
	}

	@Override
	protected void initEvent() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				Intent intent = new Intent(getActivity(), YueDetailActivity.class);
				intent.putExtra("id", mData.get(arg2).getId());
				intent.putExtra("ziji", mData.get(arg2).getZiji());
				startActivity(intent);
			}
		});
	}

	private void getAllYue(final boolean refresh) {
		if (mPersonHome == null) {
			if (callback != null)
				callback.callback(2, 1);
			return;
		}
		if (refresh) {
			mCurcor = 0;
		}
		AjaxParams params = new AjaxParams();
		if (!String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))) {// 判断不是本人,则加载Id
			nullConTV.setVisibility(View.GONE);
			nullCon.setVisibility(View.VISIBLE);
			params.put("otherId", mPersonHome.getUserId()+"");
		} else {
			nullConTV.setVisibility(View.VISIBLE);
			nullCon.setVisibility(View.GONE);
		}
		params.put("start", String.valueOf(mCurcor));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (callback != null)
					callback.callback(2, 1);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					List<YueItem> response = null;
					try {
						JSONObject data = new JSONObject(msg.obj.toString());
						response = YueItem.parseList(data.getString("yue").toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (response != null) {
						if (refresh)
							mData.clear();
						mData.addAll(response);
						adapter.setData(mData);
						mCurcor = mData.size();
						if (mData.size() <= 0) {
							if (!String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))) {
								nullConTV.setVisibility(View.GONE);
								nullCon.setVisibility(View.VISIBLE);
							} else {
								nullConTV.setVisibility(View.VISIBLE);
								nullCon.setVisibility(View.GONE);
								nullConTV.setText("O(^_^)O空空如也，请请勤劳发帖~");
							}
						} else {
							nullCon.setVisibility(View.GONE);
							nullConTV.setVisibility(View.GONE);
						}
					}
					break;
				}
			}
		}, params, NetworkUtil.GET_MY_YUE, false, 1);
	}

	@Override
	public void setFragmentCallBack(FragmentCallBack callback) {
		this.callback = callback;
	}

	public void setPersonHome(PersonHome personHome) {
		this.mPersonHome = personHome;
		pullDown();
	}

	@Override
	public void pullUp() {
		getAllYue(false);
	}

	@Override
	public void pullDown() {
		getAllYue(true);
	}

}
