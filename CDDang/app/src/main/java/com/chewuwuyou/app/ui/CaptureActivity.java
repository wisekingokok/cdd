package com.chewuwuyou.app.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Vector;

import net.tsz.afinal.http.AjaxParams;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.camera.CameraManager;
import com.barcode.decode.CaptureActivityHandler;
import com.barcode.decode.FinishListener;
import com.barcode.decode.InactivityTimer;
import com.barcode.view.ViewfinderView;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUser;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;

/**
 * 扫描二维码
 *
 * @author 火蚁（http://my.oschina/LittleDY）
 */

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {

    private boolean hasSurface;
    private String characterSet;

    private ViewfinderView viewfinderView;
    //	private ImageView mCodeImg;
    // private AppContext ac;
    // private ImageView back;
    // private ImageView flash;
    private ProgressDialog mProgress;

    /**
     * 活动监控器，用于省电，如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
     * 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
     */
    private InactivityTimer inactivityTimer;
    private CameraManager cameraManager;
    private Vector<BarcodeFormat> decodeFormats;// 编码格式
    private CaptureActivityHandler mHandler;// 解码线程
    private RelativeLayout mTitleHeight;// 标题布局高度

    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet.of(
            ResultMetadataType.ISSUE_NUMBER, ResultMetadataType.SUGGESTED_PRICE,
            ResultMetadataType.ERROR_CORRECTION_LEVEL, ResultMetadataType.POSSIBLE_COUNTRY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSetting();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture);
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText("扫一扫");
        findViewById(R.id.code_img_ll).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("type", 0) == 1) {
                    finish();
                    return;
                }
                Intent intent = new Intent(CaptureActivity.this, PersonalDataMyQrCodeActivity.class);
                intent.putExtra("scanning", 1);// 代表是从消息扫描进去
                startActivity(intent);
            }
        });
        if (getIntent().getIntExtra("index", 0) == 1) {
            findViewById(R.id.code_img_ll).setVisibility(View.GONE);
        }
        initView();
    }

    /**
     * 初始化窗口设置
     */
    private void initSetting() {

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
        // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
    }

    /**
     * 初始化视图
     */
    private void initView() {
        // ac = (AppContext) getApplication();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        // back = (ImageView) findViewById(R.id.capture_back);
        // flash = (ImageView) findViewById(R.id.capture_flash);
        // back.setOnClickListener(this);
        // flash.setOnClickListener(this);
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
//		mCodeImg = (ImageView) findViewById(R.id.code_img);
        isTitle(mTitleHeight);// 根据不同手机判断
    }

    /**
     * 主要对相机进行初始化工作
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {

        inactivityTimer.onActivity();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            // 如果SurfaceView已经渲染完毕，会回调surfaceCreated，在surfaceCreated中调用initCamera()
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        // 恢复活动监控器
        inactivityTimer.onResume();
        super.onResume();
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 初始化摄像头。打开摄像头，检查摄像头是否被开启及是否被占用
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the mHandler starts the preview, which can also throw a
            // RuntimeException.
            if (mHandler == null) {
                mHandler = new CaptureActivityHandler(this, decodeFormats, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            MyLog.i("YUY", "aaa相机出现问题");
            ToastUtil.toastShow(this, "请开启相机访问权限");
            finishActivity();
//			displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
//            displayFrameworkBugMessageAndExit();
            MyLog.i("YUY", "点对点相机出现问题");
            ToastUtil.toastShow(this, "请开启相机访问权限");
            finishActivity();
        }
    }

    /**
     * 初始化照相机失败显示窗口
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton("确定", new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    /**
     * 暂停活动监控器,关闭摄像头
     */
    @Override
    protected void onPause() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        // 暂停活动监控器
        inactivityTimer.onPause();
        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
        super.onPause();
    }

    /**
     * 停止活动监控器,保存最后选中的扫描类型
     */
    @Override
    protected void onDestroy() {
        // 停止活动监控器
        inactivityTimer.shutdown();
        if (mProgress != null) {
            mProgress.dismiss();
        }
        super.onDestroy();
    }

    /**
     * 获取扫描结果
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        // inactivityTimer.onActivity();
        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {

            // Then not from history, so beep/vibrate and we have an image to
            // draw on
        }
        // DateFormat formatter =
        // DateFormat.getDateTimeInstance(DateFormat.SHORT,
        // DateFormat.SHORT);
        Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
        StringBuilder metadataText = new StringBuilder(20);
        if (metadata != null) {
            for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet()) {
                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                    metadataText.append(entry.getValue()).append('\n');
                }
            }
            if (metadataText.length() > 0) {
                metadataText.setLength(metadataText.length() - 1);
            }
        }
        parseBarCode(rawResult.getText());// www.LINK.com\n\nshangjia:12345
        MyLog.i("YUY", "====扫描二维码的数据====" + rawResult.getText());
    }

    // 解析二维码
    private void parseBarCode(String msg) {
        // 手机震动
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        mProgress = ProgressDialog.show(CaptureActivity.this, null, "已扫描，正在处理···", true, true);
        mProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                restartPreviewAfterDelay(1l);
            }
        });
        // if (!msg.matches("^\\S+BD:[0-9]+$")) {
        // showDialog(msg);
        // } else
        MyLog.i("YUY", "二维码扫描信息 ================= " + msg);
        {
            // msg.lastIndexOf(":")二维码的文本格式:www.chewuwuyou.com\nBD:5;
            // 最新扫描信息 MD:1;
            if (CacheTools.getUserData("role") == null && msg.contains("BD")) {// 由商家推广
                Intent intent = new Intent(CaptureActivity.this, RegisterActivity.class);
                CacheTools.setUserData("caBusinessId", Tools.getDigit("BD:", msg));
                startActivity(intent);
            } else if (CacheTools.getUserData("role") != null && msg.contains("MD")) {
                msg.substring(msg.lastIndexOf(":") + 1, msg.length() - 1);

                Intent intent = new Intent(CaptureActivity.this, PersonalHomeActivity2.class);
         //       intent.putExtra("userId", Tools.getDigit("MD:", msg));
                intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, Tools.getDigit("MD:", msg) + "");
                startActivity(intent);

//                AjaxParams params = new AjaxParams();
//                params.put("userId", Tools.getDigit("MD:", msg));

//                requestNet(new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        switch (msg.what) {
//                            case Constant.NET_DATA_SUCCESS:
//                                ChatUser chatUser = ChatUser.parse(msg.obj.toString());
//                                if (chatUser != null) {
//                                    Intent intent = new Intent(CaptureActivity.this, PersonalHomeActivity2.class);
//                                    // Bundle bundle = new Bundle();
//                                    // bundle.putSerializable(Constant.CHAT_USER_SER,
//                                    // chatUser);
//                                    // intent.putExtras(bundle);
//                                    intent.putExtra("userId", Integer.parseInt(chatUser.getId()));
//                                    intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, Integer.parseInt(chatUser.getId()));
//                                    startActivity(intent);
//                                }
//                                break;
//                            case Constant.NET_DATA_FAIL:
//                                ToastUtil.toastShow(CaptureActivity.this, ((DataError) msg.obj).getErrorMessage());
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }, params, NetworkUtil.CHAT_SEARCH_FRIEND, false, 0);
            } else if (CacheTools.getUserData("role") != null && msg.contains("BD")) {

            } else {
                if (getIntent().getIntExtra("index", 0) == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("ind", msg + "");
                    setResult(RESULT_OK, intent);
                    finishActivity();
                } else {
                    showDialog(msg);
                }
            }

            // String bid = msg.substring(msg.lastIndexOf("BD:") + 9,
            // msg.length());
            // // showDialog(bid);
            // AjaxParams params = new AjaxParams();
            // params.put("id", bid);
            // NetworkUtil.post(NetworkUtil.GET_BIZ_BY_ID, params, new
            // AjaxCallBack<Object>() {
            //
            // @Override
            // public void onSuccess(Object t) {
            // super.onSuccess(t);
            // MyLog.i("YUY", "======二维码返回数据====" + t.toString());
            // }
            //
            // @Override
            // public void onFailure(Throwable t, int errorNo, String strMsg) {
            // super.onFailure(t, errorNo, strMsg);
            // MyLog.i("YUY", "======二维码失败返回数据====" + strMsg);
            // }
            // });

            // NetworkUtil.post(NetworkUtil.GET_BIZ_BY_ID, params, new
            // AsyncHttpResponseHandler() {
            // @Override
            // public void onSuccess(int statusCode, Header[] headers, byte[]
            // responseBody) {
            // mProgress.dismiss();
            // String res = new String(responseBody);//
            // mJsonArray.getJSONObject(arg2).toString();
            // try {
            // JSONObject jo = new JSONObject(res);
            // if (jo.getInt("result") == 1) {
            // System.out.println("xxx=" + res);
            // Intent intent = new Intent(CaptureActivity.this,
            // NativeServiceListDetailsActivity.class);
            // intent.putExtra("data", jo.getString("data"));
            // startActivity(intent);
            // finishActivity();
            // } else {
            // Toast.makeText(CaptureActivity.this, "处理失败",
            // Toast.LENGTH_SHORT).show();
            // }
            //
            // } catch (JSONException e) {
            // e.printStackTrace();
            // Toast.makeText(CaptureActivity.this, "处理失败",
            // Toast.LENGTH_SHORT).show();
            // }
            // }
            //
            // @Override
            // public void onFailure(int statusCode, Header[] headers, byte[]
            // responseBody,
            // Throwable error) {
            // mProgress.dismiss();
            // Toast.makeText(CaptureActivity.this, "请求失败",
            // Toast.LENGTH_SHORT).show();
            // }
            // });
        }
        // // 判断是否符合基本的json格式
        // if (!msg.matches("^\\{.*")) {
        // showDialog(msg);
        // } else {
        // try {
        // Barcode barcode = Barcode.parse(msg);
        // int type = barcode.getType();
        // if (barcode.isRequireLogin()) {
        // if (!ac.isLogin()) {
        // UIHelper.showLoginDialog(CaptureActivity.this);
        // return;
        // }
        // }
        // switch (type) {
        // case Barcode.SIGN_IN:// 签到
        // signin(barcode);
        // break;
        // default:
        // break;
        // }
        // } catch (AppException e) {
        // UIHelper.ToastMessage(this, "json数据解析异常");
        // mProgress.dismiss();
        // }
        // }
    }

    /**
     * 扫描结果对话框
     *
     * @param msg
     */
    private void showDialog(final String msg) {
        new AlertDialog.Builder(CaptureActivity.this).setTitle("扫描结果").setMessage("非车务无忧提供的二维码\n内容：" + msg)
                .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgress.dismiss();
                        dialog.dismiss();
                        // 获取剪贴板管理服务
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本数据复制到剪贴板
                        cm.setText(msg);
                        // UIHelper.ToastMessage(CaptureActivity.this, "复制成功");
                        Toast.makeText(CaptureActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgress.dismiss();
                dialog.dismiss();
                restartPreviewAfterDelay(0L);

            }
        }).show();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {

        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AppManager.getAppManager().finishActivity(CaptureActivity.this);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //
    // private void setFlash() {
    // if (flash.getTag() != null) {
    // cameraManager.setTorch(true);
    // flash.setTag(null);
    // flash.setBackgroundResource(R.drawable.flash_open);
    // } else {
    // cameraManager.setTorch(false);
    // flash.setTag("1");
    // flash.setBackgroundResource(R.drawable.flash_default);
    // }
    // }
    //
}
