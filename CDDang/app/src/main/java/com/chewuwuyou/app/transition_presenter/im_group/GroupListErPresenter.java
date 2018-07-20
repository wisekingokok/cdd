package com.chewuwuyou.app.transition_presenter.im_group;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.GroupListFragment;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.transition_view.activity.im_group.GroupListErActivtiy;
import com.chewuwuyou.app.transition_view.activity.im_group_fragment.GroupListErFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.Message;

/**
 * 群列表
 * liuchun
 */

public class GroupListErPresenter extends BasePresenter {


    private GroupListErActivtiy mGroupListErActivtiy;
    private String group;//群
    private Message mMessage;//转发到群的消息
    public GroupListErPresenter(GroupListErActivtiy mGroupListErActivtiy) {
        super(mGroupListErActivtiy);
        this.mGroupListErActivtiy = mGroupListErActivtiy;
    }

    /**
     * 根据传递过来的值取值并判断
     */
    public void initDate(){
        if(!TextUtils.isEmpty(mGroupListErActivtiy.getIntent().getStringExtra("group"))){
            group = mGroupListErActivtiy.getIntent().getStringExtra("group");
            mMessage = mGroupListErActivtiy.getIntent().getParcelableExtra("message");
        }
        mGroupListErActivtiy.mFragments = new Fragment[1];
        mGroupListErActivtiy.fragmentManager = mGroupListErActivtiy.getSupportFragmentManager();
        if (mGroupListErActivtiy.mFragments[0] == null) {
            //群id ,那个界面传递，群名称
            mGroupListErActivtiy.mFragments[0] = new GroupListErFragment();
            Bundle bundle = new Bundle();
            bundle.putString("group",group);
            bundle.putParcelable("message",mMessage);
            mGroupListErActivtiy.mFragments[0].setArguments(bundle);
            mGroupListErActivtiy.fragmentManager.beginTransaction().add(R.id.establish_first_step, mGroupListErActivtiy.mFragments[0]).commit();
            mGroupListErActivtiy.fragmentTransaction = mGroupListErActivtiy.fragmentManager.beginTransaction().hide(mGroupListErActivtiy.mFragments[0]);
            mGroupListErActivtiy.fragmentTransaction.show(mGroupListErActivtiy.mFragments[0]).commit();// 设置第几页显示
        }
    }
}
