package com.chewuwuyou.app.transition_view.activity.im_group_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.im_group_fragment.GroupListFragmentErPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseFragment;
import com.chewuwuyou.app.widget.XListView;
import com.chewuwuyou.rong.adapter.GroupListAdapter;
import com.chewuwuyou.rong.bean.WholeGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群列表
 * liuchun
 */

public class GroupListErFragment extends BaseFragment {

    @BindView(R.id.sub_header_bar_left_ibtn)//返回上一页
           public ImageButton subHeaderBarLeftIbtn;
    @BindView(R.id.sub_header_bar_tv)//标题
    public TextView subHeaderBarTv;
    @BindView(R.id.sub_header_bar_right_tv)//创建群
    public TextView subHeaderBarRightTv;
    @BindView(R.id.network_request)//网络请求
    public LinearLayout networkRequest;
    @BindView(R.id.network_abnormal_layout)//网络异常界面
    public LinearLayout networkAbnormalLayout;
    @BindView(R.id.group_listwu)//没有群
    public LinearLayout groupListwu;
    @BindView(R.id.group_search)//点击搜索
    public LinearLayout groupSearch;
    @BindView(R.id.my_establish_group_list)//创建群组显示
    public XListView myEstablishGroupList;
    @BindView(R.id.my_establish_group)//创建群组显示隐藏
    public LinearLayout myEstablishGroup;
    @BindView(R.id.my_join_group_list)//加入群组显示
    public XListView myJoinGroupList;
    @BindView(R.id.my_join_group)//加入群组显示隐藏
    public LinearLayout myJoinGroup;
    @BindView(R.id.swipe_list)//页面滚动条
    public ScrollView swipeList;
    @BindView(R.id.sub_header_bar_left_tv)
    public TextView subHeaderBarLeftTv;
    @BindView(R.id.sub_header_bar_right_ibtn)
    public ImageButton subHeaderBarRightIbtn;
    @BindView(R.id.title_height)
    public RelativeLayout titleHeight;
    @BindView(R.id.my_establish_group_tv)
    public TextView myestablishgrouptv;
    @BindView(R.id.my_join_group_tv)
    public TextView myjoingrouptv;

    private View mContentView;

    private GroupListFragmentErPresenter mGroupListFragmentErPresenter;

    public List<WholeGroup> mwholeGroups;//我创建
    public List<WholeGroup> mwholeGroupser;//我加入
    public GroupListAdapter mGroupListAdapt;
    public GroupListAdapter mGroupListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.group_list_activtiy, null);
        initView();
        initEvent();
        return mContentView;
    }

    /**
     * 初始化界面
     */
    public void initView() {
        ButterKnife.bind(this, mContentView);

        mGroupListFragmentErPresenter = new GroupListFragmentErPresenter(getActivity(), GroupListErFragment.this);
        subHeaderBarTv.setText(getResources().getString(R.string.group_lsit_title));
        subHeaderBarRightTv.setText(getResources().getString(R.string.establish_group));
        mGroupListFragmentErPresenter.groupMessage();//接收传递过来的转发消息
        mwholeGroups = new ArrayList<>();
        mwholeGroupser = new ArrayList<WholeGroup>();
        mGroupListAdapt = new GroupListAdapter(mwholeGroups, getActivity());
        myEstablishGroupList.setAdapter(mGroupListAdapt);
        mGroupListAdapter = new GroupListAdapter(mwholeGroupser, getActivity());
        myJoinGroupList.setAdapter(mGroupListAdapter);


    }

    @OnClick({R.id.sub_header_bar_left_ibtn, R.id.sub_header_bar_right_tv,R.id.network_again})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                getActivity().finish();
                break;
            case R.id.sub_header_bar_right_tv://创建群
                mGroupListFragmentErPresenter.createGroup();
                break;
            case R.id.network_again://重新加载
                networkRequest.setVisibility(View.VISIBLE);
                networkAbnormalLayout.setVisibility(View.GONE);
                mGroupListFragmentErPresenter.requestGroupData();
                break;
         }
    }

    @Override
    public void onResume() {
        super.onResume();
        networkRequest.setVisibility(View.VISIBLE);
        networkAbnormalLayout.setVisibility(View.GONE);
        mGroupListFragmentErPresenter.requestGroupData();//请求群数据
    }

    private void initEvent(){
        /**
         * 我创建
         */
        myEstablishGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGroupListFragmentErPresenter.onClickItemEstablishGroup(position);
            }
        });
        /**
         * 我加入的群
         */
        myJoinGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGroupListFragmentErPresenter.onClickItemJoinGroup(position);
            }
        });
    }
}
