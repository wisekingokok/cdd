package com.chewuwuyou.app.widget;

import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:自定义的textview，用来处理复制粘贴的消息
 * @author:yuyong
 * @date:2015-4-21下午4:48:09
 * @version:1.2.1
 */
public class PasteEditText extends EditText {
	@SuppressWarnings("unused")
	private Context mContext;

	public PasteEditText(Context context) {
		super(context);
		this.mContext = context;
	}

	public PasteEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public PasteEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onTextContextMenuItem(int id) {
		if (id == android.R.id.paste) {
			ClipboardManager clip = (ClipboardManager) getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			if (clip == null || clip.getText() == null) {
				return false;
			}
			// String text = clip.getText().toString();
			// if (text.startsWith(ChatActivity.COPY_IMAGE)) {
			// // intent.setDataAndType(Uri.fromFile(new
			// // File("/sdcard/mn1.jpg")), "image/*");
			// text = text.replace(ChatActivity.COPY_IMAGE, "");
			// Intent intent = new Intent(context, AlertDialog.class);
			// String str = context.getResources().getString(
			// R.string.Send_the_following_pictures);
			// intent.putExtra("title", str);
			// intent.putExtra("forwardImage", text);
			// intent.putExtra("cancel", true);
			// ((Activity) context).startActivityForResult(intent,
			// ChatActivity.REQUEST_CODE_COPY_AND_PASTE);
			// // clip.setText("");
			// }
		}
		return super.onTextContextMenuItem(id);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		// if (!TextUtils.isEmpty(text)
		// && text.toString().startsWith(ChatActivity.COPY_IMAGE)) {
		// setText("");
		// }
		// else if(!TextUtils.isEmpty(text)){
		// setText(SmileUtils.getSmiledText(getContext(),
		// text),BufferType.SPANNABLE);
		// }
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

}
