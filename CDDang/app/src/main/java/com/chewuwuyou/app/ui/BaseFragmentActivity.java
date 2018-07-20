/**
 *
 */
package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Serie;
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

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 类名称：BaseFragmentActivity 类描述： 创建人：Administrator 创建时间：2014-12-8 下午2:42:15
 * 修改人：Administrator 修改时间：2014-12-8 下午2:42:15 修改备注：
 */
@SuppressLint("InlinedApi")
public class BaseFragmentActivity extends FragmentActivity {
    // 是否允许全屏
    private boolean allowFullScreen = true;
    public WaitingDialog dialog;
    // 是否允许销毁
    private boolean allowDestroy = true;

    private View view;
    private WaitingDialog mWaitingDialog;
    private BroadcastReceiver receiver;
    public String url = "";

    protected static final DisplayMetrics mOutMetrics = new DisplayMetrics();
    protected int mTuWidth = 0;
    protected int mTuHeight = 0;

    protected int mBgTuWidth = 0;
    protected int mBgTuHeight = 0;

    public boolean isTitleDisplay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            isTitleDisplay = true;
        } else {
            isTitleDisplay = false;
        }

        mWaitingDialog = new WaitingDialog(this);
        allowFullScreen = true;
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        // 注册广播监听网络变化
        receiver = new MyReceiver();
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

    }

    /**
     * 判断手机版本是否是4.4以上 不是显示false里面的布局
     */
    public void isTitle(RelativeLayout mTitleHeight) {
        android.view.ViewGroup.LayoutParams layout = mTitleHeight.getLayoutParams();
        mTitleHeight.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int density = metric.densityDpi;
        // 240当前手机的像素
        if (isTitleDisplay) {
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
        unregisterReceiver(receiver);
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
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
                if (IsNetworkUtil.isNetworkAvailable(BaseFragmentActivity.this) == false) {
                    Toast.makeText(BaseFragmentActivity.this,
                            "网络不可用，请检查您的网络是否连接", Toast.LENGTH_LONG);
                } else {
//					ToastUtil.toastShow(BaseFragmentActivity.this,"网络异常，请稍后再试");
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
                if (isCricle == 0)
                    dismissWaitingDialog();
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
                                ToastUtil.toastShow(BaseFragmentActivity.this, "您长时间未操作,请重新登录");
                                Tools.clearInfo(BaseFragmentActivity.this);
                                Intent intent = new Intent(BaseFragmentActivity.this,
                                        LunchPageActivity.class);
                                startActivity(intent);
                                BaseFragmentActivity.this.finish();
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE) {
                                DialogUtil.loginInOtherPhoneDialog(BaseFragmentActivity.this);
                            } else if (errorCode == ErrorCodeUtil.IndividualCenter.ROLE_CHANGE || errorCode == ErrorCodeUtil.Business.NOT_BUSINESS) {//角色变更
                                DialogUtil.roleChange(BaseFragmentActivity.this);
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
    public String getVehicleImage(String brand, String model) {
        // 车型对象集合
        List<Serie> series;
        // 品牌集合
        List<CarModel> carModels;
        // 品牌对象
        CarModel carModel;
        // 车型对象
        Serie serie;
        // 获取车辆品牌车型对象
        carModels = CarModel.parseBrands(getFromAssets("mobile_models"));
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
        String res = null;
        String[] proj = {MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null,
                null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        File file = new File(res);
        return file;
    }

    /**
     * 设置图片
     */
    // public void setImage(ImageView view, String imgUrl) {
    // Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(view,
    // NetworkUtil.IMAGE_BASE_URL + imgUrl, new ImageCallBack() {
    //
    // @Override
    // public void imageLoad(View imageView, Bitmap bitmap) {
    // ((ImageView) imageView).setImageBitmap(bitmap);
    // }
    // });
    // if (bitmap != null) {
    // view.setImageBitmap(bitmap);
    // }
    // }
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

}
