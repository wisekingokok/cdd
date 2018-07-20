package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BlackList;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.ImageUtils;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by CLOUD on 2016/8/10.
 */
public class BlackListAdapter extends BaseAdapter {

    private static final int TYPE_TAG = 1;
    private static final int TYPE_CONTENT = 2;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BlackList> mLists;

    public BlackListAdapter(List<BlackList> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListViewa
     *
     * @param list
     */
    public void updateListView(List<BlackList> list) {
        this.mLists.clear();
        mLists.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        final BlackList mBlackList = mLists.get(position);
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_blacklist, null);
            mViewHolder.mCircleImageView = (CircleImageView) view.findViewById(R.id.item_blacklist_head);
            mViewHolder.mTextView_name = (TextView) view.findViewById(R.id.item_black_name);
            mViewHolder.mTextView_delete = (TextView) view.findViewById(R.id.delete);
//            mViewHolder.mTextView_ddh = (TextView) view.findViewById(R.id.item_black_number);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.mTextView_name.setText(mBlackList.getNickName());
//        mViewHolder.mTextView_ddh.setText("ID:" + mBlackList.getDangDangHao());
        ImageUtils.displayImage(mBlackList.getHeadImage(), mViewHolder.mCircleImageView, 0);

        mViewHolder.mTextView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnDeleteListener != null) {
                    mOnDeleteListener.onDelete(position);

                }


            }
        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mIntent = new Intent(mContext, PersonalHomeActivity2.class);
//                mIntent.putExtra("userId", mLists.get(position).getOther());
//                mContext.startActivity(mIntent);
//            }
//        });

        return view;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mLists.get(position).getSortLetters().charAt(0);
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mLists.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    public static class ViewHolder {
        CircleImageView mCircleImageView;
        TextView mTextView_name;
        TextView mTextView_delete;
//        TextView mTextView_ddh;

    }


    public interface onDeleteListener {

        public void onDelete(int posion);

    }


    public onDeleteListener mOnDeleteListener;

    public void setOnDeleteListener(onDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }
}
