package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.view.SweepListView;
import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.NewFriend;
import com.chewuwuyou.app.bean.SearchFriend;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.UmengShareUtils;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.view.NoticeAdapter;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddfriendActivity extends CDDBaseActivity {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;

    /**
     * 标题
     */
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mTitleTV;

    @ViewInject(id = R.id.add_contacts_friend_ll, click = "onAction")
    private TextView mAddContactsFriendLL;

    @ViewInject(id = R.id.weixin, click = "onAction")
    private TextView mTextView_weixin;
    @ViewInject(id = R.id.qq, click = "onAction")
    private TextView mTextView_qq;
    @ViewInject(id = R.id.add_contacts_friend_sao, click = "onAction")
    private TextView mTextView_sao;

    @ViewInject(id = R.id.layoutContainer, click = "onAction")
    private FrameLayout mFrameLayout;//搜索layout

    @ViewInject(id = R.id.layout_erweima, click = "onAction")
    private FrameLayout mFrameLayout_erweima;

    private String content = "";//分享的内容
    private String final_url;
    String portiUrl;
    private String title;
    String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        mTitleTV.setText("添加好友");
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {

        String name = CacheTools.getUserData("nickName");
        portiUrl = CacheTools.getUserData("url");
        String id = CacheTools.getUserData("userId");
        age = CacheTools.getUserData("age");
        String sex;
        if (CacheTools.getUserData("sex", 0) == 0) {
            sex = "man";
        } else
            sex = "women";

        final String url0 = "http://www.cddang.com/mhwcw/share/share.html?name=" + name + "&headImage=" + portiUrl + "&sex=" + sex + "&age=" + age;
        final_url = url0;

        try {
            String url = "http://www.cddang.com/mhwcw/share/share.html?name=" + URLEncoder.encode(name, "utf-8") + "&headImage=" + portiUrl + "&id=" + id + "&sex=" + sex + "&age=" + age;

            //   url = URLEncoder.encode(url, "utf-8");
            title = "发送给";
            content = "一起来车当当分享有车新生活,我在车当当的昵称是" + CacheTools.getUserData("nickName");

            AjaxParams params = new AjaxParams();
            params.put("url", url);

            String test="http://192.168.8.102:6060/api/common/dwz/v340/getShortUrl";

            NetworkUtil.getFhMulti().post(NetworkUtil.GET_SHORT_URL, params, new AjaxCallBack<String>() {
                @Override
                public boolean isProgress() {
                    return super.isProgress();
                }

                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        ErrorCodeUtil.doErrorCode(AddfriendActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));
                        if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                            final_url = jsonObject.getString("data");
                            MyLog.i("短地址---->", final_url);

                        } else {
                            final_url = url0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    //       ToastUtil.toastShow(AddfriendActivity.this,"网络错误，请稍后再试");
                    MyLog.i("短地址失败");
                    final_url = url0;
                }
            });


        } catch (UnsupportedEncodingException mE) {
            mE.printStackTrace();
        }

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }


    public void onAction(View v) {
        Intent intent = new Intent();
        ComponentName comp;
        switch (v.getId()) {
            case R.id.search_friend_ibtn:
                searchFriend();
                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.add_contacts_friend_ll:
                Intent addContactsIntent = new Intent(AddfriendActivity.this, AddContactsFriendActivity.class);
                startActivity(addContactsIntent);


                break;
            case R.id.weixin:


                MyLog.i("微信分享。。。。。。。。。。。。。");

//                new AlertDialog.Builder(this)
//                        .setTitle("选择")
//                        .setCancelable(true)
//                        .setItems(R.array.weixin, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if (which == 0) {
//                                    UmengShareUtils.share2weixin(AddfriendActivity.this, url, content, umShareListener);
//                                } else
//                                    UmengShareUtils.share2pyq(AddfriendActivity.this, url, content, umShareListener);
//
//                            }
//                        }).create()
//                        .show();


                String[] strs = new String[]{"微信好友", "朋友圈"};
                setTheme(R.style.ActionSheetStyleIOS7);
                ActionSheet menuView = new ActionSheet(this);
                menuView.setCancelButtonTitle("取 消");// before add items
                menuView.addItems(strs);
                menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
                    @Override
                    public void onItemClick(int itemPosition) {

                        if (itemPosition == 0) {
                            UmengShareUtils.share2weixin(AddfriendActivity.this, title, final_url, content, umShareListener, portiUrl);
                        } else
                            UmengShareUtils.share2pyq(AddfriendActivity.this, title, final_url, content, umShareListener, portiUrl);


                    }
                });
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();


                break;
            case R.id.qq:


                //      UmengShareUtils.share2weixin(AddfriendActivity.this, url, content, umShareListener);

                String[] str = new String[]{"QQ好友", "QQ空间"};
                setTheme(R.style.ActionSheetStyleIOS7);
                ActionSheet menuView1 = new ActionSheet(this);
                menuView1.setCancelButtonTitle("取 消");// before add items
                menuView1.addItems(str);
                menuView1.setItemClickListener(new ActionSheet.MenuItemClickListener() {
                    @Override
                    public void onItemClick(int itemPosition) {

                        if (itemPosition == 0) {

                            UmengShareUtils.share2qq(AddfriendActivity.this, title, final_url, content, umShareListener, portiUrl);


                        } else
                            UmengShareUtils.share2qzone(AddfriendActivity.this, title, final_url, content, umShareListener, portiUrl);


                    }
                });
                menuView1.setCancelableOnTouchMenuOutside(true);
                menuView1.showMenu();


