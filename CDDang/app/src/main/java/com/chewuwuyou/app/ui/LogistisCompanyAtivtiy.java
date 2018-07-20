package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.LogisticsCompany;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 填写物流信息
 */
public class LogistisCompanyAtivtiy extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn) //返回上一页
    private ImageButton mBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitleTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv)//保存
    private TextView mBarRightTV;
    @ViewInject(id = R.id.logistics_company_name)//物流公司名称
    private TextView mLogisticsCompanyNameTV;
    @ViewInject(id = R.id.logistics_numbers)//物流单号
    private EditText mLogisticsNumbersTV;
    @ViewInject(id = R.id.logistics_code)//扫描单号
    private ImageView mLogisticsCode;
    @ViewInject(id = R.id.logistics_code_text)//扫描单号
    private TextView mTextView_code;

    private final int LOGISTIS = 20;//选择物流公司名称
    private final int CODE = 10;//扫描二维码
    private String mCommpanycode, mCommpanyName;
    private List<LogisticsCompany> mLogisticsCompanies;//物流公司集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_company);
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

        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_right_tv://保存
                if (TextUtils.isEmpty(mLogisticsCompanyNameTV.getText().toString())) {
                    ToastUtil.toastShow(LogistisCompanyAtivtiy.this, "请选择物流公司名称");
                } else if (TextUtils.isEmpty(mLogisticsNumbersTV.getText().toString())) {
                    ToastUtil.toastShow(LogistisCompanyAtivtiy.this, "请填写运单号");
                } else if (TextUtils.isEmpty(mCommpanycode)) {
                    ToastUtil.toastShow(LogistisCompanyAtivtiy.this, "请选择物流公司名称");
                } else if (mLogisticsNumbersTV.getText().toString().length() < 6 || mLogisticsNumbersTV.getText().toString().length() > 50) {
                    ToastUtil.toastShow(LogistisCompanyAtivtiy.this, "物流单号格式不正确");
                } else {
                    dialog("提示信息", mLogisticsCompanyNameTV.getText().toString() + "\n" + mLogisticsNumbersTV.getText().toString() + "\n" + "物流信息不可修改，请核实后确认", "");
                }
                break;
            case R.id.logistics_company_name://选择物流公司名称
                intent = new Intent(LogistisCompanyAtivtiy.this, LogisticsCompanyActivtiy.class);
                startActivityForResult(intent, LOGISTIS);
                break;
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.logistics_code://扫描二维码
                intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("index", 1);
                startActivityForResult(intent, CODE);
                break;

            case R.id.logistics_code_text://扫描二维码
                intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("index", 1);
                startActivityForResult(intent, CODE);
                break;
        }
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        mTitleTV.setText("物流详情");
        mBarRightTV.setText("保存");
        mBarRightTV.setVisibility(View.VISIBLE);
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mLogisticsCompanies = LogisticsCompany.parseList(getFromAssets("wuliu.json"));
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        mBarRightTV.setOnClickListener(this);
        mLogisticsCompanyNameTV.setOnClickListener(this);
        mLogisticsCode.setOnClickListener(this);
        mTextView_code.setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return false;
    }

    /**
     * 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOGISTIS:
                    mCommpanyName = data.getStringExtra("commpanyname");
                    for (int i = 0; i < mLogisticsCompanies.size(); i++) {
                        if (mLogisticsCompanies.get(i).getCommpanyName().equals(mCommpanyName)) {
                            mCommpanycode = mLogisticsCompanies.get(i).getCommpanyCode();
                        }
                    }
                    mLogisticsCompanyNameTV.setText(mCommpanyName);
                    break;
                case CODE:
                    if (!TextUtils.isEmpty(data.getStringExtra("ind"))) {
                        mLogisticsNumbersTV.setText(data.getStringExtra("ind") + "");
                    }


                    break;

            }
        }
    }

    /**
     * 添加物流信息
     */
    public void AddLogisyisCompany() {

//        intent.putExtra("expressNo", mTask.getExpressNo());
//        intent.putExtra("companyNo", mTask.getCompanyNo());

        AjaxParams params = new AjaxParams();
        if (getIntent().getStringExtra("flag").equals("3")) {
            params.put("taskId", getIntent().getStringExtra("taskId"));//收货人
            params.put("companyNo", mCommpanycode);//物流代码
            params.put("expressNo", mLogisticsNumbersTV.getText().toString());//物流单号
        } else {
            params.put("taskId", getIntent().getStringExtra("taskId"));//收货人
            params.put("revCompanyNo", mCommpanycode);//物流代码
            params.put("revExpressNo", mLogisticsNumbersTV.getText().toString());//物流单号
            params.put("expressNo", getIntent().getStringExtra("expressNo"));//物流代码
            params.put("companyNo", getIntent().getStringExtra("companyNo"));//物流单号
        }
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            finishActivity();//关闭当前页面
                            break;
                        case Constant.NET_DATA_FAIL:
                            ToastUtil.toastShow(LogistisCompanyAtivtiy.this,
                                    ((DataError) msg.obj).getErrorMessage());
                            break;
                        default:
                            break;
                    }
                } else {
                    ToastUtil.toastShow(LogistisCompanyAtivtiy.this, "数据解析错误");
                }
            }
        }, params, NetworkUtil.ADD_LOGISTICS, false, 0);
    }

    /**
     * 提示用户是否进行操作
     */
    public void dialog(String title, String context, final String txet) {
        new com.chewuwuyou.app.utils.AlertDialog(LogistisCompanyAtivtiy
                .this).builder().setTitle(title)
                .setMsg(context)
                .setPositiveButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                }).setNegativeButton("确定", new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AddLogisyisCompany();

            }
        }).show();
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle(title);
//        dialog.setMessage(context);
//        dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                AddLogisyisCompany();
//            }
//        });
//        dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                arg0.dismiss();
//            }
//        });
//        dialog.create().show();
    }
}
