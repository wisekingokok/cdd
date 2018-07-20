package com.chewuwuyou.app.transition_presenter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.RongChatMsgModel;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.chewuwuyou.rong.utils.RongApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.CustomServiceConfig;
import io.rong.imlib.ICustomServiceListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.CSGroupItem;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.CustomServiceMode;
import io.rong.imlib.model.Message;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by xxy on 2016/10/17 0017.
 */

public class RongChatMsgPresenter extends BasePresenter {
    RongChatMsgModel model;
    RongChatMsgFragment view;

    private String address;//是否有邮寄地址
    private String mIsBusOrKefu;
    private String mBusinesId;
    private String mHandlerId;
    private String mUserTelephone;
    private String black;
    private boolean havePhone = false;
    private boolean haveOrder = false;

    public RongChatMsgPresenter(Context context, RongChatMsgFragment view) {
        super(context);
        this.view = view;
        model = new RongChatMsgModel();
    }

    public String getAddress() {
        return address;
    }

    public String getBlack() {
        return black;
    }

    public boolean isHaveOrder() {
        return haveOrder;
    }

    public boolean isHavePhone() {
        return havePhone;
    }

    public String getmBusinesId() {
        return mBusinesId;
    }

    public String getmHandlerId() {
        return mHandlerId;
    }

    public String getmIsBusOrKefu() {
        return mIsBusOrKefu;
    }

    public String getmUserTelephone() {
        return mUserTelephone;
    }

