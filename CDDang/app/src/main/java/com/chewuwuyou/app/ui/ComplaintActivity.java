
package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.RadioGroup;
import com.chewuwuyou.app.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @describe:投诉商家或客服
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-25下午5:59:24
 */
public class ComplaintActivity extends BaseActivity {
    /**
     * 选择投诉商家还是客服
     */
    @ViewInject(id = R.id.ts_group)
    private RadioGroup mTsGroup;
    /**
     * 商家电话或客服编号
     */
    @ViewInject(id = R.id.phone_or_kfnum)
    private EditText mPhoneOrKfNum;
    /**
     * 投诉内容
     */
    @ViewInject(id = R.id.suggestion_text)
    private EditText mSuggestion;
    /**
     * 确认投诉
     */
    @ViewInject(id = R.id.submit_btn, click = "onAction")
    private Button mSubmitBtn;
    /**
     * 返回
     */
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackBtn;
    /**
     * 标题
     */
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mTitle;
    /**
     * 判断是商家还是用户
     */
    private int isBusiness = 0;
    /**
     * 投诉内容和（商家电话或订单号）
     */
    private String suggestion, phoneOrNum;//商家电话
    private String COMPLAIN_URL = NetworkUtil.BASE_URL + "api/complain/addComplainBusiness";
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_ac);
        initView();
    }

    public void initView() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
        if (CacheTools.getUserData("role").contains("1") && isBusiness == 0) {
            mPhoneOrKfNum.setVisibility(View.GONE);
        } else {
            mPhoneOrKfNum.setVisibility(View.VISIBLE);
        }
        mTitle.setText("投   诉");
        mTsGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String isBusi = ((RadioButton) findViewById(checkedId)).getText()
                        .toString();
                MyLog.i("YUY", isBusi);
                if (isBusi.equals("商家")) {
                    COMPLAIN_URL = NetworkUtil.BASE_URL + "api/complain/addComplainBusiness";
                    isBusiness = 0;
                } else {
                    isBusiness = 1;
                    COMPLAIN_URL = NetworkUtil.BASE_URL + "api/complain/addComplainCustom";
                }
                
                if (CacheTools.getUserData("role").contains("1") && isBusiness == 0) {
                    mPhoneOrKfNum.setVisibility(View.GONE);
                } else if (CacheTools.getUserData("role").contains("1") && isBusiness == 1) {
                    mPhoneOrKfNum.setVisibility(View.VISIBLE);
                    mPhoneOrKfNum.setHint("客服编号");
                } else if (CacheTools.getUserData("role").contains("2") && isBusiness == 0) {
                    mPhoneOrKfNum.setVisibility(View.VISIBLE);
                    mPhoneOrKfNum.setHint("商家电话");
                } else {
                    mPhoneOrKfNum.setVisibility(View.VISIBLE);
                    mPhoneOrKfNum.setHint("客服编号");
                }
            }
        });
    }
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                suggestion = mSuggestion.getText().toString();
                AjaxParams params = new AjaxParams();
                if (isBusiness == 0 && CacheTools.getUserData("role").contains("2")) {
                    phoneOrNum = mPhoneOrKfNum.getText().toString();
                    if (phoneOrNum.isEmpty()) {
                        ToastUtil.showToast(ComplaintActivity.this,
                                R.string.please_input_busi_phone);
                    } else {
                        params.put("mobile", phoneOrNum);
                        submitComplain(params);
                    }
                } else if (isBusiness == 0 && CacheTools.getUserData("role").contains("1")) {
                    submitComplain(params);
                } else if (isBusiness == 1) {
                    phoneOrNum = mPhoneOrKfNum.getText().toString();
                    if (phoneOrNum.isEmpty()) {
                        ToastUtil.showToast(ComplaintActivity.this, R.string.please_input_kef_num);
                    } else {
                        params.put("customNum", phoneOrNum);
                        submitComplain(params);
                    }
                }

                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
        }
    }

    /**
     * 提交投诉
     */
    private void submitComplain(AjaxParams params) {
        if (TextUtils.isEmpty(suggestion)) {
            ToastUtil.showToast(ComplaintActivity.this, R.string.please_input_tsnr);
        } else {
            params.put("reason", suggestion);
            requestNet(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            mSuggestion.setText("");
                            mPhoneOrKfNum.setText("");
                            ToastUtil.showToast(ComplaintActivity.this,
                                    R.string.complain_successs);
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(ComplaintActivity.this,
                                    ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            break;
                    }
                }
            }, params, COMPLAIN_URL, false, 0);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(ComplaintActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(ComplaintActivity.this);
    }
}
