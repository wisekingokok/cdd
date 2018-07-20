package com.chewuwuyou.rong.view.chatview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

public class EmojiconGridAdapter extends ArrayAdapter<EaseEmojicon> {

    private EaseEmojicon.Type emojiconType;
    private Context mContext;

    public EmojiconGridAdapter(Context context, int textViewResourceId, List<EaseEmojicon> objects, EaseEmojicon.Type emojiconType) {
        super(context, textViewResourceId, objects);
        this.emojiconType = emojiconType;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (emojiconType == EaseEmojicon.Type.BIG_EXPRESSION) {
                convertView = View.inflate(getContext(), R.layout.ease_row_big_expression, null);
            } else {
                convertView = View.inflate(getContext(), R.layout.ease_row_expression, null);
            }
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
        EaseEmojicon emojicon = getItem(position);
        if (textView != null && emojicon.getName() != null) {
            textView.setText(emojicon.getName());
        }
        if (EaseSmileUtils.DELETE_KEY.equals(emojicon.getEmojiText())) {
            imageView.setImageResource(R.drawable.ease_delete_expression);
        } else {
            if (emojicon.getIcon() != null) {
                try {
                    Bitmap mBitmap = null;
                    if (emojicon.getType() == EaseEmojicon.Type.BIG_EXPRESSION) {
                        mBitmap = BitmapFactory.decodeStream(mContext.getAssets().open("static_emoticons/" + emojicon.getIcon()));
                    } else {
                        mBitmap = BitmapFactory.decodeStream(mContext.getAssets().open("face/" + emojicon.getIcon()));
                    }
                    imageView.setImageBitmap(mBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                imageView.setImageResource(emojicon.getIcon());
            } else if (emojicon.getIconPath() != null) {
                // Glide.with(getContext()).load(emojicon.getIconPath()).placeholder(R.drawable.ease_default_expression).into(imageView);
            }
        }


        return convertView;
    }

}
