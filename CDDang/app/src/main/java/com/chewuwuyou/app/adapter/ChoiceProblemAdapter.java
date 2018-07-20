package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:板块Adapter
 * @author:XH
 * @version
 * @created:
 */
public class ChoiceProblemAdapter extends BaseAdapter {

	private List<String> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    
	public ChoiceProblemAdapter(Context context, List<String> mList) {
		this.mContext = context;
		this.mList = mList;
		this.mInflater = LayoutInflater.from(mContext);
	}


	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ControlView view = null;
		if (convertView == null) {
			view = new ControlView();
			convertView = mInflater.inflate(R.layout.choice_problem_item, null);
			view.choicePrompt = (TextView) convertView.findViewById(R.id.prompt);
			convertView.setTag(view);
		} else {
			view = (ControlView) convertView.getTag();
		}
		view.choicePrompt.setText(mList.get(position));
		return convertView;
	}
	
	public class ControlView{
		TextView choicePrompt;
	}

}
