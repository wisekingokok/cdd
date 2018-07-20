package com.chewuwuyou.app.transition_presenter.im_group;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.chewuwuyou.app.transition_entity.EstablishGroupSuccess;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.im_group.GroupEstablishSecondErMondel;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupEstablishSecondErActivtiy;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.TokenObtain;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 群列表
 * liuchun
 */

public class GroupEstablishSecondErPresenter extends BasePresenter {

    private GroupEstablishSecondErActivtiy mGroupEstablishSecondErActivtiy;
    private String dataCityUrl;//图片地址
    private GroupEstablishSecondErMondel mGroupEstablishSecondErMondel;

    private String userIds, groupName, groupImgUrl;

    public GroupEstablishSecondErPresenter(GroupEstablishSecondErActivtiy mGroupEstablishSecondErActivtiy) {
        super(mGroupEstablishSecondErActivtiy);
        this.mGroupEstablishSecondErActivtiy = mGroupEstablishSecondErActivtiy;
        mGroupEstablishSecondErMondel = new GroupEstablishSecondErMondel();
    }

    /**
     * 接收传递过来的参数
     */
    public void initView() {

        if (!TextUtils.isEmpty(mGroupEstablishSecondErActivtiy.getIntent().getStringExtra("addGroup"))) {
            userIds = mGroupEstablishSecondErActivtiy.getIntent().getStringExtra("addGroup");
        }
    }

    /**
     * 更换头像
     */
    public void uploadPortrait() {
        if (TextUtils.isEmpty(CacheTools.getUserData("qiniutoken"))) {
            TokenObtain tokenObtain = new TokenObtain();
            tokenObtain.Group(mGroupEstablishSecondErActivtiy);
        }
        Acp.getInstance(mGroupEstablishSecondErActivtiy).request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(mGroupEstablishSecondErActivtiy, MultiImageSelectorActivity.class);
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                        mGroupEstablishSecondErActivtiy.startActivityForResult(intent, Constant.UPLOAD_PORTRAIT_GROUP);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.toastShow(mGroupEstablishSecondErActivtiy, permissions.toString() + "权限拒绝");
                    }
                });
    }

    /**
     * 群头像回调方法
     */
    public void onGroupPortraitResult(int requestCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case Constant.UPLOAD_PORTRAIT_GROUP:
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (data != null) {
                        mGroupEstablishSecondErActivtiy.groupChoicePortrait.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                    }
                    if (!TextUtils.isEmpty(paths.get(0).toString().trim())) {
                        String sd = new TokenObtain().qiniuImg(mGroupEstablishSecondErActivtiy, FileUtils.getSmallBitmap(paths.get(0).toString().trim()));//压缩图片
                        EstablishGroup(mGroupEstablishSecondErActivtiy, sd);
                    }
                    break;
            }
        }
    }

    /**
     * 上传图片
     */
    public void EstablishGroup(final Context context, String dataCity) {
        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在上传");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(dataCity, String.valueOf(System.currentTimeMillis()), CacheTools.getUserData("qiniutoken"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                MyLog.e("YUY", "七牛上传图片c = " + info.statusCode);
                MyLog.e("YUY", "七牛上传图片qiniutoken = " + CacheTools.getUserData("qiniutoken"));
                if (info.statusCode == 200) {
                    ToastUtil.toastShow(context, "图片上传成功");
                    //res包含hash、key等信息，具体字段取决于上传策略的设置。
                    dataCityUrl = NetworkUtil.QI_NIU_BASE_URL + key;
                    mProgressDialog.dismiss();
                } else if (info.statusCode == 401) {
                    TokenObtain tokenObtain = new TokenObtain();
                    tokenObtain.Group(context);
                    mProgressDialog.dismiss();
                    ToastUtil.toastShow(context, "七牛token过期");
                } else {
                    mProgressDialog.dismiss();
                    ToastUtil.toastShow(context, "图片上传失败");
                    if (TextUtils.isEmpty(CacheTools.getUserData("qiniutoken"))) {
                        TokenObtain tokenObtain = new TokenObtain();
                        tokenObtain.Group(context);
                    }
                }
            }
        }, null);
    }


    /**
     * 提交信息
     */
    public void sbminGroup(String grouName) {
        rx.Observable<EstablishGroupSuccess> observable = mGroupEstablishSecondErMondel.groupCreation(userIds, CacheTools.getUserData("rongUserId"), grouName, dataCityUrl);
        observable.compose(this.<EstablishGroupSuccess>applySchedulers()).subscribe(defaultSubscriber(new Action1<EstablishGroupSuccess>() {
            @Override
            public void call(EstablishGroupSuccess establishGroupSuccess) {
                ToastUtil.toastShow(mGroupEstablishSecondErActivtiy, "创建成功");
                EstablishGroupSuccess groupSuccess = new EstablishGroupSuccess();
                EventBus.getDefault().post(groupSuccess);// 像适配器传递值
                mGroupEstablishSecondErActivtiy.finish();
            }
        }, new Action0() {
            @Override
            public void call() {
                mGroupEstablishSecondErActivtiy.isClick(true);
            }
        }, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mGroupEstablishSecondErActivtiy.isClick(true);
                return false;
            }
        }));
    }
}
