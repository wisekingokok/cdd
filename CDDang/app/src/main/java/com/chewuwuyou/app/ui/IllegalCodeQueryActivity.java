package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.DqdmFragment;
import com.chewuwuyou.app.fragment.WzdmFragment;
import com.viewpagerindicator.TabPageIndicator;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章代码查询
 * @author:yuyong
 * @date:2015-10-19下午3:12:31
 * @version:1.2.1
 */
public class IllegalCodeQueryActivity extends BaseFragmentActivity implements
        OnClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private RelativeLayout mTitleHeight;//标题布局高度

    private RadioGroup tabs;
    private ViewPager mViewPager;
    DqdmFragment mDqdmFragment;
    WzdmFragment mWzdmFragment;
    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[]{"地区代码", "违章代码"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.illegal_code_query_ac);
        initView();
        initData();
        initEvent();
    }


    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);

        tabs = (RadioGroup) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);

    }


    protected void initData() {

        mDqdmFragment = DqdmFragment.newInstance();
        mWzdmFragment = WzdmFragment.newInstance();

        isTitle(mTitleHeight);//根据不同手机判断
        mTitleTV.setText("代码查询");
        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.checkbox_dq:

                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.checkbox_wz:
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
             if (position==0)
                 tabs.check(R.id.checkbox_dq);
                else
                 tabs.check(R.id.checkbox_wz);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//
//        mIndicator.addTab(mIndicator.newTab().setText("地区代码").setTabListener(this));
//        mIndicator.addTab(mIndicator.newTab().setText("违章代码").setTabListener(this));


    }


    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
//		mWzdmLV.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				String province = mWzdms.get(arg2).getProvince();
//				Intent intent = new Intent(IllegalCodeQueryActivity.this,
//						VehicleServiceActivity.class);
//				intent.putExtra("tag", "1");
//				intent.putExtra("isWzdmActivity", 1);
//				intent.putExtra("province", province);
//				if (!province.equals("重庆市") && !province.equals("北京市")
//						&& !province.equals("天津市") && !province.equals("上海市")) {
//					intent.putExtra("city", mWzdms.get(arg2).getCity());
//				} else {
//					intent.putExtra("city", "");
//				}
//
//				intent.putExtra("serviceType",
//						Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
//				startActivity(intent);
//			}
//		});
        // mQueryBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            // case R.id.query_btn:
            // String illegalareaCode = mIllegalCodeQueryET.getText().toString();
            // int len = mJsonArray.length();
            // for (int i = 0; i < len; i++) {
            // try {
            // JSONObject jo = mJsonArray.getJSONObject(i);
            // if (illegalareaCode.equals(jo.getString("illegalCode"))) {
            // AlertDialog.Builder dialog = new AlertDialog.Builder(
            // IllegalCodeQueryActivity.this);
            // dialog.setTitle("违章代码查询结果");
            // String province = jo.getString("province");
            // String city = jo.getString("city");
            // String area = jo.getString("area");
            // dialog.setMessage("通过查询违章地区为:" + province + city + area);
            // if (province.equals(city) && province.equals(area)) {
            // dialog.setMessage("通过查询违章地区为:" + province);
            // } else if (province.equals(city)
            // && !province.equals(area)) {
            // dialog.setMessage("通过查询违章地区为:" + province + area);
            // } else {
            // dialog.setMessage("通过查询违章地区为:" + province + city
            // + area);
            // }
            //
            // dialog.setPositiveButton("确定",
            // new DialogInterface.OnClickListener() {
            //
            // @Override
            // public void onClick(DialogInterface arg0,
            // int arg1) {
            // arg0.dismiss();
            // }
            // });
            // dialog.show();
            // } else {
            // AlertDialog.Builder dialog = new AlertDialog.Builder(
            // IllegalCodeQueryActivity.this);
            // dialog.setTitle("违章代码查询结果");
            // dialog.setMessage("暂未查询到目标地区");
            // dialog.setPositiveButton("确定",
            // new DialogInterface.OnClickListener() {
            //
            // @Override
            // public void onClick(DialogInterface arg0,
            // int arg1) {
            // arg0.dismiss();
            // }
            // });
            // dialog.show();
            // }
            // } catch (JSONException e) {
            // e.printStackTrace();
            // }
            //
            // }
            // break;
            default:
                break;
        }
    }


    class PageAdapter extends FragmentPagerAdapter {


        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return mDqdmFragment;
            else
                return mWzdmFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }


    }


}
