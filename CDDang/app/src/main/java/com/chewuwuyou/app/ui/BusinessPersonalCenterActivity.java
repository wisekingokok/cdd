package com.chewuwuyou.app.ui;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.barcode.view.PullableScrollView;
import com.barcode.view.PullableScrollView.OnScrollChangedListener;
import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.BusinessServicePro;
import com.chewuwuyou.app.bean.BussinessDetailsBook;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Service;
import com.chewuwuyou.app.bean.TrafficBusinessListBook;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import de.greenrobot.event.EventBus;

/**
 * @describe:商家个人中心
 * @author:liuchun
 */

public class BusinessPersonalCenterActivity extends BaseFragmentActivity
        implements OnClickListener {

    private TextView mBusinessProvince, mBusinessBranch;// 标题,地址,等级分

    private TextView mBusinessEnterprise;
    private LinearLayout mBusinessOrder, mBusinessDetails, mLinearBao;// 下单，详情,商家地址详情
    // private BusinessContactService mBusinessContactService;// 客服对象
    private List<Service> mBusinesscontact;// 客服集合
    private CommonAdapter<Service> mServiceAdapter;// 客服适配器
    private ImageView mBusinessPersonalCollection, mBusinessPortrait,
            mBusinessCollectionStore;// 收藏
    private RatingBar mBusinierssGrade;// 商家等级
    // private BussinessDetailsBook mTrafficBroker;// 经纪人实体
    private TextView mShopNameTV;// 店铺名称
    private int HEIGHT = 0;
    // private String mProjectNum;// 接收传递过来的Id和服务类型
    private int position;
    public static Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    // private int mServiceType;
    private int mCollection;

    private List<BusinessServicePro> mIllegalList, mVehicleList, mDrivingList;// 服务项目集合
    private TextView mOnIllegal, OnVehicle, OnAmount;// 违章代办，车辆服务，驾证服务
    private RelativeLayout mRelat;
    private PullableScrollView ablesScrolView;
    private FrameLayout mPortraitBdGradient;
    private ImageButton mSubBusiness;
    private List<BussinessDetailsBook> mBussinessDetailsBooks = new ArrayList<BussinessDetailsBook>();
    private int isCollect;// 判断是否是从收藏进入的;
    public static String address;
    // private String ManagId;
    private LinearLayout mBusinessAddress;// 商家地址\
    private TextView mBusinessTitle;

    private LinearLayout mNetworkRequest,mNetworkAbnormalLayout,mBusinessDetailsGogn;
    private ImageButton mHeaderBarLeftIbtn;
    private TextView mHeaderBarTV;
    private TextView mNetworkAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mServiceType = getIntent().getIntExtra("serviceType", 0);
        setContentView(R.layout.business_personal_center);

        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initView();// 初始化控件
        initData();// 逻辑处理
        initEvent();// 事件监听

        setFragmentIndicator();
    }

    /**
     * 初始化控件
     */
    public void initView() {

        mIllegalList = new ArrayList<BusinessServicePro>();
        mVehicleList = new ArrayList<BusinessServicePro>();
        mDrivingList = new ArrayList<BusinessServicePro>();

        // mProjectNum = getIntent().getStringExtra("projectNum");

        mHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mHeaderBarTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mHeaderBarTV.setText("商家详情");

        mNetworkAgain = (TextView) findViewById(R.id.network_again);
        mBusinessDetailsGogn = (LinearLayout) findViewById(R.id.business_details_gogn);
        mNetworkRequest = (LinearLayout) findViewById(R.id.network_request);
        mNetworkAbnormalLayout = (LinearLayout) findViewById(R.id.network_abnormal_layout);

        mBusinessBranch = (TextView) findViewById(R.id.business_branch);// 等级分
        mBusinessProvince = (TextView) findViewById(R.id.business_province);// 地址、

        mBusinessTitle = (TextView) findViewById(R.id.business_title);// 地址、

        mBusinierssGrade = (RatingBar) findViewById(R.id.businierss_grade);// 商家等级

        mBusinessPortrait = (ImageView) findViewById(R.id.business_portrait);// 头像
        mBusinessPersonalCollection = (ImageView) findViewById(R.id.business_personal_collection);// 收藏
        mBusinessCollectionStore = (ImageView) findViewById(R.id.business_collection_store);// 门店

        mBusinessAddress = (LinearLayout) findViewById(R.id.business_address);
        mBusinessOrder = (LinearLayout) findViewById(R.id.business_order_onclick);
        mBusinessDetails = (LinearLayout) findViewById(R.id.business_details_onclick);
        mShopNameTV = (TextView) findViewById(R.id.shop_name_tv);

        mOnIllegal = (TextView) findViewById(R.id.service_on_illegal);
        OnVehicle = (TextView) findViewById(R.id.service_on_vehicle);
        OnAmount = (TextView) findViewById(R.id.service_on_amount);
        mRelat = (RelativeLayout) findViewById(R.id.business_title_ba);
        ablesScrolView = (PullableScrollView) findViewById(R.id.business_pullable);
        mPortraitBdGradient = (FrameLayout) findViewById(R.id.portrait_background_gradient);

        mBusinessEnterprise = (TextView) findViewById(R.id.business_enterprise);
        mSubBusiness = (ImageButton) findViewById(R.id.sub_business);
        mLinearBao = (LinearLayout) findViewById(R.id.linear_bao);
        isCollect = getIntent().getIntExtra("isCollect", 0);




        // 动态高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int height = (31 * screenW) / 75;
        LayoutParams params = (LayoutParams) mPortraitBdGradient
                .getLayoutParams();
        params.height = height;
        mPortraitBdGradient.setLayoutParams(params);
    }

    /**
     * 好评 中评 差评
     *
     * @param
     */
    private void setFragmentIndicator() {
        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.highpraise); // 违章
        mFragments[1] = fragmentManager.findFragmentById(R.id.evaluation); // 车辆
        mFragments[2] = fragmentManager.findFragmentById(R.id.badreview); // 驾证

        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示
    }

    /**
     * 点击事件
     */
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_personal_collection:// 收藏
                StatService.onEvent(BusinessPersonalCenterActivity.this,
                        "ClickCattleAgentsListAttTV", "点击车务经纪人关注/已关注按钮");

                if (String.valueOf(mBussinessDetailsBooks.get(0).getUserId())
                        .equals(CacheTools.getUserData("userId"))) {
                    ToastUtil.toastShow(BusinessPersonalCenterActivity.this,
                            "不能收藏自己");
                    return;
                }

                if (mCollection == 1) {// 判断是否收藏 1 收藏| 0 未收藏
                    networkNotCollection();
                } else {
                    networkCollection();
                }
                break;
            case R.id.network_again:// 点击重新加载
                mNetworkRequest.setVisibility(View.VISIBLE);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(getIntent().getStringExtra("businessId"))){
                    getRefreshList(getIntent().getStringExtra("businessId"));// 访问网络获取详情信息
                }
                break;
            case R.id.sub_header_bar_left_ibtn:// 返回上一页
                finishActivity();
                break;
            case R.id.business_order_onclick:
                if (mBussinessDetailsBooks != null) {
                    if (!TextUtils.isEmpty(mBussinessDetailsBooks.get(0)
                            .getMobile())) {
                        DialogUtil.call(mBussinessDetailsBooks.get(0).getMobile(),
                                BusinessPersonalCenterActivity.this);
                    }else{
                        ToastUtil.toastShow(BusinessPersonalCenterActivity.this,"该商家暂无电话");
                    }
                }
                break;
            case R.id.business_details_onclick:// 详情
                if (mBussinessDetailsBooks.size() != 0) {
                    if (mBussinessDetailsBooks.get(0).getInteraction() != null && mBussinessDetailsBooks.get(0).getInteraction().size() > 1) {
                        createWalletPayDialog(this);
                    } else {
                        if (String.valueOf(
                                mBussinessDetailsBooks.get(0).getUserId()).equals(
                                CacheTools.getUserData("userId"))) {
                            ToastUtil.toastShow(
                                    BusinessPersonalCenterActivity.this,
                                    "不能跟自己聊天哦！");
                            return;
                        }
                        // Intent intent = new Intent(
                        // BusinessPersonalCenterActivity.this,
                        // ChatActivity.class);
                        // intent.putExtra("to", mBussinessDetailsBooks.get(0)
                        // .getUserId() + "@iz232jtyxeoz");// 聊天人的唯一ID
                        // intent.putExtra("isOrderto", 1);// 标识进入订单可以显示聊天页面
                        // intent.putExtra("handlerId", ManagId);// 管理平台账号ID//
                        // 包含商家和客服
                        // intent.putExtra("isCollect",
                        // getIntent().getIntExtra("isCollect", 0));
                        // intent.putExtra("telephone",
                        // mBussinessDetailsBooks.get(0)
                        // .getMobile());
                        // intent.putExtra("serviceType", mServiceType);
                        // intent.putExtra("serviceProject", getIntent()
                        // .getStringExtra("serviceProject"));
                        // intent.putExtra("projectNum", mProjectNum);
                        // intent.putExtra("businessId", String
                        // .valueOf(mBussinessDetailsBooks.get(0).getUserId()));
                        // intent.putExtra("serviceType", mServiceType);
                        // Bundle bundle = new Bundle();
                        // bundle.putSerializable(Constant.TRAFFIC_SER,
                        // mBussinessDetailsBooks.get(0));
                        // intent.putExtras(bundle);
                        // startActivity(intent);

                        AppContext.createChat(BusinessPersonalCenterActivity.this,
                                String.valueOf(mBussinessDetailsBooks.get(0).getUserId()));
                    }
                }
                break;
            case R.id.service_on_illegal:// 违章
                fragmentSwitch(0);
                break;
            case R.id.service_on_vehicle:// 车辆
                fragmentSwitch(1);
                break;
            case R.id.service_on_amount:// 驾证
                fragmentSwitch(2);
                break;
            case R.id.sub_business:// 返回上一页
                finishActivity();
                break;
            case R.id.linear_bao:// 查看保险
                Insurance();
                break;
            case R.id.business_address:// // 商家地址详情
                if (isInstallByread("com.baidu.BaiduMap")) {
                    if (!TextUtils.isEmpty(mBussinessDetailsBooks.get(0).getAddress())) {
                        Intent intent = null;
                        try {
                            intent = Intent.getIntent("intent://map/direction?origin=latlng:" + CacheTools.getUserData("Lat") + "," + CacheTools.getUserData("Lng") + "|name:我的位置&destination=" + mBussinessDetailsBooks.get(0).getAddress() + "&mode=driving®ion=" + "" + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        this.startActivity(intent);
                    } else {
                        ToastUtil.toastShow(BusinessPersonalCenterActivity.this,
                                "暂无详细地址");
                    }
                } else {
                    ToastUtil.toastShow(BusinessPersonalCenterActivity.this,
                            "未安装百度地图应用");
                }
                break;
            default:
                break;
        }

    }

    /**
     * 判断是否安装百度地图
     *
     * @param packageName
     * @return
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 逻辑处理
     */
    public void initData() {
        if(!TextUtils.isEmpty(getIntent().getStringExtra("businessId"))){
            getRefreshList(getIntent().getStringExtra("businessId"));// 访问网络获取详情信息
        }
    }

    /**
     * 点击事件监听
     */
    public void initEvent() {
        mBusinessOrder.setOnClickListener(this);// 下单
        mBusinessDetails.setOnClickListener(this);// 详情
        mBusinessPersonalCollection.setOnClickListener(this);// 收藏
        mOnIllegal.setOnClickListener(this);// 违章代办
        OnVehicle.setOnClickListener(this);// 车辆服务
        OnAmount.setOnClickListener(this);// 驾证服务
        mSubBusiness.setOnClickListener(this);
        mLinearBao.setOnClickListener(this);
        mHeaderBarLeftIbtn.setOnClickListener(this);
        mNetworkAgain.setOnClickListener(this);
        mBusinessAddress.setOnClickListener(this);// 商家地址
        ablesScrolView
                .setOnScrollChangedListener(new OnScrollChangedListener() {

                    @Override
                    public void onScrollChanged(ScrollView who, int l, int t,
                                                int oldl, int oldt) {
                        mRelat.setBackgroundColor(getResources().getColor(
                                R.color.new_blue));// 设置标题背景颜色

                        if (mBusinessPortrait != null
                                && mPortraitBdGradient.getHeight() > 0) {

                            HEIGHT = mPortraitBdGradient.getHeight();

                            if (t < HEIGHT) {// =
                                int progress = (int) (new Float(t)
                                        / new Float(HEIGHT) * 100);// 255


                                // 底色的渐变
                                mRelat.getBackground().setAlpha(progress);

                                HEIGHT = progress;
                            } else {
                                HEIGHT = 225;
                                mRelat.getBackground().setAlpha(255);
                                mBusinessTitle.setText("商家详情");
                            }
                        }
                    }
                });
    }

    /**
     * 访问网络收藏该商家
     */
    public void networkCollection() {
        AjaxParams params = new AjaxParams();
        params.put("businessId",
                String.valueOf(mBussinessDetailsBooks.get(0).getId()));

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        ToastUtil.toastShow(BusinessPersonalCenterActivity.this,
                                "收藏成功");
                        if (isCollect == 2) {
                            if(!TextUtils.isEmpty(getIntent().getStringExtra("position"))){
                                if(getIntent().getStringExtra("position").equals("-1")){
                                    mBusinessPersonalCollection.setImageResource(R.drawable.business_collection);
                                    mCollection = 1;
                                }else{
                                    position = Integer.parseInt(getIntent().getStringExtra("position"));
                                    EventBusAdapter busAdapter = new EventBusAdapter();
                                    busAdapter.setPosition(position);
                                    busAdapter.setBusinessPersonalCenterCollection(1);
                                    EventBus.getDefault().post(busAdapter);// 像适配器传递值
                                    mCollection = 1;
                                    mBusinessPersonalCollection.setImageResource(R.drawable.business_collection);
                                }
                            }else{
                                ToastUtil.toastShow(BusinessPersonalCenterActivity.this,"请传入下标");
                            }
                        } else {
                            if (!TextUtils.isEmpty(getIntent().getStringExtra("position"))) {//从有些页面到本页面不需要此参数
                                if(getIntent().getStringExtra("position").equals("-1")){
                                    mBusinessPersonalCollection.setImageResource(R.drawable.business_collection);
                                    mCollection = 1;
                                }else{
                                    position = Integer.parseInt(getIntent().getStringExtra(
                                            "position"));
                                    EventBusAdapter busAdapter = new EventBusAdapter();
                                    busAdapter.setPosition(position);
                                    busAdapter.setBusinessPersonalCenterCollection(1);
                                    busAdapter.setPersonalCenterCollection(1);
                                    EventBus.getDefault().post(busAdapter);// 像适配器传递值
                                    mCollection = 1;
                                    mBusinessPersonalCollection.setImageResource(R.drawable.business_collection);
                                }
                            }else{
                                ToastUtil.toastShow(BusinessPersonalCenterActivity.this,"请传入下标");
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
        }, params, NetworkUtil.DELETE_COLLECTION_URL, false, 0);
    }

    /**
     * 访问网络取消收藏该商家
     */
    public void networkNotCollection() {
        AjaxParams params = new AjaxParams();
        params.put("businessId",
                String.valueOf(mBussinessDetailsBooks.get(0).getId()));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        int isCollect = getIntent().getIntExtra("isCollect", 0);

                        ToastUtil.toastShow(BusinessPersonalCenterActivity.this,
                                "取消收藏");
                        if (isCollect == 2) {
                            if (!TextUtils.isEmpty(getIntent().getStringExtra("position"))) {//从有些页面到本页面不需要此参数
                                if(getIntent().getStringExtra("position").equals("-1")){
                                    mBusinessPersonalCollection.setImageResource(R.drawable.not_collected);
                                    mCollection = 0;
                                }else{
                                    position = Integer.parseInt(getIntent().getStringExtra(
                                            "position"));
                                    EventBusAdapter busAdapter = new EventBusAdapter();
                                    busAdapter.setPosition(position);
                                    busAdapter.setBusinessPersonalCenterCollection(0);
                                    EventBus.getDefault().post(busAdapter);// 像适配器传递值
                                    mCollection = 0;
                                    mBusinessPersonalCollection.setImageResource(R.drawable.not_collected);
                                }
                            }else{
                                ToastUtil.toastShow(BusinessPersonalCenterActivity.this,"请传入下标");
                            }
                        } else {
                            if (!TextUtils.isEmpty(getIntent().getStringExtra("position"))) {//从有些页面到本页面不需要此参数
                                if(getIntent().getStringExtra("position").equals("-1")){
                                    mBusinessPersonalCollection
                                            .setImageResource(R.drawable.not_collected);
                                    mCollection = 0;
                                }else{
                                    position = Integer.parseInt(getIntent().getStringExtra(
                                            "position"));
                                    EventBusAdapter busAdapter = new EventBusAdapter();
                                    busAdapter.setPosition(position);
                                    busAdapter.setBusinessPersonalCenterCollection(0);
                                    busAdapter.setPersonalCenterCollection(0);
                                    EventBus.getDefault().post(busAdapter);// 像适配器传递值
                                    mCollection = 0;
                                    mBusinessPersonalCollection.setImageResource(R.drawable.not_collected);
                                }
                            }else{
                                ToastUtil.toastShow(BusinessPersonalCenterActivity.this,"请传入下标");
                            }

                        }

                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.DELETE_COLLECTION_URL, false, 0);
    }

    /**
     * 详情
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    public void createWalletPayDialog(final Context context) {

        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);

        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.business_service_dialog, null);

        LinearLayout mBusinessService = (LinearLayout) layout
                .findViewById(R.id.business_service_layout);
        ListView mList = (ListView) layout
                .findViewById(R.id.business_list_dialog);
        mBusinesscontact = new ArrayList<Service>();// 客服集合
        mBusinesscontact.addAll(mBussinessDetailsBooks.get(0).getInteraction());

        if (mBusinesscontact.size() == 0) {
            Service busiService = new Service();
            busiService.setUrl(mBussinessDetailsBooks.get(0).getUrl());
            busiService
                    .setNick(mBussinessDetailsBooks.get(0).getBusinessName());
            busiService.setManagId(mBussinessDetailsBooks.get(0).getUserId());
            busiService.setUserId(mBussinessDetailsBooks.get(0).getUserId());
            busiService.setRole(1);
            mBusinesscontact.add(busiService);
        }

        // 客服适配器
        mServiceAdapter = new CommonAdapter<Service>(
                BusinessPersonalCenterActivity.this, mBusinesscontact,
                R.layout.business_list_dialog_item) {
            @Override
            public void convert(ViewHolder holder, final Service t, int p) {

                holder.setText(R.id.business_name, t.getNick());
                ImageUtils.commDisplayImage(t.getUrl(),
                        ((ImageView) holder.getView(R.id.business_portrait)),
                        10, R.drawable.user_fang_icon);// 评论人头像
                holder.setOnClickListener(R.id.business_news,
                        new OnClickListener() {// 联系客服
                            @Override
                            public void onClick(View v) {
                                if (String.valueOf(t.getUserId()).equals(
                                        CacheTools.getUserData("userId"))) {
                                    ToastUtil
                                            .toastShow(
                                                    BusinessPersonalCenterActivity.this,
                                                    "不能跟自己聊天哦！");
                                    return;
                                }
                                AppContext.createChat(BusinessPersonalCenterActivity.this,
                                        String.valueOf(t.getUserId()));


                                // Intent intent = new
                                // Intent(BusinessPersonalCenterActivity.this,
                                // ChatActivity.class);
                                // intent.putExtra("to", t.getUserId() +
                                // "@iz232jtyxeoz");// 聊天人的唯一ID
                                // intent.putExtra("isOrderto", 1);//
                                // 标识进入订单可以显示聊天页面
                                // intent.putExtra("handlerId",
                                // String.valueOf(t.getManagId()));// 管理平台账号ID
                                // // 包含商家和客服
                                //
                                // ManagId = String.valueOf(t.getManagId());
                                // intent.putExtra("isCollect", getIntent()
                                // .getIntExtra("isCollect", 0));
                                // intent.putExtra("telephone",
                                // t.getTelephone());
                                // intent.putExtra("serviceType", mServiceType);
                                // intent.putExtra("serviceProject", getIntent()
                                // .getStringExtra("serviceProject"));
                                // intent.putExtra("projectNum", mProjectNum);
                                // intent.putExtra("businessId", String
                                // .valueOf(mTrafficBroker.getUserId()));
                                // intent.putExtra("serviceType", mServiceType);
                                // Bundle bundle = new Bundle();
                                // bundle.putSerializable(Constant.TRAFFIC_SER,
                                // mTrafficBroker);
                                // intent.putExtras(bundle);
                                // startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                holder.setOnClickListener(R.id.business_phone,
                        new OnClickListener() {// 打电话
                            @Override
                            public void onClick(View v) {

                                if(TextUtils.isEmpty(t.getTelephone())){
                                    ToastUtil.toastShow(BusinessPersonalCenterActivity.this,"该客服暂无电话");
                                }else {
                                    dialog.dismiss();
                                    DialogUtil.call(t.getTelephone(),
                                            BusinessPersonalCenterActivity.this);
                                }
                            }
                        });

            }
        };
        /**
         * 关闭适配器
         */
        mBusinessService.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        mList.setAdapter(mServiceAdapter);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myrightstyle); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }

    public void fragmentSwitch(int page) {
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);

        if (page == 0) {
            mOnIllegal.setTextColor(getResources().getColor(R.color.white));
            OnVehicle.setTextColor(getResources().getColor(R.color.business_h));
            OnAmount.setTextColor(getResources().getColor(R.color.business_h));

            mOnIllegal.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_z));
            OnVehicle.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_h));
            OnAmount.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_h));
        } else if (page == 1) {
            mOnIllegal
                    .setTextColor(getResources().getColor(R.color.business_h));
            OnVehicle.setTextColor(getResources().getColor(R.color.white));
            OnAmount.setTextColor(getResources().getColor(R.color.business_h));
            mOnIllegal.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_h));
            OnVehicle.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_z));
            OnAmount.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_h));

        } else if (page == 2) {
            mOnIllegal
                    .setTextColor(getResources().getColor(R.color.business_h));
            OnVehicle.setTextColor(getResources().getColor(R.color.business_h));
            OnAmount.setTextColor(getResources().getColor(R.color.white));
            mOnIllegal.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_h));
            OnVehicle.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_h));
            OnAmount.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_proname_z));
        }

        fragmentTransaction.show(mFragments[page]).commit();// 设置第几页显示
    }

    /**
     * 访问网络查询商家详情
     */
    private void getRefreshList(String businessId) {
        AjaxParams params = new AjaxParams();
        params.put("businessId", businessId);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:


                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.GONE);
                        mBusinessDetailsGogn.setVisibility(View.GONE);

                        mBussinessDetailsBooks = new ArrayList<BussinessDetailsBook>();
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj
                                    .toString());
                            BussinessDetailsBook bussinessDetailsBook = new BussinessDetailsBook();
                            bussinessDetailsBook.setId(Integer.parseInt(jsonObject
                                    .getString("id")));
                            bussinessDetailsBook.setUserId(Integer
                                    .parseInt(jsonObject.getString("userId")));
                            bussinessDetailsBook.setBusinessName(jsonObject
                                    .getString("businessName"));
                            bussinessDetailsBook.setIntroduce(jsonObject
                                    .getString("introduce"));
                            bussinessDetailsBook.setBusinessScope(jsonObject
                                    .getString("businessScope"));
                            bussinessDetailsBook.setLocation(jsonObject
                                    .getString("location"));
                            bussinessDetailsBook.setRealName(jsonObject
                                    .getString("realName"));
                            bussinessDetailsBook.setMobile(jsonObject
                                    .getString("mobile"));
                            bussinessDetailsBook.setStoreLevel(jsonObject
                                    .getString("storeLevel"));
                            bussinessDetailsBook.setQq(jsonObject.getString("qq"));
                            bussinessDetailsBook.setCategory(jsonObject
                                    .getString("category"));
                            bussinessDetailsBook.setAddress(jsonObject
                                    .getString("address"));
                            bussinessDetailsBook.setZfbAccount(jsonObject
                                    .getString("zfbAccount"));
                            bussinessDetailsBook.setWszfAccount(jsonObject
                                    .getString("wszfAccount"));
                            bussinessDetailsBook.setUpAccount(jsonObject
                                    .getString("upAccount"));
                            bussinessDetailsBook.setStar(Float
                                    .parseFloat(jsonObject.getString("star")));
                            bussinessDetailsBook.setCompanyName(jsonObject.getString("companyName"));
                            if (jsonObject.get("url") instanceof String) {
                                bussinessDetailsBook.setUrl(jsonObject.getString("url"));
                            } else {
                                bussinessDetailsBook.setUrl(jsonObject.getJSONObject("url").getString("url"));
                            }
                            bussinessDetailsBook.setCommentsize(Integer
                                    .parseInt(jsonObject.getString("commentsize")));
                            bussinessDetailsBook.setConsumeNum(Integer
                                    .parseInt(jsonObject.getString("consumeNum")));
                            bussinessDetailsBook.setIsfavorite(Integer
                                    .parseInt(jsonObject.getString("isfavorite")));
                            bussinessDetailsBook.setExpireDate(jsonObject
                                    .getString("expireDate"));
                            bussinessDetailsBook.setImages(jsonObject
                                    .getString("images"));
                            bussinessDetailsBook.setStartDate(jsonObject
                                    .getString("startDate"));

                            TrafficBusinessListBook mBusinessListBook = new TrafficBusinessListBook();
                            mBusinessListBook.setRealName(jsonObject.getString("realName"));
                            mBusinessListBook.setId(jsonObject.getString("id"));
                            mBusinessListBook.setUserId(jsonObject.getString("userId"));

                            List<Service> mServices = new ArrayList<Service>();

                            for (int i = 0; i < jsonObject.getJSONArray("wzdj")
                                    .length(); i++) {
                                if (!jsonObject.getJSONArray("wzdj")
                                        .getJSONObject(i).getString("projectName")
                                        .contains("上门")) {
                                    mIllegalList
                                            .add(new BusinessServicePro(
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString("id")),
                                                    Double.parseDouble(jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString("fees")),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString("type")),
                                                    jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "projectName"),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString("projectNum")),
                                                    jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString("projectImg"),
                                                    jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "serviceDesc"),
                                                    String.valueOf(jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getJSONArray(
                                                                    "serviceFolder")),
                                                    Double.parseDouble(jsonObject
                                                            .getJSONArray("wzdj")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "servicePrice")), jsonObject
                                                    .getString("location")));
                                }

                            }
                            for (int i = 0; i < jsonObject.getJSONArray("clfw")
                                    .length(); i++) {
                                if (!jsonObject.getJSONArray("clfw")
                                        .getJSONObject(i).getString("projectName")
                                        .contains("上门")) {
                                    mVehicleList
                                            .add(new BusinessServicePro(
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString("id")),
                                                    Double.parseDouble(jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString("fees")),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString("type")),
                                                    jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "projectName"),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString("projectNum")),
                                                    jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString("projectImg"),
                                                    jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "serviceDesc"),
                                                    String.valueOf(jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getJSONArray(
                                                                    "serviceFolder")),
                                                    Double.parseDouble(jsonObject
                                                            .getJSONArray("clfw")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "servicePrice")), jsonObject
                                                    .getString("location")));
                                }
                            }
                            for (int i = 0; i < jsonObject.getJSONArray("jzfw")
                                    .length(); i++) {
                                if (!jsonObject.getJSONArray("jzfw")
                                        .getJSONObject(i).getString("projectName")
                                        .contains("上门")) {
                                    mDrivingList
                                            .add(new BusinessServicePro(
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString("id")),
                                                    Double.parseDouble(jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString("fees")),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString("type")),
                                                    jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "projectName"),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString("projectNum")),
                                                    jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString("projectImg"),
                                                    jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "serviceDesc"),
                                                    String.valueOf(jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getJSONArray(
                                                                    "serviceFolder")),
                                                    Double.parseDouble(jsonObject
                                                            .getJSONArray("jzfw")
                                                            .getJSONObject(i)
                                                            .getString(
                                                                    "servicePrice")), jsonObject
                                                    .getString("location")));
                                }
                            }
                            EventBusAdapter bsAdapter = new EventBusAdapter();
                            bsAdapter.setmListComment(mIllegalList);
                            bsAdapter.setmCommentsList(mVehicleList);
                            bsAdapter.setmBadList(mDrivingList);
                            bsAdapter.setmBusinessListBook(mBusinessListBook);
                            bsAdapter.setIndex(1);
                            EventBus.getDefault().post(bsAdapter);// 像适配器传递值
                            mBussinessDetailsBooks.add(bussinessDetailsBook);
                            mShopNameTV.setText(mBussinessDetailsBooks.get(0)
                                    .getRealName());
                            mBusinierssGrade.setRating(mBussinessDetailsBooks
                                    .get(0).getStar());// 等级
                            mBusinessBranch.setText(mBussinessDetailsBooks.get(0)
                                    .getStar() + "分");// 等级分

                            if (!TextUtils.isEmpty(bussinessDetailsBook.getCompanyName())) {
                                mBusinessEnterprise.setText(bussinessDetailsBook.getCompanyName());
                            } else {
                                mBusinessEnterprise.setVisibility(View.GONE);
                            }


                            mCollection = mBussinessDetailsBooks.get(0)
                                    .getIsfavorite();
                            if (mCollection == 1) {// 判断是否收藏 1 收藏| 0 未收藏
                                mBusinessPersonalCollection
                                        .setImageResource(R.drawable.business_collection);
                            } else {
                                mBusinessPersonalCollection
                                        .setImageResource(R.drawable.not_collected);
                            }
                            address = mBussinessDetailsBooks.get(0).getAddress();
                            mBusinessProvince.setText(mBussinessDetailsBooks.get(0).getAddress());// 显示地址并将，好替换成空格
                            ImageUtils.displayImage(mBussinessDetailsBooks.get(0)
                                    .getImages(), mBusinessCollectionStore, 10, R.drawable.default_busi, R.drawable.default_busi);

                            /**
                             * 判断头像地址是否为null
                             */
                            if (!TextUtils.isEmpty(mBussinessDetailsBooks.get(0)
                                    .getUrl())) {
                                ImageUtils.displayImage(
                                        mBussinessDetailsBooks.get(0).getUrl(),
                                        mBusinessPortrait, 10, R.drawable.user_fang_icon, R.drawable.user_fang_icon);
                            } else {
                                mBusinessPortrait
                                        .setImageResource(R.drawable.user_fang_icon);
                            }
                            if (jsonObject.getJSONArray("interaction") != null && jsonObject.getJSONArray("interaction").length() > 0) {
                                for (int i = 0; i < jsonObject.getJSONArray(
                                        "interaction").length(); i++) {
                                    mServices
                                            .add(new Service(
                                                    jsonObject
                                                            .getJSONArray("interaction")
                                                            .getJSONObject(i)
                                                            .getString("nick"),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("interaction")
                                                            .getJSONObject(i)
                                                            .getString("role")),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("interaction")
                                                            .getJSONObject(i)
                                                            .getString("managId")),
                                                    Integer.parseInt(jsonObject
                                                            .getJSONArray("interaction")
                                                            .getJSONObject(i)
                                                            .getString("userId")),
                                                    jsonObject
                                                            .getJSONArray("interaction")
                                                            .getJSONObject(i)
                                                            .getString("telephone"),
                                                    jsonObject
                                                            .getJSONArray("interaction")
                                                            .getJSONObject(i)
                                                            .getString("url")));
                                    bussinessDetailsBook.setInteraction(mServices);
                                }
                            } else {
                                bussinessDetailsBook.setInteraction(null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(BusinessPersonalCenterActivity.this, ((DataError) msg.obj).getErrorMessage());
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        mBusinessDetailsGogn.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        mBusinessDetailsGogn.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }, params, NetworkUtil.SELECT_BUSSINESS_DETAILS, false, 1);
    }

    /**
     * 商家承诺
     */
    @SuppressWarnings("deprecation")
    private void Insurance() {

        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);

        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.business_promise, null);

        Button mInsurance = (Button) layout.findViewById(R.id.insurance_button);
        mInsurance.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myBusinessTheme); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }
}
