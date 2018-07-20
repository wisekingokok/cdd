package com.chewuwuyou.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.chewuwuyou.app.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;


/**
 * 友盟分享工具类
 *
 * @author tangxian
 */
public class UmengShareUtils {

    private Activity mActivity;


    /**
     * 调用分享面板
     */
    public void share() {
        //      mController.openShare(mActivity, false);
    }

    //发送给微信朋友
    public static void share2weixin(Activity activity, String title, String url, String content, UMShareListener umShareListener, String porti_url) {
        ShareAction shareAction = new ShareAction(activity);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
   //     shareAction.withTitle(title);
        shareAction.withMedia(new UMImage(activity, porti_url));
        shareAction.withTargetUrl(url);
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).share();

    }

    //分享到微信朋友圈
    public static void share2pyq(Activity activity, String title, String url, String content, UMShareListener umShareListener, String porti_url) {
        ShareAction shareAction = new ShareAction(activity);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
       shareAction.withTitle(content);
        shareAction.withMedia(new UMImage(activity, porti_url));
        shareAction.withTargetUrl(url);
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).share();

    }

    public static void share2qq(Activity activity, String title, String url, String content, UMShareListener umShareListener, String port_url) {
        ShareAction shareAction = new ShareAction(activity);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
        shareAction.withMedia(new UMImage(activity, port_url));
     //   shareAction.withTitle(title);
        shareAction.withTargetUrl(url);
        shareAction.setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).share();

    }


    public static void share2qzone(Activity activity, String title, String url, String content, UMShareListener umShareListener, String port_url) {
        ShareAction shareAction = new ShareAction(activity);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
      //  shareAction.withTitle(title);
        shareAction.withMedia(new UMImage(activity, port_url));
        shareAction.withTargetUrl(url);
        shareAction.setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener).share();

    }

}
