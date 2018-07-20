package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CompanyService;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.fragment.CompanyServiceFragment;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

public class CompanySearchActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditText;
    private TextView mTextView_sure;
    private ListView mListView;
    private TextView mTextView_empty;

    private CompanyServiceFragment.Adapter mAdapter;
    private List<CompanyService> mList = new ArrayList<>();
    private int mType;

    private String province;
    private String city;
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);
        initView();
        initData();
        initEvent();

    }

    protected void initView() {
        mEditText = (EditText) findViewById(R.id.et_search);
        mTextView_sure = (TextView) findViewById(R.id.sure);
        mListView = (ListView) findViewById(R.id.list);
        mTextView_empty = (TextView) findViewById(R.id.empty_tv);
        mAdapter = new CompanyServiceFragment.Adapter(this, mList, R.layout.item_company_advantage);
        mListView.setAdapter(mAdapter);

    }

    protected void initData() {
        mType = getIntent().getIntExtra(CompanyServiceActivity.TYPE, 1);
        province = getIntent().getStringExtra(CompanyServiceActivity.PROVICE);
        if (!"全部".equals(getIntent().getStringExtra(CompanyServiceActivity.CITY)))
            city = getIntent().getStringExtra(CompanyServiceActivity.CITY);
        if (!"全部".equals(getIntent().getStringExtra(CompanyServiceActivity.DISTRICT)))
            area = getIntent().getStringExtra(CompanyServiceActivity.DISTRICT);
    }


    protected void initEvent() {
        mTextView_sure.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {

                    mTextView_sure.setText("取消");

                } else {
                    mTextView_sure.setText("搜索");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sure:

                if (mTextView_sure.getText().toString().equals("搜索"))
                    getData();
                else
                    finishActivity();

                break;

            default:
                break;

        }
    }

    private void getData() {

        AjaxParams params = new AjaxParams();
        params.put("type", String.valueOf(mType + 1));
        params.put("content", mEditText.getText().toString());
        params.put("province", province);
        params.put("city", city);
        params.put("area", area);

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        List<CompanyService> list = CompanyService.parseList(String.valueOf(msg.obj));
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mTextView_empty.setVisibility(View.GONE);
                        if (mList.isEmpty()) {
                            mTextView_empty.setVisibility(View.VISIBLE);
                            ToastUtil.toastShow(CompanySearchActivity.this, "无相关数据");
                        }
                        break;

                    case Constant.NET_DATA_FAIL:

                        DataError mDataError = DataError.parse(String.valueOf(msg.obj));
                        ToastUtil.toastShow(CompanySearchActivity.this, mDataError.getErrorMessage());

                        break;
                    case Constant.NET_DATA_NULL:

                        if (mAdapter.getCount() == 0)
                            mTextView_empty.setVisibility(View.VISIBLE);
                        ToastUtil.toastShow(CompanySearchActivity.this, "无相关数据");
                        break;
                    default:
                        break;


                }


            }
        }, params, NetworkUtil.SJYS, false, 0);


    }


    public static void launch(Context mContext, Bundle m) {
        Intent mIntent = new Intent(mContext, CompanySearchActivity.class);
        if (m != null)
            mIntent.putExtras(m);
        mContext.startActivity(mIntent);

    }


}
