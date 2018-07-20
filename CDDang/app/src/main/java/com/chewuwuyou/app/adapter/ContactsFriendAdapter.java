package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.SearchFriendBean;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.util.StringUtil;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:添加联系人好友
 * @author:yuyong
 * @date:2015-5-29下午1:49:04
 * @version:1.2.1
 */
public class ContactsFriendAdapter extends BaseAdapter implements View.OnClickListener {

    private List<SearchFriendBean> mSearchFriendBeans;
    private Context mContext;
    private LayoutInflater inflater;

    public ContactsFriendAdapter(Context context, List<SearchFriendBean> searchFriends) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.mSearchFriendBeans = searchFriends == null ? new ArrayList<SearchFriendBean>() : searchFriends;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mSearchFriendBeans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mSearchFriendBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ListItemView listItemView = null;
        final Integer position_integer = Integer.valueOf(position);
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = inflater.inflate(R.layout.add_contacts_friend_item, null);
            listItemView.mHeadIV = (ImageView) convertView.findViewById(R.id.user_head_iv);
            listItemView.mNickTV = (TextView) convertView.findViewById(R.id.user_nick_tv);
            listItemView.mAddFriendBtn = (TextView) convertView.findViewById(R.id.add_friend_btn);
            listItemView.mTextView_contact = (TextView) convertView.findViewById(R.id.user_nick_contact_tv);

            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        final SearchFriendBean searBean = mSearchFriendBeans.get(position);
        if (!TextUtils.isEmpty(searBean.getUrl())) {
            ImageUtils.displayImage(searBean.getUrl(), listItemView.mHeadIV, 8);
        } else {
            listItemView.mHeadIV.setImageResource(R.drawable.user_fang_icon);
        }
        listItemView.mHeadIV.setOnClickListener(this);
        listItemView.mHeadIV.setTag(position_integer);

        listItemView.mTextView_contact.setText("通讯录好友：" + searBean.getContactName());
        if (!TextUtils.isEmpty(searBean.getNickName())) {
            listItemView.mNickTV.setText(searBean.getNickName());
        } else if (!TextUtils.isEmpty(searBean.getUserName())) {
            listItemView.mNickTV.setText(searBean.getUserName());
        } else
            listItemView.mNickTV.setText(searBean.getUserId());

        final TextView addFriendBtn = listItemView.mAddFriendBtn;
        if (searBean.getFriend().equals("0")) {
            listItemView.mAddFriendBtn.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("static-access")
                @Override
                public void onClick(View arg0) {

                    View view = LayoutInflater.from(mContext).inflate(R.layout.view_edittext, null);
                    final EditText mcontent = (EditText) view.findViewById(R.id.input);
                    mcontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                    if (!TextUtils.isEmpty(CacheTools.getUserData("nickName"))) {
                        mcontent.setText("我是" + CacheTools.getUserData("nickName"));
                    } else if (!TextUtils.isEmpty(CacheTools.getUserData("realName")))
                        mcontent.setText("我是" + CacheTools.getUserData("realName"));
                    else
                        mcontent.setText("我是");
                    mcontent.setHint("请输入验证信息");


                    new AlertDialog.Builder(mContext)
                            .setTitle("验证信息")
                            .setView(view)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final ProgressDialog m = ProgressDialog.show(mContext, null, "请求发送中", false);
                                    m.show();

                                    // TODO Auto-generated method stub
                                    AjaxParams params = new AjaxParams();
                                    params.put("userId", CacheTools.getUserData("rongUserId"));
                                    params.put("friendId", searBean.getUserId() + "");
                                    params.put("groupId", "1");
                                    params.put("remarks", mcontent.getText().toString());
                                    new NetworkUtil().postMulti(NetworkUtil.ADD_FRIEND, params, new AjaxCallBack<Object>() {
                                        @Override
                                        public void onSuccess(Object t) {
                                            // TODO Auto-generated method stub
                                            super.onSuccess(t);
                                            MyLog.i("加好友发送成功");
                                            Toast.makeText(mContext, R.string.wait_friend_agree, Toast.LENGTH_SHORT).show();

//                                            addFriendBtn.setBackgroundColor(Color.parseColor("#00000000"));
//                                            addFriendBtn.setText("已添加");
//                                            addFriendBtn.setTextColor(mContext.getResources().getColor(R.color.common_text_color));
//                                            addFriendBtn.setFocusable(false);
//                                            addFriendBtn.setClickable(false);


                                            m.cancel();
                                        }
                                    });


                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(@NonNull DialogInterface dialog, int which) {
                                }
                            })
                            .create().show();


                }
            });
        } else {
            listItemView.mAddFriendBtn.setBackgroundColor(Color.parseColor("#00000000"));
            listItemView.mAddFriendBtn.setText("已添加");
            listItemView.mAddFriendBtn.setTextColor(mContext.getResources().getColor(R.color.common_text_color));
            listItemView.mAddFriendBtn.setFocusable(false);
            listItemView.mAddFriendBtn.setClickable(false);
        }
        return convertView;
    }

    public class ListItemView {
        ImageView mHeadIV;
        TextView mNickTV;
        TextView mAddFriendBtn;
        TextView mTextView_contact;
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Object tag = arg0.getTag();
        Integer position_integer = null;
        if (tag instanceof Integer) {
            position_integer = (Integer) tag;
        }
        switch (arg0.getId()) {
            case R.id.user_head_iv:
                Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
                intent.putExtra("userId", mSearchFriendBeans.get(position_integer).getId());
                mContext.startActivity(intent);
                break;
        }
    }

}
