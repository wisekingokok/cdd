package com.chewuwuyou.app.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ExamineBook;
import com.chewuwuyou.app.bean.PersonalInfo;
import com.chewuwuyou.app.ui.AboutAppActivity;
import com.chewuwuyou.app.ui.BusinessAuthenticationAdopt;
import com.chewuwuyou.app.ui.ChooseVehicleActivity;
import com.chewuwuyou.app.ui.ComplainSuggestionActivity;
import com.chewuwuyou.app.ui.IDVerificationActivity;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.MyWorkActivity;
import com.chewuwuyou.app.ui.OtherYueActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.QuanActivity;
import com.chewuwuyou.app.ui.SettingActivity;
import com.chewuwuyou.app.ui.TaskManagerActivity;
import com.chewuwuyou.app.ui.TieActivity;
import com.chewuwuyou.app.ui.VerifyPayActivity;
import com.chewuwuyou.app.ui.WalletActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.KeyboardUtil;
import com.chewuwuyou.app.utils.KeyboardUtil.InputFinishListener;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的 liuchun
 * <p>
 * 向修改
 */
public class MyFragment extends BaseFragment implements OnClickListener {

    private View view;

    private LinearLayout mWork, mWallet, mVehicle, mAuthentication, mShare,
            mSuggestion, mScore, mAbout, mDetails;// 秘书，钱包，车辆，认证,分享,反馈,评分,关于,详情
    private PersonalInfo mPersonalInfo;// 个人信息实体
    private ImageView mPortrait;// 头像
    private TextView mMyUserName, mDang;

    private LinearLayout mBusinessAuthentication;//商家认证

    private TextView mWorkTV, mWorkTip;// 秘书
    private TextView mSetUp;// 设置
    private LayoutInflater mLinearLayout;// 加载dilog布局
    private Dialog mDdialog;// 关闭提示框
    private TextView mDialogTetil;// 标题，内容;
    private Button mWsCancel, mWsConfirm;// 取消,确认
    private ImageView mDelegateIV;// 代理图标
    private TextView mVIPTV;// VIP描述 驻店商家 会员商家
    private TextView myQuan, myPost, myActivtiy;
    private LinearLayout quan_ll, tie_ll, activity_ll;
    private KeyboardView keyboardView;
    private KeyboardUtil keyBoard;
    private ExamineBook response;

    Drawable drawable;//vip svip的显示

