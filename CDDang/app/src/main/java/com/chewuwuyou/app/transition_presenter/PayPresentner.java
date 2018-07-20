package com.chewuwuyou.app.transition_presenter;

import android.content.Context;

import com.chewuwuyou.app.transition_constant.Constants;
import com.chewuwuyou.app.transition_entity.PayReusltBean;
import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.TransferParamBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.PayModel;
import com.chewuwuyou.app.transition_view.activity.fragment.PayDialogFragment;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.rong.bean.CDDHBMsg;
import com.chewuwuyou.rong.bean.CDDZZMsg;
import com.chewuwuyou.rong.bean.RefreshBean;
import com.chewuwuyou.rong.utils.RongApi;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Created by yuyong on 16/10/17.
 */

public class PayPresentner extends BasePresenter {
    private PayDialogFragment payDialogFragment;
    private PayModel payModel;
    private Context mContext;

    public PayPresentner(Context context, PayDialogFragment payDialogFragment) {
        super(context);
        this.mContext = context;
        this.payDialogFragment = payDialogFragment;
        payModel = new PayModel();
    }


    /**
     * 支付红包
     *
     * @param redInit
     * @param pwd
     */
    public void payRedPacket(final RedInit redInit, final String pwd) {
        rx.Observable<PayReusltBean> observable = payModel.sendRedPacket(UserBean.getInstall(payDialogFragment.getActivity()).getId(), redInit.getId(), MD5Util.getMD5(pwd));
        observable.compose(this.<PayReusltBean>applySchedulers()).subscribe(defaultSubscriber(new Action1<PayReusltBean>() {
            @Override
            public void call(PayReusltBean payReusltBean) {
                sendRedPacketMsg(redInit);
                EventBus.getDefault().post(Constants.SEND_RED_PACKET_SUCCESS);
                payDialogFragment.showSuccessView("发送成功");
                payDialogFragment.dismiss();
            }
        }, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                return true;
            }
        }));

    }

    /**
     * 转账
     *
     * @param transferParamBean
     */
    public void transfer(final TransferParamBean transferParamBean, String pwd) {
        rx.Observable<ResponseNBean<String>> observable = payModel.transfer(transferParamBean.getAmount(), MD5Util.getMD5(pwd), transferParamBean.getMeAccId(), transferParamBean.getOtherAccId(), transferParamBean.getLeaveMessage());
        observable.compose(this.<ResponseNBean<String>>applySchedulers()).subscribe(defaultSubscriber(new Action1<ResponseNBean<String>>() {
            @Override
            public void call(ResponseNBean<String> trans) {
                if (trans.getCode() == 0) {
                    payDialogFragment.showSuccessView("转账成功");
                    sendTransferMsg(transferParamBean, trans.getData());
                    payDialogFragment.finishActivity();
                } else
                    payDialogFragment.showSuccessView(trans.getMessage());
                payDialogFragment.dismiss();
            }
        }, null, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                return false;
            }
        }));

    }

    /**
     * 发送转账消息
     *
     * @param transferParamBean
     */
    public void sendTransferMsg(TransferParamBean transferParamBean, String transId) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Message message = Message.obtain(transferParamBean.getOtherAccId(), Conversation.ConversationType.PRIVATE, CDDZZMsg.obtain(transId, transferParamBean.getLeaveMessage(), transferParamBean.getAmount()));
        RongApi.sendMessage(message, "[转账]", simpleDate.format(new Date()), new IRongCallback.ISendMessageCallback() {

            @Override
            public void onAttached(Message message) {
                EventBus.getDefault().post(message);
            }

            @Override
            public void onSuccess(Message message) {
                EventBus.getDefault().post(new RefreshBean());
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                EventBus.getDefault().post(new RefreshBean());
            }
        });
    }

    /**
     * 发送红包消息
     *
     * @param
     */
    public void sendRedPacketMsg(RedInit redInit) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Message message = Message.obtain(redInit.getAccidFriend(), redInit.getType().equals(Constants.RED_PACKET_TYPE.PERSON_RED_PACKET) ? Conversation.ConversationType.PRIVATE : Conversation.ConversationType.GROUP, CDDHBMsg.obtain(redInit.getId(), redInit.getLeaveMessage(), redInit.getMoney()));
        RongApi.sendMessage(message, "[红包]", simpleDate.format(new Date()), new IRongCallback.ISendMessageCallback() {

            @Override
            public void onAttached(Message message) {
                EventBus.getDefault().post(message);
            }

            @Override
            public void onSuccess(Message message) {
                EventBus.getDefault().post(new RefreshBean());
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                EventBus.getDefault().post(new RefreshBean());
            }
        });


    }


}
