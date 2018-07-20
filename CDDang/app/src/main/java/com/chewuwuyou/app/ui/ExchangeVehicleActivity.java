//
//package com.chewuwuyou.app.ui;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.baidu.mobstat.StatService;
//import com.chewuwuyou.app.R;
//import com.chewuwuyou.app.adapter.ExchangeVehicleAdapter;
//import com.chewuwuyou.app.bean.CarModel;
//import com.chewuwuyou.app.bean.Vehicle;
//import com.chewuwuyou.app.utils.CacheTools;
//import com.chewuwuyou.app.utils.Constant;
//import com.chewuwuyou.app.utils.NetworkUtil;
//
//import net.tsz.afinal.annotation.view.ViewInject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExchangeVehicleActivity extends BaseActivity {
//    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
//    private ImageButton mBackBtn;
//    @ViewInject(id = R.id.sub_header_bar_tv)
//    private TextView mHeaderTV;
//    @ViewInject(id = R.id.empty_tv)
//    private TextView mEmptyText;
//    private List<Vehicle> vehicles;
//    private ListView mList;
//    private List<String> mPlateNumberList;
//    private int vehicleId;
//    // 存入相应车辆的品牌和车型的图片
//    private List<String> mVehicleImageList;
//    private List<CarModel> carModels;
//    private ExchangeVehicleAdapter adapter;
//    private ProgressDialog mProgressDialog;
////    public static final String SER_KEY = "com.chewuwuyou.app.bean.vehicle.ser";
//    private int flag;
//    private Handler mHandle = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//                case Constant.NET_DATA_SUCCESS:
//                    vehicles = Vehicle.parseList(msg.obj.toString());
//                    if (vehicles != null && !vehicles.isEmpty()) {
//                        mEmptyText.setVisibility(View.GONE);
//                    } else {
//                        mEmptyText.setVisibility(View.VISIBLE);
//                    }
//                    if (vehicles != null) {
//                        List<CarModel> carModels = CarModel.parseBrands(getFromAssets("mobile_models"));
//                        mVehicleImageList = new ArrayList<String>();
//                        for (int i = 0, size = vehicles.size(); i < size; i++) {
//                            if (mVehicleImageList != null) {
//                                mVehicleImageList.add(getVehicleImage(
//                                        vehicles.get(i).getBrand(), vehicles.get(i)
//                                                .getModelNumber(),carModels));
//                            }
//
//                        }
//
//                        adapter = new ExchangeVehicleAdapter(
//                                ExchangeVehicleActivity.this, vehicles,
//                                mVehicleImageList);
//                        mList.setAdapter(adapter);
//                        mList.setOnItemClickListener(new OnItemClickListener() {
//
//                            @Override
//                            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int arg2, long arg3) {
//
//                                // Intent intent = new Intent();
//                                // intent.putExtra("vehicleId",
//                                // vehicles.get(arg2).getId());
//                                // intent.putExtra("plateNum",
//                                // vehicles.get(arg2).getPlateNumber());
//                                // intent.putExtra(
//                                // "imgUrl",
//                                // getVehicleImage(vehicles.get(arg2).getBrand(),
//                                // vehicles
//                                // .get(arg2).getModelNumber()));
//                                // setResult(40, intent);
//                                // finishActivity();
//                                try {
//                                    CacheTools.saveObject(CacheTools.getUserData("telephone"),
//                                            vehicles.get(arg2));
//                                } catch (IOException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                finishActivity();
//
//                                // else if (flag == 3) {
//                                // Intent intent = new Intent();
//                                // Bundle bundle = new Bundle();
//                                // bundle.putSerializable(Constant.VEHICLE_SER,
//                                // vehicles.get(arg2));
//                                // intent.putExtras(bundle);
//                                // setResult(35, intent);
//                                // finishActivity();
//                                //
//                                // } else {
//                                // Intent intent = new Intent(
//                                // ExchangeVehicleActivity.this,
//                                // MainActivity.class);
//                                // CacheTools.clearUserData("violationCount");//
//                                // 清空上一次车辆查询的数据缓存
//                                // CacheTools.clearUserData("violationMoney");
//                                // startActivity(intent);
//                                //
//                                // }
//
//                            }
//                        });
//                    }
//                    break;
//
//            }
//            super.handleMessage(msg);
//        }
//
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.exchange_vehicle_ac);
//        initView();
//    }
//
//    /**
//     * 初始化布局
//     */
//    private void initView() {
//
//        mList = (ListView) findViewById(R.id.vehicle_lv);
//        mHeaderTV.setText(R.string.vip_apply_choose_vehicle);
//        // 返回
//        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                finishActivity();
//            }
//        });
//        requestNet(mHandle, null, NetworkUtil.VEHICLE_MANAGE, true, 0);
//
//    }
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		StatService.onPause(ExchangeVehicleActivity.this);
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		StatService.onResume(ExchangeVehicleActivity.this);
//	}
//}
