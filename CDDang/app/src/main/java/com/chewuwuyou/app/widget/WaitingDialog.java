
package com.chewuwuyou.app.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class WaitingDialog extends ProgressDialog implements OnKeyListener {

    private Context mContext;
    private View mView;
    private ImageView mLoadingIV;
    private Animation operatingAnim1;
    public WaitingDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public WaitingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    protected WaitingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context);
        this.mContext = context;
    }

    public void init() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        if(mContext==null)
        	return;
        super.show();
        if (mView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mView = inflater.inflate(R.layout.loading_ac, null);
            mLoadingIV=(ImageView) mView.findViewById(R.id.rotate_iv1);
            operatingAnim1 = AnimationUtils.loadAnimation(mContext, R.anim.tip1);
    		LinearInterpolator lin = new LinearInterpolator();
    		operatingAnim1.setInterpolator(lin);
    		operatingAnim1.setDuration(2000);
    		if (operatingAnim1 != null) {
    			mLoadingIV.startAnimation(operatingAnim1);
    		} else {
    			mLoadingIV.clearAnimation();
    		}
            init();
        }
        setContentView(mView);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            if (mContext != null) {
                ((Activity) mContext).finish();
            }
        }
        return false;
    }

    public void show(String msg) {
    	
        super.show();
        setCancelable(false);
        
        if (mView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mView = inflater.inflate(R.layout.waiting_dialog, null);
            ((TextView) mView.findViewById(R.id.waiting_dialog_tv)).setText(msg);
            init();
        }
        setContentView(mView);
    }
}
