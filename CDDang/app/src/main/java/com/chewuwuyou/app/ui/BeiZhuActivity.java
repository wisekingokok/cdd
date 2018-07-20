package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.EditTextWithDelete;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class BeiZhuActivity extends CDDBaseActivity {

    public static final String FRIEND_ID = "friend_id";
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackIBtn;

    /**
     * 标题
     */
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mTitleTV;

    @ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
    private TextView mTextView_right;

    @ViewInject(id = R.id.username, click = "onAction")
    EditTextWithDelete mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bei_zhu);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mTitleTV.setText("备注名");
        mTextView_right.setText("完成");
        mTextView_right.setVisibility(View.VISIBLE);
        mTextView_right.setEnabled(false);
        mTextView_right.setClickable(false);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mTextView_right.setEnabled(false);
                    mTextView_right.setClickable(false);
                } else {
                    mTextView_right.setEnabled(true);
                    mTextView_right.setClickable(true);
                }


                if (s.length() >= 12) {
                    ToastUtil.toastShow(BeiZhuActivity.this, "不能超过12个字哟");
                }


            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onAction(View v) {
        Intent intent = new Intent();
        ComponentName comp;
        switch (v.getId()) {

            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;

            case R.id.sub_header_bar_right_tv:

                submit();

                break;


        }
    }

    private void submit() {

        final ProgressDialog m = ProgressDialog.show(BeiZhuActivity.this, null, "请稍后……", false, true);

        String friendId = getIntent().getStringExtra(FRIEND_ID);


        AjaxParams params = new AjaxParams();
        params.put("userId", CacheTools.getUserData("rongUserId"));
        params.put("friendId", friendId);
        params.put("name", mEditText.getText().toString());
        NetworkUtil.postMulti(NetworkUtil.ALTER_FRIEND_NAME, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                MyLog.i("success");

                m.dismiss();
                JSONObject m = null;
                try {
                    m = new JSONObject(s);
                    String json = m.getString("data");

                    ErrorCodeUtil.doErrorCode(BeiZhuActivity.this, m.getInt("code"), m.getString("message"));

                    ToastUtil.toastShow(BeiZhuActivity.this, "设置成功");

                    mTextView_right.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent m = new Intent();
                            m.putExtra(FRIEND_ID, mEditText.getText().toString());
                            setResult(RESULT_OK, m);
                            BeiZhuActivity.this.finish();
                        }
                    }, 1000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(BeiZhuActivity.this, "修改备注失败");
                m.dismiss();
            }
        });


    }

}
