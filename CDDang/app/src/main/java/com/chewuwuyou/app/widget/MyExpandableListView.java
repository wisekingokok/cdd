package com.chewuwuyou.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:自定义方便计算其高度
 * @author:yuyong
 * @date:2015-4-2上午11:52:26
 * @version:1.2.1
 */
public class MyExpandableListView extends ExpandableListView {

    /**
     * @param context
     * @param attrs
     */
    public MyExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
