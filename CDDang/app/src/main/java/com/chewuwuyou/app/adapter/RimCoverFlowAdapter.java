package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Driving;
import com.chewuwuyou.app.extras.FancyCoverFlow;
import com.chewuwuyou.app.extras.FancyCoverFlowAdapter;
import com.chewuwuyou.app.extras.RoundedImageView;

public class RimCoverFlowAdapter extends FancyCoverFlowAdapter {
	private Context mContext;

	public List<Driving> list;
	private int mItemWidth = 0;
	private int mItemHeight = 0;

	/**
	 * 屏幕的密度
	 * 
	 * @param dipValue
	 * @return
	 */
	public int dp2px(float dipValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public RimCoverFlowAdapter(Context context, List<Driving> list) {
		mContext = context;
		this.list = list;
		mItemWidth = dp2px(260);
		mItemHeight = dp2px(280);
	}

	@Override
	public View getCoverFlowItem(int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_fancycoverflow, null);
			convertView.setLayoutParams(new FancyCoverFlow.LayoutParams(
					mItemWidth, mItemHeight));
			holder = new ViewHolder();
			holder.product_name = (RoundedImageView) convertView
					.findViewById(R.id.profile_image);
			holder.drivingTypeName = (TextView) convertView
					.findViewById(R.id.driving_type_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.drivingTypeName.setText(list.get(position % list.size())
				.getDrivingName());
		
		switch (list.get(position % list.size()).getDrivingId()) {
		case 1:
			holder.product_name.setImageDrawable(mContext.getResources().getDrawable(R.drawable.chequan_haoyouche));
			break;
		case 2:
			holder.product_name.setImageDrawable(mContext.getResources().getDrawable(R.drawable.chequan_cheyouhuodong));
			break;
		case 3:
			holder.product_name.setImageDrawable(mContext.getResources().getDrawable(R.drawable.chequan_haoyoutucao));
			break;
		case 4:
			holder.product_name.setImageDrawable(mContext.getResources().getDrawable(R.drawable.chequan_fujincheyou));
			break;
		case 5:
			holder.product_name.setImageDrawable(mContext.getResources().getDrawable(R.drawable.chequan_tongxiche));
			
			break;

		default:
			break;
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Driving getItem(int i) {
		return list.get(i % list.size());
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	class ViewHolder {
		RoundedImageView product_name;
		TextView drivingTypeName;
	}
}
