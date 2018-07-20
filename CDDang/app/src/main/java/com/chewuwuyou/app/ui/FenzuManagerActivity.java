package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.helper.ItemTouchHelperAdapter;
import com.chewuwuyou.app.helper.OnStartDragListener;
import com.chewuwuyou.app.helper.SimpleItemTouchHelperCallback;
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

import java.util.Collections;
import java.util.List;

public class FenzuManagerActivity extends CDDBaseActivity implements OnStartDragListener {


    public static final String GROUP_LIST = "group_list";
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
    private RecyclerView mRecyclerView;

    private ItemTouchHelper mItemTouchHelper;
    Adapter mAdapter;
    List<AllGroup> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenzu_manager);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mTitleTV.setText("分组管理");
        mTextView_right.setText("完成");
        mTextView_right.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void initData() {
        mList = (List<AllGroup>) getIntent().getSerializableExtra(GROUP_LIST);
        mAdapter = new Adapter(mList, this, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initEvent() {
        mTextView_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                            ToastUtil.toastShow(FenzuManagerActivity.this, "请输入内容");


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

        if (TextUtils.isEmpty(name)) {
            ToastUtil.toastShow(this, "请输入名称");
            return;

        }

        mProgressDialog = DialogUtil.showProgressDialog(this);
        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("friendGroupName", name);
        NetworkUtil.postMulti(NetworkUtil.ADD_FRIEND_FENZU, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                MyLog.i("success");
                ToastUtil.toastShow(FenzuManagerActivity.this, "新建分组成功");

                try {

                    JSONObject jsonObject = new JSONObject(s);

                    ErrorCodeUtil.doErrorCode(FenzuManagerActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));


                    int groupId = jsonObject.getInt("data");
                    AllGroup mAllGroup = new AllGroup();
                    mAllGroup.setFriendGroupName(name);
                    mAllGroup.setGroupId(groupId);
                    mList.add(mAllGroup);
                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(FenzuManagerActivity.this, "新建组失败");
                mProgressDialog.dismiss();
            }
        });


    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

        private List<AllGroup> mList;
        private Context mContext;
        private final OnStartDragListener mDragStartListener;

        public Adapter(List<AllGroup> mlist, Context m, OnStartDragListener listener) {
            this.mContext = m;
            mList = mlist;
            mDragStartListener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new viewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_new_group, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final viewHolder m = (viewHolder) holder;

            //     mProgressDialog.mTextView_name.setText(mList.get(position).getName());
            // Start a drag whenever the handle view it touched

            m.mTextView_name.setText(mList.get(position).getFriendGroupName());
            if ("我的好友".equals(mList.get(position).getFriendGroupName()))
                m.mImageView_delete.setVisibility(View.INVISIBLE);
            else
                m.mImageView_delete.setVisibility(View.VISIBLE);

//编辑分组
            m.mTextView_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder mAlertDialog;//
                    mAlertDialog = new AlertDialog.Builder(mContext);
                    View view = getLayoutInflater().inflate(R.layout.view_edittext, null);
                    final EditText name = (EditText) view.findViewById(R.id.input);
                    name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    name.setHint(mList.get(position).getFriendGroupName());
                    name.setHintTextColor(getResources().getColor(R.color.lightgray));


//                    name.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            if (TextUtils.isEmpty(s.toString()))
//                                    mAlertDialog.setNegativeButton()
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//
//                        }
//                    });


                    if ("黑名单".equals(mList.get(position).getFriendGroupName()) || "我的好友".equals(mList.get(position).getFriendGroupName()))
                        return;
                    else
                        mAlertDialog.setTitle("编辑分组名称")
                                .setView(view)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!TextUtils.isEmpty(name.getText().toString()))
                                            alterGroupName(name.getText().toString(), mList.get(position).getGroupId() + "", position);
                                        else
                                            ToastUtil.toastShow(FenzuManagerActivity.this, "请输入内容");
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


            m.mImageView_remove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(m);
                    }
                    return false;
                }
            });


            m.mImageView_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder mAlertDialog;//
                    mAlertDialog = new AlertDialog.Builder(mContext);
                    String s;
//不能删除默认组
                    if ("我的好友".equals(mList.get(position).getFriendGroupName())) {
                        return;
                    }
                    if (mList.get(position).getFriends().size() == 0)//当组内没有人的时候
                        s = "是否确认删除";
                    else
                        s = "删除分组后，此分组的好友会移至默认分组中";
                    mAlertDialog.setTitle("提示")
                            .setMessage(s)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    mProgressDialog = DialogUtil.showProgressDialog(FenzuManagerActivity.this);


                                    AjaxParams params = new AjaxParams();
                                    params.put("userId", CacheTools.getUserData("rongUserId"));
                                    params.put("groupId", mList.get(position).getGroupId() + "");
                                    NetworkUtil.postMulti(NetworkUtil.DELETE_FIREND_FENZU, params, new AjaxCallBack<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            super.onSuccess(s);
                                            MyLog.i("success");


                                            mProgressDialog.dismiss();
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(s);

                                                ErrorCodeUtil.doErrorCode(FenzuManagerActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));

                                                ToastUtil.toastShow(FenzuManagerActivity.this, "删除分组成功");
                                                mList.remove(position);
                                                notifyDataSetChanged();


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }

                                        @Override
                                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                                            super.onFailure(t, errorNo, strMsg);
                                            ToastUtil.toastShow(FenzuManagerActivity.this, "删除组失败");
                                            mProgressDialog.dismiss();
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

        }


        //编辑分组
        private void alterGroupName(final String name, String id, final int position) {

            mProgressDialog = ProgressDialog.show(FenzuManagerActivity.this, null, "请稍后……", false);
            AjaxParams params = new AjaxParams();
            params.put("userId", CacheTools.getUserData("rongUserId"));
            params.put("groupId", id);
            params.put("friendGroupName", name);
            NetworkUtil.postMulti(NetworkUtil.ALTER_FENZU_NAME, params, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);

                    mProgressDialog.dismiss();

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        ErrorCodeUtil.doErrorCode(FenzuManagerActivity.this, jsonObject.getInt("code"), jsonObject.getString("message"));

                        MyLog.i("success");
                        ToastUtil.toastShow(FenzuManagerActivity.this, "修改分组成功");
                        mList.get(position).setFriendGroupName(name);
                        mAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    ToastUtil.toastShow(FenzuManagerActivity.this, "修改组失败");
                    mProgressDialog.dismiss();
                }
            });


        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {


            Collections.swap(mList, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            mList.remove(position);
            notifyItemRemoved(position);
        }


        class viewHolder extends RecyclerView.ViewHolder {

            private ImageView mImageView_delete;
            private ImageView mImageView_remove;
            private TextView mTextView_name;


            public viewHolder(View itemView) {
                super(itemView);
                mImageView_delete = (ImageView) itemView.findViewById(R.id.jian);
                mImageView_remove = (ImageView) itemView.findViewById(R.id.remove);
                mTextView_name = (TextView) itemView.findViewById(R.id.name);

            }
        }
    }

}
