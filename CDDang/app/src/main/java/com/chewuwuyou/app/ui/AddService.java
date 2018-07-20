package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ServiceProAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ServiceAdministration;
import com.chewuwuyou.app.tools.EditInputFilterThousand;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.MyGridView;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddService extends CDDBaseActivity implements View.OnClickListener {


    private TextView mTextTV;//标题
    private ImageButton mBarLeftIbtn;//返回上一页
    private TextView mOnIllegal, OnVehicle, OnAmount;// 违章代办，车辆服务，驾证服务
    private ImageView mIllegalImg, mVehicleImg, mAnountImg;
    private TextView mServiceFees, mServiceService, mTotalService;//规费，服务费，总价
    private EditText mEditService;//服务费
    private TextView mRedMondel;
    private ServiceAdministration mServiceAdminis;//实体
    private List<ServiceAdministration> mServiceAdminisList, mVehicleList, mDrivingList;//违章，车辆，驾证
    private MyGridView mServiceProjectList;
    private ServiceProAdapter mProAdapter;
    private TextView mSubmintService;
    private int indexsd = 1;
    private String mServiceType;
    private int mServiceId;

    private String servicePrice,serviceType;
    private int serviceId;

    @ViewInject(id = R.id.network_request)//网络动画
    private LinearLayout mNetworkRequest;

    @ViewInject(id = R.id.network_abnormal_layout)
    private LinearLayout mNetworkAbnormalLayout;
    @ViewInject(id = R.id.network_again)//重新加载
    private TextView mNetworkAgain;

    private String min;
    private String max;
    @ViewInject(id = R.id.service_text)
    private TextView mServiceText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service);
        initView();
        initData();
        initEvent();

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                AddService.this.finishActivity();
                break;
            case R.id.network_again://重新加载
                mNetworkRequest.setVisibility(View.VISIBLE);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                AddServiceProject();
                break;
            case R.id.service_on_illegal:
                indexsd = 1;
                mServiceFees.setText("");
                switchService(1);//违章
                break;
            case R.id.service_on_vehicle:
                indexsd = 2;
                mServiceFees.setText("");
                switchService(2);//车辆
                break;
            case R.id.service_on_amount:
                indexsd = 3;
                mServiceFees.setText("");
                switchService(3);//驾证
                break;
            case R.id.submint_service://提交
                if(TextUtils.isEmpty(mServiceFees.getText().toString())){
                    ToastUtil.toastShow(AddService.this,"请选择服务项目");
                }else if(TextUtils.isEmpty(mEditService.getText().toString())|| mEditService.getText().toString().equals(".")){
                    ToastUtil.toastShow(AddService.this,"服务费不能为空");
                }else if(Double.parseDouble(mEditService.getText().toString())<Double.parseDouble(min)){
                    ToastUtil.toastShow(AddService.this,"服务费不能小于"+min);
                }else if(Double.parseDouble(mEditService.getText().toString())>Double.parseDouble(max)-999.99){
                    ToastUtil.toastShow(AddService.this,"服务费不能大于"+(Double.parseDouble(max)-999.99));
                }else{
                    submitAddService();//提交信息
                }
                break;
        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTextTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mOnIllegal = (TextView) findViewById(R.id.service_on_illegal);
        OnVehicle = (TextView) findViewById(R.id.service_on_vehicle);
        OnAmount = (TextView) findViewById(R.id.service_on_amount);
        mIllegalImg = (ImageView) findViewById(R.id.illegal_img);
        mVehicleImg = (ImageView) findViewById(R.id.vehicle_img);
        mAnountImg = (ImageView) findViewById(R.id.anount_img);

        mServiceFees = (TextView) findViewById(R.id.service_fees);
        mEditService = (EditText) findViewById(R.id.edit_service);
        mTotalService = (TextView) findViewById(R.id.total_service);//总价
        mRedMondel = (TextView) findViewById(R.id.red_mondel);//错误金额显示
        mServiceProjectList = (MyGridView) findViewById(R.id.service_project_list);
        mServiceAdminisList = new ArrayList<ServiceAdministration>();//违章
        mVehicleList = new ArrayList<ServiceAdministration>();//车辆
        mDrivingList = new ArrayList<ServiceAdministration>();//驾证
        mSubmintService = (TextView) findViewById(R.id.submint_service);
        if(!TextUtils.isEmpty(getIntent().getStringExtra("min"))){
            min = getIntent().getStringExtra("min");
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("max"))){
            max = getIntent().getStringExtra("max");
        }
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTextTV.setText("添加服务");
        AddServiceProject();
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(this);
        mOnIllegal.setOnClickListener(this);// 违章代办
        OnVehicle.setOnClickListener(this);// 车辆服务
        OnAmount.setOnClickListener(this);// 驾证服务
        mNetworkAgain.setOnClickListener(this);
        mSubmintService.setOnClickListener(this);
        mEditService.addTextChangedListener(new TextWatcher() {
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

                if (!TextUtils.isEmpty(mEditService.getText().toString())) {
                    double sd = Double.parseDouble(mEditService.getText().toString());
                    if(TextUtils.isEmpty(mServiceFees.getText().toString())){
                        mTotalService.setText(""+df.format(Double.parseDouble(mEditService.getText().toString())+0));
                    }else {
                        mTotalService.setText(""+df.format(Double.parseDouble(mEditService.getText().toString()) + Double.parseDouble(mServiceFees.getText().toString())));
                    }
//                    if (sd < Double.valueOf(min)) {
//                        mRedMondel.setText("金额错误");
//                        mRedMondel.setVisibility(View.VISIBLE);
//                    } else if (sd > Double.valueOf(max)-999.99) {
//                        mRedMondel.setText("金额错误");
//                        mRedMondel.setVisibility(View.VISIBLE);
//                    } else {
//                        mRedMondel.setVisibility(View.INVISIBLE);
//                    }
                }else{
                    mTotalService.setText("");
                    mRedMondel.setVisibility(View.INVISIBLE);
                }
            }
        });
        /**
         * 选服务项目
         */
        mServiceProjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DecimalFormat df = new DecimalFormat("######0.00");
                if(indexsd == 1){
                    for (int i = 0;i<mServiceAdminisList.size();i++){

                        if(position == i){
                            mServiceAdminisList.get(position).setmBoolea(true);
                        }else{
                            mServiceAdminisList.get(i).setmBoolea(false);
                        }
                    }
                    servicePrice = mServiceAdminisList.get(position).getPrice();
                    serviceId = mServiceAdminisList.get(position).getId();
                    serviceType = mServiceAdminisList.get(position).getType();
                    mServiceFees.setText(""+df.format(Double.parseDouble(mServiceAdminisList.get(position).getFees())));
                }else if(indexsd == 2){
                    for (int i = 0;i<mVehicleList.size();i++){
                        if(position == i){
                            mVehicleList.get(position).setmBoolea(true);
                        }else{
                            mVehicleList.get(i).setmBoolea(false);
                        }
                    }
                    servicePrice = mVehicleList.get(position).getPrice();
                    serviceId = mVehicleList.get(position).getId();
                    serviceType = mVehicleList.get(position).getType();
                    mServiceFees.setText(""+df.format(Double.parseDouble(mVehicleList.get(position).getFees())));
                }else if(indexsd == 3){
                    for (int i = 0;i<mDrivingList.size();i++){
                        if(position == i){
                            mDrivingList.get(position).setmBoolea(true);
                        }else{
                            mDrivingList.get(i).setmBoolea(false);
                        }
                    }
                    servicePrice = mDrivingList.get(position).getPrice();
                    serviceId = mDrivingList.get(position).getId();
                    serviceType = mDrivingList.get(position).getType();
                    mServiceFees.setText(""+df.format(Double.parseDouble(mDrivingList.get(position).getFees())));
                }
                mProAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * 服务切换
     */
    public void switchService(int id) {
        switch (id) {
            case 1:
                mOnIllegal.setTextColor(getResources().getColor(R.color.white));
                OnVehicle.setTextColor(getResources().getColor(R.color.business_h));
                OnAmount.setTextColor(getResources().getColor(R.color.business_h));

                mIllegalImg.setVisibility(View.VISIBLE);
                mVehicleImg.setVisibility(View.INVISIBLE);
                mAnountImg.setVisibility(View.INVISIBLE);

                mOnIllegal.setBackgroundResource(R.drawable.message_proname_z);
                OnVehicle.setBackgroundResource(R.drawable.message_proname_h);
                OnAmount.setBackgroundResource(R.drawable.message_proname_h);

                for (int i = 0;i<mServiceAdminisList.size();i++){
                    mServiceAdminisList.get(i).setmBoolea(false);
                }

                mProAdapter.notifyDataSetChanged();

                mProAdapter = new ServiceProAdapter(AddService.this,mServiceAdminisList);
                mServiceProjectList.setAdapter(mProAdapter);
                break;
            case 2:
                mOnIllegal.setTextColor(getResources().getColor(R.color.business_h));
                OnVehicle.setTextColor(getResources().getColor(R.color.white));
                OnAmount.setTextColor(getResources().getColor(R.color.business_h));

                mIllegalImg.setVisibility(View.INVISIBLE);
                mVehicleImg.setVisibility(View.VISIBLE);
                mAnountImg.setVisibility(View.INVISIBLE);

                mOnIllegal.setBackgroundResource(R.drawable.message_proname_h);
                OnVehicle.setBackgroundResource(R.drawable.message_proname_z);
                OnAmount.setBackgroundResource(R.drawable.message_proname_h);

                for (int i = 0;i<mVehicleList.size();i++){
                    mVehicleList.get(i).setmBoolea(false);
                }

                mProAdapter = new ServiceProAdapter(AddService.this,mVehicleList);
                mServiceProjectList.setAdapter(mProAdapter);
                break;
            case 3:
                mOnIllegal.setTextColor(getResources().getColor(R.color.business_h));
                OnVehicle.setTextColor(getResources().getColor(R.color.business_h));
                OnAmount.setTextColor(getResources().getColor(R.color.white));

                mIllegalImg.setVisibility(View.INVISIBLE);
                mVehicleImg.setVisibility(View.INVISIBLE);
                mAnountImg.setVisibility(View.VISIBLE);

                mOnIllegal.setBackgroundResource(R.drawable.message_proname_h);
                OnVehicle.setBackgroundResource(R.drawable.message_proname_h);
                OnAmount.setBackgroundResource(R.drawable.message_proname_z);

                for (int i = 0;i<mDrivingList.size();i++){
                    mDrivingList.get(i).setmBoolea(false);
                }
                mProAdapter = new ServiceProAdapter(AddService.this,mDrivingList);
                mServiceProjectList.setAdapter(mProAdapter);
                break;
        }
    }


    public void AddServiceProject() {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            mNetworkRequest.setVisibility(View.GONE);
                            mNetworkAbnormalLayout.setVisibility(View.GONE);

                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("projects");

                            if(!TextUtils.isEmpty(jsonObject.getString("min"))){
                                min = jsonObject.getString("min");
                            }
                            if(!TextUtils.isEmpty(jsonObject.getString("max"))){
                                max = jsonObject.getString("max");
                            }
                            mServiceText.setText("服务费范围"+min+"元-"+(Double.parseDouble(max)-999.99)+"元之间");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.getJSONObject(i).getString("type").equals("1")) {
                                    mServiceAdminisList.add(new ServiceAdministration(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")),
                                            0,
                                            jsonArray.getJSONObject(i).getString("projectName"), jsonArray.getJSONObject(i).getString("projectNum"),
                                            jsonArray.getJSONObject(i).getString("type"), jsonArray.getJSONObject(i).getString("projectImg"),
                                            null,
                                            jsonArray.getJSONObject(i).getString("fees"), jsonArray.getJSONObject(i).getString("servicePrice"),null,false,1));

                                } else if (jsonArray.getJSONObject(i).getString("type").equals("2")) {
                                    mVehicleList.add(new ServiceAdministration(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")),
                                            0,
                                            jsonArray.getJSONObject(i).getString("projectName"), jsonArray.getJSONObject(i).getString("projectNum"),
                                            jsonArray.getJSONObject(i).getString("type"), jsonArray.getJSONObject(i).getString("projectImg"),
                                            null,
                                            jsonArray.getJSONObject(i).getString("fees"), jsonArray.getJSONObject(i).getString("servicePrice"),null,false,2));
                                } else if (jsonArray.getJSONObject(i).getString("type").equals("3")) {
                                    mDrivingList.add(new ServiceAdministration(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")),
                                            0,
                                            jsonArray.getJSONObject(i).getString("projectName"), jsonArray.getJSONObject(i).getString("projectNum"),
                                            jsonArray.getJSONObject(i).getString("type"), jsonArray.getJSONObject(i).getString("projectImg"),
                                            null,
                                            jsonArray.getJSONObject(i).getString("fees"), jsonArray.getJSONObject(i).getString("servicePrice"),null,false,3));
                                }
                            }
                            mProAdapter = new ServiceProAdapter(AddService.this,mServiceAdminisList);
                            mServiceProjectList.setAdapter(mProAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(AddService.this,
                                ((DataError) msg.obj).getErrorMessage());
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mNetworkRequest.setVisibility(View.GONE);
                        mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

        }, null, NetworkUtil.ADD_SERVICE_MODIFY, false, 0);
    }

    /**
     * 提交
     */
    public void submitAddService(){
        AjaxParams params = new AjaxParams();
        params.put("servicePrice",mEditService.getText().toString());//服务费
        params.put("serviceType", serviceType);//服务费
        params.put("serviceId",serviceId+"");//服务费
        params.put("businessId",CacheTools.getUserData("userId")+"");//服务费
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        finishActivity();//关闭当前页面
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(AddService.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.ADD_SERVICE, false, 0);
    }
}