    // private TextView mPayErrorTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        registerReceiver();//注册广播
        initView();
        initData();
        initEvent();
        return view;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.chewuwuyou.chequan.tizi");
        intentFilter.addAction("com.chewuwuyou.app.ui.personinfo");
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 点击事件
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.onclick_work:
                StatService.onEvent(getActivity(), "ClickMain1ActivityMyOrder",
                        "点击主界面我的订单或秘书");
                if (CacheTools.getUserData("role") != null
                        && (CacheTools.getUserData("role").contains("2") || CacheTools
                        .getUserData("role").contains("3"))) {// A类商家和B类商家都可进入秘书台
                    Intent oIntent = new Intent(getActivity(), MyWorkActivity.class);
                    startActivity(oIntent);
                } else if (CacheTools.getUserData("role") != null) {
                    Intent tIntent = new Intent(getActivity(),
                            TaskManagerActivity.class);
                    startActivity(tIntent);
                } else {
                    Intent lIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(lIntent);
                }
                break;
            case R.id.onclick_wallet:
                StatService.onEvent(getActivity(), "ClickMainWallet", "点击主界面钱包");
                if (CacheTools.getUserData("role") != null) {
                    // TODO 判断是否开启设置支付密码 本版本不开启此功能
                    // if(CacheTools.getUserData("isPayPass")!=null&&CacheTools.getUserData("isPayPass").equals("1")){
                    // createWalletPayDialog(getActivity());
                    // }else{
                    Intent intent = new Intent(getActivity(), WalletActivity.class);
                    startActivity(intent);
                    // }
                } else {
                    Intent lIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(lIntent);
                }
                break;
            case R.id.onclick_vehicle:
                StatService.onEvent(getActivity(), "ClickMain1ActivityVehiManager",
                        "点击主界面车辆管理");
                if (CacheTools.getUserData("role") != null) {
                    Intent vIntent = new Intent(getActivity(),
                            ChooseVehicleActivity.class);
                    startActivity(vIntent);
                } else {
                    Intent lIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(lIntent);
                }
                break;
            case R.id.onclick_authentication:
                mAuthentication.setClickable(false);
                StatService.onEvent(getActivity(), "IDVerificationActivity",
                        "点击主界面商家认证");
                ToastUtil.toastShow(getActivity(), "敬请期待!");
                break;
            case R.id.onclick_setup:
                StatService.onEvent(getActivity(), "ClickMain1ActivitySettng",
                        "点击主界面设置");
                if (CacheTools.getUserData("role") != null) {
                    Intent sIntent = new Intent(getActivity(),
                            SettingActivity.class);
                    startActivity(sIntent);
                } else {
                    Intent lIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(lIntent);
                }
                break;
            case R.id.my_details:
                if (CacheTools.getUserData("telephone") != null
                        && !"".equals(CacheTools.getUserData("telephone"))) {
                    StatService.onEvent(getActivity(), "clickPersonLayout",
                            "进入个人中心");
                    Intent peIntent = new Intent(getActivity(),
                            PersonalHomeActivity2.class);
                    peIntent.putExtra("skip", 1);
                    startActivity(peIntent);
                } else {
                    Intent lIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(lIntent);
                }
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/*");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "www.cddang.com");
                startActivity(sendIntent);
                break;
            case R.id.suggestion_setting:
                Intent sugIntent = new Intent(getActivity(),
                        ComplainSuggestionActivity.class);
                startActivity(sugIntent);
                break;
            case R.id.score_setting:
                // Uri uri = Uri.parse("market://details?id=" +
                // getActivity().getPackageName());
                // Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);
                ToastUtil.toastShow(getActivity(), "敬请期待!");
                break;
            case R.id.about_setting:
                Intent appIntent = new Intent(getActivity(), AboutAppActivity.class);
                startActivity(appIntent);
                break;
            case R.id.quan_ll:
                Intent intent_quan = new Intent(getActivity(), QuanActivity.class);
                intent_quan.setAction("com.chewuwuyou.app.other_quan_wen");
                intent_quan.putExtra("other_id", CacheTools.getUserData("userId"));
                intent_quan.putExtra("other_name", !TextUtils.isEmpty(mPersonalInfo.getNickName()) ? mPersonalInfo.getNickName() : mPersonalInfo.getUserName());
                startActivity(intent_quan);
                break;
            case R.id.tie_ll:
                Intent intent2 = new Intent(getActivity(), TieActivity.class);
                intent2.putExtra("other_id", CacheTools.getUserData("userId"));
                intent2.putExtra("other_name", !TextUtils.isEmpty(mPersonalInfo
                        .getNickName()) ? mPersonalInfo.getNickName()
                        : mPersonalInfo.getUserName());
                startActivity(intent2);
                break;
            case R.id.activity_ll:
                Intent intent3 = new Intent(getActivity(), OtherYueActivity.class);
                intent3.setAction("com.chewuwuyou.app.other_yue");
                intent3.putExtra("other_id", CacheTools.getUserData("userId"));
                intent3.putExtra("other_name", !TextUtils.isEmpty(mPersonalInfo
                        .getNickName()) ? mPersonalInfo.getNickName()
                        : mPersonalInfo.getUserName());
                startActivity(intent3);
                break;

            case R.id.business_authentication://商家认证
                businessAuthentication();
                break;

            default:
                break;
        }

    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mPersonalInfo = new PersonalInfo();

        mPortrait = (ImageView) view.findViewById(R.id.my_user_portrait);
        mBusinessAuthentication = (LinearLayout) view.findViewById(R.id.business_authentication);
        mWork = (LinearLayout) view.findViewById(R.id.onclick_work);
        mWallet = (LinearLayout) view.findViewById(R.id.onclick_wallet);
        mVehicle = (LinearLayout) view.findViewById(R.id.onclick_vehicle);
        // mCommunity = (LinearLayout)
        // view.findViewById(R.id.onclick_community);
        // mRemind = (LinearLayout) view.findViewById(R.id.onclick_remind);
        mAuthentication = (LinearLayout) view
                .findViewById(R.id.onclick_authentication);
        mSetUp = (TextView) view.findViewById(R.id.onclick_setup);

        mMyUserName = (TextView) view.findViewById(R.id.my_user_name);
//        mDang = (TextView) view.findViewById(R.id.d_dang);
        mWorkTV = (TextView) view.findViewById(R.id.work_tv);
        mWorkTip = (TextView) view.findViewById(R.id.work_tip_tv);
        mScore = (LinearLayout) view.findViewById(R.id.score_setting);
        mAbout = (LinearLayout) view.findViewById(R.id.about_setting);
        mShare = (LinearLayout) view.findViewById(R.id.share);
        mSuggestion = (LinearLayout) view.findViewById(R.id.suggestion_setting);
        mDetails = (LinearLayout) view.findViewById(R.id.my_details);
        mDelegateIV = (ImageView) view.findViewById(R.id.delegate_iv);

        myQuan = (TextView) view.findViewById(R.id.my_quan);
        myPost = (TextView) view.findViewById(R.id.my_post);
        myActivtiy = (TextView) view.findViewById(R.id.my_activtiy);
        mVIPTV = (TextView) view.findViewById(R.id.vip_tv);
        quan_ll = (LinearLayout) view.findViewById(R.id.quan_ll);
        tie_ll = (LinearLayout) view.findViewById(R.id.tie_ll);
        activity_ll = (LinearLayout) view.findViewById(R.id.activity_ll);
        if (CacheTools.getUserData("role") == null) {
            mWorkTV.setText("我的订单");
            mWorkTV.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.my_order_icon), null, null, null);
            mWorkTip.setText("显示全部");
        } else if (CacheTools.getUserData("role").contains("2")
                || CacheTools.getUserData("role").contains("3")) {
            mWorkTV.setText("秘书服务");
            mWorkTip.setText("工作平台");
        } else {
            mWorkTV.setText("我的订单");
            mWorkTV.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.my_order_icon), null, null, null);
            mWorkTip.setText("查看全部");
        }
        if (CacheTools.getUserData("role") == null) {
            mDelegateIV.setVisibility(View.GONE);
            mVIPTV.setVisibility(View.GONE);
            return;
        }
        if (CacheTools.getUserData("daiLitype") != null) {
            if (!CacheTools.getUserData("daiLitype").equals("1")
                    && !CacheTools.getUserData("daiLitype").equals("2")
                    && !CacheTools.getUserData("daiLitype").equals("3")) {
                mDelegateIV.setVisibility(View.GONE);
            }
        }
        if (CacheTools.getUserData("role") != null) {

            if (CacheTools.getUserData("role").contains("2")) {// 驻店商家
                mVIPTV.setText("SVIP");
                mVIPTV.setVisibility(View.VISIBLE);
                drawable=getResources().getDrawable(R.drawable.vip_lv);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                mVIPTV.setCompoundDrawables(drawable,null,null,null);

            } else if (CacheTools.getUserData("role").contains("3")) {// 会员商家
                mVIPTV.setText("VIP");
                mVIPTV.setVisibility(View.VISIBLE);
                drawable=getResources().getDrawable(R.drawable.huiyuan);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                mVIPTV.setCompoundDrawables(drawable,null,null,null);

            } else {
                mVIPTV.setVisibility(View.GONE);
            }

            if (CacheTools.getUserData("role").contains("2") || CacheTools.getUserData("role").contains("3")) {
                mScore.setVisibility(View.VISIBLE);
                mBusinessAuthentication.setVisibility(View.GONE);
            } else {
                mScore.setVisibility(View.GONE);
                mBusinessAuthentication.setVisibility(View.VISIBLE);
            }

        }
        if (CacheTools.getUserData("daiLitype").equals("1")) {// 省代
            mDelegateIV.setImageResource(R.drawable.province_daili_icon);
        }
        if (CacheTools.getUserData("daiLitype").equals("2")) {// 市代
            mDelegateIV.setImageResource(R.drawable.city_daili_icon);
        }
        if (CacheTools.getUserData("daiLitype").equals("3")) {// 区代
            mDelegateIV.setImageResource(R.drawable.district_daili_icon);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        userNetwork();
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        userNetwork();// 访问网络加载个人信息
    }

    /**
     * 点击事件
     */
    @Override
    protected void initEvent() {
        mWork.setOnClickListener(this);
        mWallet.setOnClickListener(this);
        mVehicle.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mScore.setOnClickListener(this);
        mAuthentication.setOnClickListener(this);
        mSetUp.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mDetails.setOnClickListener(this);
        quan_ll.setOnClickListener(this);
        tie_ll.setOnClickListener(this);
        activity_ll.setOnClickListener(this);
        mBusinessAuthentication.setOnClickListener(this);
    }

