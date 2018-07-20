package com.chewuwuyou.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.RimCoverFlowAdapter;
import com.chewuwuyou.app.adapter.ServiceViewPagerAdapter;
import com.chewuwuyou.app.bean.BanKuaiItem;
import com.chewuwuyou.app.bean.Banner;
import com.chewuwuyou.app.bean.Driving;
import com.chewuwuyou.app.extras.FancyCoverFlow;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.ui.HotTieClassificationAdapter;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.NearByCarActivity;
import com.chewuwuyou.app.ui.QuanActivity;
import com.chewuwuyou.app.ui.SameBrandFriendActivity;
import com.chewuwuyou.app.ui.YueActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by root on 16-3-28.
 */
public class CyqFragment extends BaseFragment implements OnClickListener {

	private View mContentView;
	private TextView mTitleTV;// 标题
	private ImageButton mSubHeaderBarLeftIB;// 返回上一页
	private RelativeLayout mTitleHeight;
	private AutoScrollViewPager mAutoScrollViewPager;// 显示广告图
	private CirclePageIndicator mCirclePageIndicator;// 画圆点
	private FancyCoverFlow mFancyCoverFlow;
	private MyListView mFriendListview;
	private List<Banner> mBanners;// 运营位集合
	private List<Driving> mDrivingsList;
	private RimCoverFlowAdapter mRimCoverFlowAdapter;
	private List<BanKuaiItem> mData=new ArrayList<BanKuaiItem>();// 图文集合
	private HotTieClassificationAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.friend_fragment_ac, null);
		initView();
		initData();
		initEvent();

		return mContentView;
	}

	@Override
	protected void initView() {
		mTitleHeight = (RelativeLayout) mContentView
				.findViewById(R.id.title_height);
		mTitleTV = (TextView) mContentView.findViewById(R.id.sub_header_bar_tv);
		mTitleTV.setText("车圈");
		mSubHeaderBarLeftIB = (ImageButton) mContentView
				.findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页
		mSubHeaderBarLeftIB.setVisibility(View.GONE);
		mAutoScrollViewPager = (AutoScrollViewPager) mContentView
				.findViewById(R.id.serviceviewpager);// 循环轮播
		mFancyCoverFlow = (FancyCoverFlow) mContentView
				.findViewById(R.id.driving_option);// 手动滑动
		mFriendListview = (MyListView) mContentView
				.findViewById(R.id.friend_listview);// 手动滑动
		mCirclePageIndicator = (CirclePageIndicator) mContentView
				.findViewById(R.id.circle_page_indicator);// 画圆点

		addBanKuan();

	}

	/**
	 * 添加所有版块
	 */
	@SuppressWarnings("deprecation")
	public void addBanKuan() {
		mDrivingsList = new ArrayList<Driving>();
		Driving mDrivingyi = new Driving();
		mDrivingyi.setDrivingId(1);
		mDrivingyi.setDrivingName("好友车圈");
		mDrivingsList.add(mDrivingyi);

		Driving mDrivinger = new Driving();
		mDrivinger.setDrivingId(2);
		mDrivinger.setDrivingName("车友活动");
		mDrivingsList.add(mDrivinger);

		// Driving mDrivingsan = new Driving();
		// mDrivingsan.setDrivingId(3);
		// mDrivingsan.setDrivingName("车友吐槽");
		// mDrivingsList.add(mDrivingsan);

		Driving mDrivingsi = new Driving();
		mDrivingsi.setDrivingId(4);
		mDrivingsi.setDrivingName("附近车友");
		mDrivingsList.add(mDrivingsi);

		Driving mDrivingwu = new Driving();
		mDrivingwu.setDrivingId(5);
		mDrivingwu.setDrivingName("同系车友");
		mDrivingsList.add(mDrivingwu);

		mRimCoverFlowAdapter = new RimCoverFlowAdapter(getActivity(),
				mDrivingsList);
		mFancyCoverFlow.setAdapter(mRimCoverFlowAdapter);
		mFancyCoverFlow.setCallbackDuringFling(false);
		mFancyCoverFlow.setUnselectedAlpha(0.5f);// 通明度
		mFancyCoverFlow.setUnselectedSaturation(0.5f);// 设置选中的饱和度
		mFancyCoverFlow.setUnselectedScale(0.8f);// 设置选中的规模
		mFancyCoverFlow.setSpacing(0);// 设置间距
		mFancyCoverFlow.setMaxRotation(0);// 设置最大旋转
		mFancyCoverFlow.setScaleDownGravity(0.5f);
		mFancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
		int arg = Integer.MAX_VALUE / 2 - 3;
		mFancyCoverFlow.setSelection(arg);
	}

	@Override
	protected void initData() {
		isTitle(mTitleHeight);// 根据不同手机判断
		mData = new ArrayList<BanKuaiItem>();

		getBanner();
		getAllHotTieClassification();
	}

	@Override
	protected void initEvent() {
		/**
		 * FancyCoverFlow 滚动监听事件
		 */
		mFancyCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		/**
		 * FancyCoverFlow 点击事件
		 */
		mFancyCoverFlow
				.setOnItemClickListener(new FancyCoverFlow.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						int is = position % mDrivingsList.size();
						Intent intent;
						switch (is) {
						case 0:// 好友车圈
							if (CacheTools.getUserData("userId") != null
									&& Integer.parseInt(CacheTools
											.getUserData("userId")) > 0) {
								intent = new Intent(getActivity(),
										QuanActivity.class);
								startActivity(intent);
							} else {
								// 跳到登录界面
								intent = new Intent(getActivity(),
										LoginActivity.class);
								startActivity(intent);
								getActivity().finish();
							}
							break;
						case 1:// 车友活动
							intent = new Intent(getActivity(),
									YueActivity.class);
							getActivity().startActivity(intent);
							break;
						case 2:// 附近车
							intent = new Intent(getActivity(),
									NearByCarActivity.class);
							getActivity().startActivity(intent);
							break;
						case 3:// 同系车
							intent = new Intent(getActivity(),
									SameBrandFriendActivity.class);
							getActivity().startActivity(intent);
							break;
						default:
							break;
						}
					}
				});

	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * 访问网络请求轮播图
	 */
	private void getBanner() {
		mBanners = new ArrayList<Banner>();
		AjaxParams params = new AjaxParams();
		params.put("type", Constant.BANNER_TYPE.CARQUAN_BANNER);
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mBanners = Banner.parseList(jo.getJSONArray("banners")
								.toString());
						if (mBanners.size() == 0) {
							Banner banner = new Banner();
							banner.setImageUrl("");
							banner.setTiaoType(1);
							banner.setTiaoUrl("http://www.cddang.com");
							mBanners.add(banner);
						}
						mAutoScrollViewPager.setAdapter(new ServiceViewPagerAdapter(
								getActivity(), mBanners));// banner数据库绑定
						mAutoScrollViewPager.startAutoScroll();
						mAutoScrollViewPager.setInterval(2000);
						mCirclePageIndicator.setViewPager(mAutoScrollViewPager);
						mAutoScrollViewPager.setCurrentItem(Integer.MAX_VALUE / 2
								- Integer.MAX_VALUE / 2 % mBanners.size());
					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.GET_BANNER, false, 1);
	
	}

	/**
	 * 屏幕的密度
	 * 
	 * @param dipValue
	 * @return
	 */
	public int dp2px(float dipValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	private void getAllHotTieClassification() {

		AjaxParams params = new AjaxParams();
		params.put("reuse", String.valueOf(1));

		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mData = BanKuaiItem.parseList(msg.obj.toString());
					mAdapter = new HotTieClassificationAdapter(getActivity(),
							mData);
					mFriendListview.setAdapter(mAdapter);
					break;
				case Constant.NET_DATA_FAIL:
					break;
				default:
					break;
				}

			}
		}, params, NetworkUtil.GET_ALL_BANKUAI, false, 1);

	}
}
