package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ServiceTypeGridAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener;
import com.chewuwuyou.app.widget.PullToRefreshGridView;

/**
 * 服务类主界面 (包括 车辆服务、驾证服务、违章代缴)
 * <p/>
 * 因三个UI界面相似，这里就只使用同一个类来创建。 进入的时候，传"serverType",取相应的值来判别，1为违章代缴、2为车辆服务、3驾证服务
 *
 * @author zengys
 */

public class ServiceTypeActivity extends CDDBaseActivity implements
        OnClickListener, OnRefreshListener<GridView> {

    private TextView mTitle;// 标题
    private ImageButton mBackImage;// 返回
    private PullToRefreshGridView mPullRefreshGridView;
    private ImageView mTopLeftImage;// 顶部左边的服务图
    private ImageView mTopRightImage;// 顶部右边的服务图
    private int mServiceType;// 总服务类型,共分为车辆服务、驾证服务、违章代缴
    private ServiceTypeGridAdapter mAdapter;
    private List<ServicePro> mAllServicePros;// 所有服务项目
    private GridView mServiceGv;
    private JSONArray mJsonArray;//存放资料
    private LinearLayout mNetworkRequest;
    private boolean mIsSetEmptyTV = false;

    private LinearLayout mNetworkAbnormalLayout;//暂无订单信息或网络异常
    private TextView mNetworkAgain;//点击重新加载


    //	private String jsonObject;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    mNetworkRequest.setVisibility(View.GONE);
                    mNetworkAbnormalLayout.setVisibility(View.GONE);
                    mPullRefreshGridView.onRefreshComplete();
                    mAllServicePros.clear();
                    try {
                        JSONObject jo = new JSONObject(msg.obj.toString());
                        mJsonArray = jo.getJSONArray("projects");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            mAllServicePros.add(new ServicePro(
                                    Integer.parseInt(mJsonArray.getJSONObject(i).getString("id")),
                                    Double.parseDouble(mJsonArray.getJSONObject(i).getString("fees")),
                                    Integer.parseInt(mJsonArray.getJSONObject(i).getString("type")),
                                    mJsonArray.getJSONObject(i).getString("projectName"),
                                    Integer.parseInt(mJsonArray.getJSONObject(i).getString("projectNum")),
                                    mJsonArray.getJSONObject(i).getString("projectImg"),
                                    mJsonArray.getJSONObject(i).getString("serviceDesc"),
                                    String.valueOf(mJsonArray.getJSONObject(i).getJSONArray("serviceFolder"))));
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case Constant.NET_DATA_FAIL:
                    mNetworkRequest.setVisibility(View.GONE);
                    mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                    ToastUtil.toastShow(ServiceTypeActivity.this,((DataError) msg.obj).getErrorMessage());
                    break;
                default:
                    mNetworkRequest.setVisibility(View.GONE);
                    mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
                    mPullRefreshGridView.onRefreshComplete();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_type_layout);

        mServiceType = getIntent().getIntExtra("serviceType", 0); // 获取传过来的serviceType



        initView();
        initData();
        initEvent();
        getProData();
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.sub_header_bar_left_ibtn://返回上一页
                finish();
                break;
            case R.id.network_again://重新加载
                mNetworkRequest.setVisibility(View.VISIBLE);
                mNetworkAbnormalLayout.setVisibility(View.GONE);
                getProData();
                break;

            case R.id.service_top_left_imaview:
                Intent serviceFolderIntent = new Intent(ServiceTypeActivity.this,
                        ServiceFolderActivity.class);
                serviceFolderIntent.putExtra("serviceType", mServiceType);
                startActivity(serviceFolderIntent);
                break;
            case R.id.service_top_right_imaview:
                Intent serviceFlowIntent = new Intent(ServiceTypeActivity.this,
                        ServiceFlowActivity.class);
                serviceFlowIntent.putExtra("serviceType", mServiceType);
                startActivity(serviceFlowIntent);
                break;
            default:
                break;
        }

    }

    @Override
    protected void initView() {
        mNetworkAbnormalLayout = (LinearLayout) findViewById(R.id.network_abnormal_layout);
        mNetworkAgain = (TextView) findViewById(R.id.network_again);
        mTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackImage = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTopLeftImage = (ImageView) findViewById(R.id.service_top_left_imaview);
        mTopRightImage = (ImageView) findViewById(R.id.service_top_right_imaview);
        mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.bankuai_gv);
        mPullRefreshGridView.setOnRefreshListener(this);
        mServiceGv = mPullRefreshGridView.getRefreshableView();
        mAllServicePros = new ArrayList<ServicePro>();
        mNetworkRequest = (LinearLayout) findViewById(R.id.network_request);


        mAdapter = new ServiceTypeGridAdapter(mAllServicePros, ServiceTypeActivity.this);
        mServiceGv.setAdapter(mAdapter);

        updateUI(mServiceType);// 更新UI
    }

    @Override
    protected void initData() {

        mServiceGv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(ServiceTypeActivity.this,VehicleServiceActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("serviceType", mServiceType);
                bundle.putSerializable("servicedata", mAllServicePros.get(arg2));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initEvent() {
        mBackImage.setOnClickListener(this);
        mTopLeftImage.setOnClickListener(this);
        mTopRightImage.setOnClickListener(this);
        mPullRefreshGridView.setOnRefreshListener(this);
        mNetworkAgain.setOnClickListener(this);
    }

    /**
     * 更新UI
     *
     * @param servicetype
     */
    private void updateUI(int servicetype) {
        switch (servicetype) {
            case 1:
                mTitle.setText("违章代缴");
                break;
            case 2:
                mTitle.setText("车辆服务");
                break;
            case 3:
                mTitle.setText("驾证服务");
                break;

            default:
                break;
        }

    }

    /**
     * 请求服务器，获取数据
     */
    private void getProData() {
        AjaxParams params = new AjaxParams();
        params.put("type", String.valueOf(mServiceType));
        mPullRefreshGridView.setRefreshing();
        requestNet(mHandler, params, NetworkUtil.GET_ALL_PRO, false, 1);

    }

    @Override
    public void onRefresh(PullToRefreshBase<GridView> refreshView) {
        getProData();
    }

}
