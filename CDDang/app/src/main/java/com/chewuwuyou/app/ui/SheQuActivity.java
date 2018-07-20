package com.chewuwuyou.app.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.CurrentViewPager;

public class SheQuActivity extends BaseFragmentActivity implements View.OnClickListener, OnCheckedChangeListener{
	private static final String[] CONTENT = new String[] {"我的帖子", "我的活动" };
	private ImageButton mBackBtn;
	private RadioGroup mMyRadioGroup;// 标题
	private CurrentViewPager mViewPager;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shequ_ac2);
		mTitleHeight =  (RelativeLayout) findViewById(R.id.headerbar);
		isTitle(mTitleHeight);//根据不同手机判断
		mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(this);


		FragmentStatePagerAdapter adapter = new GroupAdapter(SheQuActivity.this);

		mViewPager = (CurrentViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(adapter);
		mViewPager.setmNotScroll(true);
		
		mMyRadioGroup = (RadioGroup) findViewById(R.id.my_radio_group);
		mMyRadioGroup.setOnCheckedChangeListener(this);

//		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//		indicator.setViewPager(mViewPager);
	}

	class GroupAdapter extends FragmentStatePagerAdapter {
		
		public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

		public GroupAdapter(FragmentActivity activity) {
			super(activity.getSupportFragmentManager());
//				addTab(MyQuanFragment.newInstance(SheQuActivity.this));
			    addTab(MyTieFragment.newInstance(SheQuActivity.this));
				addTab(MyYueFragment.newInstance(SheQuActivity.this));
				
		}
		
		public ArrayList<Fragment> getmFragments() {
			return mFragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragments.get(arg0);
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
			super.destroyItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

	@Override
	public void onClick(View v) {
		//Intent intent;
		switch (v.getId()) {
		// 退出
		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.my_tie_btn:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.my_yue_btn:
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}

	}

}
