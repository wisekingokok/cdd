package com.chewuwuyou.rong.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.MyFriendAdapter;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.eventbus.MessageEvent;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupListErActivtiy;
import com.chewuwuyou.app.ui.BeiZhuActivity;
import com.chewuwuyou.app.ui.FenzuManagerActivity;
import com.chewuwuyou.app.ui.NewFriendActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.MyExpandableListView;
import com.chewuwuyou.rong.bean.ContactNotificationMessage;
import com.chewuwuyou.rong.utils.Constant;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.model.Conversation;


/**
 * 通讯录
 */
public class RongContacterFragment extends Fragment implements OnClickListener, MyFriendAdapter.onBeizhuListener {

    public static final String ALTER_NAME = "alter_name";
    private View mContentView;
    //   private ContacterExpandAdapter mExpandAdapter = null;
    private TextView nullText;
    private LinearLayout searchLL;
    private LinearLayout mLinearLayout;//新朋友
    private TextView mNewFriendTV;//新朋友
    private View mView_tishi;
    private TextView mGroupTV;//群组
    private TextView mServerTV;//客服
    private ExpandableListView mExpandableListView;


    private TextView mTextView_empty;
    //   private TextView mTextView_empty;
    private String mServerId = "KEFU147315664514785";//客服Id

    private List<AllGroup> mGroups;
    //   private List<Userfriend> mUserfriends;

    private MyFriendAdapter mAdapter;
    //  private EstablishGroupFirstStepAdapter mAdapter;

    FinalDb db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.rong_contacter_ac, null);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initEvent();
        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    //当收到消息 显示小圆点
    public void onEventMainThread(ContactNotificationMessage sendMsgBean) {

        mView_tishi.setVisibility(View.VISIBLE);

    }

    //当删除好友，刷新列表
    public void onEventMainThread(MessageEvent messageEvent) {

        //重新请求。。。。刷新
        getData();

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();


        getData();
    }

    protected void initView() {

        db = FinalDb.create(getActivity());


        searchLL = (LinearLayout) mContentView.findViewById(R.id.searchLL);
        nullText = (TextView) mContentView.findViewById(R.id.nullText);
        mLinearLayout = (LinearLayout) mContentView.findViewById(R.id.new_friend_relayout);
        mNewFriendTV = (TextView) mContentView.findViewById(R.id.new_friend_tv);
        mView_tishi = mContentView.findViewById(R.id.tishi);
        mGroupTV = (TextView) mContentView.findViewById(R.id.group_tv);
        mServerTV = (TextView) mContentView.findViewById(R.id.server_tv);
        mExpandableListView = (ExpandableListView) mContentView.findViewById(R.id.mail_list_friends);
        mTextView_empty = (TextView) mContentView.findViewById(R.id.empty_tv);

        mGroups = new ArrayList<>();
        mAdapter = new MyFriendAdapter(getActivity(), mGroups);
        mAdapter.setListener(this);
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {


//                    new AlertDialog.Builder(getActivity())
//                            .setTitle("选择")
//                            .setCancelable(true)
//                            .setItems(R.array.fenzu, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    if (which == 0) {
//
//                                        Intent mIntent = new Intent(getActivity(), FenzuManagerActivity.class);
//                                        mIntent.putExtra(FenzuManagerActivity.GROUP_LIST, (Serializable) mGroups);
//                                        getActivity().startActivity(mIntent);
//
//                                    } else {
//                                        dialog.cancel();
//                                    }
//
//                                }
//                            }).create()
//                            .show();

                    String[] strs = new String[]{"分组管理"};

                    getActivity().setTheme(R.style.ActionSheetStyleIOS7);
                    ActionSheet menuView = new ActionSheet(getActivity());
                    menuView.setCancelButtonTitle("取 消");// before add items

                    menuView.addItems(strs);
                    menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
                        @Override
                        public void onItemClick(int itemPosition) {
                            Intent mIntent = new Intent(getActivity(), FenzuManagerActivity.class);
                            mIntent.putExtra(FenzuManagerActivity.GROUP_LIST, (Serializable) mGroups);
                            getActivity().startActivity(mIntent);
                        }
                    });
                    menuView.setCancelableOnTouchMenuOutside(true);
                    menuView.showMenu();


                    return true;
                }

                return false;
            }
        });
    }

    protected void initData() {

    }

    protected void initEvent() {
        searchLL.setOnClickListener(this);
        mLinearLayout.setOnClickListener(this);
        mNewFriendTV.setOnClickListener(this);
        mGroupTV.setOnClickListener(this);
        mServerTV.setOnClickListener(this);

        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mGroups.size() == 0)
                    mTextView_empty.setVisibility(View.VISIBLE);
                else
                    mTextView_empty.setVisibility(View.GONE);


            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.searchLL:


                Intent m = new Intent(getActivity(), SearchUserActivity.class);
                m.putExtra(SearchUserActivity.ALL_GROUP, (Serializable) mGroups);
                getActivity().startActivity(m);


                break;
            case R.id.new_friend_tv://新朋友
                mView_tishi.setVisibility(View.INVISIBLE);//取消小圆点
                EventBus.getDefault().post("cancel");
                Intent mIntent = new Intent(getActivity(), NewFriendActivity.class);
                this.startActivity(mIntent);
                break;

            case R.id.new_friend_relayout://新朋友...点击外部的relayout也应该可以进去
                mView_tishi.setVisibility(View.INVISIBLE);
                Intent m2 = new Intent(getActivity(), NewFriendActivity.class);
                this.startActivity(m2);
                break;
            case R.id.group_tv://群
                intent = new Intent(getActivity(), GroupListErActivtiy.class);
                startActivity(intent);
                break;
            case R.id.server_tv://客服
