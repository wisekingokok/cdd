package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.NewFriend;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.utils.VersionUtil;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @version 1.1.0
 * @describe:系统设置
 * @author:yuyong
 * @created:2014-12-10上午1:25:43+
 */
public class SettingActivity extends CDDBaseActivity {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    // 版本检测
    @ViewInject(id = R.id.version_detection, click = "onAction")
    private TextView mVersionDetection;
    // 分享给朋友
    @ViewInject(id = R.id.setting_yssm, click = "onAction")
    private TextView mSettingYssm;
    // 退出当前登录
    @ViewInject(id = R.id.exit_login, click = "onAction")
    private TextView mExitLogin;
    // 免责声明
    @ViewInject(id = R.id.disclaimer_tv, click = "onAction")
    private TextView mDisclaimerTV;
    // 免责声明
    @ViewInject(id = R.id.heimingdan_setting_tv, click = "onAction")
    private TextView mTextView_heimingdan;
    // 当前版本
    private String curVersionName;

    // 查看是否有更新:无更新时显示显示：已是最新版本 否则：有新版本
    @ViewInject(id = R.id.version_update_text)
    private TextView mVersionUpdateText;


    // 更新的版本信息
    private String appVersionStr = "", appUpdateInfo = "", downLoadUrl = "";
    // 账户管理

