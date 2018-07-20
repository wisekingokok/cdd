
package com.chewuwuyou.app.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 通知适配器
 * 
 * @author Administrator
 */
public class NotificationAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private JSONArray notiJArray;
    private JSONObject jo;

    public class ListItemView {
        TextView notificationTitle; // 通知标题
        TextView notificationContent;// 通知内容
        TextView notificationIsRead;// 通知是否已读
    }

    public NotificationAdapter(Context context,
            JSONArray notiJArray) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.notiJArray = notiJArray;
    }

    @Override
    public int getCount() {
        return notiJArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            jo = notiJArray.getJSONObject(position);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = layoutInflater.inflate(R.layout.notification_ac_item,
                    null);

            listItemView.notificationTitle = (TextView) convertView
                    .findViewById(R.id.notification_title);
            listItemView.notificationContent = (TextView) convertView
                    .findViewById(R.id.notification_content);
            listItemView.notificationIsRead = (TextView) convertView
                    .findViewById(R.id.notification_is_read);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        try {
            listItemView.notificationTitle.setText(notiJArray.getJSONObject(position)
                    .getString("title"));
            if (notiJArray.getJSONObject(position).getInt("type") == 4) {
                listItemView.notificationContent.setText("点击查看提醒详情");
            } else {
                listItemView.notificationContent.setText(notiJArray.getJSONObject(position)
                        .getString("content"));
            }

            if (notiJArray.getJSONObject(position).getInt("status") == 0) {
                listItemView.notificationIsRead.setText("未读");
                listItemView.notificationIsRead.setTextColor(Color.parseColor("#f81953"));
            } else {
                listItemView.notificationIsRead.setText("已读");
                listItemView.notificationIsRead.setTextColor(Color.parseColor("#66CD00"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

}