//                intent = new Intent(getActivity(), RongChatActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.CUSTOMER_SERVICE);
//                bundle.putString(RongChatMsgFragment.KEY_TARGET, Constant.SERVER_ID);
//                intent.putExtras(bundle);
//                startActivity(intent);
                startActivity(new Intent(getActivity(), RongServerChatActivity.class));
                break;
            default:
                break;
        }
    }


    public void getData() {

        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GET_ALL_FRIEND, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                JSONObject m = null;
                try {
                    m = new JSONObject(s);
                    String json = m.getString("data");

                    ErrorCodeUtil.doErrorCode(getActivity(), m.getInt("code"), m.getString("message"));
                    if (m.getInt("code") == 0) {

                        MyLog.i("我的好友----------->" + json);
                        mGroups.clear();
                        List<AllGroup> list = AllGroup.parseList(json);
                        boolean isHaveFriend;
                        if (list != null && list.get(0).getFriends().size() == 0)
                            isHaveFriend = false;
                        else
                            isHaveFriend = true;

                        if (list != null) {

                            mGroups.addAll(list);
                        }
                        mAdapter.notifyDataSetChanged();

                        //手动展开所有项
                        int groupCount = mGroups.size();
                        if (groupCount > 0) {
                            for (int i = 0; i < groupCount; i++) {
                                mExpandableListView.expandGroup(i);
                            }
                        }
                        ;


                    } else {
                        ToastUtil.toastShow(getActivity(), "网络异常，请稍后再试");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MyLog.i("onacti");
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            mGroups.get(group_alter).getFriends().get(child_alter).setName(data.getStringExtra(ALTER_NAME));
            mAdapter.notifyDataSetChanged();
        }

    }

    int group_alter;
    int child_alter;

    @Override
    public void onBeizhu(int group, int child) {
        group_alter = group;
        child_alter = child;
        Intent mIntent = new Intent(getActivity(), BeiZhuActivity.class);
        mIntent.putExtra(BeiZhuActivity.FRIEND_ID, mGroups.get(group).getFriends().get(child).getUserId());
        startActivityForResult(mIntent, 10);
    }


}