    // 消息设置
    @ViewInject(id = R.id.message_setting_tv, click = "onAction")
    private TextView mMessageSettingTV;
    private RelativeLayout mTitleHeight;// 标题布局高度
    @ViewInject(id = R.id.safe_setting_tv, click = "onAction")
    private TextView mTextView_safe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_ac);
        initView();
    }

    public void onAction(View v) {
        switch (v.getId()) {

            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.exit_login:
                exitApp();

                break;
            case R.id.safe_setting_tv:
                Intent mIntent = new Intent(SettingActivity.this,
                        SafeSettingActivity.class);
                startActivity(mIntent);

                break;
            case R.id.heimingdan_setting_tv:

                BlacklistActivity.launch(this, null);

                break;
            case R.id.setting_yssm:
                Intent yssmIntent = new Intent(SettingActivity.this,
                        AgreementActivity.class);
                yssmIntent.putExtra("type", 10);
                startActivity(yssmIntent);
                break;
            case R.id.version_detection:
                if (mVersionUpdateText.getText().toString().equals("有新版本")) {
                    final Dialog dialog = new Dialog(SettingActivity.this,
                            R.style.myDialogTheme);
                    LayoutInflater inflater = LayoutInflater
                            .from(SettingActivity.this);
                    final RelativeLayout layout = (RelativeLayout) inflater
                            .inflate(R.layout.update_version_ac, null);
                    dialog.setContentView(layout);
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    ((TextView) layout.findViewById(R.id.versionName))
                            .setText(appVersionStr);

                    String[] logs = appUpdateInfo.split("@");
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < logs.length; i++) {
                        sb.append(logs[i]).append("\n");
                    }
                    ((TextView) layout.findViewById(R.id.update_log)).setText(sb
                            .toString());
                    // 暂不更新
                    Button btnCancel = (Button) layout
                            .findViewById(R.id.not_update_btn);
                    btnCancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            dialog.dismiss();

                        }
                    });
                    // 更新
                    Button btnUpdate = (Button) layout
                            .findViewById(R.id.update_btn);
                    btnUpdate.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            dialog.dismiss();

                            Uri uri = Uri.parse(downLoadUrl);
                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(it);
                            // downLoadApk(downLoadUrl);

                        }
                    });

                } else {
                    Intent appUIntent = new Intent(SettingActivity.this,
                            AppUpdateActivity.class);
                    appUIntent.putExtra("appVersion", appVersionStr);
                    appUIntent.putExtra("appUpdateInfo", appUpdateInfo);
                    startActivity(appUIntent);

                }

                break;

            case R.id.message_setting_tv:
                Intent msgSettingIntent = new Intent(SettingActivity.this,
                        MessageSettingActivity.class);
                startActivity(msgSettingIntent);
                break;
            case R.id.disclaimer_tv:// 免责声明
                Intent mzsmIntent = new Intent(SettingActivity.this,
                        AgreementActivity.class);
                mzsmIntent.putExtra("type", 11);
                startActivity(mzsmIntent);
                break;
            default:
                break;
        }
    }

	/*
     * 从服务器中下载APK
	 */
    // protected void downLoadApk(final String url) {
    // final ProgressDialog pd; // 进度条对话框
    // pd = new ProgressDialog(this);
    // pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    // pd.setMessage("正在下载更新");
    // pd.show();
    // pd.setCanceledOnTouchOutside(false);
    // new Thread() {
    // @Override
    // public void run() {
    // try {
    // File file = Tools.getFileFromServer(url, pd);
    // sleep(3000);
    // installApk(file);
    // pd.dismiss(); // 结束掉进度条对话框
    // } catch (Exception e) {
    //
    // e.printStackTrace();
    // }
    // }
    // }.start();
    // }

    // 安装apk
    // protected void installApk(File file) {
    // Intent intent = new Intent();
    // // 执行动作
    // intent.setAction(Intent.ACTION_VIEW);
    // // 执行的数据类型
    // intent.setDataAndType(Uri.fromFile(file),
    // "application/vnd.android.package-archive");
    // startActivity(intent);
    // }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(SettingActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(SettingActivity.this);
    }

    @Override
    protected void initView() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mHeaderTV.setText(R.string.setting_title);

        // 获取当前客户端版本信息
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            curVersionName = info.versionName;
            // curVersionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        mVersionUpdateText.setText(curVersionName);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            appVersionStr = jo.getString("ver");

                            appUpdateInfo = jo.getString("log");
                            downLoadUrl = jo.getString("url");
                            if (VersionUtil.compareVersion(curVersionName, appVersionStr) == 2) {
                                mVersionUpdateText.setText("有新版本");
                                mVersionUpdateText.setTextColor(Color
                                        .parseColor("#f81953"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:

                        break;
                    default:
                        break;
                }
            }

        }, null, NetworkUtil.APP_VERSION_URL, false, 0);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    /**
     * 退出登录APP
     */
    private void exitApp() {

        if (CacheTools.getUserData("role") == null) {// 判断用户登录用户为空
            Intent intent = new Intent(SettingActivity.this,
                    LunchPageActivity.class);
            startActivity(intent);
        }
        new AlertDialog.Builder(SettingActivity.this).setTitle("退出登录")
                .setMessage("确认退出吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface arg0, int arg1) {
                        requestNet(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case Constant.NET_DATA_SUCCESS:
                                        MyLog.i("YUY", "====注销成功===");
                                        Intent intent = new Intent(SettingActivity.this, LunchPageActivity.class);
                                        Tools.clearInfo(SettingActivity.this);//登出清空当前登录用户信息
                                        startActivity(intent);
                                        try {
                                            AppManager.getAppManager().finishActivity(MainActivityEr.class);
                                        } catch (Exception e) {
                                            MyLog.e("YUY", "关闭类异常");
                                        }
                                        arg0.dismiss();
                                        CacheTools.setUserData("isRead", "1");

                                        //清空afinal数据库
//                                        FinalDb db = FinalDb.create(SettingActivity.this);
//                                        db.deleteAll(NewFriend.class);

                                        break;
                                    case Constant.NET_DATA_FAIL:
                                        ToastUtil.toastShow(SettingActivity.this, ((DataError) msg.obj)
                                                .getErrorMessage());
                                        break;

                                    default:
                                        ToastUtil.toastShow(SettingActivity.this, "网络异常");
                                        break;
                                }
                            }
                        }, null, NetworkUtil.EXIT_LOGIN_URL, false, 0);
                    }

                }).show();

    }

}
