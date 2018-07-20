
package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @version 1.1.0
 * @describe:投诉建议
 * @author:yuyong
 * @created:2014-12-16上午11:54:18
 */
public class ComplainSuggestionActivity extends BaseActivity {

    /**
     * 输入投诉建议
     */
    @ViewInject(id = R.id.suggestion_text)
    private EditText mSuggestion;
    private RelativeLayout mTitleHeight;//标题布局高度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain_suggestion_ac);
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);//根据不同手机判断
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText(R.string.suggestion_title);
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finishActivity();
            }
        });



        init();

    }

    private void init() {
        mSuggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() >= 50)
                    ToastUtil.toastShow(ComplainSuggestionActivity.this, "字数不能大于50个哟!");

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });



    }

    public void onAction(View v) {

        if (TextUtils.isEmpty(mSuggestion.getText().toString())) {
            ToastUtil.showToast(ComplainSuggestionActivity.this,
                    R.string.please_input_suggestion_nr);
        } else {
            AjaxParams params = new AjaxParams();
            params.put("content", mSuggestion.getText().toString());
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // TODO Auto-generated method stub
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            ToastUtil.showToast(ComplainSuggestionActivity.this,
                                    R.string.thank_suggestion);
                            mSuggestion.setText("");
                            finishActivity();
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(ComplainSuggestionActivity.this, ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            break;
                    }
                }
            }, params, NetworkUtil.ADD_FEEDBACK, false, 0);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatService.onPause(ComplainSuggestionActivity.this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatService.onResume(ComplainSuggestionActivity.this);
    }
}
