package com.chewuwuyou.app.transition_view.activity.im_group_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_presenter.im_group_fragment.EstablishGroupFirstStepErPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseFragment;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.rong.view.GroupSearchActivtiy;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 群列表
 * liuchun
 */

public class EstablishGroupFirstStepErFragment extends BaseFragment {


    @BindView(R.id.sub_header_bar_left_ibtn)//返回上一页
            public ImageButton subHeaderBarLeftIbtn;
    @BindView(R.id.sub_header_bar_tv)//标题
            public TextView subHeaderBarTv;
    @BindView(R.id.sub_header_bar_right_tv)//下一步
            public TextView subHeaderBarRightTv;
    @BindView(R.id.network_request)//网络动画
            public LinearLayout networkRequest;
    @BindView(R.id.network_again)//重新加载
            public TextView networkAgain;
    @BindView(R.id.network_abnormal_layout)//网络异常
            public LinearLayout networkAbnormalLayout;
    @BindView(R.id.group_search)//搜索
            public LinearLayout groupSearch;
    @BindView(R.id.forward_group)//是否转发到群
            public LinearLayout forwardGroup;
    @BindView(R.id.establish_group_first)//显示好友分组
            public ExpandableListView establishGroupFirst;

    private View mContentView;
    private EstablishGroupFirstStepErPresenter mEstablishGroupFirstStepErPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.item_establish_group_first_step, null);
        initView();
        initEvent();
        return mContentView;
    }

    /**
     * 初始化界面
     */
    public void initView() {

        ButterKnife.bind(this, mContentView);


        subHeaderBarRightTv.setVisibility(View.VISIBLE);
        mEstablishGroupFirstStepErPresenter = new EstablishGroupFirstStepErPresenter(getActivity(),EstablishGroupFirstStepErFragment.this);
        mEstablishGroupFirstStepErPresenter.initData();//接收传递过来的参数做相应的判断
        mEstablishGroupFirstStepErPresenter.visitInterface();//访问网络数据

    }

    @OnClick({R.id.sub_header_bar_left_ibtn, R.id.sub_header_bar_right_tv,R.id.forward_group,R.id.network_again,R.id.group_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                getActivity().finish();
                break;
            case R.id.sub_header_bar_right_tv://下一步
                mEstablishGroupFirstStepErPresenter.groupNextStep();
                break;
            case R.id.forward_group://是否转发到群
                mEstablishGroupFirstStepErPresenter.forwardGroup();
                break;
            case R.id.network_again://重新访问
                mEstablishGroupFirstStepErPresenter.visitInterface();
                break;
            case R.id.group_search://搜索
                mEstablishGroupFirstStepErPresenter.groupMondelSearch();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mEstablishGroupFirstStepErPresenter.onGroupResult(requestCode,data);//接收搜索接口回调函数
    }

    /**
     * 判断下一步是否点击
     */
    public void isClick(boolean isClick){
        if(isClick == false){
            subHeaderBarRightTv.setClickable(false);
        }else{
            subHeaderBarRightTv.setClickable(true);
        }
    }

    /**
     *事件监听
     */
    public void initEvent(){
        // 设置item点击的监听器
        establishGroupFirst.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mEstablishGroupFirstStepErPresenter.onclickItemExpandable(groupPosition,childPosition);
                return false;
            }
        });
    }

}
