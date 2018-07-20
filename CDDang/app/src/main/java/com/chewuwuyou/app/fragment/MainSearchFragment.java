package com.chewuwuyou.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.MainSearchAdapter;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.callback.FragmentCallBack;
import com.chewuwuyou.app.callback.FragmentCallBackBuilder;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.eim.util.MsgUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainSearchFragment extends BaseFragment implements OnClickListener, FragmentCallBackBuilder {
    private View mContentView;

    private EditText searchET;
    private TextView cancel;
    private ListView listView;
    private ImageView clear;
    private List<ChatUserInfo> list = new ArrayList<ChatUserInfo>();

    private Map<String, ChatUserInfo> userMap;
    private MainSearchAdapter adapter;
    public static final String NULL_USER_ID = "null_user_id";//用于识别没有搜索结果时的ID
    private String mZfMsg;//转发的消息

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_main_search, null);
        initView();
        initData();
        initEvent();
        return mContentView;
    }

    @Override
    protected void initView() {
        searchET = (EditText) mContentView.findViewById(R.id.searchET);
        cancel = (TextView) mContentView.findViewById(R.id.cancel);
        listView = (ListView) mContentView.findViewById(R.id.listView);
        clear = (ImageView) mContentView.findViewById(R.id.clear);
    }

    @Override
    protected void initData() {
        listView.setAdapter(adapter = new MainSearchAdapter(getActivity()));
    }

    @Override
    protected void initEvent() {
        cancel.setOnClickListener(this);
        clear.setOnClickListener(this);
        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.clear();
                if (s == null || TextUtils.isEmpty(s.toString()) || userMap == null || userMap.size() <= 0) {
                    adapter.setData(list);
                } else {
                    String sStr = s.toString();
                    for (ChatUserInfo v : userMap.values()) {
                        String nick = v.getNick() == null ? "" : v.getNick();
                        if (nick.indexOf(sStr) >= 0 || v.getId().indexOf(sStr) >= 0) {
                            list.add(v);
                        }
                    }
                    adapter.setData(list);
                }
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatUserInfo info = (ChatUserInfo) adapter.getItem(position);
                if (info.getId().equals(NULL_USER_ID)) return;
                if(!TextUtils.isEmpty(mZfMsg)){//转发消息不为空
                    MsgUtil.showChatMsgZfDialog(getActivity(),mZfMsg, info.getUserName(), info.getId()+"@iz232jtyxeoz");
                }else{
                    Intent intent = new Intent(getActivity(), PersonalHomeActivity2.class);
                    intent.putExtra("skip", 106);
                    intent.putExtra("userId", info.getId());
                    startActivity(intent);
                }

            }
        });
    }

    @SuppressWarnings("unchecked")
    public void setData(Object obj) {
        userMap = (Map<String, ChatUserInfo>) obj;
    }

    public void setMessage(Object obj, String msg) {
        userMap = (Map<String, ChatUserInfo>) obj;
        mZfMsg = msg;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (callback != null)
                    callback.callback(4, null);
                break;
            case R.id.clear:
                searchET.setText("");
                break;
            default:
                break;
        }
    }

    private FragmentCallBack callback;

    @Override
    public void setFragmentCallBack(FragmentCallBack callback) {
        this.callback = callback;
    }
}
