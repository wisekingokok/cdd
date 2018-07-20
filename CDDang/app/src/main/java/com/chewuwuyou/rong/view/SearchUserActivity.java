package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.rong.adapter.SearchUserAdapter;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Conversation;

/**
 * Created by xxy on 2016/9/18 0018.
 */
public class SearchUserActivity extends CDDBaseActivity implements View.OnClickListener {
    private EditText searchET;
    private TextView cancel;
    private ListView listView;
    private ImageButton clear;
    private List<AllGroup> allGroups;
    private SearchUserAdapter adapter;

    private List<Userfriend> friends = new ArrayList<>();
    public static final String ALL_GROUP = "all_group";

    @ViewInject(id = R.id.search_list)//好友列表
    private LinearLayout mSearchList;
    @ViewInject(id = R.id.search_result)//好友列表
    private LinearLayout mSearchResult;
    @ViewInject(id = R.id.search_list_context)//好友列表
    private LinearLayout mSearchListContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_user);
        allGroups = (List<AllGroup>) getIntent().getSerializableExtra(ALL_GROUP);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        searchET = (EditText) findViewById(R.id.searchET);
        cancel = (TextView) findViewById(R.id.cancel);
        listView = (ListView) findViewById(R.id.listView);
        clear = (ImageButton) findViewById(R.id.clear);
    }

    @Override
    protected void initData() {
        listView.setAdapter(adapter = new SearchUserAdapter(this));
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

                if(!TextUtils.isEmpty(s.toString().trim())){
                    friends.clear();
                    clear.setVisibility(View.VISIBLE);
                    if (s == null || TextUtils.isEmpty(s.toString()) || allGroups == null || allGroups.size() <= 0) {
                        adapter.setData(friends, "");
                    } else {
                        String sStr = s.toString();
                        for (AllGroup group : allGroups) {
                            List<Userfriend> userfriends = group.getFriends();
                            if (userfriends == null || userfriends.size() <= 0) continue;
                            for (Userfriend userfriend : userfriends) {
                                String name = TextUtils.isEmpty(userfriend.getName()) ? "" : userfriend.getName();
                                String nick = TextUtils.isEmpty(userfriend.getNickname()) ? "" : userfriend.getNickname();
                                String phone = TextUtils.isEmpty(userfriend.getPhone()) ? "" : userfriend.getPhone();
                                if (nick.indexOf(sStr) >= 0 || name.indexOf(sStr) >= 0 || phone.indexOf(sStr) >= 0)
                                    friends.add(userfriend);
                            }
                        }
                        if(friends.size()==0){
                            mSearchList.setVisibility(View.GONE);
                            mSearchResult.setVisibility(View.VISIBLE);
                        }else{
                            mSearchList.setVisibility(View.VISIBLE);
                            mSearchResult.setVisibility(View.GONE);
                            mSearchListContext.setVisibility(View.GONE);
                            adapter.setData(friends, sStr);
                        }

                    }
                }else{
                    mSearchList.setVisibility(View.VISIBLE);
                    mSearchListContext.setVisibility(View.VISIBLE);
                    mSearchResult.setVisibility(View.GONE);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Userfriend info = (Userfriend) adapter.getItem(position);
                if (!TextUtils.isEmpty(info.getUserId()) && !"_null".equals(info.getUserId())) {
//                    Intent intent = new Intent(SearchUserActivity.this, PersonalHomeActivity2.class);
//                    intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, info.getUserId());
//                    startActivity(intent);
                    Intent intent = new Intent(SearchUserActivity.this, RongChatActivity.class);
                    intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PRIVATE);
                    intent.putExtra(RongChatMsgFragment.KEY_TARGET, info.getUserId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finishActivity();
                break;
            case R.id.clear:
                searchET.setText("");
                mSearchList.setVisibility(View.VISIBLE);
                mSearchListContext.setVisibility(View.VISIBLE);
                mSearchResult.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
