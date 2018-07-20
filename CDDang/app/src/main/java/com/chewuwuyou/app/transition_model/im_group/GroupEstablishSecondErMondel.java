package com.chewuwuyou.app.transition_model.im_group;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.transition_entity.AddGroupMember;
import com.chewuwuyou.app.transition_entity.BaseListBean;
import com.chewuwuyou.app.transition_entity.EstablishGroup;
import com.chewuwuyou.app.transition_entity.EstablishGroupSuccess;
import com.chewuwuyou.app.transition_model.BaseModel;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_service.im_group.GroupService;
import com.chewuwuyou.app.transition_utils.HttpBase;
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
import rx.functions.Func0;

/**
 * 群创建
 * liuchun
 */

public class GroupEstablishSecondErMondel extends BaseModel {

    public rx.Observable<EstablishGroupSuccess> groupCreation(String userIds, String groupMain, String groupName, String groupImgUrl) {
        return HttpBase.getInstance().createApi(GroupService.class).groupCreation(userIds,groupMain,groupName,groupImgUrl);
    }

}
