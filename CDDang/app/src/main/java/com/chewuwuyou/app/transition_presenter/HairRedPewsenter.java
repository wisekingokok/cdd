package com.chewuwuyou.app.transition_presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_constant.Constants;
import com.chewuwuyou.app.transition_entity.AddGroupMember;
import com.chewuwuyou.app.transition_entity.OpenRed;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.HairRedMondel;
import com.chewuwuyou.app.transition_model.RongChatMsgModel;
import com.chewuwuyou.app.transition_view.activity.HairRedActivtiy;
import com.chewuwuyou.app.transition_view.activity.RedPacketDetailActivity;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

public class HairRedPewsenter extends BasePresenter{


    private HairRedActivtiy hairRedActivtiy;
    private HairRedMondel mGroupName;
    private RedPacketDetailEntity entity;
    private RongChatMsgModel mRongChatMsgPresenter;
    public HairRedPewsenter(HairRedActivtiy hairRedActivtiy) {
        super(hairRedActivtiy);
        this.hairRedActivtiy = hairRedActivtiy;
        mGroupName = new HairRedMondel();
        mRongChatMsgPresenter = new RongChatMsgModel();
    }


    /**
     * 接收传递的值
     */
    public void initView(){
        entity = (RedPacketDetailEntity) hairRedActivtiy.getIntent().getSerializableExtra(hairRedActivtiy.ENTITY_KEY);
        typeRed();
    }

    /**
     * 抢红包
     */
    public void robRed(){
        rx.Observable<ResponseNBean<RedPacketDetailEntity>> observable = mGroupName.getOpenRed(UserBean.getInstall(hairRedActivtiy).getId(), entity.getId());
        observable.compose(this.<ResponseNBean<RedPacketDetailEntity>>applySchedulers()).subscribe(defaultSubscriber(new Action1<ResponseNBean<RedPacketDetailEntity>>() {
            @Override
            public void call(ResponseNBean<RedPacketDetailEntity> mGroupName) {
                hairRedActivtiy.animationDrawable.stop();//停止动画
                entity = mGroupName.getData();
                if(entity.getReceiveStatus().equals(Constants.REDMOVW_RED.RED_OVERDUE)){
                    hairRedActivtiy.groupRed();
                }else if(entity.getReceiveStatus().equals(Constants.REDMOVW_RED.RED_GRAB)){
                    hairRedActivtiy.GrabRed();
                }else{
                    RedPacketDetailActivity.launch(hairRedActivtiy,mGroupName.getData());
                    hairRedActivtiy.finish();
                }
            }
        }, null,new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                hairRedActivtiy.finish();
                return false;
            }
        }));
    }

    /**
     * 根据状态判断是群红包还是个人
     */
    private void typeRed(){
        if(entity.getType()==Constants.RED_ROLE.LSSUE){
            personalRed();
        }else{
            groupRed();
        }

    }

    /**
     * 群红包
     */
    public void groupRed(){
        if(TextUtils.isEmpty(entity.getRemainSize())){
            entity.setRemainSize("0");
        }
        if(entity.getSize()==Integer.parseInt(entity.getRemainSize())){
            hairRedActivtiy.GrabRed();//已领完
        }else if(entity.getReceiveStatus().equals(Constants.REDMOVW_RED.RED_OVERDUE)){
            hairRedActivtiy.groupRed();//已过期
        }else{
            hairRedActivtiy.mHairRedName.setText(entity.getNickName());

            Glide.with(hairRedActivtiy).load(entity.getHeadImg()).crossFade().placeholder(R.drawable.user_fang_icon).error(R.drawable.user_fang_icon).into(hairRedActivtiy.mHairRedImg);
            hairRedActivtiy.mHairRedLanguage.setText(entity.getLeaveMessage());
        }
    }

    /**
     * 个人红包
     */
    public void personalRed(){
        hairRedActivtiy.mHairRedLuck.setVisibility(View.GONE);
        if(entity.getReceiveStatus().equals(Constants.REDMOVW_RED.RED_OVERDUE)){
            hairRedActivtiy.groupRed();//已过期
        }else{
            hairRedActivtiy.mHairRedName.setText(entity.getNickName());
            Glide.with(hairRedActivtiy).load(entity.getHeadImg()).crossFade().placeholder(R.drawable.user_fang_icon).error(R.drawable.user_fang_icon).into(hairRedActivtiy.mHairRedImg);
            hairRedActivtiy.mHairRedLanguage.setText(entity.getLeaveMessage());
        }
    }

    /**
     * 查看大家手气
     */
    public void everybodyLuck(){
        rx.Observable<NewBaseNetworkBean<RedPacketDetailEntity>> observable = mRongChatMsgPresenter.whetherGrabRedPacket(CacheTools.getUserData("userId"), entity.getId());
        observable.compose(this.<NewBaseNetworkBean<RedPacketDetailEntity>>applySchedulers()).subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<RedPacketDetailEntity>>() {
            @Override
            public void call(NewBaseNetworkBean<RedPacketDetailEntity> mGroupName) {
                RedPacketDetailActivity.launch(hairRedActivtiy,mGroupName.getData());
                hairRedActivtiy.finish();
            }
        }));
    }
}
