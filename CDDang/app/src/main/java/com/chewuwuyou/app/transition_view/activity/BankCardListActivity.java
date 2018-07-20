package com.chewuwuyou.app.transition_view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_adapter.BancCardListAdapter;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by xxy on 2016/10/20 0020.
 */

public class BankCardListActivity extends BaseTitleActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private BancCardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_list);
        ButterKnife.bind(this);
        setBarTitle("银行卡");
        setLeftBarBtnImage(R.drawable.backbutton);
        setRightBarTV("添加");
        setRefreshingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BancCardListAdapter(this));
        adapter.setItemClickListener(new BancCardListAdapter.ItemClickListener() {
            @Override
            public void click(View v, int position) {
                showToast(position + "");
            }
        });
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable.timer(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                setRefreshing(false, false);
                            }
                        });
            }
        });
    }

    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag) {
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
            case TITLE_TAG_RIGHT_TV:
                // 头部跳转
                Intent intent = new Intent(BankCardListActivity.this,BindingBankCardActivtiy.class);
                startActivity(intent);
                break;
        }
    }
}
