package com.chewuwuyou.app.adapter;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.bean.QuanTuItem;
import com.chewuwuyou.app.ui.MyQuanFragment;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.ImageUtils;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class FriendCircleAdapter extends BaseAdapter implements OnClickListener {
	private List<QuanItem> mData;
	private String mAction;
	private String mOtherId;
	private Handler mHandler;
	private int mTuWidth;
	private Activity context;

	public FriendCircleAdapter(Activity context, List<QuanItem> data) {
		this.context = context;
		this.mData = data;
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mTuWidth = dm.widthPixels / 5;
	}

	public void setData(List<QuanItem> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	public void setOtherId(String otherId) {
		this.mOtherId = otherId;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_friend_circle, null);
			vh = new ViewHolder();
			vh.delete = (TextView) convertView.findViewById(R.id.quan_thumbnail_delete_tv);
			vh.picFL = (LinearLayout) convertView.findViewById(R.id.quan_thumbnail_tus_fl);
			vh.picSize = (TextView) convertView.findViewById(R.id.quan_thumbnail_tus_size_tv);
			vh.picTip = (TextView) convertView.findViewById(R.id.quan_thumbnail_content_tv);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final Integer position_integer = Integer.valueOf(position);
		final QuanItem item = mData.get(position);
		vh.picFL.removeAllViews();
		// LinearLayout.LayoutParams fl_params = (LinearLayout.LayoutParams)
		// vh.picFL.getLayoutParams();
		// fl_params.height = mTuWidth;
		// fl_params.width = mTuWidth;

		List<QuanTuItem> tus = item.getTus();
		vh.picFL.setVisibility(tus.size() > 0 ? View.VISIBLE : View.GONE);
		int tuSize = tus.size() > 4 ? 4 : tus.size();
		for (int i = 0; i < tuSize; i++) {
			ImageView tuIV = new ImageView(context);
			tuIV.setAdjustViewBounds(true);
			tuIV.setScaleType(ScaleType.FIT_XY);
			LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(new LayoutParams(mTuWidth, mTuWidth));
			iv_params.setMargins(5, 0, 5, 0);
			tuIV.setLayoutParams(iv_params);
			tuIV.setImageResource(R.drawable.image_default);
			String url = tus.get(i).getUrl();

			ImageUtils.displayImage(url, tuIV, 0, R.drawable.image_default, R.drawable.image_default);

			vh.picFL.addView(tuIV);
		}

		vh.picTip.setText(item.getContent());
		if (item.getTucnt() > 4) {
			vh.picSize.setVisibility(View.VISIBLE);
			vh.picSize.setText("共" + item.getTucnt() + "张");
		} else {
			vh.picSize.setVisibility(View.GONE);
		}
		vh.delete.setOnClickListener(this);
		vh.delete.setVisibility(
				mAction != null && mAction.equals("com.chewuwuyou.app.my_quan") ? View.VISIBLE : View.GONE);
		vh.delete.setTag(position_integer);
		return convertView;
	}

	class ViewHolder {
		public LinearLayout picFL;
		public TextView picTip;
		public TextView picSize;
		public TextView delete;
	}

	@Override
	public void onClick(View v) {
		Message msg;
		Object tag = v.getTag();
		Integer position_integer = null;
		QuanItem quan = null;
		if (tag instanceof Integer)
			position_integer = (Integer) tag;
		if (position_integer != null) {
			quan = (QuanItem) mData.get(position_integer.intValue());
		}
		switch (v.getId()) {
		case R.id.quan_thumbnail_bg_avatar_iv:
			// 删除一个评论
			// 进入到个人详情中
			Intent intent2 = new Intent(context, PersonalHomeActivity2.class);
			intent2.putExtra("userId", mOtherId);
			context.startActivity(intent2);
			break;
		case R.id.quan_thumbnail_delete_tv:
			// 进入到个人详情中
			msg = new Message();
			msg.what = MyQuanFragment.SHOW_DEL_QUAN_DIALOG;
			msg.obj = quan;
			mHandler.sendMessage(msg);
			break;
		default:
			break;
		}
	}

}