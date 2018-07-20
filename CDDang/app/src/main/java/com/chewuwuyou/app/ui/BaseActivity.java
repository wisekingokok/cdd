package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Serie;
import com.chewuwuyou.app.utils.AsyncBitmapLoader;
import com.chewuwuyou.app.utils.AsyncBitmapLoader.ImageCallBack;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.JpushUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.WaitingDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 应用程序Activity的基类
 *
 * @author xiajy
 * @version 1.0
 * @created 2012-4-8
 */
@SuppressLint("InlinedApi")
public class BaseActivity extends FinalActivity {
    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 是否允许全屏
    private boolean allowFullScreen = true;
    public WaitingDialog dialog;
    // 是否允许销毁
    private boolean allowDestroy = true;

    private View view;
    private WaitingDialog mWaitingDialog;
    private BroadcastReceiver receiver;
    public static LocationClient mLocationClient;
    public static MyLocationListenner myListener = new MyLocationListenner();
    private InputMethodManager mImm;

    protected static final DisplayMetrics mOutMetrics = new DisplayMetrics();
    protected int mTuWidth = 0;
    protected int mTuHeight = 0;

    protected int mBgTuWidth = 0;
    protected int mBgTuHeight = 0;

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    protected boolean isTitkeDisplay = false;// 判断标题是否显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // Window window = getWindow();
        // window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        // WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // isTitkeDisplay = true;
        // } else {
        // isTitkeDisplay = false;
        // }

