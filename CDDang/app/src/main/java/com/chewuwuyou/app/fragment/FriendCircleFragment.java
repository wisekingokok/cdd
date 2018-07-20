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
import com.chewuwuyou.app.adapter.FriendCircleAdapter;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.callback.PullFragmentCallBack;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.ui.QuanDetailActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;

/**
 * 圈子
 * 
 * @author 向
 *
 */
public class FriendCircleFragment extends BaseFragment implements FragmentCallBackBuilder, PullFragmentCallBack {
	private View mContentView;
	private MyListView listView;
	private ImageView nullCon;
	private TextView nullConTV;
	private FriendCircleAdapter adapter;
	private FragmentCallBack callback;
	private PersonHome mPersonHome;
	private int mCurcor = 0;// 当前有几条
	private List<QuanItem> mData = new ArrayList<QuanItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

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
		adapter = new FriendCircleAdapter(getActivity(), mData);
		listView.setAdapter(adapter);
	}

	@Override
	protected void initEvent() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(getActivity(), QuanDetailActivity.class);
				Bundle b = new Bundle();
				b.putSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN, mData.get(arg2));
				// intent.putExtra("id", mData.get(arg2 - 1).getId());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

	public void setPersonHome(PersonHome personHome) {
		this.mPersonHome = personHome;
		pullDown();
	}

	// 获得所有圈文
	private void getAllQuan(final boolean refresh) {
		if (mPersonHome == null) {
			if (callback != null)
				callback.callback(1, 1);
			return;
		}
		if (refresh)
			mCurcor = 0;
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));
		if (!String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))) {// 判断不是本人,则加载Id
			params.put("otherId", String.valueOf(mPersonHome.getUserId()));
			nullConTV.setVisibility(View.GONE);
			nullCon.setVisibility(View.VISIBLE);
		} else {
			nullConTV.setVisibility(View.VISIBLE);
			nullCon.setVisibility(View.GONE);
		}
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (callback != null)
					callback.callback(1, 1);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					try {
						JSONObject data = new JSONObject(msg.obj.toString());
						List<QuanItem> response = QuanItem.parseList(data.getString("quan").toString());
						if (response != null) {
							if (refresh) {
								mData.clear();
							}
							mData.addAll(response);
							adapter.setData(mData);
							adapter.setOtherId(mPersonHome.getUserId());
							mCurcor = mData.size();
							if (mData.size() <= 0) {
								if (!String.valueOf(mPersonHome.getUserId()).equals(CacheTools.getUserData("userId"))) {
									nullConTV.setVisibility(View.GONE);
									nullCon.setVisibility(View.VISIBLE);
								} else {
									nullConTV.setVisibility(View.VISIBLE);
									nullCon.setVisibility(View.GONE);
									nullConTV.setText("从今天起，将你的心情分享给你的朋友~");
								}
							} else {
								nullCon.setVisibility(View.GONE);
								nullConTV.setVisibility(View.GONE);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				}
			}

		}, params, NetworkUtil.GET_MY_QUAN, false, 1);
	}

	@Override
	public void setFragmentCallBack(FragmentCallBack callback) {
		this.callback = callback;
	}

	@Override
	public void pullUp() {
		getAllQuan(false);
	}

	@Override
	public void pullDown() {// 刷新
		getAllQuan(true);
	}

}
