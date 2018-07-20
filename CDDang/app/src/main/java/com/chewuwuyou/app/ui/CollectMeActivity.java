package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CattleAgentsListAdapter;
import com.chewuwuyou.app.adapter.CollectMeUserAdapter;
import com.chewuwuyou.app.bean.TrafficBroker;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.activity.im.ClientManagerChatActivity;
import com.chewuwuyou.eim.manager.XmppConnectionManager;

import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:收藏和关注我的商家
 * @author:yuyong
 * @date:2015-7-23上午11:30:32
 * @version:1.2.1
 */
public class CollectMeActivity extends CDDBaseActivity implements
        OnClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private RadioGroup mGroup;
    private JSONArray mBusinessArray = new JSONArray();
    private JSONArray mUserArray = new JSONArray();
    private ListView mCollectMeList;// 收藏我的列表
    private boolean isBusiness = true;// 是否是商家
    private Button mSendMessageBtn;// 发送消息
    private List<String> mUserChatIdList = new ArrayList<String>();
    private List<String> mBusinessChatIdList = new ArrayList<String>();

    private List<TrafficBroker> mTrafficBrokers;
    private CattleAgentsListAdapter mAdapter;
    private RelativeLayout mTitleHeight;//标题布局高度
    private RelativeLayout mNoDataRL;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    MyLog.i("YUY", "收藏我的商家 = " + msg.obj.toString());
                    try {
                        JSONObject jo = new JSONObject(msg.obj.toString());
                        JSONArray jArray = jo.getJSONArray("result");
                        for (int i = 0; i < jArray.length(); i++) {
                            if (jArray.getJSONObject(i).getString("flag").equals("1")) {
                                mUserArray.put(jArray.get(i));
                                mUserChatIdList.add(jArray.getJSONObject(i)
                                        .getString("id"));
                            } else {
                                mBusinessArray.put(jArray.get(i));
                                mBusinessChatIdList.add(jArray.getJSONObject(i)
                                        .getString("userId"));
                            }
                        }
                        if (mBusinessChatIdList.size() == 0) {
                            mNoDataRL.setVisibility(View.VISIBLE);
                        }
                        mTrafficBrokers = TrafficBroker.parseList(mBusinessArray
                                .toString());
                        mAdapter = new CattleAgentsListAdapter(
                                CollectMeActivity.this, mTrafficBrokers, 0);
                        mCollectMeList.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.NET_DATA_FAIL:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_me_ac);

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mGroup = (RadioGroup) findViewById(R.id.choose_collect_me_role_group);
        mCollectMeList = (ListView) findViewById(R.id.collect_me_list);
        mSendMessageBtn = (Button) findViewById(R.id.send_message_to_group_btn);
        mNoDataRL = (RelativeLayout) findViewById(R.id.no_date_rl);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);//根据不同手机判断
        mTitleTV.setText("客户管理");
        AjaxParams params = new AjaxParams();
        MyLog.i("YUY", "=====商家id = " + CacheTools.getUserData("userId"));
        params.put("businessId", CacheTools.getUserData("userId"));
        requestNet(mHandler, params, NetworkUtil.COLLECT_ME_URL, false, 0);
    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mSendMessageBtn.setOnClickListener(this);
        mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String isBusi = ((RadioButton) findViewById(checkedId))
                        .getText().toString();
                if (isBusi.equals("同行商家")) {
                    isBusiness = true;
                    if (mTrafficBrokers != null && mTrafficBrokers.size() > 0) {
                        mNoDataRL.setVisibility(View.GONE);
                        if (mTrafficBrokers.size() > 0) {
                            mAdapter = new CattleAgentsListAdapter(
                                    CollectMeActivity.this, mTrafficBrokers, 0);
                            mCollectMeList.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        mNoDataRL.setVisibility(View.VISIBLE);
                        mTrafficBrokers = new ArrayList<TrafficBroker>();
                        mAdapter = new CattleAgentsListAdapter(
                                CollectMeActivity.this, mTrafficBrokers, 0);
                        mCollectMeList.setAdapter(mAdapter);
                    }

                } else {
                    isBusiness = false;
                    if (mUserArray != null && mUserArray.length() > 0) {
                        mNoDataRL.setVisibility(View.GONE);
                        CollectMeUserAdapter adapter = new CollectMeUserAdapter(
                                CollectMeActivity.this, mUserArray);
                        mCollectMeList.setAdapter(adapter);
                    } else {
                        mNoDataRL.setVisibility(View.VISIBLE);
                        mUserArray = new JSONArray();
                        CollectMeUserAdapter adapter = new CollectMeUserAdapter(
                                CollectMeActivity.this, mUserArray);
                        mCollectMeList.setAdapter(adapter);
                    }
                }
            }
        });
        mCollectMeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CollectMeActivity.this,PersonalHomeActivity2.class);
                intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID,isBusiness?mBusinessChatIdList.get(position):mUserChatIdList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
//            case R.id.send_message_to_group_btn:
//                sendMessage();
//                break;
            default:
                break;
        }
    }

//    private void sendMessage() {
//        if (mUserChatIdList.size() == 0 && !isBusiness) {
//            ToastUtil.toastShow(CollectMeActivity.this, "暂无相关记录，不能群发");
//            return;
//        }
//        if (mBusinessChatIdList.size() == 0 && isBusiness) {
//            ToastUtil.toastShow(CollectMeActivity.this, "暂无相关记录，不能群发");
//            return;
//        }
//        Intent intent = new Intent(CollectMeActivity.this,
//                ClientManagerChatActivity.class);
//        Bundle bundle = new Bundle();
//        if (isBusiness && mBusinessChatIdList.size() > 0) {//关注我的商家，并且关注我的商家总数大于0
//            bundle.putSerializable("chatList",
//                    (Serializable) mBusinessChatIdList);
//        } else {
//            bundle.putSerializable("chatList", (Serializable) mUserChatIdList);
//        }
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//    }
}
