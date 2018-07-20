package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.eventbus.MessageEvent;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class FriendFenzuActivity extends CDDBaseActivity {


    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;

    /**
     * 标题
     */
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mTitleTV;

    @ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
    private TextView mTextView_right;

    @ViewInject(id = R.id.new_group, click = "onAction")
    private TextView mTextView_add;

    @ViewInject(id = R.id.group_list)
    private ListView mListView;

    private ItemTouchHelper mItemTouchHelper;
    Adapter mAdapter;
    List<AllGroup> mList;

    private String mFriendId;//聊天好友的id
    private String mFriendGroupId;//聊天好友属于哪个分组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_fenzu);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {

        mFriendId = getIntent().getStringExtra(PersonalHomeActivity2.FRIENDID);
        mFriendGroupId = getIntent().getStringExtra(PersonalHomeActivity2.FRIEND_GROUP_ID);
        mTitleTV.setText("好友分组");
        mTextView_right.setVisibility(View.VISIBLE);


    }

    @Override
    protected void initData() {

        mList = new ArrayList<>();
        mAdapter = new Adapter(mList, this, mFriendGroupId, mFriendId);
        mListView.setAdapter(mAdapter);
        getData();

    }

    @Override
    protected void initEvent() {

    }


    public void onAction(View v) {
        Intent intent = new Intent();
        ComponentName comp;
        switch (v.getId()) {
            case R.id.sub_header_bar_right_tv:

                submit();
                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.new_group:

                addNewGroup();


                break;

        }
    }

    private void submit() {


    }

    public String getCheckedGroupId() {

        int position = mListView.getCheckedItemPosition();
        if (ListView.INVALID_POSITION != position) {
            return mList.get(position).getGroupId() + "";
        } else
            return "";

    }

    private void addNewGroup() {
        AlertDialog.Builder mAlertDialog;//
        mAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.view_edittext, null);

        final EditText name = (EditText) view.findViewById(R.id.input);
        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        name.setHint("请输入新分组的名称");

        mAlertDialog.setTitle("添加分组")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!TextUtils.isEmpty(name.getText().toString()))
                            newGroupSubmit(name.getText().toString());
                        else
                            ToastUtil.toastShow(FriendFenzuActivity.this, "请输入内容");


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                    }
                })
                .create().show();

    }

    ProgressDialog mProgressDialog = null;

    private void newGroupSubmit(final String name) {


        mProgressDialog = DialogUtil.showProgressDialog(this);


        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("friendGroupName", name);
        NetworkUtil.postMulti(NetworkUtil.ADD_FRIEND_FENZU, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                MyLog.i("success");

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(FriendFenzuActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));

                    int groupId = jsonObject.getInt("data");
                    AllGroup mAllGroup = new AllGroup();
                    mAllGroup.setFriendGroupName(name);
                    mAllGroup.setGroupId(groupId);
                    mList.add(mAllGroup);
                    mAdapter.notifyDataSetChanged();
                    ToastUtil.toastShow(FriendFenzuActivity.this, "新建分组成功");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MessageEvent(null, null));
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(FriendFenzuActivity.this, "新建分组失败");
                mProgressDialog.dismiss();
            }
        });


    }

    public void getData() {

        final ProgressDialog dialog = DialogUtil.showProgressDialog(this);

        AjaxParams params = new AjaxParams();
        //  params.put("userId", "9527");
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GET_ALL_FRIEND, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                JSONObject m = null;
                try {
                    m = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(FriendFenzuActivity.this, m.getInt("code"), m.getString("message"));

                    String json = m.getString("data");
                    MyLog.i("我的好友----------->" + json);
                    List<AllGroup> list = AllGroup.parseList(json);
                    if (list != null || list.size() != 0) {
                        mList.clear();
                        mList.addAll(list);
                    }
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                ToastUtil.toastShow(FriendFenzuActivity.this, "获取分组失败……");
                dialog.dismiss();
            }
        });


    }


    class Adapter extends BaseAdapter {

        private List<AllGroup> mList;
        private Context mContext;
        private String now_group_id; //现在用户所属的组
        private String friend_id;

        public Adapter(List<AllGroup> mlist, Context m, String selected_group_id, String friend_id) {
            this.now_group_id = selected_group_id;
            this.friend_id = friend_id;
            this.mContext = m;
            mList = mlist;

        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

//            ConvertView mProgressDialog = new ConvertView(mContext);
//            mProgressDialog.setName(mList.get(position).getFriendGroupName());
//            if (!TextUtils.isEmpty(mList.get(position) + "") && now_group_id.equals(mList.get(position).getGroupId() + ""))
//                mProgressDialog.setChecked(true);
//            return mProgressDialog;
            Holder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
                holder = new Holder();
                holder.mText = (TextView) convertView.findViewById(R.id.name);
                holder.mImageView = (ImageView) convertView.findViewById(R.id.group_selected);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.mText.setText(mList.get(position).getFriendGroupName());
            if (!TextUtils.isEmpty(now_group_id) && now_group_id.equals(mList.get(position).getGroupId() + ""))
                holder.mImageView.setVisibility(View.VISIBLE);
            else
                holder.mImageView.setVisibility(View.GONE);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (now_group_id!=null&&now_group_id.equals(String.valueOf(mList.get(position).getGroupId()))) {

                        return;

                    }

                    mProgressDialog = DialogUtil.showProgressDialog(mContext);


                    AjaxParams params = new AjaxParams();
                    params.put("userId", CacheTools.getUserData("rongUserId"));
                    params.put("friendId", friend_id);
                    params.put("groupId", mList.get(position).getGroupId() + "");

                    NetworkUtil.get(NetworkUtil.REMOVE_FIREND_TO_ZU, params, new AjaxCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            super.onSuccess(s);


                            mProgressDialog.dismiss();
                            try {

                                JSONObject j = new JSONObject(s);

                                ErrorCodeUtil.doErrorCode(FriendFenzuActivity.this, j.getInt("code"), j.getString("message"));

                                int code = j.getInt("code");
                                if (code != 0) {
                                    ToastUtil.toastShow(FriendFenzuActivity.this, "移动失败");
                                } else {
                                    MyLog.i("移动好友至分组success");
                                    now_group_id = mList.get(position).getGroupId() + "";
                                    mProgressDialog.dismiss();
                                    ToastUtil.toastShow(FriendFenzuActivity.this, "已分组到" + mList.get(position).getFriendGroupName());
                                    notifyDataSetChanged();
                                    EventBus.getDefault().post(new MessageEvent(null, null));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            ToastUtil.toastShow(FriendFenzuActivity.this, "移动失败");
                            mProgressDialog.dismiss();
                        }
                    });


                }
            });


            return convertView;
        }

        class Holder {
            private TextView mText;
            private ImageView mImageView;

        }

    }


//    class ConvertView extends LinearLayout implements Checkable {
//
//
//        private TextView mText;
//        private CheckBox mCheckBox;
//
//
//        public ConvertView(Context context) {
//            super(context);
//            initView(context);
//        }
//
//        public ConvertView(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            initView(context);
//        }
//
//        public ConvertView(Context context, AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//            initView(context);
//        }
//
//
//        private void initView(Context context) {
//            // 填充布局
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View v = inflater.inflate(R.layout.item_group, this, true);
//            mCheckBox = (CheckBox) v.findViewById(R.id.group_selected);
//            mText = (TextView) v.findViewById(R.id.name);
//        }
//
//        @Override
//        public void setChecked(boolean checked) {
//            mCheckBox.setChecked(checked);
//        }
//
//        @Override
//        public boolean isChecked() {
//            return mCheckBox.isChecked();
//        }
//
//        @Override
//        public void toggle() {
//            mCheckBox.toggle();
//        }
//
//
//        public void setName(String text) {
//            mText.setText(text);
//        }
//    }


}


