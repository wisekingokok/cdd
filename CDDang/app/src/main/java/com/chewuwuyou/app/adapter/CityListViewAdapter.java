package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.LatelyCity;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe:板块Adapter
 * @author:XH
 * @created:
 */
public class CityListViewAdapter extends BaseAdapter {

    private List<LatelyCity> mData;
    private Context mContext;

    public CityListViewAdapter(Context context, List<LatelyCity> mData) {
        this.mContext = context;
        this.mData = mData == null ? new ArrayList<LatelyCity>() : mData;
    }

    public void setData(List<LatelyCity> mData) {
        this.mData = mData == null ? new ArrayList<LatelyCity>() : mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        converView cView = null;
        if (convertView == null) {
            cView = new converView();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.area_list_item, parent, false);
            cView.loCity = (TextView) convertView
                    .findViewById(android.R.id.text1);
            convertView.setTag(cView);
        } else {
            cView = (converView) convertView.getTag();
        }
        LatelyCity info = mData.get(position);
        String district = info.getDistrict() == null ? "" : info.getDistrict();
        String city = info.getCity() == null ? "" : info.getCity();
        String province = info.getProvince() == null ? "" : info.getProvince();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(province.equals(city) ? province : (province + city)).append(district.equals("全部") ? "" : district);

        cView.loCity.setText(stringBuffer.toString());
        return convertView;
    }

    class converView {
        TextView loCity;
    }

}