//                new AlertDialog.Builder(this)
//                        .setTitle("选择")
//                        .setCancelable(true)
//                        .setItems(R.array.qq, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if (which == 0) {
//                                    UmengShareUtils.share2qq(AddfriendActivity.this, url, content, umShareListener);
//                                } else
//                                    UmengShareUtils.share2qzone(AddfriendActivity.this, url, content, umShareListener);
//                            }
//                        }).create()
//                        .show();


                break;
            case R.id.layoutContainer:
                Intent mIntent = new Intent(this, SearchNewFriendActivity.class);
                this.startActivity(mIntent);
                break;

            case R.id.add_contacts_friend_sao:
                Intent i = new Intent(this, CaptureActivity.class);
                startActivity(i);
                break;

            case R.id.layout_erweima:

                Intent barcodeIntent = new Intent(this,
                        PersonMyBarCodeActivity.class);
                startActivity(barcodeIntent);


                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(AddfriendActivity.this, " 微信收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {

                if (platform == SHARE_MEDIA.QQ)

                    Toast.makeText(AddfriendActivity.this, " QQ分享成功啦", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.QZONE)
                    Toast.makeText(AddfriendActivity.this, " QQ空间分享成功啦", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.WEIXIN)
                    Toast.makeText(AddfriendActivity.this, " 微信分享成功啦", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE)
                    Toast.makeText(AddfriendActivity.this, "朋友圈分享成功啦", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddfriendActivity.this, platform + "分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(AddfriendActivity.this, " 微信收藏失败了", Toast.LENGTH_SHORT).show();
            } else {

                if (platform == SHARE_MEDIA.QQ)

                    Toast.makeText(AddfriendActivity.this, " QQ分享失败了", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.QZONE)
                    Toast.makeText(AddfriendActivity.this, " QQ空间分享失败了", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.WEIXIN)
                    Toast.makeText(AddfriendActivity.this, " 微信分享失败了", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE)
                    Toast.makeText(AddfriendActivity.this, "朋友圈分享失败了", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddfriendActivity.this, platform + "分享失败了", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(AddfriendActivity.this, " 微信收藏取消了", Toast.LENGTH_SHORT).show();
            } else {

                if (platform == SHARE_MEDIA.QQ)

                    Toast.makeText(AddfriendActivity.this, " QQ分享取消了", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.QZONE)
                    Toast.makeText(AddfriendActivity.this, " QQ空间分享取消了", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.WEIXIN)
                    Toast.makeText(AddfriendActivity.this, " 微信分享取消了", Toast.LENGTH_SHORT).show();
                else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE)
                    Toast.makeText(AddfriendActivity.this, "朋友圈分享取消了", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddfriendActivity.this, platform + "分享取消了", Toast.LENGTH_SHORT).show();
            }

        }


    };


    /**
     * 关闭键盘事件
     *
     * @author sxk
     * @update 2012-7-4 下午2:34:34
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void searchFriend() {

    }


}
