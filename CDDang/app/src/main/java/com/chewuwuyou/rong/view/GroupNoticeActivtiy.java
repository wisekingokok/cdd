package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.SendMsgBean;
import com.chewuwuyou.rong.utils.RongApi;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

public class GroupNoticeActivtiy extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//添加
    private TextView mHeadberBarRight;
    @ViewInject(id = R.id.group_noticetext)
    private EditText mGroupNoticeText;//群公告

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_notice_activtiy);
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
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();//返回上一页
                break;
            case R.id.sub_header_bar_right_tv:
               if(TextUtils.isEmpty(mGroupNoticeText.getText().toString())){
                   ToastUtil.toastShow(GroupNoticeActivtiy.this,"群公告不能为空");
               }else if(mGroupNoticeText.getText().toString().length()>120){
                    ToastUtil.toastShow(GroupNoticeActivtiy.this,"公告只能在120字之内");
                }else{
                    AjaxParams params = new AjaxParams();
                    params.put("id", getIntent().getStringExtra("groupId"));
                    params.put("userId", CacheTools.getUserData("rongUserId"));
                    params.put("groupAnnouncement", mGroupNoticeText.getText().toString());
                    groupNoticeSubmit(params);
                }

                break;
        }
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        mTitel.setText("群公告");
        if(!TextUtils.isEmpty(getIntent().getStringExtra("groupmain"))&&getIntent().getStringExtra("groupmain").equals("0")){
            mHeadberBarRight.setVisibility(View.VISIBLE);
            mHeadberBarRight.setText("提交");
        }else{
            mGroupNoticeText.setHint("   请输入群公告信息");
            mGroupNoticeText.setFocusable(false);
        }
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        if(!TextUtils.isEmpty(getIntent().getStringExtra("gonggao"))){
            mGroupNoticeText.setText(getIntent().getStringExtra("gonggao"));
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
     * 群公告
     * @param params
     */
    private void groupNoticeSubmit(AjaxParams params){
        NetworkUtil.get(NetworkUtil.GROUP_NOTICE, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(GroupNoticeActivtiy.this,jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){

                        String na = "群公告："+"\n"+mGroupNoticeText.getText().toString();
                        sendMsg(CDDYWZMsg.obtain(na),na);

                        ToastUtil.toastShow(GroupNoticeActivtiy.this,"提交成功");
                        Intent intent  = new Intent();
                        intent.putExtra("group_announcement",mGroupNoticeText.getText().toString());
                        setResult(RESULT_OK, intent);
                        finishActivity();
                    }else{
                        ToastUtil.toastShow(GroupNoticeActivtiy.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupNoticeActivtiy.this,"提交失败");
            }
        });
    }

    private void sendMsg(MessageContent messageContent, String pushC) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Message message = Message.obtain(getIntent().getStringExtra("groupId"), Conversation.ConversationType.GROUP, messageContent);

        String pushAuthor =(TextUtils.isEmpty(getIntent().getStringExtra("groupNamae")) ? getIntent().getStringExtra("groupId") : getIntent().getStringExtra("groupNamae")) + "(" + getIntent().getStringExtra("groupSize") + ")" ;
        RongApi.sendMessage(message, pushAuthor + ":" + pushC, simpleDateFormat.format(new Date()),  new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                EventBus.getDefault().post(message);
            }

            @Override
            public void onSuccess(Message message) {
                EventBus.getDefault().post(new SendMsgBean(0,message));
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                EventBus.getDefault().post(new SendMsgBean(0,message));
            }
        });
    }



}
