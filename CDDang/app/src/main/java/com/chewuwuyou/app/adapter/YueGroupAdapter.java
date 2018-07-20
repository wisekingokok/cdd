package com.chewuwuyou.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.chewuwuyou.app.bean.YueTabItem;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：VehicleAdapter 类描述： 创建人：Administrator 创建时间：2014-12-8 下午9:49:49
 * 修改人：Administrator 修改时间：2014-12-8 下午9:49:49 修改备注：
 */
public class YueGroupAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
    public ArrayList<Fragment> mFragments;
    private List<YueTabItem> mData;// 驾证信息集合

    public YueGroupAdapter(FragmentActivity activity, List<YueTabItem> data, ArrayList<Fragment> mFragments) {
        super(activity.getSupportFragmentManager());
        this.mData = data;
        this.mFragments = mFragments;
    }

    public ArrayList<Fragment> getmFragments() {
        return mFragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragments.get(arg0);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void addTab(Fragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return mData.get(position).getTitle();
    }

    @Override
    public int getIconResId(int index) {
        // TODO Auto-generated method stub
        return mData.get(index).getIconResId();
    }
}