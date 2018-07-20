package com.chewuwuyou.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ServiceViewPagerAdapter;
import com.chewuwuyou.app.bean.Banner;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.ui.AccountBookActivity;
import com.chewuwuyou.app.ui.AddVehicleActivity;
import com.chewuwuyou.app.ui.BusinessHallActivity;
import com.chewuwuyou.app.ui.CarAssistanActivtiyEr;
import com.chewuwuyou.app.ui.ChooseCarBrandActivity;
import com.chewuwuyou.app.ui.InsureComputeAcitivity;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.ProvinceAgencyManagerActivity;
import com.chewuwuyou.app.ui.ServiceTypeActivity;
import com.chewuwuyou.app.ui.SystemRemindActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.AutoTextView;
import com.chewuwuyou.rong.utils.RongMsgType;
import com.viewpagerindicator.CirclePageIndicator;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import de.greenrobot.event.EventBus;

/**
 * Created by yuyong on 16-3-28. 服务
 */
public class ServiceFragment extends BaseFragment implements OnClickListener {

    private View mContentView;
    private RelativeLayout mIllegalServiceRL;// 违章服务
    private RelativeLayout mVehicleServiceRL;// 车辆服务
    private RelativeLayout mlicenseServiceRL;// 驾证服务
    private RelativeLayout mServiceHallRL;// 业务大厅
    private RelativeLayout mBusStatisticsRL;// 省代统计 
    private RelativeLayout mCityStatisticsRL;// 市代理统计
    private RelativeLayout mDistrictStatisticsRL;// 区代管理
    private LinearLayout mDrivingAssistantIV;// 行车助手
    private LinearLayout mBaoxianAssistantIV;// 保险助手
    private LinearLayout mXinCheAssistantIV;// 新车查价
    private LinearLayout mLoveCarBookTV;// 爱车账本
    private AutoScrollViewPager mServiceViewPager;// 服务页viewPager循环
    private CirclePageIndicator mCirclePageIndicator;
    private TextView mTitel;// 标题
    private ImageButton mSubHeaderBarLeftIbtn;// 返回上一页
    private RelativeLayout mTitleHeight;
    private List<Banner> mBanners;// 运营位集合
    private AutoTextView mAutoTextView;
    private List<String> mAutoStrs = new ArrayList<String>();
    private static int mTixingCount = 0;// 自定义信息条数
    private String content = null;
    private int mErrorCode;
    private FrameLayout frameLayout;
    private boolean stopAuto = false;//结束线程标志

