package com.chewuwuyou.rong.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.TokenObtain;
import com.chewuwuyou.rong.bean.WholeGroup;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 群创建 选择群头像以及名称
 * liuchun
 */
public class GroupEstablishSecondActivtiy extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//上一步
    private TextView mHeadberBarRight;
    @ViewInject(id = R.id.group_submit)//提交群信息
    private Button mGroupSubmit;
    @ViewInject(id = R.id.group_choice_portrait)//选择群头像
    private ImageView mGroupChoicePortrait;
    @ViewInject(id = R.id.add_group_name)//选择群头像
    private EditText mAddGroupName;

    private String dataCityUrl = "";//上传群头像路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_establish_second);
        initView();
        initData();
        initEvent();
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_right_tv://上一步
                finishActivity();
                break;
            case R.id.group_choice_portrait://选择群头像
                if (TextUtils.isEmpty(CacheTools.getUserData("qiniutoken"))) {
                    TokenObtain tokenObtain = new TokenObtain();
                    tokenObtain.Group(GroupEstablishSecondActivtiy.this);
                }
                Acp.getInstance(GroupEstablishSecondActivtiy.this).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        , Manifest.permission.CAMERA)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(GroupEstablishSecondActivtiy.this, MultiImageSelectorActivity.class);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                                startActivityForResult(intent, 2);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtil.toastShow(GroupEstablishSecondActivtiy.this, permissions.toString() + "权限拒绝");
                            }
                        });

                break;
            case R.id.group_submit://提交信息
                if (TextUtils.isEmpty(mAddGroupName.getText().toString())) {
                    ToastUtil.toastShow(GroupEstablishSecondActivtiy.this, "请输入群名称");
                } else if (mAddGroupName.getText().toString().length() < 2 || mAddGroupName.getText().toString().length() > 10) {
                    ToastUtil.toastShow(GroupEstablishSecondActivtiy.this, "群名称为2-10个字");
                } else {
                    mGroupSubmit.setClickable(false);
                    sbminGroup();
                }
                break;
        }
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        mTitel.setText("群创建");
        mSubHeaderBarLeftIbtn.setVisibility(View.GONE);
        mHeadberBarRight.setVisibility(View.VISIBLE);
        mHeadberBarRight.setText("上一步");
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mGroupSubmit.setOnClickListener(this);
        mGroupChoicePortrait.setOnClickListener(this);
        mHeadberBarRight.setOnClickListener(this);
    }

    /**
     * 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 2:
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (data != null) {
                        mGroupChoicePortrait.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                    }
                    if (!TextUtils.isEmpty(paths.get(0).toString().trim())) {
                        String sd = new TokenObtain().qiniuImg(GroupEstablishSecondActivtiy.this, FileUtils.getSmallBitmap(paths.get(0).toString().trim()));//压缩图片
                        EstablishGroup(GroupEstablishSecondActivtiy.this, sd);
                    }
                    break;
            }
        }
    }

    /**
     * 创建群信息
     */
    private void sbminGroup() {
        if (TextUtils.isEmpty(dataCityUrl)) {
            ToastUtil.toastShow(GroupEstablishSecondActivtiy.this, "请上传图片");
            mGroupSubmit.setClickable(true);
            return;

        }
        AjaxParams params = new AjaxParams();
        params.put("userIds", getIntent().getStringExtra("addGroup"));
        params.put("groupMain", CacheTools.getUserData("rongUserId"));
        params.put("groupName", mAddGroupName.getText().toString());
        params.put("groupImgUrl", dataCityUrl);
        NetworkUtil.get(NetworkUtil.ADD_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                try {

                    System.out.print("++++++++++++++"+o);

                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(GroupEstablishSecondActivtiy.this, jsonObject.getInt("code"), jsonObject.getString("message"));
                    if (jsonObject.getString("code").equals("0")) {

                        ToastUtil.toastShow(GroupEstablishSecondActivtiy.this, "创建成功");
                        EventBusAdapter bsAdapter = new EventBusAdapter();
                        bsAdapter.setGroupEstablish("0");
                        EventBus.getDefault().post(bsAdapter);// 像适配器传递值
                        finishActivity();
                        mGroupSubmit.setClickable(true);
                    } else {
                        ToastUtil.toastShow(GroupEstablishSecondActivtiy.this, jsonObject.getString("message"));
                        mGroupSubmit.setClickable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mGroupSubmit.setClickable(true);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupEstablishSecondActivtiy.this, "创建失败");
                mGroupSubmit.setClickable(true);
            }
        });
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


}
