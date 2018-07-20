package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ServiceAdministrationAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ServiceAdministration;
import com.chewuwuyou.app.transition_view.activity.HairRedActivtiy;
import com.chewuwuyou.app.transition_view.activity.PersonalSendRedActivtiy;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.XListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdministrationActivity extends CDDBaseActivity implements View.OnClickListener {

    private TextView mTextTV, mBrightTV;//标题
    private ImageButton mBarLeftIbtn;//返回上一页
    private XListView mServiceListAdministration, mVehicle, mDriving;//违章，车辆，驾证
    private ServiceAdministration mServiceAdminis;//实体
    private List<ServiceAdministration> mServiceAdminisList, mVehicleList, mDrivingList;//违章，车辆，驾证
    private ServiceAdministrationAdapter mServiceAdministra,mServiceAdministraER,mServiceAdministraSAN;
    private LinearLayout mIllegalPay,mVehicleService,mDrivingService,mServiceFirst;
    private Button mAddService;//添加服务
    private ScrollView mScrollGogn;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            deleteService(bundle.getString("sid"),bundle.getInt("service"),bundle.getInt("index"));//删除服务方法
        }
    };

    private String min;
    private String max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_administration);

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
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv://添加
                intent = new Intent(this,AddService.class);
                intent.putExtra("min",min);
                intent.putExtra("max",max);
                startActivity(intent);
                break;

            case R.id.add_service://添加服务
                intent = new Intent(this,AddService.class);
                intent.putExtra("min",min);
                intent.putExtra("max",max);
                startActivity(intent);
                break;

        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mTextTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBrightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mServiceListAdministration = (XListView) findViewById(R.id.Illegal_list);
        mVehicle = (XListView) findViewById(R.id.vehicle_list);
        mDriving = (XListView) findViewById(R.id.driving_list);

        mScrollGogn = (ScrollView) findViewById(R.id.scroll_gogn);
        mIllegalPay = (LinearLayout) findViewById(R.id.illegal_pay);
        mVehicleService = (LinearLayout) findViewById(R.id.vehicle_service);
        mDrivingService = (LinearLayout) findViewById(R.id.driving_service);
        mServiceFirst = (LinearLayout) findViewById(R.id.service_first);

        mAddService = (Button) findViewById(R.id.add_service);

        mServiceAdminisList = new ArrayList<ServiceAdministration>();//违章
        mVehicleList = new ArrayList<ServiceAdministration>();//车辆
        mDrivingList = new ArrayList<ServiceAdministration>();//驾证



    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTextTV.setText("服务管理");
        mBrightTV.setText("添加");
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkService();//访问网络获取详细服务信息
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBarLeftIbtn.setOnClickListener(ServiceAdministrationActivity.this);
        mBrightTV.setOnClickListener(ServiceAdministrationActivity.this);
        mAddService.setOnClickListener(ServiceAdministrationActivity.this);
    }

    /**
     * 访问网络获取详细服务信息
     */
    private void networkService() {
        mServiceAdminisList.clear();
        mDrivingList.clear();
        mVehicleList.clear();
        AjaxParams params=new AjaxParams();
        params.put("newVer","y");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("prices");
                            min = jsonObject.getString("min");
                            max = jsonObject.getString("max");
                            for (int  i = 0;i<jsonArray.length();i++){
                                if(jsonArray.getJSONObject(i).getString("type").equals("1")){//违章
                                    mServiceAdminisList.add(new ServiceAdministration(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")),
                                                                                         Integer.parseInt(jsonArray.getJSONObject(i).getString("serviceId")),
                                                                                         jsonArray.getJSONObject(i).getString("projectName"),jsonArray.getJSONObject(i).getString("projectNum"),
                                                                                         jsonArray.getJSONObject(i).getString("type"),jsonArray.getJSONObject(i).getString("projectImg"),
                                                                                         jsonArray.getJSONObject(i).getString("serviceDesc"),
                                                                                         jsonArray.getJSONObject(i).getString("fees"),jsonArray.getJSONObject(i).getString("price"),jsonArray.getJSONObject(i).getString("sid"),false,1));
                                }else if (jsonArray.getJSONObject(i).getString("type").equals("2")){//车辆
                                    mVehicleList.add(new ServiceAdministration(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")),
                                            Integer.parseInt(jsonArray.getJSONObject(i).getString("serviceId")),
                                            jsonArray.getJSONObject(i).getString("projectName"),jsonArray.getJSONObject(i).getString("projectNum"),
                                            jsonArray.getJSONObject(i).getString("type"),jsonArray.getJSONObject(i).getString("projectImg"),
                                            jsonArray.getJSONObject(i).getString("serviceDesc"),
                                            jsonArray.getJSONObject(i).getString("fees"),jsonArray.getJSONObject(i).getString("price"),jsonArray.getJSONObject(i).getString("sid"),false,2));
                                }else if(jsonArray.getJSONObject(i).getString("type").equals("3")){//驾证
                                    mDrivingList.add(new ServiceAdministration(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")),
                                            Integer.parseInt(jsonArray.getJSONObject(i).getString("serviceId")),
                                            jsonArray.getJSONObject(i).getString("projectName"),jsonArray.getJSONObject(i).getString("projectNum"),
                                            jsonArray.getJSONObject(i).getString("type"),jsonArray.getJSONObject(i).getString("projectImg"),
                                            jsonArray.getJSONObject(i).getString("serviceDesc"),
                                            jsonArray.getJSONObject(i).getString("fees"),jsonArray.getJSONObject(i).getString("price"),jsonArray.getJSONObject(i).getString("sid"),false,3));
                                }else{
                                    ToastUtil.toastShow(ServiceAdministrationActivity.this,"暂无类型");
                                }
                            }

                            if(mServiceAdminisList.size()==0 && mVehicleList.size() == 0 && mDrivingList.size()==0){
                                mServiceFirst.setVisibility(View.VISIBLE);
                                mScrollGogn.setVisibility(View.GONE);
                                mBrightTV.setVisibility(View.GONE);
                            }else{
                                mServiceFirst.setVisibility(View.GONE);
                                mScrollGogn.setVisibility(View.VISIBLE);
                                mBrightTV.setVisibility(View.VISIBLE);
                                if(mServiceAdminisList.size() > 0){
                                    mIllegalPay.setVisibility(View.VISIBLE);
                                    mServiceAdministra = new ServiceAdministrationAdapter(ServiceAdministrationActivity.this,mServiceAdminisList,handler,min,max);
                                    mServiceListAdministration.setAdapter(mServiceAdministra);//违章
                                }
                                if(mVehicleList.size() > 0){
                                    mVehicleService.setVisibility(View.VISIBLE);
                                    mServiceAdministraER = new ServiceAdministrationAdapter(ServiceAdministrationActivity.this,mVehicleList,handler,min,max);
                                    mVehicle.setAdapter(mServiceAdministraER);//车辆
                                }
                                if(mDrivingList.size() > 0){
                                    mDrivingService.setVisibility(View.VISIBLE);
                                    mServiceAdministraSAN = new ServiceAdministrationAdapter(ServiceAdministrationActivity.this,mDrivingList,handler,min,max);
                                    mDriving.setAdapter(mServiceAdministraSAN);//驾证
                                }
                            }
                            if(mServiceAdminisList.size()==2&&mVehicleList.size()==10&&mDrivingList.size()==5){
                                mBrightTV.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.SELECT_SERVICE_ADMINISTRATION, false, 0);
    }

    /**
     * 删除服务
     */
    public void deleteService(String sid, final int service, final int index){
        AjaxParams params=new AjaxParams();
        params.put("sid",sid);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        ToastUtil.toastShow(ServiceAdministrationActivity.this,"删除成功");
                        if(service == 1){
                            mServiceAdminisList.remove(index);
                            mServiceAdministra.notifyDataSetChanged();//刷新适配器
                        }else if(service == 2){
                            mVehicleList.remove(index);
                            mServiceAdministraER.notifyDataSetChanged();
                        }else if(service == 3){
                            mDrivingList.remove(index);
                            mServiceAdministraSAN.notifyDataSetChanged();
                        }
                        deleteDisplay();

                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(ServiceAdministrationActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                    default:
                        break;
                }
            }

        }, params, NetworkUtil.DELETE_SERVICE, false, 0);
    }

    /**
     * 删除完成判断显示隐藏
     */
    public void deleteDisplay(){

        if(mServiceAdminisList.size()==0 && mVehicleList.size() == 0 && mDrivingList.size()==0){
            mScrollGogn.setVisibility(View.GONE);
            mServiceFirst.setVisibility(View.VISIBLE);
            mBrightTV.setVisibility(View.GONE);
        }
        if (mServiceAdminisList.size()>0){
            mIllegalPay.setVisibility(View.VISIBLE);
        }else {
            mIllegalPay.setVisibility(View.GONE);
        }

        if (mVehicleList.size()>0){
            mVehicleService.setVisibility(View.VISIBLE);
        }else {
            mVehicleService.setVisibility(View.GONE);
        }

        if(mDrivingList.size()>0){
            mDrivingService.setVisibility(View.VISIBLE);
        }else {
            mDrivingService.setVisibility(View.GONE);
        }
    }
}