    public void ClearMessagesUnreadStatus(final Conversation.ConversationType conversationType, final String targetId) {
        //进入时把所有消息变为已读
        RongApi.clearMessagesUnreadStatus(conversationType, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                EventBus.getDefault().post(new ClearMessagesBean(conversationType, targetId));
                EventBus.getDefault().post(new ClearMessagesBean());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("RongChatMsgFragment", "重置状态失败");
            }
        });
    }

    public void getLatestMessages(final boolean isPull, int addSize, final int oldCount, Conversation.ConversationType conversationType, String targetId) {
        model.getLatestMessages(isPull, addSize, oldCount, conversationType, targetId, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (messages == null) messages = new ArrayList<>();
                Collections.reverse(messages);
                view.setMessage(!isPull, messages);
                if (isPull)
                    view.setSelection(messages.size() - oldCount);
                view.setRefreshing();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                view.setRefreshing();
            }
        });
    }

    public void getGroupById(final String targetId) {
        model.getGroupById(targetId)
                .compose(this.<NewBaseNetworkBean<GroupBean>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<GroupBean>>() {
                    @Override
                    public void call(NewBaseNetworkBean<GroupBean> groupBeanNewBaseNetworkBean) {
                        view.registerExtendMenuItem();
                        GroupBean groupBean = null;
                        if (groupBeanNewBaseNetworkBean == null || (groupBean = groupBeanNewBaseNetworkBean.getData()) == null)
                            return;
                        view.setGroupBean(groupBean);
                        view.setTitle((TextUtils.isEmpty(groupBean.getGroupName()) ? targetId : groupBean.getGroupName()) + "(" + groupBean.getImGroupMemberCount() + ")");
                    }
                }));
    }

    public void isInGroup(String targetId, String sendId) {
        model.isInGroup(targetId, sendId)
                .compose(this.<NewBaseNetworkBean<Integer>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<Integer>>() {
                    @Override
                    public void call(NewBaseNetworkBean<Integer> booleanNewBaseNetworkBean) {
                        if (booleanNewBaseNetworkBean.getData() == 1) view.setSettingVisibility();
                    }
                }));
    }

    public void startCustomService(final String targetId, CSCustomServiceInfo csInfo) {
        model.startCustomService(targetId, csInfo, new ICustomServiceListener() {

            @Override
            public void onSuccess(CustomServiceConfig customServiceConfig) {
                view.setTitle(customServiceConfig.companyName);
                RongIMClient.getInstance().switchToHumanMode(targetId);
            }

            @Override
            public void onError(int i, String s) {
                view.showDialog();
            }

            @Override
            public void onModeChanged(CustomServiceMode customServiceMode) {
                MyLog.i("YUY", "启动客服onModeChanged " + customServiceMode);
            }

            @Override
            public void onQuit(String s) {
                MyLog.i("YUY", "启动客服onQuit");
            }

            @Override
            public void onPullEvaluation(String s) {
                MyLog.i("YUY", "启动客服onPullEvaluation");
            }

            @Override
            public void onSelectGroup(List<CSGroupItem> list) {
                MyLog.i("YUY", "启动客服onSelectGroup");
            }
        });
    }

    public void whetherGrabRedPacket(String userId, String redBId, final Boolean isSend) {
        view.showProgressBar();
        model.whetherGrabRedPacket(userId, redBId)
                .compose(this.<NewBaseNetworkBean<RedPacketDetailEntity>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<RedPacketDetailEntity>>() {
                    @Override
                    public void call(NewBaseNetworkBean<RedPacketDetailEntity> whetherGrabRedBNewBaseNetworkBean) {
                        RedPacketDetailEntity whetherGrabRedB = null;
                        if (whetherGrabRedBNewBaseNetworkBean == null || (whetherGrabRedB = whetherGrabRedBNewBaseNetworkBean.getData()) == null)
                            return;
                        switch (whetherGrabRedB.getType()) {
                            case 1://群普通
                                if (isSend || whetherGrabRedB.getReceiveStatus().equals("2") || whetherGrabRedB.getReceiveStatus().equals("1")) {
                                    view.toDetailActivity(whetherGrabRedB);//去详情
                                } else {
                                    view.toActivity(whetherGrabRedB);  //去弹出框
                                }
                                break;
                            case 0://单聊
                                if (isSend || whetherGrabRedB.getReceiveStatus().equals("2") || whetherGrabRedB.getReceiveStatus().equals("1")) {
                                    view.toDetailActivity(whetherGrabRedB);  //去详情
                                } else {
                                    view.toActivity(whetherGrabRedB);  //去弹出框
                                }
                                break;
                            case 2://群随机
                                if (isSend) {
                                    if (whetherGrabRedB.getReceiveStatus().equals("1")) {
                                        view.toDetailActivity(whetherGrabRedB);   //去详情
                                    } else {
                                        view.toActivity(whetherGrabRedB); //去弹出
                                    }
                                } else {
                                    if (whetherGrabRedB.getReceiveStatus().equals("1")) {
                                        view.toDetailActivity(whetherGrabRedB); //去详情
                                    } else {
                                        view.toActivity(whetherGrabRedB); //去弹出
                                    }
                                }
                                break;
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        view.disProgressBar();
                    }
                }, new Func1<CustomException, Boolean>() {
                    @Override
                    public Boolean call(CustomException e) {
                        e.printStackTrace();
                        return false;
                    }
                }));
    }

    public void getUserById(String id) {
        model.getUserById(id)
                .compose(this.<NewBaseNetworkBean<List<RongUserBean>>>applySchedulers())
                .subscribe(defaultSubscriber(new Action1<NewBaseNetworkBean<List<RongUserBean>>>() {
                    @Override
                    public void call(NewBaseNetworkBean<List<RongUserBean>> listNewBaseNetworkBean) {
                        List<RongUserBean> rongUserBeans = null;
                        if (listNewBaseNetworkBean == null || (rongUserBeans = listNewBaseNetworkBean.getData()) == null || rongUserBeans.size() <= 0) {
                            view.registerExtendMenuItem();
                            return;
                        }
                        RongUserBean rongUserBean = rongUserBeans.get(0);
                        view.setRongUserBean(rongUserBean);
                        String noteName = rongUserBean.getNoteName();
                        String nickName = rongUserBean.getNick();
                        String userName = rongUserBean.getUserName();
                        String mUserName = CarFriendQuanUtils.showCarFriendName(noteName, nickName, userName);
                        view.setTitle(mUserName);
                        mIsBusOrKefu = String.valueOf(rongUserBean.getIsBusOrKeFu());
                        mBusinesId = String.valueOf(rongUserBean.getBusUserId());
                        mHandlerId = String.valueOf(rongUserBean.getHandlerId());
                        mUserTelephone = rongUserBean.getTelephone();
                        if (rongUserBean.getIsBusOrKeFu() == com.chewuwuyou.app.utils.Constant.CHAT_USER_ROLE.BUSINESS || rongUserBean.getIsBusOrKeFu() == com.chewuwuyou.app.utils.Constant.CHAT_USER_ROLE.SERVER) {// 如果是商家或客服才显示出给TA下单
                            haveOrder = true;
                            havePhone = true;
                            if (rongUserBean.getIsBusOrKeFu() == com.chewuwuyou.app.utils.Constant.CHAT_USER_ROLE.BUSINESS)
                                view.setTitle(rongUserBean.getRealName());
                        }
                        RongUserBean.MailingAddressBean mailingAddressBean = rongUserBean.getMailingAddress();
                        if (mailingAddressBean != null && !TextUtils.isEmpty(mailingAddressBean.getReceiver())) {//判断联系人不为空
                            StringBuffer mMailAddressSB = new StringBuffer();
                            mMailAddressSB.append("联系人:").append(mailingAddressBean.getReceiver()).append("\n");
                            mMailAddressSB.append("联系电话:").append(mailingAddressBean.getPhone()).append("\n");
                            mMailAddressSB.append("邮寄地址:").append(mailingAddressBean.getRegion()).append(mailingAddressBean.getAddress()).append("\n");
                            mMailAddressSB.append("邮编:").append(mailingAddressBean.getZipCode()).append("\n");
                            address = mMailAddressSB.toString();
                        }
                        if ("2".equals(rongUserBean.getBusType())) {
                            String str = "品牌-" + view.getTitle();
                            int roleStart = 0;
                            int roleEnd = 3;
                            int start = 4;
                            int end = str.length();
                            SpannableStringBuilder style = new SpannableStringBuilder(str);
                            style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.b_pinpai)), roleStart, roleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            style.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            view.setTitle(style);
                            view.setTitleCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.pinpai));
                        } else if ("3".equals(rongUserBean.getBusType())) {
                            String str = "会员-" + view.getTitle();
                            int roleStart = 0;
                            int roleEnd = 3;
                            int start = 4;
                            int end = str.length();
                            SpannableStringBuilder style = new SpannableStringBuilder(str);
                            style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.f77b00)), roleStart, roleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            style.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            view.setTitle(style);
                            view.setTitleCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.huiyuan));
                        }
                        black = rongUserBean.getBlack();
                        view.registerExtendMenuItem();
                    }
                }));
    }
}
