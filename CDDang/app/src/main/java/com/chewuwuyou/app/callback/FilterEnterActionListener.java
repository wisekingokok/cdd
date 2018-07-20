package com.chewuwuyou.app.callback;

import android.view.KeyEvent;
import android.widget.TextView;

/**
 * 禁止换行
 * Created by Administrator on 2016/8/1 0001.
 */
public class FilterEnterActionListener implements TextView.OnEditorActionListener {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }
}
