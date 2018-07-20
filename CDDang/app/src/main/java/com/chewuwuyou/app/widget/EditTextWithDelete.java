package com.chewuwuyou.app.widget;

/**
 * 类名称：EditTextWithDelete
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2014-12-11 上午11:47:27
 * 修改人：Administrator
 * 修改时间：2014-12-11 上午11:47:27
 * 修改备注：
 *
 * @version
 */

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.RemoveEmojiWatcher;

public class EditTextWithDelete extends EditText implements OnFocusChangeListener {
    private Drawable mCompoundDrawableLeft;
    private Drawable mCompoundDrawableRight;

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;


    public EditTextWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initEditText();
        init();

    }

    private void init() {
        // 获取图片资源
        Drawable[] d = getCompoundDrawables();
        mCompoundDrawableLeft = d[0];
        mCompoundDrawableRight = d[2];

        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    /**
     * 设置删除图片
     */
    private void setDrawable() {
        if (length() == 0) {
            setCompoundDrawables(mCompoundDrawableLeft, null, null, null);
        } else {
            setCompoundDrawables(mCompoundDrawableLeft, null, mCompoundDrawableRight, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCompoundDrawableRight != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            // 判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) && (x < (getWidth() - getPaddingRight()));
            // 获取删除图标的边界，返回一个Rect对象
            Rect rect = mCompoundDrawableRight.getBounds();
            // 获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            // 计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            // 判断触摸点是否在竖直范围内(可能会有点误差)
            // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerWidth && isInnerHeight) {
                setText("");
            }

        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setDrawable();
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private Context mContext;

    public EditTextWithDelete(Context context) {
        super(context);
        this.mContext = context;
        initEditText();
    }


    public EditTextWithDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initEditText();
    }

    // 初始化edittext 控件
    private void initEditText() {
        addTextChangedListener( new RemoveEmojiWatcher(this, new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
//                if (!resetText) {
//                    cursorPos = getSelectionEnd();
//                    // 这里用s.toString()而不直接用s是因为如果用s，
//                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
//                    // inputAfterText也就改变了，那么表情过滤就失败了
//                    inputAfterText = s.toString();
//                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!resetText) {
//                    if (count >= 2) {//表情符号的字符长度最小为2
//                        Log.e("YUY", "-----" + cursorPos + " " + cursorPos + count);
//                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
//                        if (containsEmoji(input.toString())) {
//                            resetText = true;
//                            Toast.makeText(mContext, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
//                            //是表情符号就将文本还原为输入表情符号之前的内容
//                            setText(inputAfterText);
//                            CharSequence text = getText();
//                            if (text instanceof Spannable) {
//                                Spannable spanText = (Spannable) text;
//                                Selection.setSelection(spanText, text.length());
//                            }
//                        }
//                    }
//                } else {
//                    resetText = false;
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

}
