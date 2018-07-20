package com.chewuwuyou.app.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.TokenObtain;
import com.chewuwuyou.app.utils.VersionUtil;
import com.chewuwuyou.app.widget.EditTextWithDelete;
import com.chewuwuyou.rong.utils.CDDRongApi;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 注册提交头像 、昵称、 性别 、年龄等资料
 *
 * @author yuyong
 */
public class RegisterCommitInfoActivity extends CDDBaseActivity implements
        OnClickListener {

    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String SEX = "sex";
    private ImageView mBackIV;
    private ImageView mHeadIV;
    private EditTextWithDelete mNickNameET;// 昵称
    private RadioGroup mChooseSexRG;// 选择性别
    private TextView mBirghdayTV;// 输入年龄
    private int mSex = 0;// 计算出的年龄 用于上传至服务器
    private Button mCommitBtn;
    private String mHeadStr;// 上传图片的地址
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_commit_info_ac);
        initView();
        initData();
        initEvent();

    }

    @Override
    protected void initView() {
        mBackIV = (ImageView) findViewById(R.id.register_back_iv);
        mHeadIV = (ImageView) findViewById(R.id.user_head_iv);
        mNickNameET = (EditTextWithDelete) findViewById(R.id.register_nick_et);
        mChooseSexRG = (RadioGroup) findViewById(R.id.choose_sex_rg);
        mBirghdayTV = (TextView) findViewById(R.id.age_et);
        mCommitBtn = (Button) findViewById(R.id.commit_btn);
        CacheTools.setUserData("version",
                VersionUtil.getVersion(RegisterCommitInfoActivity.this));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mBackIV.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
        mHeadIV.setOnClickListener(this);
        mBirghdayTV.setOnClickListener(this);
        mChooseSexRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg1 == R.id.man_rb) {// 选择男
                    mSex = 0;
                } else {
                    mSex = 1;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.register_back_iv:
                finishActivity();
                break;
            case R.id.user_head_iv:
                if (TextUtils.isEmpty(CacheTools.getUserData("qiniutoken"))) {
                    TokenObtain tokenObtain = new TokenObtain();
                    tokenObtain.Group(RegisterCommitInfoActivity.this);
                }
                Acp.getInstance(RegisterCommitInfoActivity.this).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        , Manifest.permission.CAMERA)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(RegisterCommitInfoActivity.this, MultiImageSelectorActivity.class);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                                startActivityForResult(intent, 2);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtil.toastShow(RegisterCommitInfoActivity.this, permissions.toString() + "权限拒绝");
                            }
                        });

                break;
            case R.id.commit_btn://提交
                commitUserRegistInfo();
                break;
            case R.id.age_et://选择时间
                chooseTimeDialog();
                break;

            default:
                break;
        }
    }

    /**
     * 提交用户注册的资料
     */
    private void commitUserRegistInfo() {
        String nickName = mNickNameET.getText().toString().trim();
        String age = mBirghdayTV.getText().toString().trim();
        if (TextUtils.isEmpty(mHeadStr)) {
            ToastUtil.toastShow(RegisterCommitInfoActivity.this, "请上传您的头像，以便更好体验");
        } else if (TextUtils.isEmpty(nickName)) {
            ToastUtil.toastShow(RegisterCommitInfoActivity.this, "请输入昵称");
        } else if (TextUtils.isEmpty(age)) {
            ToastUtil.toastShow(RegisterCommitInfoActivity.this, "请输入年龄");
        } else {
            AjaxParams params = new AjaxParams();
            params.put("phone", getIntent().getStringExtra("telephone"));
            params.put("password", getIntent().getStringExtra("password"));
            params.put("nickName", nickName);
            params.put("birthday", age);
            params.put("headImageUrl", mHeadStr);
            params.put("sex", String.valueOf(mSex));
            MyLog.e("YUY", "注册信息 = " + getIntent().getStringExtra("telephone") + " " + getIntent().getStringExtra("password") + " " + nickName + " " + age + " " + mHeadStr + " " + mSex);
            NetworkUtil.postNoHeader(NetworkUtil.USER_REGISTE, params, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    try {
                        JSONObject jo = new JSONObject(s);
                        if (jo.getInt("code") == 0) {
                            ToastUtil.toastShow(RegisterCommitInfoActivity.this, "注册成功");
                            Intent intent = new Intent(RegisterCommitInfoActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finishActivity();
                        } else {
                            ToastUtil.toastShow(RegisterCommitInfoActivity.this, jo.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    MyLog.i("YUY", "注册失败 =" + strMsg);
                    ToastUtil.toastShow(RegisterCommitInfoActivity.this, "系统异常");
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //有些机型在拍照后会销毁当前的activity，因此有必要保存下数据。

        outState.putString(NAME, mNickNameET.getText().toString());

        outState.putString(AGE, mBirghdayTV.getText().toString());

        outState.putInt(SEX, mChooseSexRG.getCheckedRadioButtonId());


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //恢复数据
        mNickNameET.setText(savedInstanceState.getString(NAME));
        mBirghdayTV.setText(savedInstanceState.getString(AGE));
        mChooseSexRG.check(savedInstanceState.getInt(SEX));


        int i = savedInstanceState.getInt(SEX);

        if (i == R.id.man_rb) {// 选择男
            mSex = 0;

        } else {
            mSex = 1;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 2) {
                List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (paths.size() > 0) {
                    mHeadIV.setImageBitmap(FileUtils.getSmallBitmap(paths.get(0)));
                    upLoadHead(paths.get(0).toString().trim());
                }
            }

        }

    }

    /**
     * 选择时间
     */
    private void chooseTimeDialog() {
        final Calendar c;
        Dialog dialog = null;
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        dialog = new DatePickerDialog(RegisterCommitInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                if (calendar.getTime().after(new Date())) {//判断选择
                    ToastUtil.toastShow(RegisterCommitInfoActivity.this, "时间不能大于当前时间");
                } else if (calendar.get(Calendar.YEAR) < new Date().getYear() - 100) {
                    ToastUtil.toastShow(RegisterCommitInfoActivity.this, "选择日期不正确");
                } else {
                    mBirghdayTV.setText(year + "-"
                            + ((("" + (month + 1)).length() == 1) ? ("0" + (month + 1)) : ("" + (month + 1))) + "-"
                            + ((("" + (dayOfMonth)).length() == 1) ? ("0" + (dayOfMonth)) : ("" + (dayOfMonth))));
                }

            }
        }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH)); // 传入天数
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 上传群头像
     */
    private void upLoadHead(String imgUrl) {

        mProgressDialog = new ProgressDialog(RegisterCommitInfoActivity.this);
        mProgressDialog.setMessage("正在上传");
        mProgressDialog.show();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(imgUrl, String.valueOf(System.currentTimeMillis()), CacheTools.getUserData("qiniutoken"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                if (info.statusCode == 200) {
                    ToastUtil.toastShow(RegisterCommitInfoActivity.this, "图片上传成功");
                    //res包含hash、key等信息，具体字段取决于上传策略的设置。
                    mHeadStr = NetworkUtil.QI_NIU_BASE_URL + key;
                    mProgressDialog.dismiss();
                } else {
                    ToastUtil.toastShow(RegisterCommitInfoActivity.this, "图片上传失败");
                    TokenObtain tokenObtain = new TokenObtain();
                    tokenObtain.Group(RegisterCommitInfoActivity.this);
                    mProgressDialog.dismiss();
                }
            }
        }, null);
    }


}
