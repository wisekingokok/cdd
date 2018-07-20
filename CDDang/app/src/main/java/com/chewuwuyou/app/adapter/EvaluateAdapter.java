package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Comment;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.MyLog;

/**
 * @describe:评价adapter
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-1下午5:53:06
 */
public class EvaluateAdapter extends BaseAdapter {
	private Context mContext;
	private List<Comment> mComments;
	private LayoutInflater layoutInflater;
	private ListItemView listItemView = null;

	public EvaluateAdapter(Context context, List<Comment> mComments) {
		this.mContext = context;
		this.mComments = mComments;
		layoutInflater = LayoutInflater.from(mContext);
	}

	public final class ListItemView {
		TextView mEvaluate;// 评价
		ImageView mHeadImg;// 头像
		RatingBar mRatingBar;// 评分
		TextView mUserName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(R.layout.user_evaluate_item,
					null);
			listItemView.mHeadImg = (ImageView) convertView
					.findViewById(R.id.user_head_img);
			listItemView.mRatingBar = (RatingBar) convertView
					.findViewById(R.id.rating_bar);
			listItemView.mEvaluate = (TextView) convertView
					.findViewById(R.id.user_evaluate);
			listItemView.mUserName = (TextView) convertView
					.findViewById(R.id.user_name);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		Comment comment = mComments.get(position);
		listItemView.mEvaluate.setText(comment.getContent());
		MyLog.i("YUY", "====评论用户的头像地址====" + mComments.get(position).getUrl()
				);
		if (comment.getUrl() != null && !"".equals(comment.getUrl())) {

			ImageUtils.displayImage(comment.getUrl(), listItemView.mHeadImg,
					360, R.drawable.user_fang_icon, R.drawable.user_fang_icon);
		}
		listItemView.mRatingBar.setRating(comment.getStar());
		listItemView.mUserName.setText(comment.getNickName());
		return convertView;

	}

}