    private void userNetwork() {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mPersonalInfo = PersonalInfo.parse(msg.obj.toString());
                        CacheTools.setOtherCacheData("headUrl", mPersonalInfo.getUrl());
                        if (!TextUtils.isEmpty(mPersonalInfo.getUrl())) {
                            ImageUtils.displayImage(mPersonalInfo.getUrl(), mPortrait, 10, R.drawable.user_fang_icon, R.drawable.user_fang_icon);
                            CacheTools.setUserData(Constant.HEAD_URL, mPersonalInfo.getUrl());
                        }
                        if (!TextUtils.isEmpty(mPersonalInfo.getNickName())) {
                            CacheTools.setUserData(Constant.NIKE_NAME, mPersonalInfo.getNickName());
                            mMyUserName.setText(mPersonalInfo.getNickName());

                        } else {
                            CacheTools.setUserData(Constant.USER_NAME, mPersonalInfo.getNickName());
                            mMyUserName.setText(mPersonalInfo.getUserName());
                        }
//                        mDang.setText("ID: " + mPersonalInfo.getUserName());
                        CacheTools.setUserData(Constant.USER_ID, mPersonalInfo.getUserName());
                        myQuan.setText(mPersonalInfo.getQuanWens() + "");
                        myPost.setText(mPersonalInfo.getTieZis() + "");
                        myActivtiy.setText(mPersonalInfo.getYueYues() + "");
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(getActivity(),
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        if (CacheTools.getOtherCacheData("headUrl") != null) {
                            String path = ImageLoader.getInstance().getDiscCache()
                                    .get(CacheTools.getOtherCacheData("headUrl"))
                                    .getPath();
                            ImageLoader.getInstance().displayImage(
                                    "file://" + path, mPortrait);
                        }
                        break;
                }
            }
        }, null, NetworkUtil.SELECT_PERSONAL_DATA, false, 1);
    }

    @SuppressWarnings("deprecation")
    public void createWalletPayDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.wallet_pay_layout, null);
        layout.setAlpha(1);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle); // 添加动画

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        ImageView closePayIV = (ImageView) layout
                .findViewById(R.id.wallet_pay_close_iv);
        TextView mForgetPayPassTV = (TextView) layout
                .findViewById(R.id.forget_pay_password_tv);
        // mPayErrorTV = (TextView) layout.findViewById(R.id.text_error);
        mForgetPayPassTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, VerifyPayActivity.class);
                intent.putExtra("clilckType", Constant.CLICK_FORGET_PAYPASS);
                startActivity(intent);
            }
        });
        closePayIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        LinearLayout layout_input = (LinearLayout) layout
                .findViewById(R.id.layout_input);

        keyboardView = (KeyboardView) layout.findViewById(R.id.keyboard_view);
        keyBoard = new KeyboardUtil(getActivity(), getActivity(), keyboardView,
                layout_input, new InputFinishListener() {

            @Override
            public void inputHasOver(String text) {
                // TODO 此处验证支付密码并进行相应的跳转及提示
                // MD5Util.getMD5(text);
                Intent intent = new Intent(getActivity(),
                        WalletActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        keyBoard.showKeyboard();
        dialog.show();
        layout_input.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoard.showKeyboard();
                return false;
            }
        });
    }

    /**
     * 接受广播
     */
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.chewuwuyou.chequan.tizi")) {
                userNetwork();// 访问网络加载个人信息
            } else if (action.equals("com.chewuwuyou.app.ui.personinfo")) {
                userNetwork();
            }
        }
    };

    /**
     * 商家认证
     */
    private void businessAuthentication() {
        mAuthentication.setClickable(false);
        StatService.onEvent(getActivity(), "IDVerificationActivity", "点击主界面商家认证");
        String role = CacheTools.getUserData("role");
        if (role != null) {
            if (role.contains("2") || role.contains("3")) {
                mAuthentication.setClickable(true);
                Intent businessEnterLL = new Intent(getActivity(), BusinessAuthenticationAdopt.class);
                businessEnterLL.putExtra("index", 1);
                getActivity().startActivity(businessEnterLL);
                return;
            }
            AjaxParams params = new AjaxParams();
            params.put("userId", CacheTools.getUserData("userId"));
            requestNet(new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Intent intent;
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            try {
                                JSONObject jo = new JSONObject(msg.obj.toString());
                                response = ExamineBook.parse(msg.obj.toString());
                                int exist = Integer.valueOf(jo.getString("exist"));// 是否通过0：未存在1：已存在
                                if (exist == Constant.BUSINESS_PASS_STATUS.REGIST_NOT_PASS) {
                                    mAuthentication.setClickable(true);
                                    Intent businessEnterLL = new Intent(getActivity(), IDVerificationActivity.class);
                                    startActivity(businessEnterLL);
                                    return;
                                }
                                int status = jo.getInt("status");// 0:发布，1:审核中，2:通过，3:拒绝，4:通过角色被修改
                                if (status == Constant.BUSINESS_REGIST_STATUS.REGIST_PASS
                                        && !CacheTools.getUserData("role").contains("2")
                                        && !CacheTools.getUserData("role").contains("3")) {
                                    mAuthentication.setClickable(true);
                                    Intent businessEnterLL = new Intent(getActivity(), BusinessAuthenticationAdopt.class);
                                    businessEnterLL.putExtra("index", 1);
                                    startActivity(businessEnterLL);
                                } else if (status == Constant.BUSINESS_REGIST_STATUS.REGIST_DOING) {//审核认证中
                                    mAuthentication.setClickable(true);
                                    intent = new Intent(getActivity(), BusinessAuthenticationAdopt.class);
                                    intent.putExtra("index", 2);
                                    getActivity().startActivity(intent);
                                } else if (status == Constant.BUSINESS_REGIST_STATUS.REGIST_PASS) {
                                    mAuthentication.setClickable(true);
                                    Intent businessEnterLL = new Intent(getActivity(), BusinessAuthenticationAdopt.class);
                                    businessEnterLL.putExtra("index", 1);
                                    startActivity(businessEnterLL);
                                } else if (status == Constant.BUSINESS_REGIST_STATUS.REGIST_NOT_PASS) {//审核失败
                                    mAuthentication.setClickable(true);
                                    intent = new Intent(getActivity(), BusinessAuthenticationAdopt.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("response", response);
                                    intent.putExtras(bundle);
                                    intent.putExtra("index", 3);
                                    intent.putExtra("fail", response.getAuditComment());
                                    getActivity().startActivity(intent);
                                } else if (status == Constant.BUSINESS_REGIST_STATUS.REGIST_FOUR_PASS) {//原来是商家，现在是普通用户
                                    intent = new Intent(getActivity(), IDVerificationActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("response", response);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(getActivity(), ((DataError) msg.obj).getErrorMessage());
                            mAuthentication.setClickable(true);
                            break;
                        default:
                            mAuthentication.setClickable(true);
                            break;
                    }
                }
            }, params, NetworkUtil.SEARCH_BUSINESS_REGIST_STATUS, false, 0);

        } else {
            Intent lIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(lIntent);
        }
    }

//    /**
//     * 商家认证通过
//     */
//    private void businessExamine() {
//        new AlertDialog.Builder(getActivity()).setTitle("退出登录").setMessage("权限变动，请重新登录")
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        arg0.dismiss();
//                    }
//                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(final DialogInterface arg0, int arg1) {
//                Intent intent = new Intent(getActivity(),
//                        LoginActivity.class);
//                Tools.clearInfo(getActivity());//清空当前登录用户信息
//                startActivity(intent);
//                MainActivityEr.activity.finishActivity();
//                arg0.dismiss();
//            }
//        }).show();
//    }
}
