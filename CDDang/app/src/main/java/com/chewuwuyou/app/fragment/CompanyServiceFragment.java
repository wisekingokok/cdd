package com.chewuwuyou.app.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.CompanyService;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Wzdm;
import com.chewuwuyou.app.ui.BusinessPersonalCenterActivity;
import com.chewuwuyou.app.ui.CompanyServiceActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyServiceFragment extends BaseFragment implements CompanyServiceActivity.onAddressSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TYPE = "type";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mType;
    private String mParam2;
    //地区
    private String province;
    private String city;
    private String area;
    private TextView mTextView_empty;
    ListView mListView;
    private List<CompanyService> mList = new ArrayList<>();
    private Adapter mAdapter;


    public void setCity(String privoce, String city, String area) {

        this.province = privoce;
        this.city = city;
        this.area = area;

    }

    public CompanyServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type   Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyServiceFragment newInstance(int type, String param2) {
        CompanyServiceFragment fragment = new CompanyServiceFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initEvent() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_advantage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mListView = (ListView) view.findViewById(R.id.list);
        mTextView_empty = (TextView) view.findViewById(R.id.empty_tv);
        mAdapter = new Adapter(getActivity(), mList, R.layout.item_company_advantage);
        mListView.setAdapter(mAdapter);
        getData();

    }

    ProgressDialog mProgress;

    private void getData() {

        mProgress = ProgressDialog.show(getActivity(), null, "加载中……");

        AjaxParams params = new AjaxParams();
        params.put("type", String.valueOf(mType));
        params.put("province", province);
        params.put("city", city);
        params.put("area", area);
        NetworkUtil.postMulti(NetworkUtil.SJYS, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String mS) {
                super.onSuccess(mS);
                mProgress.cancel();
                MyLog.i(mS);
                try {
                    JSONObject m = new JSONObject(mS);
                    JSONArray mA = m.getJSONArray("data");
                    List<CompanyService> list = CompanyService.parseList(mA.toString());
                    mList.clear();
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    if (mAdapter.getCount() == 0)
                        mTextView_empty.setVisibility(View.VISIBLE);
                    else
                        mTextView_empty.setVisibility(View.GONE);

                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                mProgress.cancel();
                ToastUtil.toastShow(getActivity(), "加载失败");
                if (mAdapter.getCount() == 0)
                    mTextView_empty.setVisibility(View.VISIBLE);
                else
                    mTextView_empty.setVisibility(View.GONE);


            }
        });
//        requestNet(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case Constant.NET_DATA_SUCCESS:
//
//
//                        List<CompanyService> mList = CompanyService.parseList(String.valueOf(msg.obj));
//                        mList.addAll(mList);
//                        mAdapter.notifyDataSetChanged();
//                        if (mList.isEmpty())
//                            //            ToastUtil.toastShow(getActivity(), "无相关数据");
//                            break;
//                    case Constant.NET_DATA_FAIL:
//
//
////                        DataError mDataError = DataError.parse(String.valueOf(msg.obj));
////                        ToastUtil.toastShow(getActivity(), mDataError.getErrorMessage());
//                        ToastUtil.toastShow(getActivity(), msg.obj.toString());
//
//                        break;
//                    default:
//                        break;
//
//
//                }
//
//
//            }
//        }, params, NetworkUtil.SJYS, false, 0);


    }

    public void refresh() {
        getData();
    }

    //重新请求数据并清空 adapter
    public void refreshWithClear() {

        mList.clear();
        mAdapter.notifyDataSetChanged();
        getData();

    }

    @Override
    public void onAddressset(String pro, String city, String area) {

    }


    public static class Adapter extends CommonAdapter<CompanyService> {


        public Adapter(Context context, List<CompanyService> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, final CompanyService mCompanyService, final int position) {

            ImageUtils.displayImage(mCompanyService.getAvatar(), (ImageView) holder.getView(R.id.head), 0);
            holder.setText(R.id.name, mCompanyService.getNickName());
            holder.setText(R.id.sub, mCompanyService.getTitle());
            holder.setText(R.id.content, mCompanyService.getContent());
            holder.setText(R.id.address, mCompanyService.getProvince() + mCompanyService.getCity() + mCompanyService.getArea());
            holder.setText(R.id.time, mCompanyService.getPublishTime());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent mIntent = new Intent(mContext, BusinessPersonalCenterActivity.class);
                    mIntent.putExtra("businessId", mCompanyService.getBusinessId() + "");
                    mIntent.putExtra("position", position + "");
                    mContext.startActivity(mIntent);

                }
            });
        }
    }

}
