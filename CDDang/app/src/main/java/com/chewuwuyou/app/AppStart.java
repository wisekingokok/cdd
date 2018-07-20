package com.chewuwuyou.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;
import com.chewuwuyou.app.ui.BaseActivity;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.LunchPageActivity;
import com.chewuwuyou.app.ui.MainActivityEr;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.VersionUtil;
import com.chewuwuyou.rong.utils.MyReceivePushMseListener;
import com.chewuwuyou.rong.utils.RongApi;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.RongIMClient;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 *
 * @author xiajy
 * @version 1.0
 * @created 2012-4-8
 */
public class AppStart extends BaseActivity {

    // private static final String TAG = "AppStart";
    // 定位
    // private static final int UPDATE_TIME = 5000;
//    public LocationClient mLocationClient;
//    public MyLocationListenner myListener = new MyLocationListenner();
    public static boolean isForeground = false;

    /**
     * 标记连接融云操作是否完成
     */
    private boolean rongConnect = false;
    /**
     * 标记动画是否完成
     */
    private boolean animation = false;
    //只有动画与连接（不管是否连接成功 ）操作都完成才跳转界面

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMessageReceiver(); // used for receive msg
        // 获取内部存储的路径并存储起来
        CacheTools.setUserData("cacheDir", getFilesDir() + "");
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        CacheTools.setUserData("screenWidth", String.valueOf(dm.widthPixels));
        CacheTools.setUserData("screenHeight", String.valueOf(dm.heightPixels));
        MyLog.e("YUY", "----版本号存储----" + VersionUtil.getVersion(getApplication()));
        CacheTools.setUserData("version", VersionUtil.getVersion(getApplication()));//存储现在的版本号
        // baidu统计add
        StatService.setAppChannel(this, "", false);
        StatService.setSessionTimeOut(1);
        final View view = View.inflate(this, R.layout.app_start1, null);
        setContentView(view);
        NetworkUtil.postNoHeader(NetworkUtil.GET_ALL_CITY, null,
                new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        try {
                            JSONObject jo = new JSONObject(t.toString());
                            CacheTools.setUserData("citysData", jo.getJSONObject("data")
                                    .getJSONArray("cities").toString());// 存入所有的城市数据
                            MyLog.i("YUY", "城市数据" + jo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                animation = true;
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
//        mLocationClient = new LocationClient(this);
//        mLocationClient.registerLocationListener(myListener);
//        setLocationOption();// 设定定位参数
//        mLocationClient.start();// 开始定位

        if (CacheTools.getUserData("role") == null) {
            rongConnect = true;
        } else {
            connect(CacheTools.getUserData("rongToken"), getApplicationContext());
        }
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public void connect(String token, Context context) {
        if (context.getApplicationInfo().packageName.equals(AppContext.getCurProcessName(context.getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongApi.connect(context, token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.e("onTokenIncorrect", "Token失效");
                    startActivity(new Intent(AppStart.this, LoginActivity.class));
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("onSuccess", "rong连接成功");
                    rongConnect = true;
                    redirectTo();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("onError", "rong连接失败");
                    startActivity(new Intent(AppStart.this, LoginActivity.class));
                }
            });
        }
    }


    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.chewuwuyou.app.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                ToastUtil.toastShow(AppStart.this, showMsg.toString());
                // setCostomMsg(showMsg.toString());
            }
        }
    }

    // 定位
    // 设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.disableCache(false);// 禁止启用缓存定位
        option.setPoiNumber(5); // 最多返回POI个数
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setPoiDistance(1000); // poi查询距离
        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);

    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        if (!rongConnect || !animation) return;
        Intent intent;
        if (CacheTools.getUserData("role") == null) {
            intent = new Intent(this, LunchPageActivity.class);
        } else {
            intent = new Intent(this, MainActivityEr.class);
            Bundle bundle = intent.getBundleExtra(MyReceivePushMseListener.RE_TAG);
            if (bundle != null) {
                intent.putExtra(RongChatMsgFragment.KEY_TARGET, bundle.getString(RongChatMsgFragment.KEY_TARGET));
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, bundle.getSerializable(RongChatMsgFragment.KEY_TYPE));
            }

        }
        startActivity(intent);
        finish();

    }

    /**
     * 监听函数，更新位置
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        // 接收位置信息
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                CacheTools.setUserData("province", location.getProvince());
                CacheTools.setUserData("city", location.getCity());
                CacheTools.setUserData("district", location.getDistrict());
                CacheTools.setUserData("address", location.getAddrStr() + "");
                CacheTools.setUserData("Lat", location.getLatitude() + "");
                CacheTools.setUserData("Lng", location.getLongitude() + "");
                MyLog.i("YUY",
                        "xxxx定位地区xx" + location.getProvince()
                                + location.getCity() + "---"
                                + location.getLatitude() + "----"
                                + location.getLongitude() + location.getAddrStr());

                // 退出时销毁定位
                mLocationClient.unRegisterLocationListener(myListener);
                mLocationClient.stop();
            }
        }

        // 接收POI信息函数
        @Override
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }


    @Override
    protected void onResume() {
        StatService.onResume(AppStart.this);
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        StatService.onPause(AppStart.this);
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        if (mLocationClient != null && mLocationClient.isStarted()) {
//            mLocationClient.stop();// 停止定位
//            mLocationClient.unRegisterLocationListener(myListener);
//            mLocationClient = null;
//            myListener = null;
//        }
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
