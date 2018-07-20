
package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.SystemNotification;

/**
 * 通知适配器
 * 
 * @author Administrator
 */
public class SysNotificationAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<SystemNotification> mSNotifications;

    public class ListItemView {
        TextView notificationTitle; // 通知标题
        TextView notificationContent;// 通知内容
        TextView notificationIsRead;// 通知是否已读
    }

    public SysNotificationAdapter(Context context,
            List<SystemNotification> mSNotifications) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.mSNotifications = mSNotifications;
    }

    @Override
    public int getCount() {
        return mSNotifications.size();
    }

    @Override
    public Object getItem(int position) {
        return mSNotifications.get(position);

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

        listItemView.notificationTitle.setText(mSNotifications.get(position)
                .getTitle());
        listItemView.notificationContent.setText(mSNotifications.get(position)
                .getContent());
        listItemView.notificationIsRead.setText(mSNotifications.get(position).getServiceType());

        return convertView;
    }

}
