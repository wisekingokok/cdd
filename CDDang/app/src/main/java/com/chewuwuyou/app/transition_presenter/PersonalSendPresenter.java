package com.chewuwuyou.app.transition_presenter;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.RedPacketParam;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.SendRedBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.GroupRedPacketSendMode;
import com.chewuwuyou.app.transition_model.RedPacketModel;
import com.chewuwuyou.app.transition_view.activity.PersonalSendRedActivtiy;
import com.chewuwuyou.app.transition_view.activity.base.BaseFragment;
import com.chewuwuyou.app.transition_view.activity.fragment.PayDialogFragment;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.WaitingDialog;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 个人发布红包Presenter
 * liuchun
 */
public class PersonalSendPresenter extends BasePresenter{

    private PersonalSendRedActivtiy mPersonalSendRedActivtiy;
    private GroupRedPacketSendMode mPersonalSendModel;
    private RedPacketModel mRedPacketModel;


    public PersonalSendPresenter(PersonalSendRedActivtiy mPersonalSendRedActivtiy){
        super(mPersonalSendRedActivtiy);
        this.mPersonalSendRedActivtiy = mPersonalSendRedActivtiy;
        mPersonalSendModel = new GroupRedPacketSendMode();
        mRedPacketModel = new RedPacketModel();

    }

    /**
     * 请求接口
     */
    public void networkRequest(){
        mPersonalSendRedActivtiy.waitingDialog.show(mPersonalSendRedActivtiy.getString(R.string.load_in));
        rx.Observable<ResponseNBean<RedPacketParam>> observable = mPersonalSendModel.getRedPacketParam(CacheTools.getUserData("userId"));
        observable.compose(this.<ResponseNBean<RedPacketParam>>applySchedulers()).subscribe(defaultSubscriber(new Action1<ResponseNBean<RedPacketParam>>() {
            @Override
            public void call(ResponseNBean<RedPacketParam> redPacketParamResponseNBean) {
                if(Double.parseDouble(mPersonalSendRedActivtiy.edtRedpacketNum.getText().toString())>redPacketParamResponseNBean.getData().getDateMoney()){//判断进入限额
                    mPersonalSendRedActivtiy.walletError.setVisibility(View.VISIBLE);
                    mPersonalSendRedActivtiy.walletError.setText(mPersonalSendRedActivtiy.getString(R.string.redmoney_limit_max)+redPacketParamResponseNBean.getData().getDateMoney()+"元");
                    mPersonalSendRedActivtiy.waitingDialog.dismiss();
                }else if(Double.parseDouble(mPersonalSendRedActivtiy.edtRedpacketNum.getText().toString())<0.01){
                    mPersonalSendRedActivtiy.walletError.setVisibility(View.VISIBLE);

                    mPersonalSendRedActivtiy.walletError.setText(mPersonalSendRedActivtiy.getString(R.string.red_incorrect));

                   mPersonalSendRedActivtiy.walletError.setText(mPersonalSendRedActivtiy.getString(R.string.red_incorrect));

                    mPersonalSendRedActivtiy.waitingDialog.dismiss();
                }else if(Double.parseDouble(mPersonalSendRedActivtiy.edtRedpacketNum.getText().toString())>redPacketParamResponseNBean.getData().getOneSizeMoney()){//判断总限额
                    mPersonalSendRedActivtiy.walletError.setVisibility(View.VISIBLE);
                    double sd = Double.parseDouble(mPersonalSendRedActivtiy.edtRedpacketNum.getText().toString())-redPacketParamResponseNBean.getData().getOneSizeMoney();
                    mPersonalSendRedActivtiy.walletError.setText(mPersonalSendRedActivtiy.getString(R.string.redmoneyday_limit)+sd+mPersonalSendRedActivtiy.getString(R.string.unit_money));
                    mPersonalSendRedActivtiy.waitingDialog.dismiss();
                }else{
                    mPersonalSendRedActivtiy.walletError.setVisibility(View.GONE);
                    redPayment();//红包支付
                }
            }
        }, new Action0() {
            @Override
            public void call() {

            }
        }, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mPersonalSendRedActivtiy.waitingDialog.dismiss();
                return true;
            }
        }));
    }

    /**
     * 输入监听
     */
    public void inputPersonal(){
        mPersonalSendRedActivtiy.inputPersonalAc();
    }

    /**
     * 说明
     */
    public void redExplain(){
//        Intent intent = new Intent();
//        mPersonalSendRedActivtiy.startActivity(intent);
    }

    /**
     * 关闭页面
     */
    public void closeInterface(){
        mPersonalSendRedActivtiy.finish();
    }


    /**
     * 红包支付
     */
    public void redPayment(){
        SendRedBean sendRedBean = new SendRedBean();
        sendRedBean.setUserId(UserBean.getInstall(mPersonalSendRedActivtiy).getId());
        sendRedBean.setType("0");
        sendRedBean.setSize(1);
        sendRedBean.setMoney(mPersonalSendRedActivtiy.edtRedpacketNum.getText().toString());
        sendRedBean.setFriendAccid(mPersonalSendRedActivtiy.mTargetId);
        if(TextUtils.isEmpty(mPersonalSendRedActivtiy.textRedpacketRemark.getText().toString())){
            sendRedBean.setLeaveMessage(mPersonalSendRedActivtiy.getString(R.string.redpacket_remark_hint));
        }else{
            sendRedBean.setLeaveMessage(mPersonalSendRedActivtiy.textRedpacketRemark.getText().toString());
        }
        rx.Observable<ResponseNBean<RedInit>> observable = mRedPacketModel.sendPacketGroup(sendRedBean);
        observable.compose(this.<ResponseNBean<RedInit>>applySchedulers()).subscribe(defaultSubscriber(new Action1<ResponseNBean<RedInit>>() {
            @Override
            public void call(ResponseNBean<RedInit> redPacketDetailEntityResponseNBean) {
                if (redPacketDetailEntityResponseNBean.getData() != null) {
                    // 跳转到输入密码界面
                    PayDialogFragment.sendRed(redPacketDetailEntityResponseNBean.getData()).show(mPersonalSendRedActivtiy.getSupportFragmentManager(), "11");
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mPersonalSendRedActivtiy.waitingDialog.dismiss();
            }
        }, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                mPersonalSendRedActivtiy.waitingDialog.dismiss();
                return false;
            }
        }));


    }
}
