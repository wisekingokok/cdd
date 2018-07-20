package com.chewuwuyou.app.utils;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AppVersion;
import com.chewuwuyou.app.ui.AgreementActivity;

@SuppressLint("NewApi")
public class VersionUtil {
    /**
     * 检测版本/ 弹起广告
     *
     * @param context
     * @param isOpenGuangGao true弹起广告 false 不弹广告
     */
    public static void judgeVersionMsg(final Context context,
                                       final boolean isOpenGuangGao) {

        NetworkUtil.postNoHeader(NetworkUtil.APP_VERSION_URL, null,
                new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        MyLog.i("YUY", "检测版本更新信息 = " + t);
                        try {
                            JSONObject jo = new JSONObject(t);
                            final AppVersion appVer = AppVersion.parse(jo
                                    .getString("data"));
                            MyLog.e("YUY", "local - " + getVersion(context) + " server = " + appVer.getVer());
                            MyLog.e("YUY", "版本号比较-----" + compareVersion(getVersion(context), appVer.getVer()));
                            if (compareVersion(getVersion(context), appVer.getVer()) == 2) {// 比较系统版本小于服务器版本
                                if (appVer.getIsForce() == 1) {// 强制更新
                                    showQZUpdateDialog(context, appVer);
                                } else {// 普通更新
                                    showPTUpdataDialog(context, appVer,
                                            isOpenGuangGao);
                                }
                            } else {
                                if (DateTimeUtil.compareData(appVer.getToTime())
                                        && isOpenGuangGao) {// 过期时间大于当前时间可以弹广告
                                    if (isShowGuangGao(appVer)) {
                                        guangGDialog(context, appVer);
                                    }

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });

    }

    /**
     * 强制更新
     */
    public static void showQZUpdateDialog(final Context context,
                                          final AppVersion appVersion) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.update_version_ac, null);

        dialog.setContentView(layout);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }

            }
        });
        ((TextView) layout.findViewById(R.id.version_title_tv)).setText("新版本 "
                + appVersion.getVer() + " (" + appVersion.getSizeM() + "M)");
        ((TextView) layout.findViewById(R.id.versionName)).setText(appVersion
                .getVer());
        String[] logs = appVersion.getLog().split("@");
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < logs.length; i++) {
            sb.append(logs[i]).append("\n");
        }
        ((TextView) layout.findViewById(R.id.update_log))
                .setText(sb.toString());

        // 暂不更新
        Button btnCancel = (Button) layout.findViewById(R.id.not_update_btn);
        btnCancel.setVisibility(View.GONE);
        // 强制更新
        Button btnUpdate = (Button) layout.findViewById(R.id.update_btn);
        btnUpdate.setBackground(context.getResources().getDrawable(
                R.drawable.qiangzhi_update_btn_bg));
        btnUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Uri uri = Uri.parse(appVersion.getUrl());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(it);
                // downLoadApk(downLoadUrl);

            }
        });

    }

    /**
     * 普通更新
     *
     * @param context
     * @param appVersion
     */
    public static void showPTUpdataDialog(final Context context,
                                          final AppVersion appVersion, final boolean openGunagGao) {

        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.update_version_ac, null);
        dialog.setContentView(layout);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) layout.findViewById(R.id.version_title_tv)).setText("新版本"
                + appVersion.getVer());
        ((TextView) layout.findViewById(R.id.versionName)).setText(appVersion
                .getVer());
        String[] logs = appVersion.getLog().split("@");
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < logs.length; i++) {
            sb.append(logs[i]).append("\n");
        }
        ((TextView) layout.findViewById(R.id.update_log))
                .setText(sb.toString());

        // 暂不更新
        Button btnCancel = (Button) layout.findViewById(R.id.not_update_btn);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

                if (appVersion.getToTime() != null) {
                    if (DateTimeUtil.compareData(appVersion.getToTime())
                            && openGunagGao) {// 过期时间大于当前时间可以弹广告
                        guangGDialog(context, appVersion);

                    }
                }
            }
        });
        // 更新
        Button btnUpdate = (Button) layout.findViewById(R.id.update_btn);
        btnUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

                Uri uri = Uri.parse(appVersion.getUrl());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(it);
                // downLoadApk(downLoadUrl);

            }
        });

    }

    /**
     * 弹出广告提示框
     */
    @SuppressWarnings("deprecation")
    public static void guangGDialog(final Context context,
                                    final AppVersion appVersion) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.gaungg_dialog, null);

        dialog.setContentView(layout);
//        WindowManager windowManager = ((Activity) context).getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth() - 100); // 设置宽度
//        lp.height = (int) (display.getHeight() - 200);
//        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        ImageView iv = (ImageView) layout.findViewById(R.id.guangg_iv);
        ImageUtils.displayImage(appVersion.getImageURL(), iv, 0);

        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(appVersion.getLinkURL())) {
                    Intent intent = new Intent(context, AgreementActivity.class);
                    intent.putExtra("linkURL", appVersion.getLinkURL());
                    intent.putExtra("type", 9);
                    context.startActivity(intent);
                }
                dialog.dismiss();
            }
        });

    }

    /**
     * 获取当前应用的版本号
     *
     * @param ctx
     * @return
     */
    public static String getVersion(Context ctx) {
        String version = null;
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * @param sysVersion    //系统版本号
     * @param serverVersion //服务器版本号
     * @return 0:系统版本号相同 1:系统版本号大于服务器版本号 2：系统版本号小于服务器版本号
     */
    public static int compareVersion(String sysVersion, String serverVersion) {
        if (TextUtils.isEmpty(sysVersion) || TextUtils.isEmpty(serverVersion)) {
            MyLog.e("YUY", "---------版本号为空----");
            return 0;
        }
        if (sysVersion.equals(serverVersion)) {
            return 0;
        }
        String[] version1Array = sysVersion.split("\\.");
        String[] version2Array = serverVersion.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }

            return 0;
        } else {
            return diff > 0 ? 1 : 2;

        }

    }

    /**
     * 是否显示广告
     */
    public static boolean isShowGuangGao(AppVersion appVersion) {
        if (appVersion.getImageURL() == null) {//图片地址为空
            return false;
        }
        if (CacheTools.getOtherCacheData("guangGaoUrl") == null && !TextUtils.isEmpty(appVersion.getImageURL())) {//显示广告并且存储图片链接
            //显示广告
            //缓存
            CacheTools.setOtherCacheData("guangGaoUrl", appVersion.getImageURL());
            return true;
        } else if (!appVersion.getImageURL().equals(CacheTools.getOtherCacheData("guangGaoUrl"))) {
            //显示广告
            return true;
        } else {//不显示
            return false;
        }


    }


}
