package com.chewuwuyou.app.transition_presenter;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.RedPacketParam;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.SendRedBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.GroupRedPacketSendMode;
import com.chewuwuyou.app.transition_model.RedPacketModel;
import com.chewuwuyou.app.transition_view.activity.GroupRedPacketSendActivity;
import com.chewuwuyou.app.transition_view.activity.fragment.PayDialogFragment;
import com.chewuwuyou.app.utils.CacheTools;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ZQ on 2016/10/12.
 */

public class GroupRedPacketSendPresenter extends BasePresenter {
    private GroupRedPacketSendActivity mActivity;
    private GroupRedPacketSendMode mMode;
    private RedPacketModel mRedPacketModel;
    private String mRemark;
//    红包输入的金额
    private double mRedMoney;
    //    红包数量
    private int mRedCount;
    //    红吧的总金额
    private double mAllRedMoeney;



    public GroupRedPacketSendPresenter(GroupRedPacketSendActivity mActivity) {
        super(mActivity);
        mMode = new GroupRedPacketSendMode();
        mRedPacketModel=new RedPacketModel();

        this.mActivity = mActivity;
    }

    public void actionPay(final Editable redpacketNum, Editable redpacketMoney, Editable redpacketRemarkText, final int mRedType, final String mTargetId) {
        if (TextUtils.isEmpty(redpacketNum)) {
            mActivity.showNumHint(mActivity.getString(R.string.rednum_limit));
            return;
        }
        if (TextUtils.isEmpty(redpacketMoney) ) {
            mActivity.showMoneyHint(mActivity.getString(R.string.redmoney_limit));
            return;
        }
//        排除只输入小数点的情况导致的解析异常
        if (redpacketMoney.toString().contains(".")&&redpacketMoney.toString().length() == 1) {
            mRedMoney = 0;
        } else {
            mRedMoney=Double.parseDouble(redpacketMoney.toString());
        }
        mRedCount=Integer.parseInt(redpacketNum.toString());


        if (mRedMoney==0){
            mActivity.showMoneyHint(mActivity.getString(R.string.redmoney_limit));
            return;
        }
       if (mRedType==mActivity.REDTYPE_NORMAL){
           if (mRedCount<1){
               mActivity.showNumHint(mActivity.getString(R.string.rednum_limit));
               return;
           }
           mAllRedMoeney=mRedCount*mRedMoney;

       }else if (mRedType==mActivity.REDTYPE_RANDOM){
           if (mRedCount<2){
               mActivity.showNumHint(mActivity.getString(R.string.rednum_limit2));
               return;
           }
           if (mRedMoney/mRedCount<0.01){
               mActivity.showMoneyHint(mActivity.getString(R.string.redmoneysingle_limit));
               return;
           }

           mAllRedMoeney=mRedMoney;
       }

        if (TextUtils.isEmpty(redpacketRemarkText)) {
            mRemark = mActivity.getString(R.string.redpacket_remark_hint);
        } else {
            mRemark = redpacketRemarkText.toString();
        }

        mActivity.mDialog.show(mActivity.getString(R.string.processing));
        mMode.getRedPacketParam(CacheTools.getUserData("userId"))
                .compose(this.<ResponseNBean<RedPacketParam>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<ResponseNBean<RedPacketParam>>() {
                    @Override
                    public void call(ResponseNBean<RedPacketParam> redPacketParamResponseBean) {
                        if (redPacketParamResponseBean.getData() != null) {
//                            日限额
                            double dateMoney = redPacketParamResponseBean.getData().getDateMoney();
//                            日限数量
                            int dateSize = redPacketParamResponseBean.getData().getDateSize();
//                            一次性限额
                            double oneSizeMoney = redPacketParamResponseBean.getData().getOneSizeMoney();
                            if (mRedCount > dateSize) {
                                mActivity.showNumHint(mActivity.getString(R.string.rednum_limit_max) + dateSize + mActivity.getString(R.string.unit_count));
                                return;
                            }
                            if (mAllRedMoeney > oneSizeMoney) {
                                mActivity.showMoneyHint(mActivity.getString(R.string.redmoney_limit_max) + oneSizeMoney + mActivity.getString(R.string.unit_money));
                                return;
                            }
                            if (mAllRedMoeney > dateMoney) {
                                mActivity.showMoneyHint(mActivity.getString(R.string.redmoneyday_limit) + oneSizeMoney + mActivity.getString(R.string.unit_money));
                                return;
                            }
                            initRedOrder(mRedCount,mRedMoney,mAllRedMoeney,mRedType,mRemark,mTargetId);
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mActivity.mDialog.dismiss();
                    }
                }, new Func1<CustomException, Boolean>() {
                    @Override
                    public Boolean call(CustomException e) {
                        mActivity.mDialog.dismiss();
                        return false;
                    }
                }));

    }
// 生成红包订单
    public void initRedOrder(int redNum, double unitredMoney, double allRedMoeney, int type, String remark, String targetId) {


        SendRedBean sendRedBean=new SendRedBean();
        sendRedBean.setLeaveMessage(remark);
        sendRedBean.setSize(redNum);
        sendRedBean.setUserId(UserBean.getInstall(mActivity).getId());
        sendRedBean.setMoney(allRedMoeney+"");
        sendRedBean.setType(type+"");

       if (type==mActivity.REDTYPE_NORMAL){
           sendRedBean.setUnitPrice(unitredMoney+"");
        }
        sendRedBean.setFriendAccid(targetId);
        mRedPacketModel.sendPacketGroup(sendRedBean).
                compose(this.<ResponseNBean<RedInit>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<ResponseNBean<RedInit>>() {
                    @Override
                    public void call(ResponseNBean<RedInit> redPacketDetailEntityResponseBean) {
                        mActivity.mDialog.dismiss();
                        if (redPacketDetailEntityResponseBean.getData() != null) {
                            // 跳转到输入密码界面
                            PayDialogFragment.sendRed(redPacketDetailEntityResponseBean.getData())
                                    .show(mActivity.getSupportFragmentManager(), "11");
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mActivity.mDialog.dismiss();
                    }
                }, new Func1<CustomException, Boolean>() {
                    @Override
                    public Boolean call(CustomException e) {
                        mActivity.mDialog.dismiss();
                        return false;
                    }
                }));




    }

    public void switchRedType() {
        mActivity.switchRedKind();
    }


}
