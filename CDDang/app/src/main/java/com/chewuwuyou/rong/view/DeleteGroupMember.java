package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformation;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.rong.adapter.DeleteGroupAdapter;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeleteGroupMember extends CDDBaseActivity implements View.OnClickListener {


    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitel;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//删除
    private TextView mSubBarRightTV;
    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;
    @ViewInject(id = R.id.network_abnormal_layout)//网络访问
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.country_lvcountry)//群列表
    private ListView mCountryLvcountry;
    @ViewInject(id = R.id.group_search)//群列表
    private LinearLayout mGroup_Search;


    private DeleteGroupAdapter mDeleteAdapter;
    private List<GroupSetUpMemberInformationEr> mwholeGroups;//群列表
    private String delete = "";
    private final int DELETE_GROUP = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group_member);
        initView();
        initData();
        initEvent();
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn://上一页
                finishActivity();
                break;
            case R.id.group_search://删除搜索
                intent = new Intent(DeleteGroupMember.this, GroupSearchActivtiy.class);
                intent.putExtra("search_type", Constant.DELETE_GROUP);
                intent.putExtra("groupName", getIntent().getStringExtra("groupName"));
                intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                startActivityForResult(intent, DELETE_GROUP);
                break;
            case R.id.sub_header_bar_right_tv://删除

                for (int i = 0; i < mwholeGroups.size(); i++) {
                    if (mwholeGroups.get(i).isSelected() == true) {
                        if (mwholeGroups.get(i).getAccid().equals(getIntent().getStringExtra("id"))) {
                            ToastUtil.toastShow(DeleteGroupMember.this, "群主不能删除自己");
                            return;
                        }
                    }
                }
                String ss = "";
                for (int i = 0; i < mwholeGroups.size(); i++) {
                    if (mwholeGroups.get(i).isSelected() == true) {

                        delete += mwholeGroups.get(i).getAccid() + "-";

                        if (!TextUtils.isEmpty(mwholeGroups.get(i).getUser_friend_name()) && !mwholeGroups.get(i).getUser_friend_name().equals("null")) {
                            ss += mwholeGroups.get(i).getUser_friend_name() + ",";
                        } else if (!TextUtils.isEmpty(mwholeGroups.get(i).getUser_group_name()) && !mwholeGroups.get(i).getUser_group_name().equals("null")) {
                            ss += mwholeGroups.get(i).getUser_group_name() + ",";
                        } else {
                            ss += mwholeGroups.get(i).getUser_own_name() + ",";
                        }
                    }
                }

                if (TextUtils.isEmpty(delete)) {
                    ToastUtil.toastShow(DeleteGroupMember.this, "请选择删除的成员");
                } else {
                    dialog("", "确定要删除群成员" + ss, "");
                }
                break;
        }
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        mTitel.setText("删除成员");
        mSubBarRightTV.setVisibility(View.VISIBLE);
        mSubBarRightTV.setText("删除");
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mwholeGroups = new ArrayList<GroupSetUpMemberInformationEr>();
        mDeleteAdapter = new DeleteGroupAdapter(mwholeGroups, DeleteGroupMember.this);
        mCountryLvcountry.setAdapter(mDeleteAdapter);
        GroupMember();
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mSubBarRightTV.setOnClickListener(this);
        mGroup_Search.setOnClickListener(this);
        mCountryLvcountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mwholeGroups.get(position).isSelected() == true) {
                    mwholeGroups.get(position).setSelected(false);
                } else {
                    mwholeGroups.get(position).setSelected(true);
                }
                mDeleteAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 提示用户是否进行操作
     */
    public void dialog(String title, String context, final String txet) {
        new com.chewuwuyou.app.utils.AlertDialog(DeleteGroupMember.this).builder().setTitle(title)
                .setMsg(context)
                .setPositiveButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                }).setNegativeButton("确定", new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                deleteGroup(delete);
            }
        }).show();
    }

    private void GroupMember() {
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("userId", CacheTools.getUserData("rongUserId"));
        NetworkUtil.get(NetworkUtil.GROUP_SET_UP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(DeleteGroupMember.this,jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        GroupSetUpMemberInformation mGroupSetUpMemberInformation = GroupSetUpMemberInformation.parse(o);
                        mwholeGroups.addAll(mGroupSetUpMemberInformation.getData());
                        mDeleteAdapter.notifyDataSetChanged();
                    }else{
                        ToastUtil.toastShow(DeleteGroupMember.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(DeleteGroupMember.this,"删除失败");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 删除群成员
     *
     * @param deleteId
     */
    private void deleteGroup(String deleteId) {
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("loginUserId", CacheTools.getUserData("rongUserId"));
        params.put("userIds", deleteId);
        NetworkUtil.get(NetworkUtil.DELETE_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                MyLog.e("--", "删除群成员 = " + o);
                try {
                    JSONObject jo = new JSONObject(o);
                    if (jo.getInt("code") == 0) {
                        ToastUtil.toastShow(DeleteGroupMember.this, "删除成功");
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finishActivity();
                    } else {
                        ToastUtil.toastShow(DeleteGroupMember.this, jo.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(DeleteGroupMember.this,"删除失败");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            switch (requestCode) {
                case DELETE_GROUP:
                    for (int i = 0; i < mwholeGroups.size(); i++) {
                        if (mwholeGroups.get(i).getAccid().equals(data.getStringExtra("userId"))) {
                            if (mwholeGroups.get(i).isSelected() == false) {
                                mwholeGroups.get(i).setSelected(true);
                            } else {
                                mwholeGroups.get(i).setSelected(false);
                            }
                        }
                    }
                    mDeleteAdapter.notifyDataSetChanged();
                    break;
            }
        }

    }
}
