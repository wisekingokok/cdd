///**
// * 
// */
//package com.chewuwuyou.app.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.view.ViewGroup;
//
//import com.chewuwuyou.app.bean.BanKuaiItem;
//
///**
// * 类名称：VehicleAdapter 类描述： 创建人：Administrator 创建时间：2014-12-8 下午9:49:49
// * 修改人：Administrator 修改时间：2014-12-8 下午9:49:49 修改备注：
// * 
// * @version
// * 
// */
//public class HotTieGroupAdapter extends FragmentStatePagerAdapter {
//	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
//	private List<BanKuaiItem> mData;// 驾证信息集合
//	private Activity mActivity;
//
//	public HotTieGroupAdapter(FragmentActivity activity, List<BanKuaiItem> data) {
//		super(activity.getSupportFragmentManager());
//		this.mActivity = activity;
//		this.mData = data;
//		for(BanKuaiItem item : mData) {
//			addTab(new HotTieFragment(mActivity, item.getId()));
//		}
//	}
//	
//	public ArrayList<Fragment> getmFragments() {
//		return mFragments;
//	}
//
//	@Override
//	public Fragment getItem(int arg0) {
//		return mFragments.get(arg0);
//	}
//
//	@Override
//	public int getCount() {
//		return mFragments.size();
//	}
//
//	public void addTab(Fragment fragment) {
//		mFragments.add(fragment);
//		notifyDataSetChanged();
//	}
//
//	@Override
//	public int getItemPosition(Object object) {
//		return POSITION_NONE;
//	}
//
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
//	}
//
//	@Override
//	public CharSequence getPageTitle(int position) {
//		// TODO Auto-generated method stub
//		return mData.get(position).getTitle();
//	}
//}