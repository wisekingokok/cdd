package com.chewuwuyou.rong.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AllGroup;
import com.chewuwuyou.app.fragment.GroupSearchFragment;
import com.chewuwuyou.app.ui.BaseFragmentActivity;
import com.chewuwuyou.app.utils.Constant;

import java.util.List;

import io.rong.imlib.model.Message;

public class GroupSearchActivtiy extends BaseFragmentActivity {


    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String mGroupName;
    private String mGroupId;
    private String mGroup;
    private List<AllGroup> mAllGroup;
    private String id;
    private Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        initView();
    }

    /**
     * 初始化内容
     */
    private void initView(){
        mFragments = new Fragment[1];
        fragmentManager = getSupportFragmentManager();

        if(!TextUtils.isEmpty(getIntent().getStringExtra("id"))){
            id = getIntent().getStringExtra("id");
        }
        if (getIntent().getStringExtra("search_type").equals(Constant.GROUP_SETUP_SEARCH) || getIntent().getStringExtra("search_type").equals(Constant.GROUP_MANGMENT_SEARCH) || getIntent().getStringExtra("search_type").equals(Constant.DELETE_GROUP)) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("groupName")) && !TextUtils.isEmpty(getIntent().getStringExtra("groupId"))) {
                mGroupName = getIntent().getStringExtra("groupName");
                mGroupId = getIntent().getStringExtra("groupId");
            }
        } else if (getIntent().getStringExtra("search_type").equals(Constant.GROUP_LIST_SEARCH)) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("group"))) {
                mGroup = getIntent().getStringExtra("group");
            }
            if(getIntent().getParcelableExtra("message")!=null){
                message = getIntent().getParcelableExtra("message");
            }
        }else if(getIntent().getStringExtra("search_type").equals(Constant.GROUP_FRIENDS)){
            mAllGroup = (List<AllGroup>) getIntent().getSerializableExtra("list");
        }
        if (mFragments[0] == null) {
            //群id ,那个界面传递，群名称
            mFragments[0] = new GroupSearchFragment(getIntent().getStringExtra("search_type"),mGroupName,mGroupId,mGroup,mAllGroup,id,message);
            fragmentManager.beginTransaction().add(R.id.establish_first_step, mFragments[0]).commit();
        }
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]);
        fragmentTransaction.show(mFragments[0]).commit();// 设置第几页显示
    }
}
