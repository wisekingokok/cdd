package com.chewuwuyou.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.utils.ImageLoaderBuilder;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.rong.bean.CDDLBSMsg;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.SendMsgBean;
import com.chewuwuyou.rong.bean.WholeGroup;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.rong.utils.RongMsgType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

/**
 * Created by xxy on 2016/9/14 0014.
 */
public class SelectFirentDialog extends DialogFragment {

    private GridView gridView;
    private TextView textView;
    private EditText editText;
    private Button cancel;
    private Button ok;
    private ImageView imgContent;
    private GridAdapter adapter;
    private List<ReSendBean> userlist;
    private String pushStr = "";
    private String objectName;
    private MessageContent messageContent;
    private FinishCallback finishCallback;

    public static final String MESSAGE_KEY = "msg_key";
    public static final String LIST_KEY = "list_key";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//yyyy-MM-dd H:m:s


    static class ReSendBean implements Serializable {
        public String id;
        public String url;
        public Conversation.ConversationType conversationType;

        ReSendBean() {
        }

        ReSendBean(String id, String url, Conversation.ConversationType conversationType) {
            this.id = id;
            this.url = url;
            this.conversationType = conversationType;
        }
    }

    /**
     * 转发给用户
     *
     * @param message
     * @param userList
     * @return
     */
    public static SelectFirentDialog getIntense(Message message, List<Userfriend> userList) {
        if (userList == null) return null;
        List<ReSendBean> list = new ArrayList<>();
        for (Userfriend u : userList) {
            list.add(new ReSendBean(u.getUserId(), u.getPortraitUri(), Conversation.ConversationType.PRIVATE));
        }
        SelectFirentDialog selectFirentDialog = new SelectFirentDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MESSAGE_KEY, message);
        bundle.putSerializable(LIST_KEY, (Serializable) list);
        selectFirentDialog.setArguments(bundle);
        return selectFirentDialog;
    }

    /**
     * 转发给群
     *
     * @param message
     * @param wholeGroup
     * @return
     */
    public static SelectFirentDialog getIntense(Message message, WholeGroup wholeGroup) {
        if (wholeGroup == null) return null;
        List<ReSendBean> list = new ArrayList<>();
        list.add(new ReSendBean(wholeGroup.getId() + "", wholeGroup.getGroupImgUrl(), Conversation.ConversationType.GROUP));
        SelectFirentDialog selectFirentDialog = new SelectFirentDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MESSAGE_KEY, message);
        bundle.putSerializable(LIST_KEY, (Serializable) list);
        selectFirentDialog.setArguments(bundle);
        return selectFirentDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Message message = bundle.getParcelable(MESSAGE_KEY);
            userlist = (List<ReSendBean>) bundle.getSerializable(LIST_KEY);
            messageContent = message.getContent();
            objectName = message.getObjectName();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_sele_firent, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imgContent = (ImageView) view.findViewById(R.id.imgContent);
        gridView = (GridView) view.findViewById(R.id.gridView);
        textView = (TextView) view.findViewById(R.id.content);
        editText = (EditText) view.findViewById(R.id.con_et);
        cancel = (Button) view.findViewById(R.id.cancel);
        ok = (Button) view.findViewById(R.id.ok);
        if (adapter == null) {
            adapter = new GridAdapter(getActivity(), userlist);
        }
        gridView.setAdapter(adapter);
        init();
        return view;
    }

    private void init() {
        ok.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        if (objectName.equals(RongMsgType.CDD_TXT_MSG)) {//颜文字
            CDDYWZMsg cddywzMsg = (CDDYWZMsg) messageContent;
            textView.setText(pushStr = cddywzMsg.getContent());
            textView.setVisibility(View.VISIBLE);
            imgContent.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.GIF_TXT_MSG)) {
            textView.setText(pushStr = "[表情]");
            textView.setVisibility(View.VISIBLE);
            imgContent.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.CDD_LBS_MSG)) {
            CDDLBSMsg cddywzMsg = (CDDLBSMsg) messageContent;
            textView.setText(pushStr = "[位置]" + cddywzMsg.getPoi());
            textView.setVisibility(View.VISIBLE);
            imgContent.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.RC_TXT_MSG)) {
            TextMessage cddywzMsg = (TextMessage) messageContent;
            textView.setText(pushStr = cddywzMsg.getContent());
            textView.setVisibility(View.VISIBLE);
            imgContent.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.RC_VC_MSG)) {
            textView.setText(pushStr = "[语音]");
            textView.setVisibility(View.VISIBLE);
            imgContent.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.RC_IMG_MSG)) {
            ImageMessage imageMessage = (ImageMessage) messageContent;
            textView.setText(pushStr = "[图片]");
            textView.setVisibility(View.GONE);
            imgContent.setVisibility(View.VISIBLE);
            ImageLoaderBuilder.Builder().loadFromSDCard(imageMessage.getThumUri().getPath()).displayImage(imgContent);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    dismiss();
                    if (finishCallback != null)
                        finishCallback.finishActivity(false);
                    break;
                case R.id.ok:
                    String tip = editText.getText().toString();
                    sendMsg(tip);
                    break;
            }
        }
    };

    private void sendMsg(final String tip) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final ReSendBean t : userlist) {
                    sendMsg(messageContent, t.id, t.conversationType);
                    if (!TextUtils.isEmpty(tip))
                        sendMsg(TextMessage.obtain(tip), t.id, t.conversationType);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        dismiss();
        if (finishCallback != null)
            finishCallback.finishActivity(true);
    }

    public void setFinishCallback(FinishCallback finishCallback) {
        this.finishCallback = finishCallback;
    }

    public interface FinishCallback {
        /**
         * 是否需要关闭Activity
         *
         * @param isFinishActivity
         */
        void finishActivity(boolean isFinishActivity);
    }

    /**
     * 统一发送消息
     *
     * @param messageContent
     * @return
     */
    private void sendMsg(MessageContent messageContent, String targetId, Conversation.ConversationType conversationType) {
        Message message = Message.obtain(targetId, conversationType, messageContent);
        RongApi.sendMessage(message, pushStr, simpleDateFormat.format(new Date()), getSendMsgCallback());
    }

    /**
     * 取得回调
     *
     * @return
     */
    private IRongCallback.ISendMessageCallback getSendMsgCallback() {
        return new IRongCallback.ISendMessageCallback() {

            @Override
            public void onAttached(Message message) {
                EventBus.getDefault().post(new SendMsgBean(1, message));
            }

            @Override
            public void onSuccess(Message message) {
                EventBus.getDefault().post(new SendMsgBean(message));
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                EventBus.getDefault().post(new SendMsgBean(message));
            }
        };
    }

    class GridAdapter extends BaseAdapter {
        private List<ReSendBean> list;
        private Context context;

        public GridAdapter(Context context, List<ReSendBean> list) {
            this.context = context;
            this.list = list == null ? new ArrayList<ReSendBean>() : list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder(convertView = LayoutInflater.from(context).inflate(R.layout.item_select_f_grid, null));
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getServerIP(list.get(position).url)).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(vh.img);
            return convertView;
        }


        class ViewHolder {
            ImageView img;

            public ViewHolder(final View view) {
                img = (ImageView) view.findViewById(R.id.img);
            }
        }
    }
}
