package com.chewuwuyou.app.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AreaInfo;
import com.chewuwuyou.app.ui.AreaSelectActivity;
import com.chewuwuyou.app.ui.BaseFragment;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区Fragment
 *
 * @author zengys
 */
@SuppressLint("HandlerLeak")
public class AreaFragment extends BaseFragment implements OnClickListener {

    private static final String ARG_PARAM1 = "";// 请求省地区参数字段
    private static final String PROVINCEID = "provinceId";// 请求市地区参数字段
    private static final String CITYID = "cityId";// 请求区地区参数字段
    private int type;// 地区类型，1表示省(第一级);2表示市(第二级);3表示区(第三级)
    private String mUrl;// 接口地址
    private ListView mRefreshListView;
    private OnFragmentInteractionListener mListener; // Fragment 监听事件
    private AreaAdapter adapter;
    private List<AreaInfo> mData;// list数据
    private String id, name;//请求 ID Name
    private String selectName;


    // 市ID
    // 省地名
    // 城市地名

    public AreaFragment() {
    }

    /**
     * @param type 1 获取全省数据(第一级)；2 获取城市数据(第二级)； 3 获取区数据(第三级)
     * @param id   请求参数
     * @return
     */
    public static AreaFragment newInstance(int type, String id, String name, String selectName) {
        AreaFragment fragment = new AreaFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", id);
        args.putString("name", name);
        args.putString("selectName", selectName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            id = getArguments().getString("id");
            name = getArguments().getString("name");
            selectName = getArguments().getString("selectName");
        }
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area, container, false);
        mRefreshListView = (ListView) view.findViewById(R.id.refresh_list_view);
        AjaxParams params = new AjaxParams();
        switch (type) {
            case 1:
                mUrl = NetworkUtil.GET_ALL_PROVINCE;
                params.put(ARG_PARAM1, id);
                break;
            case 2:
                mUrl = NetworkUtil.GET_CITY_BY_PROVINCE;
                params.put(PROVINCEID, id);
                break;
            case 3:
                mUrl = NetworkUtil.GET_DISTRICT_BY_CITY;
                params.put(CITYID, id);
                break;
        }
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            if (type == 1) {
                                List<AreaInfo> mRefreshData = AreaInfo.parseList(jo.getJSONArray("provinces").toString());
                                mData.addAll(mRefreshData);
                            } else if (type == 2) {
                                List<AreaInfo> mRefreshData = AreaInfo.parseList(jo.getJSONArray("citys").toString());
                                mData.addAll(mRefreshData);
                            } else {
                                List<AreaInfo> mRefreshData = new ArrayList<AreaInfo>();
                                List<AreaInfo> data = AreaInfo.parseList(jo.getJSONArray("districts").toString());
                                // 商家列表UI，第三级城市选择增加“全部”，表示选择全部区。(注：只在商家列表进入城市选择才有这一选项)
                                if (Constant.CITY_CHOOSE == 1||Constant.CITY_SHOW_ALL) {
                                    AreaInfo araInfo = new AreaInfo();
                                    AreaInfo info;
                                    if (data != null && data.size() >= 1) {
                                        info = data.get(0);
                                    } else {
                                        info = AreaSelectActivity.selectInfo;
                                    }
                                    araInfo.setId(0);
                                    araInfo.setDistrictName("全部");
                                    araInfo.setProvinceId(info.getProvinceId());
                                    araInfo.setProvinceName(info.getProvinceName());
                                    araInfo.setCityId(info.getCityId());
                                    araInfo.setCityName(info.getCityName());
                                    mRefreshData.add(araInfo);
                                }
                                mRefreshData.addAll(data);
                                mData.addAll(mRefreshData);
                            }

                            adapter = new AreaAdapter(getActivity(), mData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRefreshListView.setAdapter(adapter);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        break;
                }
            }

        }, params, mUrl, false, 0);
        initEvent();
        return view;
    }

    @Override
    protected void initView() {
        mData = new ArrayList<AreaInfo>();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                AreaInfo mArea = (AreaInfo) parent.getAdapter().getItem(position);
                if (mArea == null) return;
                mListener = (OnFragmentInteractionListener) getActivity();
                if (mListener != null) {
                    mListener.onFragmentInteraction(mArea, "");
                }
                switch (type) {
                    case 1:
                        selectName = mArea.getProvinceName();
                        break;
                    case 2:
                        selectName = mArea.getCityName();
                        break;
                    case 3:
                        selectName = mArea.getDistrictName();
                        break;
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onClick(View arg0) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(AreaInfo areaInfo, String str);
    }

    class AreaAdapter extends BaseAdapter {

        @SuppressWarnings("rawtypes")
        private List list;
        private Context mContext;

        private int lastPosition;

        public AreaAdapter(Context context, List<AreaInfo> list) {
            this.list = list;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.area_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView
                        .findViewById(android.R.id.text1);
                viewHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.imageView);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            AreaInfo item = (AreaInfo) list.get(position);

            /**
             * 根据传来的url值的不同，来显示相应的数据
             */
            String str;
            if (type == 1) {
                str = item.getProvinceName();
            } else if (type == 2) {
                str = item.getCityName();
            } else {
                str = item.getDistrictName();
            }
            viewHolder.textView.setText(str);
            if (selectName != null && selectName.equals(str)) {
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.textView.setTextColor(getResources().getColor(R.color.color_71cbe7));
            } else {
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.textView.setTextColor(getResources().getColor(R.color.black));
            }

            // 设置滑动动画
            if (lastPosition < position && lastPosition != 0) {
                ObjectAnimator
                        .ofFloat(convertView, "translationY",
                                convertView.getHeight() * 2, 0)
                        .setDuration(500).start();
            }
            lastPosition = position;
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            ImageView imageView;
        }
    }

}
