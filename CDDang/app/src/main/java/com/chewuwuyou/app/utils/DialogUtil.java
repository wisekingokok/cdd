package com.chewuwuyou.app.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chewuwuyou.app.AppManager;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.callback.FilterEnterActionListener;
import com.chewuwuyou.app.transition_view.activity.LoginActivity;
import com.chewuwuyou.app.ui.BaseActivity;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.ui.LunchPageActivity;
import com.chewuwuyou.app.ui.MainActivityEr;
import com.chewuwuyou.app.ui.OrderManagerActivity;
import com.chewuwuyou.app.utils.AsyncBitmapLoader.ImageCallBack;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.1.0
 * @describe:对话框工具类
 * @author:yuyong
 * @created:2014-10-30下午4:40:09
 */
public class DialogUtil {

    /**
     * 联系客服对话框的工具类
     *
     * @param context 标题
     */
    public static void showContactServiceDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.common_dialog, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        // 确定
        Button btnok = (Button) layout.findViewById(R.id.btnok);
        btnok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + NetworkUtil.phonenum));
                context.startActivity(phoneIntent);
                dialog.dismiss();

            }
        });
        // 取消
        Button btnCancel = (Button) layout.findViewById(R.id.btncancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

    }

    /***
     * 发布任务成功对话框
     *
     * @param activity
     * @param isBusiness :判断是商家还是用户：商家则跳转到OrderManagerActivity,反之则调到TaskManagerActivity
     */
    public static void releaseTaskServiceDialog(final Activity activity, final String isBusiness) {
        final Dialog dialog = new Dialog(activity, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.release_task_dialog, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        // 确定
        Button btnok = (Button) layout.findViewById(R.id.back_home_page_btn);
        btnok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(activity, MainActivityEr.class);
                activity.startActivity(intent);
                // activity.finish();
                dialog.dismiss();

            }
        });
        // 取消
        // Button btnCancel = (Button)
        // layout.findViewById(R.id.order_manager_btn);
        // btnCancel.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        // if (isBusiness.contains("2")) {
        // Intent intent = new Intent(activity, OrderManagerActivity.class);
        // activity.startActivity(intent);
        // } else {
        // Intent intent = new Intent(activity, TaskManagerActivity.class);
        // activity.startActivity(intent);
        // }
        // activity.finish();
        // dialog.dismiss();
        // }
        // });
        dialog.setCanceledOnTouchOutside(false);
    }

    /***
     * 拆分任务成功对话框
     *
     * @param activity
     */
    public static void caifTaskServiceDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.caif_task_dialog, null);

        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        // 到我的订单
        Button btnMyOrder = (Button) layout.findViewById(R.id.back_my_order_btn);
        btnMyOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(activity, OrderManagerActivity.class);
                activity.startActivity(intent);
                activity.finish();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 选择时间对话框
     *
     * @param context
     * @param tv
     */
    public static void showTimeDialog(Context context, final TextView tv) {
        Calendar c;
        Dialog dialog = null;
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                tv.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH)); // 传入天数
        // 设置dialog的样式
        // Window window = dialog.getWindow();
        // WindowManager.LayoutParams lp = window.getAttributes();
        // // // 设置透明度为0.3
        // lp.alpha = 0.6f;
        // window.setAttributes(lp);
        // // window.setBackgroundDrawableResource(R.drawable.common_dialog_bg);
        // window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
        // WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     * 选择时间对话框
     *
     * @param context
     * @param tv
     */
    public static void chooseDateDialog(Context context, final TextView tv) {
        Calendar c;
        Dialog dialog = null;
        c = Calendar.getInstance();
        dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                tv.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
            }
        }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH)); // 传入天数
        // // 设置dialog的样式
        // Window window = dialog.getWindow();
        // WindowManager.LayoutParams lp = window.getAttributes();
        // // // 设置透明度为0.3
        // lp.alpha = 0.6f;
        // window.setAttributes(lp);
        // window.setBackgroundDrawableResource(R.drawable.common_dialog_bg);
        // window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
        // WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void chooseTimeDialog(Context context, final TextView tv) {

        Calendar calendar;
        Dialog dialog = null;
        calendar = Calendar.getInstance();
        dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timerPicker, int hourOfDay, int minute) {
                tv.setText(hourOfDay + "时" + minute + "分");
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);// 是否为二十四制

        // 设置dialog的样式
        /*
         * Window window = dialog.getWindow(); WindowManager.LayoutParams lp =
		 * window.getAttributes(); // // 设置透明度为0.3 lp.alpha = 0.6f;
		 * window.setAttributes(lp);
		 * window.setBackgroundDrawableResource(R.drawable.common_dialog_bg);
		 * window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
		 * WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		 */
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showSavezJImg(Context context, String url) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.download_img, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        ImageView mShowImg = (ImageView) layout.findViewById(R.id.zj_img);
        Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(mShowImg, ServerUtils.getServerIP(url), new ImageCallBack() {

            @Override
            public void imageLoad(View imageView, Bitmap bitmap) {
                ((ImageView) imageView).setImageBitmap(bitmap);
            }
        });
        if (bitmap != null) {
            mShowImg.setImageBitmap(bitmap);
        }
        // 确定
        Button btnSave = (Button) layout.findViewById(R.id.save_btn);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 选择服务所需资料提示
     *
     * @param context
     * @param chooseP
     */
    public static void transactionDataDialog(final Context context, final String chooseP) {
        final Dialog dialog = new Dialog(context, R.style.Transparent);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.transaction_data_ac, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        ((TextView) layout.findViewById(R.id.sub_header_bar_tv)).setText(R.string.sxcl_title);
        layout.findViewById(R.id.sub_header_bar_left_ibtn).setVisibility(View.GONE);
        final LinearLayout mLinearLayout = (LinearLayout) layout.findViewById(R.id.transaction_data_layout);
        String choosePP[] = chooseP.split(",");
        for (int i = 0, len = choosePP.length; i < len; i++) {
            if (choosePP[i].equals("301")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.jzns_title),
                        context.getResources().getString(R.string.jzns_data));

            }
            if (choosePP[i].equals("302")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.jzzx_title),
                        context.getResources().getString(R.string.jzzx_content));

            }
            if (choosePP[i].equals("303")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.bbjz_title),
                        context.getResources().getString(R.string.bbjz_data));

            }
            if (choosePP[i].equals("304")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.dqhz_title),
                        context.getResources().getString(R.string.dqhz_data));

            }
            if (choosePP[i].equals("305")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.ydzr_title),
                        context.getResources().getString(R.string.ydzr_data));

            }
            if (choosePP[i].equals("306")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.jzwz_title),
                        context.getResources().getString(R.string.jzwz_data));

            }
            if (choosePP[i].equals("307")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.sgzdb_title),
                        context.getResources().getString(R.string.sgzdb_data));

            }
            if (choosePP[i].equals("308")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.qit_title),
                        context.getResources().getString(R.string.qit_data));

            }
            if (choosePP[i].equals("201")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.clnj_title),
                        context.getResources().getString(R.string.clnj_data));

            }
            if (choosePP[i].equals("202")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.clgh_title),
                        context.getResources().getString(R.string.clgh_data));

            }
            if (choosePP[i].equals("203")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.clsp_title),
                        context.getResources().getString(R.string.clsp_data));

            }
            if (choosePP[i].equals("204")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.tdzj_title),
                        context.getResources().getString(R.string.tdzj_data));

            }
            if (choosePP[i].equals("205")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.bbdjz_title),
                        context.getResources().getString(R.string.bbdjz_data));

            }
            if (choosePP[i].equals("207")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.bbpz_title),
                        context.getResources().getString(R.string.bbpz_data));

            }
            if (choosePP[i].equals("208")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.dkwts_title),
                        context.getResources().getString(R.string.dkwts_data));

            }
            if (choosePP[i].equals("209")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.dlhbbz_title),
                        context.getResources().getString(R.string.dlhbbz_data));

            }
            if (choosePP[i].equals("210")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.cdcjh_title),
                        context.getResources().getString(R.string.cdcjh_data));

            }
            if (choosePP[i].equals("206")) {
                addDataView(mLinearLayout, context, context.getResources().getString(R.string.bbxsz_title),
                        context.getResources().getString(R.string.bbxsz_data));

            }
        }
        // 确定
        Button btnSave = (Button) layout.findViewById(R.id.know_btn);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 通过选择的业务办理项来添加view
     *
     * @param mLin
     * @param context
     * @param title
     * @param content
     */
    public static void addDataView(LinearLayout mLin, Context context, String title, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.zl_data_layout, null);
        TextView mTitle = (TextView) layout.findViewById(R.id.zl_title);
        TextView mContent = (TextView) layout.findViewById(R.id.zl_content);
        mTitle.setText(title);
        mContent.setText(content);
        mLin.addView(layout);
    }

    /**
     * 修改名称
     *
     * @param activity
     * @param name     ：进入显示已修改的名称
     * @param role     ：区分商家和用户：1、2
     */
    public static void updateNameDialog(final Activity activity, String name, final TextView mName, final int role) {
        final Dialog dialog = new Dialog(activity, R.style.Transparent);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.update_name_dialog, null);
        RelativeLayout mTitleHeight = (RelativeLayout) layout.findViewById(R.id.title_height);
        isTitle(activity, mTitleHeight);// 根据不同手机判断
        ImageButton delete = (ImageButton) layout.findViewById(R.id.delete);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        final EditText mUserName = (EditText) layout.findViewById(R.id.update_name);
