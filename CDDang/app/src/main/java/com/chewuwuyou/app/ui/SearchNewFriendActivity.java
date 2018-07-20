package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.KeywordUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.EditTextWithDelete;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshListView;


import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchNewFriendActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(id = R.id.editext_number)
    private EditTextWithDelete mEditText;
    @ViewInject(id = R.id.sure)
    private TextView mTextView_sure;
    @ViewInject(id = R.id.list)
    private PullToRefreshListView mListView;
    @ViewInject(id = R.id.empty_tv)
    private TextView mTextView_empty;

    @ViewInject(id = R.id.empty_first)
    private TextView mTextView_first;

    private CommonAdapter<Userfriend> mAdapter;
    private List<Userfriend> mList;
    private int page = 1;
    private boolean isReReresh;//重新搜索

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_friend);

        initView();
        initData();
        initEvent();

    }


    protected void initView() {
        mList = new ArrayList<>();
        mAdapter = new CommonAdapter<Userfriend>(this, mList, R.layout.item_search_user) {
            @Override
            public void convert(ViewHolder holder, final Userfriend user, int position) {

                TextView m = holder.getView(R.id.name);
                m.setText(KeywordUtil.matcherSearchTitle(getResources().getColor(R.color.new_blue), user.getName(), editext));
                TextView m1 = holder.getView(R.id.id);
                m1.setText(KeywordUtil.matcherSearchTitle(getResources().getColor(R.color.new_blue), user.getPhone() + "", editext));

                ImageUtils.displayImage(user.getPortraitUri(), (ImageView) holder.getView(R.id.head), 8, R.drawable.user_fang_icon,
                        R.drawable.user_fang_icon);

                if (user.getIsFriend() == 1) {
                    holder.setText(R.id.relation, "好友");
                    holder.getView(R.id.relation).setVisibility(View.VISIBLE);
                    holder.getView(R.id.add_friend_btn).setVisibility(View.GONE);
                } else if (user.getIsFriend() == 2) {
                    //自己
                    holder.setText(R.id.relation, "自己");
                    holder.getView(R.id.relation).setVisibility(View.VISIBLE);
                    holder.getView(R.id.add_friend_btn).setVisibility(View.GONE);


                } else {
                    holder.setText(R.id.relation, "");
                    holder.getView(R.id.relation).setVisibility(View.GONE);
                    holder.getView(R.id.add_friend_btn).setVisibility(View.VISIBLE);

                    MyLog.i("llllllllllllllllllll");
                }

                holder.getView(R.id.head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(SearchNewFriendActivity.this, PersonalHomeActivity2.class);
                        mIntent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, user.getUserId());
                        mIntent.putExtra(PersonalHomeActivity2.IS_YANZHENG, 1);
                        startActivity(mIntent);
                    }
                });


            }
        };

        mListView.setAdapter(mAdapter);
        //   mListView.setEmptyView(mTextView_empty);
//        mListView.setMode(PullToRefreshBase.Mode.BOTH);
//        mListView.setOnRefreshListener(this);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);


    }


    protected void initData() {


    }


    protected void initEvent() {
        mTextView_sure.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("--","xxxxxxxxx"+s+" "+mTextView_sure);
                if (TextUtils.isEmpty(s)) {

                    mTextView_sure.setText("取消");


                } else {
                    mTextView_sure.setText("搜索");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("search_type")) && getIntent().getStringExtra("search_type").equals(Constant.GROUP_FRIENDS)) {
                    Intent intent = new Intent();
                    intent.putExtra("userId", String.valueOf(mList.get(position - 1).getUserId()));
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (mList.get(position - 1).getIsFriend() == 1 || mList.get(position - 1).getIsFriend() == 2) {
                    //1是好友，2是自己，0是非好友
                    return;
                } else {
                    View v = LayoutInflater.from(SearchNewFriendActivity.this).inflate(R.layout.view_edittext, null);
                    final EditText mcontent = (EditText) v.findViewById(R.id.input);
                    mcontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

                    if (!TextUtils.isEmpty(CacheTools.getUserData("nickName"))) {
                        mcontent.setText("我是" + CacheTools.getUserData("nickName"));
                    } else if (!TextUtils.isEmpty(CacheTools.getUserData("realName")))
                        mcontent.setText("我是" + CacheTools.getUserData("realName"));
                    else
                        mcontent.setText("我是");
                    mcontent.setHint("请输入验证信息");


                    new AlertDialog.Builder(SearchNewFriendActivity.this)
                            .setTitle("验证信息")
                            .setView(v)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final ProgressDialog m = ProgressDialog.show(SearchNewFriendActivity.this, null, "请稍后……", false, true);
                                    m.show();

                                    // TODO Auto-generated method stub
                                    AjaxParams params = new AjaxParams();


                                    params.put("userId", CacheTools.getUserData("rongUserId"));
                                    params.put("friendId", mList.get(position - 1).getUserId() + "");
                                    params.put("groupId", "1");
                                    params.put("remarks", mcontent.getText().toString());


                                    new NetworkUtil().postMulti(NetworkUtil.ADD_FRIEND, params, new AjaxCallBack<String>() {
                                        @Override
                                        public void onSuccess(String t) {
                                            // TODO Auto-generated method stub
                                            super.onSuccess(t);
                                            Toast.makeText(SearchNewFriendActivity.this, R.string.wait_friend_agree, Toast.LENGTH_SHORT).show();
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
            }
        });


//        mAdapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                if (mList.size() == 0)
//                    mTextView_empty.setVisibility(View.VISIBLE);
//                else
//                    mTextView_empty.setVisibility(View.GONE);
//
//            }
//        });


    }

    ProgressDialog mProgressDialog;
    String editext;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sure:

                if (mTextView_sure.getText().toString().equals("搜索")) {
                    mProgressDialog = ProgressDialog.show(this, "", "请稍后……", false, true);
                    page = 1;
                    isReReresh = true;
                    editext = mEditText.getText().toString();
                    getData();
                    hideKeyboard();
                } else
                    finishActivity();

                break;

            default:
                break;

        }
    }

    private void getData() {


        AjaxParams param = new AjaxParams();
        param.put("userId", CacheTools.getUserData("rongUserId"));
        param.put("pageNum", page + "");
        param.put("pageSize", 20 + "");
        param.put("name", mEditText.getText().toString());
        NetworkUtil.get(NetworkUtil.SEARCH_USER, param, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                MyLog.i("----------->" + s);
                try {

                    JSONObject m = new JSONObject(s);
                    ErrorCodeUtil.doErrorCode(SearchNewFriendActivity.this, m.getInt("code"), m.getString("message"));
                    page++;
                    String json = m.getString("data");
                    List<Userfriend> list = Userfriend.parseList(json);
                    if (isReReresh) {
                        isReReresh = false;
                        mList.clear();
                    }
                    if (list != null)
                        mList.addAll(list);


                    if (mList.size() == 0)
                        mTextView_empty.setVisibility(View.VISIBLE);
                    else
                        mTextView_empty.setVisibility(View.GONE);


                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mTextView_first.setVisibility(View.GONE);


                mListView.onRefreshComplete();


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                ToastUtil.toastShow(SearchNewFriendActivity.this, "网络异常，请稍后再试");

            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        mList.clear();
        page = 1;
        getData();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getData();

    }
}