        mWaitingDialog = new WaitingDialog(this);
        allowFullScreen = true;
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        // 注册广播监听网络变化
        receiver = new MyReceiver();
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.CONNECTIVITY_CHANGE_ACTION);
        filter.setPriority(1000);
        registerReceiver(receiver, filter);

        getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);
        mTuWidth = (mOutMetrics.widthPixels
                - 2
                * getResources().getDimensionPixelSize(R.dimen.quan_padding)
                - getResources().getDimensionPixelSize(
                R.dimen.quan_avatar_width) - 2 * getResources()
                .getDimensionPixelSize(R.dimen.quan_tu_interval)) / 3;
        mTuHeight = mTuWidth;

        mBgTuWidth = mOutMetrics.widthPixels;
        mBgTuHeight = (mOutMetrics.widthPixels * 2) / 3;
        // if (!AppContext.getInstance().stillLogingIn) {
        // AppContext.getInstance().loginXmpp();
        // }

    }

    /**
     * 判断手机版本是否是4.4以上 不是显示false里面的布局
     */
    public void isTitle(RelativeLayout mTitleHeight) {
        android.view.ViewGroup.LayoutParams layout = mTitleHeight
                .getLayoutParams();
        mTitleHeight.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int density = metric.densityDpi;
        // 240当前手机的像素
        if (isTitkeDisplay) {
            layout.height = 130 * density / 320;
        } else {
            layout.height = 64 * density / 240;
        }
        mTitleHeight.setLayoutParams(layout);
    }

    public void finishActivity() {
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 关闭指定的activity
     *
     * @param activity
     */
    public void finishToActivity(Class<?> activity) {
        AppManager.getAppManager().finishActivity(activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        unRegisterReceiver();
    }

    public void unRegisterReceiver() {
        unregisterReceiver(receiver);
    }

    public boolean isAllowFullScreen() {
        return allowFullScreen;
    }

    /**
     * 设置是否可以全屏
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.allowFullScreen = allowFullScreen;
    }

    public void setAllowDestroy(boolean allowDestroy) {
        this.allowDestroy = allowDestroy;
    }

    public void setAllowDestroy(boolean allowDestroy, View view) {
        this.allowDestroy = allowDestroy;
        this.view = view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
            view.onKeyDown(keyCode, event);
            if (!allowDestroy) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 从resources中的raw 文件夹中获取文件并读取数据
     *
     * @param rawid res文件下的raw资源文件的文件id(R.id.xxx)
     * @return
     */
    public String getFromRaw(int rawid) {
        String result = "";
        try {
            InputStream in = getResources().openRawResource(rawid);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从assets 文件夹中获取文件并读取数据
     *
     * @param fileName assets文件夹下的路径+文件名
     * @return
     */
    public String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param handler  消息句柄用于通知UI线程更新UI
     * @param params   请求参数
     * @param url      请求地址
     * @param isCache  是否缓存
     * @param isCricle 是否转圈
     */
    @SuppressWarnings("static-access")
    public void requestNet(final Handler handler, final AjaxParams params,
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
                if (IsNetworkUtil.isNetworkAvailable(BaseActivity.this) == false) {
                    ToastUtil.toastShow(BaseActivity.this, "网络不可用，请检查您的网络是否连接");
                } else {
//                    ToastUtil.toastShow(BaseActivity.this,"网络异常，请稍后再试");
                    // 请求服务器失败，请稍候再试
					/*
                     * Toast toast = Toast.makeText(CDDBaseActivity.this,
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
                            if (errorCode == 0) {
                                // 没有登录跳到登录页面
                                ToastUtil.toastShow(BaseActivity.this, "您长时间未操作,请重新登录");
                                Tools.clearInfo(BaseActivity.this);
                                Intent intent = new Intent(BaseActivity.this,
                                        LunchPageActivity.class);
                                startActivity(intent);
                                finishActivity();
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE
                                    || errorCode == ErrorCodeUtil.Business.NOT_BUSINESS) {
                                DialogUtil.loginInOtherPhoneDialog(BaseActivity.this);
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.ROLE_CHANGE) {//角色变更
                                DialogUtil.roleChange(BaseActivity.this);
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
                            // ToastUtil.toastShow(CDDBaseActivity.this, "数据异常");
                            break;
                    }
                } catch (Exception e) {
                    // handler.sendEmptyMessage(Constant.NET_DATA_EXCEPTION);
                    // ToastUtil.toastShow(CDDBaseActivity.this, "数据异常");
                    e.printStackTrace();
                }
            }
        });
    }

    public void refresh() {

    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("YUY", "PfDataTransReceiver receive action " + action);
            if (TextUtils.equals(action, Constant.CONNECTIVITY_CHANGE_ACTION)) {// 网络变化的时候会发送通知
                Log.i("YUY", "网络变化了");
                refresh();
                return;
            }
        }

    }

    /**
     * 通过品牌和车型查询车辆图片
     *
     * @param brand
     * @param model
     */
    public String getVehicleImage(String brand, String model,
                                  final List<CarModel> carModels) {
        // 车型对象集合
        List<Serie> series;
        // 品牌对象
        CarModel carModel;
        // 车型对象
        Serie serie;
        // 获取车辆品牌车型对象
        // 定义图片url
        String carImageUrl;
        for (int i = 0, size = carModels.size(); i < size; i++) {
            if (carModels.get(i).getBrand().equals(brand)) {
                carModel = carModels.get(i);
                series = carModel.getSeries();
                for (int j = 0, len = series.size(); j < len; j++) {
                    if (series.get(j).getName().equals(model)) {
                        // 得到车型的图片地址
                        serie = series.get(j);
                        carImageUrl = NetworkUtil.IMAGE_BASE_URL
                                + serie.getIcon();
                        return carImageUrl;
                    }
                }
            }
        }
        return null;
    }

    /**
     * uri传文件
     *
     * @param contentUri
     * @return
     */
    public File getfileFromURI(Uri contentUri) {
        try {
            String res = null;
            String[] proj = {MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(contentUri, proj, null,
                    null, null);
            if (cursor.moveToFirst()) {
                ;
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaColumns.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            File file = new File(res);
            return file;
        } catch (IllegalArgumentException e) {
            Toast.makeText(BaseActivity.this, "e = " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置图片
     *
     * @param view
     * @param imgUrl
     */
    public void setImage(ImageView view, String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            // Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(view,
            // NetworkUtil.IMAGE_BASE_URL + imgUrl, new ImageCallBack() {
            //
            // @Override
            // public void imageLoad(View imageView, Bitmap bitmap) {
            // ((ImageView) imageView).setImageBitmap(bitmap);
            // }
            // });
            // if (bitmap != null) {
            // // BitmapDrawable bd= new BitmapDrawable(bitmap);
            // // view.setBackgroundDrawable(bd);
            // view.setImageBitmap(bitmap);
            // }

            ImageLoader.getInstance().displayImage(
                    imgUrl,
                    view,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showStubImage(R.drawable.a1_audi)
                            .showImageForEmptyUri(R.drawable.a1_audi)
                            .showImageOnFail(R.drawable.a1_audi).build());
        }

    }

    /**
     * 设置图片
     *
     * @param view
     * @param imgUrl
     */
    @SuppressWarnings("deprecation")
    public void setImageDrawable(ImageView view, String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(view,
                    ServerUtils.getServerIP(imgUrl), new ImageCallBack() {

                        @Override
                        public void imageLoad(View imageView, Bitmap bitmap) {
                            BitmapDrawable bd = new BitmapDrawable(bitmap);
                            ((ImageView) imageView).setBackgroundDrawable(bd);
                        }
                    });
            if (bitmap != null) {
                BitmapDrawable bd = new BitmapDrawable(bitmap);
                view.setBackgroundDrawable(bd);
            }
        }

    }

    protected void showWaitingDialog() {
        if (isFinishing()) {
            dismissWaitingDialog();
            return;
        }
        if (mWaitingDialog != null && !mWaitingDialog.isShowing()) {
            mWaitingDialog.show();
        }
    }

    protected void dismissWaitingDialog() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(BaseActivity.this);
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();// 设定定位参数
        mLocationClient.start();// 开始定位
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
     * 监听函数，更新位置
     */
    public static class MyLocationListenner implements BDLocationListener {

        @Override
        // 接收位置信息
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                CacheTools.setUserData("province", location.getProvince());
                CacheTools.setUserData("city", location.getCity());
                CacheTools.setUserData("district", location.getDistrict());
                CacheTools.setUserData("Lat", location.getLatitude() + "");
                CacheTools.setUserData("Lng", location.getLongitude() + "");
                CacheTools.setUserData("addrStr", location.getAddrStr());
                MyLog.i("YUY",
                        "baseActivityxxxx定位地区xx" + location.getProvince()
                                + location.getCity() + "---"
                                + location.getLatitude() + "----"
                                + location.getLongitude());

                // 退出时销毁定位
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
    protected void onPause() {
        super.onPause();
        StatService.onPause(BaseActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(BaseActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

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
                mImm.hideSoftInputFromWindow(getCurrentFocus()
                                .getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        MyLog.i("YUY", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    break;
            }
            MyLog.e("YUY", "推送Alias ----- " + logs);

        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i("YUY", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    break;
            }
            MyLog.e("YUY", "推送tag ----- " + logs);
        }

    };


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("YUY", "Set alias in handler.");
                    JPushInterface.setAliasAndTags(BaseActivity.this, (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d("YUY", "Set tags in handler.");
                    JPushInterface.setAliasAndTags(BaseActivity.this, null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i("YUY", "Unhandled msg - " + msg.what);
                    break;
            }
        }
    };

    /**
     * 初始化极光推送并设置标签和别名
     */
    public void initUserJpush() {
        final String alias = "cwwy" + CacheTools.getUserData("userId");
        MyLog.e("YUY", "推送别名 ----------" + alias);
        MyLog.e("YUY", "推送registerId = " + JPushInterface.getRegistrationID(getApplicationContext()));
        JPushInterface.init(getApplicationContext());// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.resumePush(getApplicationContext());
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, JpushUtil.pushSetTag()));//调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias)); //调用JPush API设置Alias
    }


    /**
     * 注销极光推送
     */
    public void unregisterJPush() {
        JPushInterface.stopPush(getApplicationContext());
    }


}
