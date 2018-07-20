package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.ListViewForScrollView;

import net.tsz.afinal.annotation.view.ViewInject;

public class SearchContactActivity extends BaseActivity {

    @ViewInject(id = R.id.et_search)
    private EditText mEditText;
    @ViewInject(id = R.id.sure)
    private TextView mTextView_sure;
    @ViewInject(id = R.id.list1)
    private ListViewForScrollView mListView1;
    @ViewInject(id = R.id.list2)
    private ListViewForScrollView mListView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);
        initView();
        initData();
        initEvent();
    }


    protected void initView() {


    }


    protected void initData() {

    }


    protected void initEvent() {

    }
}
