package com.chewuwuyou.app.transition_presenter.im_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.EstablishGroupFirstStepFragment;
import com.chewuwuyou.app.transition_presenter.BasePresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseActivity;
import com.chewuwuyou.app.transition_view.activity.im_group.EstablishGroupFirstStepErActivtiy;
import com.chewuwuyou.app.transition_view.activity.im_group_fragment.EstablishGroupFirstStepErFragment;
import com.chewuwuyou.app.transition_view.activity.im_group_fragment.GroupListErFragment;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.rong.view.GroupEstablishSecondActivtiy;

import io.rong.imlib.model.Message;

/**
 * 群列表
 * liuchun
 */

public class EstabGroupFirstStepPresenter extends BasePresenter {

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String mGroupId,mType,mGroupName;
    private Message mMsg;

    private EstablishGroupFirstStepErActivtiy mEstablishGroupFirstStepErActivtiy;

    public EstabGroupFirstStepPresenter(EstablishGroupFirstStepErActivtiy mEstablishGroupFirstStepErActivtiy) {
        super(mEstablishGroupFirstStepErActivtiy);
        this.mEstablishGroupFirstStepErActivtiy = mEstablishGroupFirstStepErActivtiy;
    }

    /**
     * 根据传递过来的值取值并判断
     */
    public void initDate(){
        mFragments = new Fragment[1];
        fragmentManager = mEstablishGroupFirstStepErActivtiy.getSupportFragmentManager();
        mType = mEstablishGroupFirstStepErActivtiy.getIntent().getStringExtra("type");
        if(mType.equals("1")||mType.equals("2")){
            mGroupId = mEstablishGroupFirstStepErActivtiy.getIntent().getStringExtra("groupId");
            mGroupName = mEstablishGroupFirstStepErActivtiy.getIntent().getStringExtra("groupName");
        }else if(mType.equals(Constant.FORWARD_GROUP)){
            mMsg =  mEstablishGroupFirstStepErActivtiy.getIntent().getParcelableExtra("msg");
        }
        if (mFragments[0] == null) {
            //群id ,那个界面传递，群名称
            mFragments[0] = new EstablishGroupFirstStepErFragment();
            Bundle bundle = new Bundle();
            bundle.putString("groupId",mGroupId);
            bundle.putString("groupName",mGroupName);
            bundle.putString("type",mType);
            bundle.putParcelable("msg",mMsg);
            mFragments[0].setArguments(bundle);
            fragmentManager.beginTransaction().add(R.id.establish_first_step, mFragments[0]).commit();
        }
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]);
        fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示
    }
}
