package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.LogisticsInformationAdapter;
import com.chewuwuyou.app.bean.LogisticsCompany;
import com.chewuwuyou.app.bean.TracesEntity;
import com.chewuwuyou.app.utils.WLUtil;
import com.chewuwuyou.app.widget.CustomNodeListView;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 物流详情
 */
public class LogisticsDetailsActivtiy extends CDDBaseActivity implements View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn) //返回上一页
    private ImageButton mBarLeftIbtn;
    @ViewInject(id = R.id.sub_header_bar_tv)//标题
    private TextView mTitleTV;
    @ViewInject(id = R.id.logisics_start)//物流状态
    private TextView mLogisicsStartTV;
    @ViewInject(id = R.id.logisics_source)//来源
    private TextView mLogisicsSourceTV;
    @ViewInject(id = R.id.logisics_xq_numbers)//物流单号
    private TextView mLogisicsXqNumbersTV;
    @ViewInject(id = R.id.logistics_listview)//物流信息列表
    private CustomNodeListView mCustomNode;
    @ViewInject(id = R.id.logintics_tab)//切换
    private LinearLayout mLoginticsTab;
    @ViewInject(id = R.id.logistics_mail)//切换
    private ScrollView mLogisticsMail;
    @ViewInject(id = R.id.message)//收到
    private RadioButton mRadMessage;
    @ViewInject(id = R.id.maillist)//发出
    private RadioButton mMaillist;
    @ViewInject(id = R.id.logistics_information)//没有信息
    private TextView mLogisticasInformation;
    @ViewInject(id = R.id.network_request)//访问网络
    private LinearLayout mNetworkRequest;
    @ViewInject(id = R.id.rotate_iv1)//访问网络
    private ImageView mRotateIv1;
    @ViewInject(id = R.id.logistics_basic)//访问网络
    private LinearLayout mLogisticsBasic;
    private List<TracesEntity> mTracesEntiiy;
    private List<TracesEntity> mTracesEntiiyEr;
    private LogisticsInformationAdapter logisticsAdapter;//适配器

    private String expressNo,companyNo,revExpressNo,revCompanyNo;
    private List<LogisticsCompany> mLogisticsCompanies;//物流公司集合

    private String logisticsStart,logisticsSoure,logisicsXqNumbers;
    private String logisticsStarter,logisticsSoureer,logisicsXqNumberser;
    private Animation operatingAnim1;

    private boolean mABoolean;
    private boolean mABooleaner;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(!expressNo.equals("")&&!companyNo.equals("")&&!revExpressNo.equals("")&&!revCompanyNo.equals("")){//显示回寄信息
                        mLogisticsBasic.setVisibility(View.VISIBLE);
                        mLogisticsMail.setVisibility(View.VISIBLE);
                        mLogisticasInformation.setVisibility(View.GONE);
                        mNetworkRequest.setVisibility(View.GONE);
                        mLoginticsTab.setVisibility(View.VISIBLE);
                    }else{
                        mLoginticsTab.setVisibility(View.GONE);
                        mNetworkRequest.setVisibility(View.GONE);
                        mLogisticasInformation.setVisibility(View.GONE);
                    }

                    if(logisticsStart.equals("2")){
                        mLogisicsStartTV.setText("在途中");
                    }else if(logisticsStart.equals("3")){
                        mLogisicsStartTV.setText("已签收");
                    }else if(logisticsStart.equals("4")){
                        mLogisicsStartTV.setText("问题件");
                    }
                    for (int i = 0;i<mLogisticsCompanies.size();i++){
                        if(mLogisticsCompanies.get(i).getCommpanyCode().equals(logisticsSoure)){
                            mLogisicsSourceTV.setText(mLogisticsCompanies.get(i).getCommpanyName());
                        }
                    }
                    mLogisicsXqNumbersTV.setText(logisicsXqNumbers);
                    Collections.reverse(mTracesEntiiy);//倒序
                    logisticsAdapter = new LogisticsInformationAdapter(mTracesEntiiy,LogisticsDetailsActivtiy.this);
                    mCustomNode.setAdapter(logisticsAdapter);
                    break;
                case 2:
                    if(!expressNo.equals("")&&!companyNo.equals("")&&!revExpressNo.equals("")&&!revCompanyNo.equals("")){//显示回寄信息
                        mLogisticsBasic.setVisibility(View.VISIBLE);
                        mLogisticsMail.setVisibility(View.VISIBLE);
                        mLogisticasInformation.setVisibility(View.GONE);
                        mNetworkRequest.setVisibility(View.GONE);
                        mLoginticsTab.setVisibility(View.VISIBLE);
                    }else{
                        mLoginticsTab.setVisibility(View.GONE);
                        mNetworkRequest.setVisibility(View.GONE);
                        mLogisticasInformation.setVisibility(View.GONE);
                    }
                    if(logisticsStarter.equals("2")){
                        mLogisicsStartTV.setText("在途中");
                    }else if(logisticsStarter.equals("3")){
                        mLogisicsStartTV.setText("已签收");
                    }else if(logisticsStarter.equals("4")){
                        mLogisicsStartTV.setText("问题件");
                    }
                    for (int i = 0;i<mLogisticsCompanies.size();i++){
                        if(mLogisticsCompanies.get(i).getCommpanyCode().equals(logisticsSoureer)){
                            mLogisicsSourceTV.setText(mLogisticsCompanies.get(i).getCommpanyName());
                        }
                    }
                    mLogisicsXqNumbersTV.setText(logisicsXqNumberser);
                    Collections.reverse(mTracesEntiiyEr);//倒序
                    logisticsAdapter = new LogisticsInformationAdapter(mTracesEntiiyEr,LogisticsDetailsActivtiy.this);
                    mCustomNode.setAdapter(logisticsAdapter);
                    break;
                case 3:
                    mRadMessage.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_left_isservice));
                    mRadMessage.setTextColor(getResources().getColor(R.color.white));
                    mMaillist.setBackgroundColor(getResources().getColor(R.color.white));
                    mMaillist.setTextColor(getResources().getColor(R.color.new_blue));
                    if(mABoolean == false){
                        mNetworkRequest.setVisibility(View.VISIBLE);
                        mLogisticsBasic.setVisibility(View.VISIBLE);
                        MailLogistics(companyNo,expressNo);//寄出
                    }else{
                        if(mTracesEntiiy.size()==0){
                            mNetworkRequest.setVisibility(View.GONE);
                            mLogisticasInformation.setVisibility(View.VISIBLE);
                            mLogisticsMail.setVisibility(View.GONE);
                            mLogisticsBasic.setVisibility(View.GONE);
                            MailLogistics();
                        }else{
                            mNetworkRequest.setVisibility(View.GONE);
                            mLogisticsBasic.setVisibility(View.VISIBLE);
                            mLogisticasInformation.setVisibility(View.GONE);
                            mLogisticsMail.setVisibility(View.VISIBLE);
                            if(logisticsStart.equals("2")){
                                mLogisicsStartTV.setText("在途中");
                            }else if(logisticsStart.equals("3")){
                                mLogisicsStartTV.setText("已签收");
                            }else if(logisticsStart.equals("4")){
                                mLogisicsStartTV.setText("问题件");
                            }
                            for (int i = 0;i<mLogisticsCompanies.size();i++){
                                if(mLogisticsCompanies.get(i).getCommpanyCode().equals(logisticsSoure)){
                                    mLogisicsSourceTV.setText(mLogisticsCompanies.get(i).getCommpanyName());
                                }
                            }
                            mLogisicsXqNumbersTV.setText(logisicsXqNumbers);
                            logisticsAdapter = new LogisticsInformationAdapter(mTracesEntiiy,LogisticsDetailsActivtiy.this);
                            mCustomNode.setAdapter(logisticsAdapter);
                        }
                    }

                    break;
                case 4:
                    mRadMessage.setBackgroundColor(getResources().getColor(R.color.white));
                    mRadMessage.setTextColor(getResources().getColor(R.color.new_blue));
                    mMaillist.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_right_isservice));
                    mMaillist.setTextColor(getResources().getColor(R.color.white));
                    if(mABooleaner == false){
                        mNetworkRequest.setVisibility(View.VISIBLE);
                        mLogisticsBasic.setVisibility(View.VISIBLE);
                        ReceivedLogistics(revCompanyNo,revExpressNo);//收入
                    }else{
                        if(mTracesEntiiyEr.size()==0){
                            mNetworkRequest.setVisibility(View.GONE);
                            mLogisticsBasic.setVisibility(View.GONE);
                            mLogisticasInformation.setVisibility(View.VISIBLE);
                            mLogisticsMail.setVisibility(View.GONE);
                            ReturnLogistics();
                        }else{
                            mLogisticsBasic.setVisibility(View.VISIBLE);
                            mNetworkRequest.setVisibility(View.GONE);
                            mLogisticasInformation.setVisibility(View.GONE);
                            mLogisticsMail.setVisibility(View.VISIBLE);
                            if(logisticsStarter.equals("2")){
                                mLogisicsStartTV.setText("在途中");
                            }else if(logisticsStarter.equals("3")){
                                mLogisicsStartTV.setText("已签收");
                            }else if(logisticsStarter.equals("4")){
                                mLogisicsStartTV.setText("问题件");
                            }
                            for (int i = 0;i<mLogisticsCompanies.size();i++){
                                if(mLogisticsCompanies.get(i).getCommpanyCode().equals(logisticsSoureer)){
                                    mLogisicsSourceTV.setText(mLogisticsCompanies.get(i).getCommpanyName());
                                }
                            }
                            mLogisicsXqNumbersTV.setText(logisicsXqNumberser);
                            logisticsAdapter = new LogisticsInformationAdapter(mTracesEntiiyEr,LogisticsDetailsActivtiy.this);
                            mCustomNode.setAdapter(logisticsAdapter);
                        }
                    }

                    break;
                case 5:
                    if(!expressNo.equals("")&&!companyNo.equals("")&&!revExpressNo.equals("")&&!revCompanyNo.equals("")){//显示回寄信息
                        mLogisticsBasic.setVisibility(View.GONE);
                        mLogisticsMail.setVisibility(View.GONE);
                        mNetworkRequest.setVisibility(View.GONE);
                        mLoginticsTab.setVisibility(View.VISIBLE);
                    }else{
                        mLogisticsBasic.setVisibility(View.GONE);
                        mLoginticsTab.setVisibility(View.GONE);
                        mLogisticsMail.setVisibility(View.GONE);
                        mNetworkRequest.setVisibility(View.GONE);

                    }
                    switch (Integer.parseInt(msg.obj.toString())){
                        case 1:
                            MailLogistics();
                            break;
                        case 2:
                            ReturnLogistics();
                            break;
                    }


                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_details);
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
        Message messag;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.message://收到
                messag = new Message();
                messag.what = 3;
                handler.sendMessage(messag);
                break;
            case R.id.maillist://发出
                messag = new Message();
                messag.what = 4;
                handler.sendMessage(messag);
                break;
        }
    }

    /**
     * 初始化
     */
    protected void initView() {
        mTitleTV.setText("物流详情");
        mTracesEntiiy = new ArrayList<TracesEntity>();
        mTracesEntiiyEr = new ArrayList<TracesEntity>();
        expressNo = getIntent().getStringExtra("expressNo");
        companyNo = getIntent().getStringExtra("companyNo");
        revExpressNo = getIntent().getStringExtra("revExpressNo");
        revCompanyNo = getIntent().getStringExtra("revCompanyNo");
        mLogisticsCompanies = LogisticsCompany.parseList(getFromAssets("wuliu.json"));

        operatingAnim1 = AnimationUtils.loadAnimation(LogisticsDetailsActivtiy.this, R.anim.tip1);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim1.setInterpolator(lin);
        operatingAnim1.setDuration(2000);
        if (operatingAnim1 != null) {
            mRotateIv1.startAnimation(operatingAnim1);
        } else {
            mRotateIv1.clearAnimation();
        }

        if(expressNo.equals("")&&companyNo.equals("")&&!revExpressNo.equals("")&&!revCompanyNo.equals("")){//显示回寄信息
            mLoginticsTab.setVisibility(View.GONE);
            ReceivedLogistics(revCompanyNo,revExpressNo);//寄出
        }else if(!expressNo.equals("")&&!companyNo.equals("")&&revExpressNo.equals("")&&revCompanyNo.equals("")){//显示寄出信息
            mLoginticsTab.setVisibility(View.GONE);
            MailLogistics(companyNo,expressNo);//寄出
        }else{
            mLoginticsTab.setVisibility(View.VISIBLE);
            MailLogistics(companyNo,expressNo);//寄出
        }
    }

    /**
     * 逻辑处理
     */
    protected void initData() {

    }

    /**
     * 寄出物流信息
     */
    private void MailLogistics(){
        for (int i = 0;i<mLogisticsCompanies.size();i++){
            if(mLogisticsCompanies.get(i).getCommpanyCode().equals(companyNo)){
                mLogisicsSourceTV.setText(mLogisticsCompanies.get(i).getCommpanyName());
            }
        }
        mLogisticsBasic.setVisibility(View.VISIBLE);
        mLogisticasInformation.setVisibility(View.VISIBLE);
        mLogisticasInformation.setText("暂无物流信息,请尝试官网查询");
        mLogisicsXqNumbersTV.setText(expressNo);
        mLogisicsStartTV.setText("暂无物流信息");
    }

    /**
     * 回寄物流信息
     */
    private void ReturnLogistics(){
        for (int i = 0;i<mLogisticsCompanies.size();i++){
            if(mLogisticsCompanies.get(i).getCommpanyCode().equals(revCompanyNo)){
                mLogisicsSourceTV.setText(mLogisticsCompanies.get(i).getCommpanyName());
            }
        }
        mLogisticsBasic.setVisibility(View.VISIBLE);
        mLogisicsStartTV.setText("暂无物流信息");
        mLogisicsXqNumbersTV.setText(revExpressNo);
        mLogisticasInformation.setVisibility(View.VISIBLE);
        mLogisticasInformation.setText("暂无物流信息,请尝试官网查询");
    }

    /**
     * 事件监听
     */
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        mRadMessage.setOnClickListener(this);
        mMaillist.setOnClickListener(this);
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
     * 访问寄出物流信息
     */
    public void MailLogistics(final String number, final String OddNumbers) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jsonObject = new JSONObject(WLUtil.getOrderTracesByJson(number, OddNumbers));
                    mTracesEntiiy.clear();

                    for (int i = 0; i < jsonObject.getJSONArray("Traces").length(); i++) {
                        TracesEntity entity = new TracesEntity();
                        entity.setAcceptTime(jsonObject.getJSONArray("Traces").getJSONObject(i).getString("AcceptTime"));
                        entity.setAcceptStation(jsonObject.getJSONArray("Traces").getJSONObject(i).getString("AcceptStation"));
                        mTracesEntiiy.add(entity);
                    }

                    Message message = new Message();
                    if (mTracesEntiiy.size() >0) {

                        mABoolean = true;
                        logisticsStart =  jsonObject.getString("State");
                        logisticsSoure = jsonObject.getString("ShipperCode");
                        logisicsXqNumbers = jsonObject.getString("LogisticCode");

                        message.what = 1;
                    }else {
                        mABoolean = true;
                        message.what = 5;
                        message.obj = 1;
                    }
                    handler.sendMessage(message);

//					information.setTraces(traces);
//					information.setTraces(traces);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 访问收入物流信息
     */
    public void ReceivedLogistics(final String number, final String OddNumbers) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(WLUtil.getOrderTracesByJson(number, OddNumbers));

                    mTracesEntiiyEr.clear();
                    for (int i = 0; i < jsonObject.getJSONArray("Traces").length(); i++) {
                        TracesEntity entity = new TracesEntity();
                        entity.setAcceptTime(jsonObject.getJSONArray("Traces").getJSONObject(i).getString("AcceptTime"));
                        entity.setAcceptStation(jsonObject.getJSONArray("Traces").getJSONObject(i).getString("AcceptStation"));
                        mTracesEntiiyEr.add(entity);
                    }

                    Message message = new Message();
                    if (mTracesEntiiyEr.size() >0) {
                        mABooleaner = true;
                        logisticsStarter =  jsonObject.getString("State");
                        logisticsSoureer = jsonObject.getString("ShipperCode");
                        logisicsXqNumberser = jsonObject.getString("LogisticCode");
                        message.what = 2;
                    }else {
                        mABooleaner = true;
                        message.what = 5;
                        message.obj = 2;
                    }
                    handler.sendMessage(message);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
