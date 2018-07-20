package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.GroupSetUpEssential;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformation;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.FileUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshListView;
import com.chewuwuyou.rong.adapter.TransferGroupAdapter;
import com.chewuwuyou.rong.adapter.WholeGroupAdapter;
import com.chewuwuyou.rong.bean.WholeGroup;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.model.Message;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class GroupMemberListActivtiy extends CDDBaseActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(id = R.id.sub_header_bar_left_ibtn)//返回上一页
    private ImageButton mSubHeaderBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//订单标题
    private TextView mTitel;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//添加
    private TextView mHeadberBarRight;
    @ViewInject(id = R.id.group_member_list)//群成员列表数据
    private PullToRefreshListView mGroupMemberList;
    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;
    @ViewInject(id = R.id.network_abnormal_layout)//网络访问
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.network_again)//重新加载
    private TextView mNetworkAgain;
    @ViewInject(id = R.id.searchET)//重新加载
    private LinearLayout mSearchET;

    private int mPageNum = 1;

    private WholeGroupAdapter mTransferGroupAdapter;//群适配器

    private List<GroupSetUpMemberInformationEr> mwholeGroups;//群列表

    private final int ADD_GROUP = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_list_ac);
        initView();
        initData();
        initEvent();



    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv://添加群成员
                intent = new Intent(GroupMemberListActivtiy.this,EstablishGroupFirstStepActivtiy.class);
                intent.putExtra("type",Constant.WHOLE_GROUP);
                intent.putExtra("groupName",getIntent().getStringExtra("groupName"));
                intent.putExtra("groupId",getIntent().getStringExtra("groupId"));
                startActivityForResult(intent, ADD_GROUP);
                break;
            case R.id.searchET://搜索群成员
                intent = new Intent(GroupMemberListActivtiy.this,GroupSearchActivtiy.class);
                intent.putExtra("search_type",Constant.GROUP_SETUP_SEARCH);
                intent.putExtra("groupName",getIntent().getStringExtra("groupName"));
                intent.putExtra("groupId",getIntent().getStringExtra("groupId"));
                startActivityForResult(intent, ADD_GROUP);
                break;
        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTitel.setText("群成员");
        mHeadberBarRight.setVisibility(View.VISIBLE);

        mHeadberBarRight.setText("添加");
        mwholeGroups = new ArrayList<GroupSetUpMemberInformationEr>();
        mTransferGroupAdapter = new WholeGroupAdapter(mwholeGroups,GroupMemberListActivtiy.this);
        mGroupMemberList.setAdapter(mTransferGroupAdapter);
        mGroupMemberList.setMode(PullToRefreshBase.Mode.BOTH);


    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        WholeGroup(mPageNum);//访问网络获取数据
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mHeadberBarRight.setOnClickListener(this);
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        mNetworkAgain.setOnClickListener(this);
        mGroupMemberList.setOnRefreshListener(this);
        mSearchET.setOnClickListener(this);
        mGroupMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupMemberListActivtiy.this,PersonalHomeActivity2.class);
                intent.putExtra("new_user_id", mwholeGroups.get(position-1).getAccid());
                startActivity(intent);
            }
        });


    }

    /**
     * 下拉刷新
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPageNum = 1;
        WholeGroupEr(true);
    }

    /**
     * 上啦加载
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        WholeGroupEr(false);
    }

    /**
     * 全部群成员
     */
    private void WholeGroup(int pageNum){

        AjaxParams params = new AjaxParams();

        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("groupName", getIntent().getStringExtra("groupName"));
        params.put("pageNum", String.valueOf(pageNum));
        params.put("pageSize", Constant.GROUP_YE_SIZE);
        NetworkUtil.get(NetworkUtil.WHOLE_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                try {

                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(GroupMemberListActivtiy.this,jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(jsonObject.getString("code").equals("0")){
                        mwholeGroups.clear();
                        mPageNum+=1;
                        GroupSetUpMemberInformation mGroupSetUpMemberInformation = GroupSetUpMemberInformation.parse(o);
                        mwholeGroups.addAll(mGroupSetUpMemberInformation.getData());
                        mTransferGroupAdapter.notifyDataSetChanged();//刷新适配器
                    }else{
                        ToastUtil.toastShow(GroupMemberListActivtiy.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupMemberListActivtiy.this,"网络异常");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }
    /**
     * 全部群成员
     */
    private void WholeGroupEr(final boolean isLa){
        if(isLa){
            mPageNum=1;
        }
        AjaxParams params = new AjaxParams();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("groupName", getIntent().getStringExtra("groupName"));
        params.put("pageNum", String.valueOf(mPageNum));
        params.put("pageSize", Constant.GROUP_YE_SIZE);
        NetworkUtil.get(NetworkUtil.WHOLE_GROUP, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String o) {
                super.onSuccess(o);
                mGroupMemberList.onRefreshComplete();
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    ErrorCodeUtil.doErrorCode(GroupMemberListActivtiy.this,jsonObject.optInt("code"),jsonObject.optString("message"));
                    if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                        GroupSetUpMemberInformation mGroupSetUpMemberInformation = GroupSetUpMemberInformation.parse(o);
                        if (isLa) {
                            mGroupMemberList.onLoadMore();
                            mwholeGroups.clear();
                        } else {
                            if (mGroupSetUpMemberInformation.getData().size()==0) {
                                ToastUtil.toastShow(GroupMemberListActivtiy.this,"没有更多数据了");
                                return;
                            }else{
                                mPageNum++;
                            }
                        }
                        mwholeGroups.addAll(mGroupSetUpMemberInformation.getData());
                        mTransferGroupAdapter.notifyDataSetChanged();//刷新适配器
                    }else{
                        ToastUtil.toastShow(GroupMemberListActivtiy.this,"没有更多数据了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(GroupMemberListActivtiy.this,"网络异常");
                mNetworkRequest.setVisibility(View.GONE);//关闭网络动画
                mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 群公告
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (requestCode){
                case ADD_GROUP://添加群成员
                    EventBusAdapter bsAdapter = new EventBusAdapter();
                    bsAdapter.setGroupIndex("1");
                    EventBus.getDefault().post(bsAdapter);// 像适配器传递值
                    mNetworkRequest.setVisibility(View.VISIBLE);//关闭网络动画
                    mNetworkAbnormalLayout.setVisibility(View.GONE);
                    mPageNum = 1;
                    WholeGroup(mPageNum);
                    break;
            }
        }
    }
}
