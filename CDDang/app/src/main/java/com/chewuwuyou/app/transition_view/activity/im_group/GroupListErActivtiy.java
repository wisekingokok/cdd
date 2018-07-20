package com.chewuwuyou.app.transition_view.activity.im_group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.GroupListFragment;
import com.chewuwuyou.app.transition_presenter.im_group.GroupListErPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 群列表
 * liuchun
 */

public class GroupListErActivtiy extends BaseActivity {

    public Fragment[] mFragments;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_refund);
        initView();
    }
    /**
     * 初始化界面
     */
    private void initView(){
        ButterKnife.bind(this);
        new GroupListErPresenter(GroupListErActivtiy.this).initDate();
    }
}
