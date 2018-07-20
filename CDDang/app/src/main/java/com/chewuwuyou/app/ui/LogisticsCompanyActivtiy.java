package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.LogisticsCompanyAdapter;
import com.chewuwuyou.app.bean.CarProvinceItem;
import com.chewuwuyou.app.bean.LogisticsCompany;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.CharacterParser;
import com.chewuwuyou.app.widget.PinyinComparator;
import com.chewuwuyou.app.widget.SideBar;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 选择物流公司
 * lliuchun
 */

public class LogisticsCompanyActivtiy extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn) //返回上一页
    private ImageButton mBarLeftIbtn;
    @ViewInject(id = R.id.dialog)//选择字母提示
    private TextView mDialog;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitleTV;
    @ViewInject(id = R.id.country_lvcountry)//物流公司名称
    private ListView mLogisticsName;
    @ViewInject(id = R.id.sidrbar)//a-z字母排序
    private SideBar mSideBar;
    private CharacterParser characterParser; //实例化汉字转拼音类
    private List<LogisticsCompany> mLogisticsCompanies;//物流公司集合
    //头部
    private LinearLayout mLinearLayout1, mLinearLayout2;
    private ImageView mImageView1, mImageView2, mImageView3, mImageView4,
            mImageView5, mImageView6, mImageView7, mImageView8;


    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    /**
     * 适配器
     *
     * @param savedInstanceState
     */
    private LogisticsCompanyAdapter mLogisticsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_company_activtiy);
        initView();
        initData();
        initEvent();
    }

    /**
     * 事件点击
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        Intent intent = new Intent();


        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;

            case R.id.imageView1:

                intent.putExtra("commpanyname", "顺丰快递");
                setResult(RESULT_OK, intent);
                finishActivity();

                break;
            case R.id.imageView2:

                intent.putExtra("commpanyname", "申通快递");
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            case R.id.imageView3:
                intent.putExtra("commpanyname", "圆通速递");
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            case R.id.imageView4:
                intent.putExtra("commpanyname", "韵达快递");
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            case R.id.imageView5:
                intent.putExtra("commpanyname", "EMS");
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            case R.id.imageView6:
                intent.putExtra("commpanyname", "中通速递");
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            case R.id.imageView7:
                intent.putExtra("commpanyname", "百世汇通");
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            case R.id.imageView8:
                intent.putExtra("commpanyname", "宅急送");
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            default:
                break;


        }

    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {

        mLogisticsCompanies = LogisticsCompany.parseList(getFromAssets("wuliu.json"));
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSideBar.setTextView(mDialog);
        mTitleTV.setText("物流公司");
        //listview  header

        mLinearLayout1 = (LinearLayout) findViewById(R.id.lin);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.lin2);

        mImageView1 = (ImageView) findViewById(R.id.imageView1);
        mImageView2 = (ImageView) findViewById(R.id.imageView2);
        mImageView3 = (ImageView) findViewById(R.id.imageView3);
        mImageView4 = (ImageView) findViewById(R.id.imageView4);
        mImageView5 = (ImageView) findViewById(R.id.imageView5);
        mImageView6 = (ImageView) findViewById(R.id.imageView6);
        mImageView7 = (ImageView) findViewById(R.id.imageView7);
        mImageView8 = (ImageView) findViewById(R.id.imageView8);


    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        // 根据a-z进行排序源数据
        mLogisticsCompanies = filledData(mLogisticsCompanies);
        Collections.sort(mLogisticsCompanies, pinyinComparator);
        mLogisticsAdapter = new LogisticsCompanyAdapter(LogisticsCompanyActivtiy.this, mLogisticsCompanies);
        mLogisticsName.setAdapter(mLogisticsAdapter);

    }


    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        //设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mLogisticsAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mLogisticsName.setSelection(position);
                }

            }
        });
        mLogisticsName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("commpanyname", mLogisticsCompanies.get(position).getCommpanyName());
                setResult(RESULT_OK, intent);
                finishActivity();
            }
        });

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);
        mImageView6.setOnClickListener(this);
        mImageView7.setOnClickListener(this);
        mImageView8.setOnClickListener(this);

    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<LogisticsCompany> filledData(List<LogisticsCompany> date) {
        List<LogisticsCompany> mSortList = new ArrayList<LogisticsCompany>();

        for (int i = 0; i < date.size(); i++) {
            LogisticsCompany sortModel = new LogisticsCompany();
            sortModel.setCommpanyName(date.get(i).getCommpanyName());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getCommpanyName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<LogisticsCompany> filterDateList = new ArrayList<LogisticsCompany>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mLogisticsCompanies;
        } else {
            filterDateList.clear();
            for (LogisticsCompany sortModel : mLogisticsCompanies) {
                String name = sortModel.getCommpanyName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        mLogisticsAdapter.updateListView(filterDateList);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return false;
    }

}