//        mUserName.setOnEditorActionListener(new FilterEnterActionListener());
        InputFilter[] filters = {new EditTextLengthFilter(activity, 16)};
        mUserName.setFilters(filters);
        mUserName.setText(name);
        ((TextView) layout.findViewById(R.id.sub_header_bar_tv)).setText(R.string.update_name);
        layout.findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mUserName.setText("");
            }
        });
        TextView save = (TextView) layout.findViewById(R.id.sub_header_bar_right_tv);
        save.setText("保存");
        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(mUserName.getText().toString())) {
                    ToastUtil.showToast(activity, R.string.please_input_update_name);
                } else {
                    if (role == 1) {
                        AjaxParams params = new AjaxParams();
                        params.put("nickName", mUserName.getText().toString());
                        ((CDDBaseActivity) activity).requestNet(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case Constant.NET_DATA_SUCCESS:
                                        mName.setText(mUserName.getText().toString());
                                        dialog.dismiss();
                                        break;
                                    case Constant.NET_DATA_FAIL:
                                        ToastUtil.toastShow(activity, ((DataError) msg.obj).getErrorMessage());
                                        break;
                                }
                            }
                        }, params, NetworkUtil.UPDATE_INFO_URL, false, 0);

                    } else {
                        AjaxParams params = new AjaxParams();
                        params.put("businessName", mUserName.getText().toString());
                        ((BaseActivity) activity).requestNet(new Handler() {

                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case Constant.NET_DATA_SUCCESS:
                                        mName.setText(mUserName.getText().toString());
                                        dialog.dismiss();
                                        break;
                                    case Constant.NET_DATA_FAIL:
                                        ToastUtil.toastShow(activity, ((DataError) msg.obj).getErrorMessage());
                                        break;
                                }
                            }

                        }, params, NetworkUtil.UPDATE_BUSINESS_INFO, false, 1);
                    }

                }

                //新接口。。
                AjaxParams params = new AjaxParams();
                params.put("userId", CacheTools.getUserData("rongUserId"));
                params.put("name", mUserName.getText().toString());

                NetworkUtil.get(NetworkUtil.REFRESH_USER, params, new AjaxCallBack<String>() {
                    @Override
                    public boolean isProgress() {
                        return super.isProgress();
                    }

                    @Override
                    public void onSuccess(String s) {
                        super.onSuccess(s);
                        JSONObject m = null;
                        try {
                            JSONObject j = new JSONObject(s);
                            ErrorCodeUtil.doErrorCode(activity, j.getInt("code"), j.getString("message"));
                            MyLog.i("已上传图片至新接口");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);

                    }
                });


            }
        });


    }

    /**
     * 修改心情
     *
     * @param activity
     * @param mName
     */
    public static void updateMoodDialog(final Activity activity, final TextView mName) {
        final Dialog dialog = new Dialog(activity, R.style.Transparent);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.update_mood_dialog, null);
        layout.setAlpha(1f);
        dialog.setContentView(layout);
        dialog.show();
        final EditText mUserName = (EditText) layout.findViewById(R.id.editText);
        ((TextView) layout.findViewById(R.id.sub_header_bar_tv)).setText("修改心情");
        layout.findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        mUserName.setOnEditorActionListener(new FilterEnterActionListener());
        TextView save = (TextView) layout.findViewById(R.id.sub_header_bar_right_tv);
        save.setText("保存");
        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AjaxParams params = new AjaxParams();
                params.put("signature", mUserName.getText().toString());
                ((CDDBaseActivity) activity).requestNet(new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case Constant.NET_DATA_SUCCESS:
                                mName.setText(mUserName.getText().toString());
                                ToastUtil.toastShow(activity, "修改成功");
                                dialog.dismiss();
                                break;

                            case Constant.NET_DATA_FAIL:
                                ToastUtil.toastShow(activity, ((DataError) msg.obj).getErrorMessage());
                                dialog.dismiss();
                                break;
                            default:
                                ToastUtil.toastShow(activity, "修改失败");
                                dialog.dismiss();
                                break;

                        }
                    }

                }, params, NetworkUtil.UPDATE_INFO_URL, false, 0);
            }
        });
    }

    /**
     * 修改爱好
     *
     * @param activity
     * @param mName
     */
    public static void updateHappiesDialog(final Activity activity, final TextView mName) {
        final Dialog dialog = new Dialog(activity, R.style.Transparent);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.update_happise_dialog, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        final EditText mUserName = (EditText) layout.findViewById(R.id.editText);
        ((TextView) layout.findViewById(R.id.sub_header_bar_tv)).setText("修改兴趣爱好");
        layout.findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        TextView save = (TextView) layout.findViewById(R.id.sub_header_bar_right_tv);
        mUserName.setOnEditorActionListener(new FilterEnterActionListener());
        save.setText("保存");
        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mName.setText(mUserName.getText().toString());
                AjaxParams params = new AjaxParams();
                params.put("hobby", mUserName.getText().toString());
                ((CDDBaseActivity) activity).requestNet(new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case Constant.NET_DATA_SUCCESS:
                                mName.setText(mUserName.getText().toString());
                                ToastUtil.toastShow(activity, "修改成功");
                                dialog.dismiss();
                                break;

                            case Constant.NET_DATA_FAIL:
                                ToastUtil.toastShow(activity, ((DataError) msg.obj).getErrorMessage());
                                dialog.dismiss();
                                break;
                            default:
                                ToastUtil.toastShow(activity, "修改失败");
                                dialog.dismiss();
                                break;

                        }
                    }

                }, params, NetworkUtil.UPDATE_INFO_URL, false, 0);

            }
        });
    }

    /**
     * 当请求网络失败和无网时的提示
     *
     * @param prompt 提示内容
     */
    public static void showNetDateFailPromptDialog(Context context, String prompt) {

        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.network_anomaly_dialog, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        TextView promptTV = (TextView) layout.findViewById(R.id.net_fail_prompt_tv);
        promptTV.setText(prompt);
    }

    /**
     * 用户在其他地方登陆
     *
     * @param ac
     */
    public static void loginInOtherPhoneDialog(final Activity ac) {
        if (ac == null)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(ac);
        builder.setTitle("您的账户在另一台手机登录");

        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(ac, LunchPageActivity.class);
                Tools.clearInfo(ac);
                ac.startActivity(intent);
                ac.finish();
            }
        });
        if (ac != null)
            builder.create().show();
    }

    /**
     * 用户在其他地方登陆
     *
     * @param app
     */
    public static void loginInOtherPhoneDialog(final Application app) {
        AlertDialog.Builder builder = new AlertDialog.Builder(app);
        builder.setTitle("您的账户在另一台手机登录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(app, LoginActivity.class);
                Tools.clearInfo(app);
                app.startActivity(intent);
            }
        });
        builder.create().show();
    }

    /**
     * 选择车辆地区编码
     *
     * @param ac
     * @param TV
     */
    @SuppressWarnings("deprecation")
    public static void districeCodingDialog(final Activity ac, final TextView TV) {
        final Dialog dialog = new Dialog(ac, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(ac);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.districe_coding_dialog, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        WindowManager windowManager = ac.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        final String[] array = ac.getResources().getStringArray(R.array.palte_location_code);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("textItem", array[i]);// 按序号添加ItemText
            items.add(item);
        }
        GridView mDistriceCodingGrid = (GridView) layout.findViewById(R.id.districe_coding_grid);
        // 实例化一个适配器
        SimpleAdapter adapter = new SimpleAdapter(ac, items, R.layout.plate_diqu_item, new String[]{"textItem"},
                new int[]{R.id.item_text});
        mDistriceCodingGrid.setAdapter(adapter);
        mDistriceCodingGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                TV.setText(array[arg2]);
                dialog.dismiss();
            }
        });
        Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);

    }

    public static void showLargeImageDialog(Context context, String url) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.large_image_dialog, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
        ImageView mPhotoIV = (ImageView) layout.findViewById(R.id.photo_iv);
        Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(mPhotoIV, ServerUtils.getServerIP(url), new ImageCallBack() {

            @Override
            public void imageLoad(View imageView, Bitmap bitmap) {
                ((ImageView) imageView).setImageBitmap(bitmap);
            }
        });
        if (bitmap != null) {
            mPhotoIV.setImageBitmap(bitmap);
        }
        // 确定
        ImageView downloadIV = (ImageView) layout.findViewById(R.id.download_iv);
        downloadIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 通过选择的业务办理项来添加view
     */
    public static void showHelpPageDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.show_help_page_iv, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        dialog.show();
    }

    public static void call(final String tell, final Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("确认拨打：" + tell);
        dialog.setPositiveButton("拨打", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tell));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.toastShow(context,"打电话权限被拒绝");
                    return;
                }
                context.startActivity(intent);
                arg0.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.create().show();
    }

    /**
     * 支付密码 提交dialog
     */
    public static Dialog submitDialog(final CDDBaseActivity activity) {

        Dialog dialog = new Dialog(activity, R.style.waitingPay_progress_dialog);

        dialog.setContentView(R.layout.waitingpay_process_dialog);
        dialog.setCancelable(false);
        dialog.setTitle(null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {

                return false;
            }
        });
        return dialog;

    }

    /**
     * 密保回答错误
     */
    public static Dialog wrongPayDialog(final CDDBaseActivity activity) {

        Dialog dialog = new Dialog(activity, R.style.wrongPay_progress_dialog);

        dialog.setContentView(R.layout.wrongpay_process_dialog);
        dialog.setCancelable(false);
        dialog.setTitle(null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {

                return false;
            }
        });
        return dialog;

    }

    /**
     * 支付密码 成功dialog
     */
    public static Dialog rightPayDialog(final CDDBaseActivity activity) {

        Dialog dialog = new Dialog(activity, R.style.rightPay_progress_dialog);

        dialog.setContentView(R.layout.rightpay_process_dialog);
        dialog.setCancelable(false);
        dialog.setTitle(null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
                return false;
            }
        });
        return dialog;

    }


    /**
     * 判断手机版本是否是4.4以上 不是显示false里面的布局
     */
    public static void isTitle(Activity context, RelativeLayout mTitleHeight) {
        android.view.ViewGroup.LayoutParams layout = mTitleHeight
                .getLayoutParams();
        mTitleHeight.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int density = metric.densityDpi;
        // 240当前手机的像素


        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = context.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            layout.height = 130 * density / 320;
        } else {
            layout.height = 64 * density / 240;
        }
        mTitleHeight.setLayoutParams(layout);
    }

    /**
     * 提示dialog
     *
     * @param context
     * @param msg
     */
    public static void showTSDialog(Context context, String msg) {

        new com.chewuwuyou.app.utils.AlertDialog(context).builder().setTitle("温馨提示").setMsg(msg).setNegativeButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    /**
     * 角色变更提示
     */
    public static void roleChange(final Context context) {
        new android.app.AlertDialog.Builder(context).setTitle("退出登录").setMessage("权限变动，请重新登录").setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(context,
                                LoginActivity.class);
                        Tools.clearInfo(context);//清空当前登录用户信息
                        AppManager.getAppManager().finishAllActivity();//关闭所有activity
                        context.startActivity(intent);
                        arg0.dismiss();
                    }
                }).show();
    }


    public static void showlDiaolog(Context mContext, String title, String content, final DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder mAlertDialog;//
        mAlertDialog = new AlertDialog.Builder(mContext);

        mAlertDialog.setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        clickListener.onClick(dialog, which);
                    }
                })
                .create().show();

    }


    public static ProgressDialog showProgressDialog(Context mContext, String content) {

        ProgressDialog m = ProgressDialog.show(mContext, null, content, false, true);

        return m;
    }


    public static ProgressDialog showProgressDialog(Context mContext) {

        ProgressDialog m = ProgressDialog.show(mContext, null, "请稍后……", false, true);

        return m;

    }

}
