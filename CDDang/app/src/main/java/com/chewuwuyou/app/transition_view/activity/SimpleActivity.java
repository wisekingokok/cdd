package com.chewuwuyou.app.transition_view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.SimpleEntity;
import com.chewuwuyou.app.transition_presenter.SimplePresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Yogi on 16/9/24.
 */

public class SimpleActivity extends BaseTitleActivity{

    @BindView(R.id.tv_name)
    TextView nameTV;

    @BindView(R.id.btn_show)
    Button showBtn;

    private SimplePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_activity_simple);

        ButterKnife.bind(this);

        presenter = new SimplePresenter(this);
    }

    @OnClick(R.id.btn_show)
    public void submit(View view) {
        presenter.getName();
    }


    public void updateView(SimpleEntity simple) {
        nameTV.setText(simple.address);
    }

}
