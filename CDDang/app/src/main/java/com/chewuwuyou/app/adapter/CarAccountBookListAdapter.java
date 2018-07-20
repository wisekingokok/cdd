
package com.chewuwuyou.app.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.DBHelper.DBFIELD;

/**
 * 爱车账本列表格式
 * 
 * @author Administrator
 */
public class CarAccountBookListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    // 爱车账本数据
    private List<Map<String, String>> carAccountBook;

    public List<Map<String, String>> getCarAccountBook() {
        return carAccountBook;
    }

    public void setCarAccountBook(List<Map<String, String>> carAccountBook) {
        this.carAccountBook = carAccountBook;
    }

    public final class ListItemView { // 自定义控件集合
        public TextView recordType;
        public TextView recordTime;
        public TextView recordMoney;

    }

    public CarAccountBookListAdapter(Context context,
            List<Map<String, String>> carAccountBook) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);

        this.carAccountBook = carAccountBook;
    }

    @Override
    public int getCount() {
        return carAccountBook.size();
    }

    @Override
    public Object getItem(int position) {
        return carAccountBook.get(position);
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
            convertView = layoutInflater.inflate(
                    R.layout.car_account_book_list_item, null);

            listItemView.recordType = (TextView) convertView
                    .findViewById(R.id.record_type);
            listItemView.recordTime = (TextView) convertView
                    .findViewById(R.id.record_time);
            listItemView.recordMoney = (TextView) convertView
                    .findViewById(R.id.record_money);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        listItemView.recordType.setText(carAccountBook.get(position).get(DBFIELD.CATEGORY));// cate
        listItemView.recordTime.setText(carAccountBook.get(position).get(DBFIELD.TIME));// time
        //changed by xh start
        //listItemView.recordMoney.setText(carAccountBook.get(position).get(DBFIELD.MONEY) + "元");// money
        listItemView.recordMoney.setText(context.getString(R.string.price_contain_suffix, carAccountBook.get(position).get(DBFIELD.MONEY)));// money
        //changed by xh end
        return convertView;
    }

}
