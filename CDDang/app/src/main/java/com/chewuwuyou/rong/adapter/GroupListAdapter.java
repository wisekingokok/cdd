package com.chewuwuyou.rong.adapter;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BlackList;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageLoaderBuilder;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.activity.im.BaiduMapActivity;
import com.chewuwuyou.eim.activity.im.MessageTransActivity;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.rong.bean.CDDGifMsg;
import com.chewuwuyou.rong.bean.CDDLBSMsg;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.GroupList;
import com.chewuwuyou.rong.bean.UserBean;
import com.chewuwuyou.rong.bean.WholeGroup;
import com.chewuwuyou.rong.listener.RongLocClickListener;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.rong.utils.RongMsgType;
import com.chewuwuyou.rong.utils.RongVoicePlayer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 聊天消息适配器
 *
 * @author xxy
 */
public class GroupListAdapter extends BaseAdapter {

    private Context mContext;
    private List<WholeGroup> mWholeGroupList;

    public GroupListAdapter(List<WholeGroup> mWholeGroupList, Context mContext) {
        this.mWholeGroupList = mWholeGroupList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mWholeGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWholeGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_list, null);
            mViewHolder.mGroupHeadPortrait = (ImageView) view.findViewById(R.id.group_head_portrait);
            mViewHolder.mGroupHeadName = (TextView) view.findViewById(R.id.group_head_name);
            mViewHolder.mGroupNumber = (TextView) view.findViewById(R.id.group_number);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.mGroupHeadName.setText(mWholeGroupList.get(position).getGroupName());
        mViewHolder.mGroupNumber.setText("("+mWholeGroupList.get(position).getImGroupMemberCount()+")");
        ImageUtils.displayImage(mWholeGroupList.get(position).getGroupImgUrl(), mViewHolder.mGroupHeadPortrait, 10);
        return view;
    }
    class ViewHolder{
        private ImageView mGroupHeadPortrait;
        private TextView mGroupHeadName,mGroupNumber;
    }

}
