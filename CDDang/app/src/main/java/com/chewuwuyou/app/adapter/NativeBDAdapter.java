
package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.chewuwuyou.app.R;

public class NativeBDAdapter extends BaseAdapter {
    List<PoiInfo> mDatas;
    Context mContext;
    //当前坐标点
    double mLat=0;
    double mLng=0;

    public NativeBDAdapter(Context context,List<PoiInfo> datas) {
        mDatas = datas;
        mContext = context;
    }
    public NativeBDAdapter(Context context,List<PoiInfo> datas,double lat,double lng) {
        mDatas = datas;
        mContext = context;
        mLat=lat;
        mLng=lng;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHoder hoder;
        if (convertView == null) {
            hoder = new ViewHoder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.common_shop_item,null);
            hoder.title = (TextView) convertView.findViewById(R.id.shop_name_tv);
            hoder.distance = (TextView) convertView.findViewById(R.id.shop_distance_tv);
            //hoder.telephone = (TextView) convertView.findViewById(R.id.telephone);
            hoder.address = (TextView) convertView.findViewById(R.id.shop_address_tv);
            //hoder.telephone_btn=(Button) convertView.findViewById(R.id.telephone_btn);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        
        try {
            hoder.title.setText(mDatas.get(position).name);//.getJSONObject(position).getString("title"));
            if(mLat!=0&&mLng!=0){
                double distance= getDistance(mLat, mLng, mDatas.get(position).location.latitude, mDatas.get(position).location.longitude);
                hoder.distance.setText(""+(Math.round(distance/100)/10.0)+"公里");
            }else{
                hoder.distance.setText("");
            }
//            if(mDatas.get(position).phoneNum!=null&&!"".equals(mDatas.get(position).phoneNum.trim())){
//                hoder.telephone.setText(""+mDatas.get(position).phoneNum);
//                hoder.telephone_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        Intent phoneIntent = new Intent("android.intent.action.CALL",
//                                Uri.parse("tel:" + hoder.telephone.getText().toString()));
//                        phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(phoneIntent);
//                    }
//                });
//                hoder.telephone_btn.setVisibility(View.VISIBLE);
//            }else{
//                hoder.telephone.setText("");
//                hoder.telephone_btn.setVisibility(View.GONE);
//            }

            hoder.address.setText(mDatas.get(position).address);//.getJSONObject(position).getString("address"));
        } catch (Exception e) {
        }

        return convertView;
    }

    static class ViewHoder {
        TextView title;
        TextView distance;
        //TextView telephone;
        TextView address;
        //Button telephone_btn;
    }
    
    /**
     * google maps的脚本里代码
     */
    private final static double EARTH_RADIUS = 6378.137;

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * getDistance(29.543636,106.752925,29.543699,106.755045)
     * 
     * @param lat1 第一点的纬度坐标
     * @param lng1 第一点的经度坐标
     * @param lat2 第二点的纬度坐标
     * @param lng2 第二点的经度坐标
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;// 公里
        // s = Math.round(s * 10000) / 10000;//转化成米抛一位四舍五入
        return s * 1000;
    }
}
