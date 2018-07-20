package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Comment;
import com.chewuwuyou.app.utils.ImageUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class EvaluateListAdapter extends BaseAdapter {
	private Context context;
	private List<Comment> data;

	public EvaluateListAdapter(Context context) {
		this.context = context;
		data = new ArrayList<Comment>();
	}

	public EvaluateListAdapter(Context context, List<Comment> data) {
		this.context = context;
		this.data = data;
	}

	public void setData(List<Comment> mComments) {
		data = mComments;
		notifyDataSetChanged();
	}

	public void addData(List<Comment> comments) {
		if (comments != null && data != null)
			data.addAll(comments);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_evaluate_list, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Comment comment = data.get(position);
		vh.con.setText(comment.getContent());
		if (comment.getUrl() != null && !"".equals(comment.getUrl())) {
			ImageUtils.displayImage(comment.getUrl(), vh.portrait, 360, R.drawable.user_fang_icon,
					R.drawable.user_fang_icon);
		}
		int star = comment.getStar();
		vh.ratingbar.setRating(comment.getStar());
		vh.name.setText(comment.getNickName());
		vh.time.setText(comment.getPublishTime().substring(0,19));
		StringBuffer str = new StringBuffer(star);
		str.append(star > 3 ? "好评" : star < 3 ? "差评" : "中评");
		vh.score.setText(str.toString());
		vh.score.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder {
		ImageView portrait;// 头像
		RatingBar ratingbar;// 星
		TextView name;
		TextView score;
		TextView con;
		TextView time;

		public ViewHolder(View convertView) {
			portrait = (ImageView) convertView.findViewById(R.id.business_comment_portrait);
			ratingbar = (RatingBar) convertView.findViewById(R.id.business_ratingbar);
			name = (TextView) convertView.findViewById(R.id.business_name);
			score = (TextView) convertView.findViewById(R.id.business_score);
			con = (TextView) convertView.findViewById(R.id.business_context);
			time = (TextView) convertView.findViewById(R.id.business_time);
			ratingbar.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
		}
	}

}
