package com.chewuwuyou.app.transition_presenter;

import android.content.Context;
import android.text.TextUtils;

import com.chewuwuyou.app.transition_entity.RateFee;
import com.chewuwuyou.app.transition_entity.TransferConfig;
import com.chewuwuyou.app.transition_model.TransferAccountModel;
import com.chewuwuyou.app.transition_view.activity.TransferAccountActivity;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;
import com.chewuwuyou.rong.bean.RongUserBean;

import java.text.DecimalFormat;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func3;

/**
 * Created by xxy on 2016/10/18 0018.
 */

public class TransferAccountPresenter extends BasePresenter {
    TransferAccountModel model = new TransferAccountModel();
    TransferAccountActivity view;
    private TransferConfig transferConfig;
    private Double todayQuota = 0.0;
    private RongUserBean rongUserBean;

    public TransferAccountPresenter(Context context, TransferAccountActivity activity) {
        super(context);
        this.view = activity;
    }

    public void init(String targetID, String userId) {
        Observable<NewBaseNetworkBean<List<RongUserBean>>> getUser = model.getUserById(targetID);
        Observable<NewBaseNetworkBean<String>> getQuota = model.getTodayQuota(userId);
        Observable<NewBaseNetworkBean<TransferConfig>> getConfig = model.getLatestTransferRule();
        Observable.zip(getUser, getQuota, getConfig, new Func3<NewBaseNetworkBean<List<RongUserBean>>, NewBaseNetworkBean<String>, NewBaseNetworkBean<TransferConfig>, Boolean>() {
            @Override
            public Boolean call(NewBaseNetworkBean<List<RongUserBean>> listNewBaseNetworkBean, NewBaseNetworkBean<String> stringNewBaseNetworkBean, NewBaseNetworkBean<TransferConfig> config) {
                return null;
            }
        }).compose(this.<Boolean>applySchedulers()).subscribe(defaultSubscriber(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {

            }
        }));
    }

    public void getLatestTransferRule() {
        view.showProgressDialog();
        model.getLatestTransferRule()
                .compose(this.<NewBaseNetworkBean<TransferConfig>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<TransferConfig>>() {
                    @Override
                    public void call(NewBaseNetworkBean<TransferConfig> listNewBaseNetworkBean) {
                        if (listNewBaseNetworkBean.getCode() == 0)
                            transferConfig = listNewBaseNetworkBean.getData();
                        else {
                            view.showTip(listNewBaseNetworkBean.getMessage());
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        view.disProgressDialog();
                    }
                }, null));
    }

    public void getUserById(String userId) {
        model.getUserById(userId)
                .compose(this.<NewBaseNetworkBean<List<RongUserBean>>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<List<RongUserBean>>>() {
                    @Override
                    public void call(NewBaseNetworkBean<List<RongUserBean>> listNewBaseNetworkBean) {
                        if (listNewBaseNetworkBean.getCode() == 0) {
                            List<RongUserBean> rongUserBeans = null;
                            if (listNewBaseNetworkBean == null || (rongUserBeans = listNewBaseNetworkBean.getData()) == null || rongUserBeans.size() <= 0) {
                                return;
                            }
                            rongUserBean = rongUserBeans.get(0);
                            view.setUserIcon(rongUserBean);
                        } else {
                            view.showTip(listNewBaseNetworkBean.getMessage());
                        }
                    }
                }));
    }

    public void getTodayQuota(String sendId) {
        model.getTodayQuota(sendId).compose(this.<NewBaseNetworkBean<String>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<String>>() {
                    @Override
                    public void call(NewBaseNetworkBean<String> stringNewBaseNetworkBean) {
                        if (stringNewBaseNetworkBean.getCode() == 0) {
                            todayQuota = Double.valueOf(stringNewBaseNetworkBean.getData());
                        } else {
                            view.showTip(stringNewBaseNetworkBean.getMessage());
                        }
                    }
                }));
    }

    public void getTransferRateFee(final Double amount) {
        model.getTransferRateFee(amount + "")
                .compose(this.<NewBaseNetworkBean<RateFee>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<RateFee>>() {
                    @Override
                    public void call(NewBaseNetworkBean<RateFee> stringNewBaseNetworkBean) {
                        if (stringNewBaseNetworkBean.getCode() == 0) {
                            RateFee rateFee = stringNewBaseNetworkBean.getData();
                            view.setRateFee(rateFee.getPoundageFee(), (amount - Double.valueOf(rateFee.getPoundageFee())) + "");
                        } else {
                            view.showTip(stringNewBaseNetworkBean.getMessage());
                        }
                    }
                }));
    }

    public void canRate(Double money) {
        if (transferConfig != null) {
            if (transferConfig.getType() == 1) {
                Double maxMoney = Double.valueOf(transferConfig.getSingleQuota());
                Double minMoney = Double.valueOf(transferConfig.getMinMoney());//手续费
                if (minMoney >= money) {
                    view.showTipDialog("单笔最低转账金额不得低于" + new DecimalFormat("0.00").format(minMoney) + "人民币");
                    return;
                } else if (money > maxMoney) {
                    view.showTipDialog("您今日单笔最高转账金额为:" + new DecimalFormat("0.00").format(maxMoney) + "人民币");
                    return;
                }
            }else{
                Double maxMoney = Double.valueOf(transferConfig.getSingleQuota());
                Double minMoney = Double.valueOf(transferConfig.getMoney());//手续费
                if (minMoney >= money) {
                    view.showTipDialog("单笔最低转账金额不得低于" + new DecimalFormat("0.00").format(minMoney) + "人民币");
                    return;
                } else if (money > maxMoney) {
                    view.showTipDialog("您今日单笔最高转账金额为:" + new DecimalFormat("0.00").format(maxMoney) + "人民币");
                    return;
                }
            }

        }
        if (rongUserBean != null && !"1".equals(rongUserBean.getFriend())) {
            String name = "\n";
            if (TextUtils.isEmpty(rongUserBean.getNick())) {
                name += ("当当号:" + rongUserBean.getUserId());
            } else {
//                if (rongUserBean.getNick().length() == 1) {
                name += ("昵称:" + rongUserBean.getNick());
//                } else {
//                    name += ("昵称:" + rongUserBean.getNick().substring(0, 1));
//                    for (int i = 0; i < rongUserBean.getNick().length() - 1; i++) {
//                        name += "*";
//                    }
//                }
            }
            String num = "帐号:";
            if (!TextUtils.isEmpty(rongUserBean.getTelephone())) {
                num += rongUserBean.getTelephone().substring(0, 3);
                num += "****";
                num += rongUserBean.getTelephone().substring(7, 11);
            }
            view.showFriendTipDialog("对方不是你的好友,为避免转错账,请确认对方账户后再转:", num + name);
            return;
        }
        view.toPay();
    }
}
