package com.chewuwuyou.app.transition_presenter;

import android.content.Intent;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.TransferAccountDetail;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.TransferAccountsDetailMode;
import com.chewuwuyou.app.transition_view.activity.TransferAccountsDetailActivity;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.WalletActivity;
import com.chewuwuyou.app.utils.CacheTools;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ZQ on 2016/10/10.
 */

public class TransferAccountsDetailPresenter extends BasePresenter {

    private TransferAccountsDetailActivity mActivity;
    private TransferAccountsDetailMode mModel;


    public TransferAccountsDetailPresenter(TransferAccountsDetailActivity activity) {
        super(activity);
        this.mActivity = activity;
        mModel = new TransferAccountsDetailMode();

    }

    public void getAccountsDetail(String transferId) {
        mModel.getTransferDetail(transferId).compose(this.<ResponseNBean<TransferAccountDetail>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<ResponseNBean<TransferAccountDetail>>() {
                    @Override
                    public void call(ResponseNBean<TransferAccountDetail> transferAccountDetailResponseBean) {
                        mActivity.dismissNetRequest();
                        mActivity.updateView(transferAccountDetailResponseBean.getData());

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                      mActivity.dismissNetRequest();
                    }
                }, new Func1<CustomException, Boolean>() {
                    @Override
                    public Boolean call(CustomException e) {
                        mActivity.showErroPager();
                        return true;
                    }
                }));

    }

    public void goMyWallet() {

        StatService.onEvent(mActivity, "ClickMainWallet", "点击主界面钱包");
        if (CacheTools.getUserData("role") != null) {
            // TODO 判断是否开启设置支付密码 本版本不开启此功能
            // if(CacheTools.getUserData("isPayPass")!=null&&CacheTools.getUserData("isPayPass").equals("1")){
            // createWalletPayDialog(getActivity());
            // }else{
            Intent intent = new Intent(mActivity, WalletActivity.class);
            mActivity.startActivity(intent);
            // }
        } else {
            Intent lIntent = new Intent(mActivity, LoginActivity.class);
            mActivity.startActivity(lIntent);
        }
    }

    public void netRequestAgain(String transferId) {
        mActivity.showNetRequest();
        mActivity.dismissErroPager();
        getAccountsDetail(transferId);
    }
}
