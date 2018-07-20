package com.chewuwuyou.app.ui;

import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.tools.EditInputFilterThousand;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import java.text.DecimalFormat;

public class EditService extends CDDBaseActivity implements View.OnClickListener {

    private TextView mTextTV,mFeenMondel,mServiceType;//标题,规费,服务费
    private ImageButton mBarLeftIbtn;//返回上一页

    private ImageView mServiceImg;
    private Button mEditConfirm;//确认
    private EditText mEditServiceMondel;

    private TextView mEditMondel;
    private String id,serviceId;

    private String min;
    private String max;


    @ViewInject(id = R.id.service_text)
    private TextView mServiceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_service);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTextTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mServiceImg= (ImageView) findViewById(R.id.service_img);
        mFeenMondel = (TextView) findViewById(R.id.feen_mondel);
        mServiceType = (TextView) findViewById(R.id.service_type);
        mEditConfirm = (Button) findViewById(R.id.edit_confirm);
        mEditMondel = (TextView) findViewById(R.id.edit_mondel);
        mEditServiceMondel = (EditText) findViewById(R.id.edit_service_mondel);

    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTextTV.setText("编辑");
        DecimalFormat df = new DecimalFormat("######0.00");
        if(!TextUtils.isEmpty(getIntent().getStringExtra("projectnum"))){
            mServiceType.setText(ServiceUtils.getProjectName(getIntent().getStringExtra("projectnum")));
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("fees"))){
            mFeenMondel.setText(df.format(Double.parseDouble(getIntent().getStringExtra("fees")))+"");
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("projectimg"))){
            ImageUtils.displayImage(getIntent().getStringExtra("projectimg"), mServiceImg, 10);// 图片类型显示
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("servicePrice"))){
            mEditServiceMondel.setText(new DecimalFormat("######0.00").format(Double.parseDouble(getIntent().getStringExtra("servicePrice"))));
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("min"))){
            min = getIntent().getStringExtra("min");
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("max"))){
            max = getIntent().getStringExtra("max");
        }

        mServiceText.setText("服务费范围"+min+"元-"+(Double.parseDouble(max)-999.99)+"元之间");
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        mEditConfirm.setOnClickListener(this);
        mEditServiceMondel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DecimalFormat df = new DecimalFormat("######0.00");
                if(s.toString().equals(".")){
                    return;
                }

                if (!TextUtils.isEmpty(mEditServiceMondel.getText().toString())) {
                    if(Double.parseDouble(mEditServiceMondel.getText().toString())<Double.parseDouble(min)){
                        mEditMondel.setVisibility(View.VISIBLE);
                    }else if (Double.parseDouble(mEditServiceMondel.getText().toString())>Double.parseDouble(max)-999.99){
                        mEditMondel.setVisibility(View.VISIBLE);
                    }else{
                        mEditMondel.setVisibility(View.INVISIBLE);
                    }
                }else{
                    mEditMondel.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.edit_confirm:
                if(TextUtils.isEmpty(mEditServiceMondel.getText().toString())||mEditServiceMondel.getText().toString().equals(".")){
                    ToastUtil.toastShow(EditService.this,"服务费不正确");
                }else if(Double.parseDouble(mEditServiceMondel.getText().toString())<Double.parseDouble(min)){
                    ToastUtil.toastShow(EditService.this,"服务费不能小于"+min);
                }else if(Double.parseDouble(mEditServiceMondel.getText().toString())>Double.parseDouble(max)-999.99){
                    ToastUtil.toastShow(EditService.this,"服务费不能大于"+(Double.parseDouble(max)-999.99));
                }else{
                    editConfirm();
                }
                break;

        }
    }
    /**
     * 提交信息
     * @param
     */
    public void editConfirm(){

        AjaxParams params = new AjaxParams();
        params.put("id", getIntent().getStringExtra("id"));//服务费
        params.put("servicePrice", mEditServiceMondel.getText().toString()+"");//服务费
        params.put("serviceId", getIntent().getStringExtra("serviceId"));//服务费
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        finishActivity();//关闭当前页面
                        break;
                    case Constant.NET_DATA_FAIL:
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.EDIT_SERVICE, false, 0);
    }
}
