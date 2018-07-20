package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformation;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class SetUpGroupNickNameaActivtiy extends CDDBaseActivity implements View.OnClickListener {


    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//保存
    private TextView mHeadberBarRight;
    @ViewInject(id = R.id.group_name)//保存
    private EditText mGroupName;
    @ViewInject(id = R.id.setup_group_nick_name)//保存
    private TextView mSetupGroupNickName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_group_nick_namea_ac);
        initView();
        initData();
        initEvent();
    }


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv://保存
                if(getIntent().getStringExtra("group_name").toString().equals("0")){
                    if(TextUtils.isEmpty(mGroupName.getText().toString().trim())){
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"群名称不能为空");
                    }else if(mGroupName.getText().length()>10){
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"群名称不能多于10个字");
                    }else if(mGroupName.getText().length()<2){
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"群名称不能少于2个字");
                    }else{
                        UpdateGroupName();//群名称
                    }

                } else{
                    if(TextUtils.isEmpty(mGroupName.getText().toString().trim())){
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"群昵称不能为空");
                    }else if(mGroupName.getText().length()>32){
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"群昵称不能多于32个字");
                    }else{
                        UpdateNickName();//本群昵称
                    }
                }
                break;
        }
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        if(getIntent().getStringExtra("group_name").toString().equals("0")){
            mTitel.setText("群名称");
            mSetupGroupNickName.setText("在这里可以设置这个群的名称");
        } else{
            mTitel.setText("我的昵称");
            mSetupGroupNickName.setText("在这里可以设置这个群的昵称");
        }
        mHeadberBarRight.setVisibility(View.VISIBLE);
        mHeadberBarRight.setText("提交");
    }
    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        if(!TextUtils.isEmpty(getIntent().getStringExtra("mingc"))){
            mGroupName.setText(getIntent().getStringExtra("mingc").trim());
        }

        if(!TextUtils.isEmpty(getIntent().getStringExtra("nic"))){
            mGroupName.setText(getIntent().getStringExtra("nic"));
        }
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mHeadberBarRight.setOnClickListener(this);
    }

    /**
     * 修改群名称
     */
    private void UpdateGroupName(){
        AjaxParams params = new AjaxParams();
        params.put("id", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("groupName", mGroupName.getText().toString());
        NetworkUtil.get(NetworkUtil.GROUP_NOTICE, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);

                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(SetUpGroupNickNameaActivtiy.this,jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"提交成功");
                        Intent intent  = new Intent();
                        intent.putExtra("group_name",mGroupName.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"提价失败");
            }
        });
    }
    /**
     * 修改群名称
     */
    private void UpdateNickName(){
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("remarkName", mGroupName.getText().toString());
        params.put("loginUserId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GROUP_Nick_NAME, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(SetUpGroupNickNameaActivtiy.this,jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"提交成功");
                        Intent intent  = new Intent();
                        intent.putExtra("remark_name",mGroupName.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(SetUpGroupNickNameaActivtiy.this,"网络异常");
            }
        });
    }

}