    final Handler handler = new Handler();
    private StringBuilder mNotRemindSB = new StringBuilder("");
    private View mDotView;//业务大厅来订单提醒

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.service_fragment, null);

        initView();// 初始化控件
        initData();// 逻辑处理
        initEvent();// 事件的点击
        return mContentView;
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        mTitleHeight = (RelativeLayout) mContentView.findViewById(R.id.title_height);
        mIllegalServiceRL = (RelativeLayout) mContentView.findViewById(R.id.main_illegal_service_rl);
        mVehicleServiceRL = (RelativeLayout) mContentView.findViewById(R.id.main_vehicle_service_rl);
        mlicenseServiceRL = (RelativeLayout) mContentView.findViewById(R.id.main_license_service_rl);
        mServiceHallRL = (RelativeLayout) mContentView.findViewById(R.id.main_service_hall_rl);
        mBusStatisticsRL = (RelativeLayout) mContentView.findViewById(R.id.main_busi_statisi_rl_or);
        mDrivingAssistantIV = (LinearLayout) mContentView.findViewById(R.id.driving_assistant_iv);
        mBaoxianAssistantIV = (LinearLayout) mContentView.findViewById(R.id.baoxianzhushou_iv);
        mXinCheAssistantIV = (LinearLayout) mContentView.findViewById(R.id.xinchechajia_iv);// 加油助手
        mLoveCarBookTV = (LinearLayout) mContentView.findViewById(R.id.love_car_book_iv);// 爱车账本
        mServiceViewPager = (AutoScrollViewPager) mContentView.findViewById(R.id.serviceviewpager);// viewPager初始化
        mCirclePageIndicator = (CirclePageIndicator) mContentView.findViewById(R.id.circle_page_indicator);// 画圆点
        mTitel = (TextView) mContentView.findViewById(R.id.sub_header_bar_tv);
        mTitleHeight = (RelativeLayout) mContentView.findViewById(R.id.title_height);
        mTitel.setText("服务");
        mSubHeaderBarLeftIbtn = (ImageButton) mContentView.findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页
        mCityStatisticsRL = (RelativeLayout) mContentView.findViewById(R.id.main_city_statisi_rl);
        mSubHeaderBarLeftIbtn.setVisibility(View.GONE);
        mAutoTextView = (AutoTextView) mContentView.findViewById(R.id.switcher_tixing);
        mDistrictStatisticsRL = (RelativeLayout) mContentView.findViewById(R.id.main_district_statisi_rl);
        frameLayout = (FrameLayout) mContentView.findViewById(R.id.frameLayout);
        mDotView=mContentView.findViewById(R.id.dot_remind_view);
        // 动态高度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int height = (31 * screenW) / 75;
        LayoutParams params = (LayoutParams) frameLayout.getLayoutParams();
        params.height = height;
        frameLayout.setLayoutParams(params);
        EventBus.getDefault().register(this);//注册event事件

        if(CacheTools.getUserData("isRead")!=null){
            if(CacheTools.getUserData("isRead").equals("0")){
                mDotView.setVisibility(View.VISIBLE);
            }else{
                mDotView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initData() {
        MyLog.i("YUY", "当前类名----------------" + Tools.getNowClass(getActivity()));
        isTitle(mTitleHeight);// 根据不同手机判断
        mBanners = new ArrayList<Banner>();
        if (CacheTools.getUserData("role") == null) {
            MyLog.i("YUY", "未登陆");
            return;
        }
        String role = CacheTools.getUserData("role");
        if (role.contains("2") || role.contains("3")) {// A类商家或B类商家
            mServiceHallRL.setVisibility(View.VISIBLE);
        }
        if (CacheTools.getUserData("daiLitype") == null) {
            return;
        }
        if (CacheTools.getUserData("daiLitype").equals("1")) {// 省代
            mBusStatisticsRL.setVisibility(View.VISIBLE);
        }
        if (CacheTools.getUserData("daiLitype").equals("2")) {// 市代
            mCityStatisticsRL.setVisibility(View.VISIBLE);
        }
        if (CacheTools.getUserData("daiLitype").equals("3")) {// 区代
            mDistrictStatisticsRL.setVisibility(View.VISIBLE);
        }
        getBanner();
        mNotRemindSB.append("截止");
        mNotRemindSB.append(DateTimeUtil.getYesterDayDate());
        mNotRemindSB.append("暂无提醒");
        getRemind();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 在此处添加执行的代码
            mAutoTextView.next();
            mTixingCount++;
            if (mTixingCount >= Integer.MAX_VALUE) {
                mTixingCount = mAutoStrs.size();
            }
            mAutoTextView.setText(mAutoStrs.get(mTixingCount % mAutoStrs.size()));
            if (mAutoStrs.size() > 1 && !stopAuto) {
                handler.postDelayed(this, 5000);// 50是延时时长
            }
        }
    };

    @Override
    public void onDestroy() {
        stopAuto = true;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    public void onEventMainThread(io.rong.imlib.model.Message message) {//接收过来业务大厅订单消息显示小红点
        if(message.getTargetId().equals("4")){
            mDotView.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 获取提醒信息
     */
    private void getRemind() {
        if (CacheTools.getUserData("role") != null) {
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            try {
                                JSONObject jo = new JSONObject(msg.obj.toString());
                                content = jo.getString("content");
                                json2Bean(content);
                                if (mAutoStrs == null || mAutoStrs.size() == 0) {
                                    mAutoStrs.add(mNotRemindSB.toString());
                                    mAutoStrs.add(mNotRemindSB.toString());
                                }
                                mAutoTextView.setText(mAutoStrs.get(0));
                                // 启动计时器
                                handler.postDelayed(runnable, 3000);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Constant.NET_DATA_FAIL:
                            content = null;
                            mAutoStrs.clear();
                            if (((DataError) msg.obj).getErrorCode() == ErrorCodeUtil.Notification.NONE_REMIND) {
                                mAutoStrs.add(mNotRemindSB.toString());
                                mAutoStrs.add(mNotRemindSB.toString());
                            } else if (ErrorCodeUtil.Notification.NONE_NOTI == ((DataError) msg.obj).getErrorCode()) {
                                mAutoStrs.add("还未设置车辆");
                                mAutoStrs.add("还未设置车辆");
                            } else {
                                mAutoStrs.add("暂无提醒");
                                mAutoStrs.add("暂无提醒");
                            }
                            mErrorCode = ((DataError) msg.obj).getErrorCode();
                            mAutoTextView.setText(mAutoStrs.get(0));
                            // 启动计时器
                            handler.postDelayed(runnable, 3000);
                            break;
                    }
                }
            }, null, NetworkUtil.GET_REMIND_URL, false, 1);

        } else {
            Intent sYsIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(sYsIntent);
        }
    }

    private void json2Bean(String content) throws JSONException {
        mAutoStrs.clear();
        JSONArray illegal = new JSONArray();
        if (content != null && !"".equals(content)) {
            JSONObject theJsonObject = new JSONObject(content);
            JSONObject cars = theJsonObject.getJSONObject("cars");
            if (cars == null) {
                return;
            }
            Iterator<String> keyIter = cars.keys();
            while (keyIter.hasNext()) {
                String key = keyIter.next();// 车牌号
                JSONObject car = cars.getJSONObject(key);
                String carCheck = car.getString("carCheck");
                try {
                    illegal = car.getJSONArray("illegal");
                    mAutoStrs.add(key + "有" + illegal.length() + "次违章");
                    mAutoStrs.add(key + "年检日期为" + carCheck);
                } catch (Exception e) {
                    mAutoStrs.add(key + "年检日期为" + carCheck);
                }

            }
        }
    }
    public void onEventMainThread(EventBusAdapter busAdapter) {//接收过来业务大厅订单消息显示小红点
        if(busAdapter.getOrderRemind().equals("0")){
            CacheTools.setUserData("isRead","1");
            mDotView.setVisibility(View.GONE);
        }
    }


    /**
     * 获取广告运营资料
     */
    private void getBanner() {
        AjaxParams params = new AjaxParams();
        params.put("type", Constant.BANNER_TYPE.HOME_PAGE_BANNER);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "banner = " + msg.obj.toString());
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mBanners = Banner.parseList(jo.getJSONArray("banners").toString());

                            if (mBanners.size() == 0) {
                                Banner banner = new Banner();
                                banner.setImageUrl("");
                                banner.setTiaoType(1);
                                banner.setTiaoUrl("http://www.cddang.com");
                                mBanners.add(banner);
                            }
                            for (int i = 0; i < mBanners.size(); i++) {
                                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                                        + ServerUtils.getServerIP(mBanners.get(i).getImageUrl()));
                            }
                            mServiceViewPager.setAdapter(new ServiceViewPagerAdapter(getActivity(), mBanners));
                            mServiceViewPager.startAutoScroll();
                            mServiceViewPager.setInterval(5000);
                            mCirclePageIndicator.setViewPager(mServiceViewPager);
                            mServiceViewPager
                                    .setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mBanners.size());
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

    @Override
    protected void initEvent() {
        mIllegalServiceRL.setOnClickListener(this);
        mVehicleServiceRL.setOnClickListener(this);
        mlicenseServiceRL.setOnClickListener(this);
        mServiceHallRL.setOnClickListener(this);
        mDrivingAssistantIV.setOnClickListener(this);
        mBaoxianAssistantIV.setOnClickListener(this);
        mXinCheAssistantIV.setOnClickListener(this);
        mLoveCarBookTV.setOnClickListener(this);
        mBusStatisticsRL.setOnClickListener(this);
        mCityStatisticsRL.setOnClickListener(this);
        mAutoTextView.setOnClickListener(this);
        mDistrictStatisticsRL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_illegal_service_rl:
                StatService.onEvent(this.getActivity(), "ClickMainActWzservice", "点击主界面违章服务");
                redicToService(Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
                break;
            case R.id.main_vehicle_service_rl:
                StatService.onEvent(this.getActivity(), "ClickMainActCarservice", "点击主界面车辆服务");
                redicToService(Constant.SERVICE_TYPE.CAR_SERVICE);
                break;
            case R.id.main_license_service_rl:
                StatService.onEvent(this.getActivity(), "ClickMainActJzservice", "点击主界面驾证服务");
                redicToService(Constant.SERVICE_TYPE.LICENCE_SERVICE);
                break;
            case R.id.main_service_hall_rl:

                StatService.onEvent(this.getActivity(), "clickMainMessageInfoActivity", "点击业务大厅");
                if (CacheTools.getUserData("role") != null
                        && (CacheTools.getUserData("role").contains("2") || CacheTools.getUserData("role").contains("3"))) {
                    EventBusAdapter bsAdapter = new EventBusAdapter();
                    bsAdapter.setOrderRemind("0");
                    EventBus.getDefault().post(bsAdapter);
                    Intent msgInfoIntent = new Intent(this.getActivity(), BusinessHallActivity.class);
                    startActivity(msgInfoIntent);
                } else {
                    Intent lIntent = new Intent(this.getActivity(), LoginActivity.class);
                    startActivity(lIntent);
                    this.getActivity().finish();
                }
                break;
            case R.id.driving_assistant_iv:
                StatService.onEvent(this.getActivity(), "CarAssistantActivity", "行车助手");
                Intent intent = new Intent(this.getActivity(), CarAssistanActivtiyEr.class);
                startActivity(intent);

                break;
            case R.id.baoxianzhushou_iv:
                StatService.onEvent(this.getActivity(), "InsureComputeAcitivity", "保险助手");
                Intent pIntent = new Intent(this.getActivity(), InsureComputeAcitivity.class);
                startActivity(pIntent);
                break;
            case R.id.xinchechajia_iv:
                StatService.onEvent(this.getActivity(), "NewCarQueryPriceActivity", "新车查价");
            /*
             * Intent jyIntent = new Intent(this.getActivity(),
			 * NewCarQueryPriceActivity.class);
			 */
                Intent jyIntent = new Intent(this.getActivity(), ChooseCarBrandActivity.class);
                jyIntent.putExtra("type", 1);// 代表从新车查价进去
                startActivity(jyIntent);
                break;
            case R.id.love_car_book_iv:
                StatService.onEvent(this.getActivity(), "RecordActivity", "爱车账本");
                // Intent recIntent = new Intent(this.getActivity(),
                // RecordActivity.class);
                // StatService.onEvent(this.getActivity(), "AccountBookActivity",
                // "爱车账本");
                Intent recIntent = new Intent(this.getActivity(), AccountBookActivity.class);
                startActivity(recIntent);

                break;
            case R.id.main_busi_statisi_rl_or:
                StatService.onEvent(this.getActivity(), "ProvinceAgencyManagerActivity", "省代统计");
                Intent busiTjcIntent = new Intent(this.getActivity(), ProvinceAgencyManagerActivity.class);
                startActivity(busiTjcIntent);
                break;
            case R.id.main_city_statisi_rl:
                StatService.onEvent(this.getActivity(), "ProvinceAgencyManagerActivity", "市代统计");
                Intent cityTjcIntent = new Intent(this.getActivity(), ProvinceAgencyManagerActivity.class);
                startActivity(cityTjcIntent);
                break;
            case R.id.main_district_statisi_rl:
                StatService.onEvent(this.getActivity(), "ProvinceAgencyManagerActivity", "区代统计");
                Intent discrictTjcIntent = new Intent(this.getActivity(), ProvinceAgencyManagerActivity.class);
                startActivity(discrictTjcIntent);

                break;
            case R.id.switcher_tixing:
                if (CacheTools.getUserData("role") != null) {
                    if (mErrorCode == ErrorCodeUtil.Notification.NONE_NOTI) {// 判断是否有车辆
                        Intent cityTIntent = new Intent(getActivity(), AddVehicleActivity.class);
                        startActivity(cityTIntent);
                    } else if (mErrorCode == ErrorCodeUtil.Notification.NONE_REMIND) {// 判断是否有提醒
                        Intent sYsIntent = new Intent(getActivity(), SystemRemindActivity.class);
                        sYsIntent.putExtra("content", content);
                        startActivity(sYsIntent);
                    } else {
                        Intent sYsIntent = new Intent(getActivity(), SystemRemindActivity.class);
                        sYsIntent.putExtra("content", content);
                        startActivity(sYsIntent);
                    }
                } else {
                    Intent serintent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(serintent);
                    getActivity().finish();
                }

                break;
            default:
                break;
        }
    }


    /**
     * 跳转服务
     */
    private void redicToService(int serviceType) {
        Intent intent = null;
        if (CacheTools.getUserData("role") != null) {
            intent = new Intent(getActivity(), ServiceTypeActivity.class);
            intent.putExtra("serviceType", serviceType);
            startActivity(intent);
        } else {
            intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

    }

}
