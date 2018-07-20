package pl.droidsonroids.gif;

import java.io.IOException;

import com.chewuwuyou.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class GIFAdapter extends BaseAdapter {
	private Context context;

	public GIFAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 100;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder vh;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_gif, null);
			vh = new ViewHolder(view);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		try {
			vh.gifTextView
					.setImageDrawable(new GifDrawable(context.getAssets(), "emoticons/emotion" + i % 17 + ".gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return view;
	}

	static class ViewHolder {
		GifImageView gifTextView;

		ViewHolder(View view) {
			gifTextView = (GifImageView) view.findViewById(R.id.gifTextView);
		}
	}
}
