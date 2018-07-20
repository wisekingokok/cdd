package com.chewuwuyou.eim.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.IMMessage;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.Calendar;

/**
 * Created by yuyong on 16/8/24.
 */
public class MsgUtil {

    public static void sendMessage(Context context, String messageContent, String to) throws Exception {
        String time = DateUtil.date2Str(Calendar.getInstance(), Constant.MS_FORMART);
        Message message = new Message();
        Chat chat = ChatManager.getInstanceFor(
                XmppConnectionManager.getInstance().getConnection())
                .createChat(to, new MessageListener() {

                    @Override
                    public void processMessage(Chat arg0, Message arg1) {
                        Log.d("info", "message:" + arg1.toXML());
                    }
                });
        message.setBody(messageContent);
        if (chat != null) {// 普通聊天
            chat.sendMessage(message);
        } else if (to.contains("conference")) {// 群聊,留着备用
        }
        IMMessage newMessage = new IMMessage();
        newMessage.setMsgType(1);
        newMessage.setFromSubJid(chat.getParticipant());
        newMessage.setContent(messageContent);
        newMessage.setMsgTo(CacheTools.getUserData("userId"));
        newMessage.setTime(time);
        newMessage.setType(ChatUtils.getChatMessageType(messageContent));// 设置消息类型
        newMessage.setContent(messageContent);
        MessageManager.getInstance(context).saveIMMessage(newMessage);
        Presence presence = XmppConnectionManager.getInstance().getConnection()
                .getRoster().getPresence(to);
        if (!presence.isAvailable()) {
            // 用户不在线，或者离线的时候，我们发发推送提醒下他们来登陆看离线消息。
            AjaxParams params = new AjaxParams();
            params.put("userId", chat.getParticipant().split("@")[0]);
            params.put("talk", ChatUtils.returnMsg(messageContent));
            NetworkUtil.postMulti(NetworkUtil.SEND_UNAVAIL, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    MyLog.i("YUY", "=======发送离线消息成功");
                }
            });
        }
    }

    /**
     * @param context
     * @param msg     转发的消息
     * @param toName  转发人的名称
     * @param to      转发人id
     */
    public static void showChatMsgZfDialog(final Context context, final String msg, final String toName, final String to) {

        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.msg_img_zf_dialog, null);
        TextView title = (TextView) layout.findViewById(R.id.zf_people_tv);
        title.setText("转发给  " + toName);
        ImageView image = (ImageView) layout.findViewById(R.id.zf_iv);
        TextView msgTV= (TextView) layout.findViewById(R.id.msg_tv);
        MyLog.e("YUY", "转发的信息---- " + msg);
        if(msg.contains("#IMG")&&msg.contains("GMI#")){
            msgTV.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            ImageUtils.displayChatImage(msg, image, 10);
        }else{
            msgTV.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            msgTV.setText(msg);
        }
        Button cancenBtn = (Button) layout.findViewById(R.id.cancel_btn);
        cancenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button zfBtn = (Button) layout.findViewById(R.id.zf_btn);
        zfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //转发消息
                try {
                    sendMessage(context, msg, to);
                    ToastUtil.toastShow(context,"发送成功");
                    ((Activity)context).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.setContentView(layout);
        dialog.show();

    }

}
