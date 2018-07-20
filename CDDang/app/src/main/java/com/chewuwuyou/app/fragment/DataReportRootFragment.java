package com.chewuwuyou.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.chewuwuyou.app.R;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DataReportRootFragment extends BaseFragment {
    public static final String TYPE_DATE_KEY = "typeDateKey";//类型标志
    public static final int TYPE_7 = 7;
    public static final int TYPE_15 = 15;
    public static final int TYPE_30 = 30;

    public static final String TYPE_KEY = "typeKey";
    public static final int TYPE_ROOT = 0;
    public static final int TYPE_IN = 1;
    public static final int TYPE_OUT = 2;

    private RadioGroup tabs;
    private Fragment[] mFragments = new Fragment[3];
    private FragmentManager fragmentManager;
    private int typeKey;

    /**
     * @param type (TYPE_ROOT,TYPE_IN,TYPE_OUT)
     * @return
     */
    public static DataReportRootFragment getEntity(int type) {
        DataReportRootFragment fragment = new DataReportRootFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeKey = getArguments().getInt(TYPE_KEY);
        fragmentManager = getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_report_root, null);
        tabs = (RadioGroup) view.findViewById(R.id.tabs);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        setTab(TYPE_7);
    }

    @Override
    protected void initEvent() {
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_invitation:
                        setTab(TYPE_7);
                        break;
                    case R.id.tab_ring:
                        setTab(TYPE_15);
                        break;
                    case R.id.tab_activity:
                        setTab(TYPE_30);
                        break;
                }
            }
        });
    }

    /**
     * 切换页
     *
     * @param typeDate
     */
    private void setTab(int typeDate) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideView(transaction);
        switch (typeDate) {
            case TYPE_7:
                if (mFragments[0] == null) {
                    mFragments[0] = getEntity(typeKey, typeDate);
                    transaction.add(R.id.frameLayout, mFragments[0]);
                } else
                    transaction.show(mFragments[0]);
                break;
            case TYPE_15:
                if (mFragments[1] == null) {
                    mFragments[1] = getEntity(typeKey, typeDate);
                    transaction.add(R.id.frameLayout, mFragments[1]);
                } else
                    transaction.show(mFragments[1]);
                break;
            case TYPE_30:
                if (mFragments[2] == null) {
                    mFragments[2] = getEntity(typeKey, typeDate);
                    transaction.add(R.id.frameLayout, mFragments[2]);
                } else
                    transaction.show(mFragments[2]);
                break;
        }
        transaction.commit();
    }

    private void hideView(FragmentTransaction transaction) {
        for (int i = 0; i < mFragments.length; i++) {
            if (mFragments[i] != null)
                transaction.hide(mFragments[i]);
        }
    }

    private Fragment getEntity(int typeKey, int typeDateKey) {
        if (typeKey == TYPE_ROOT)
            return DataReportFragment.getEntity(typeKey, typeDateKey);
        else
            return DataReportInOutFragment.getEntity(typeKey, typeDateKey);
    }
}
