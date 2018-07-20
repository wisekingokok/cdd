package com.chewuwuyou.app.transition_view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.chewuwuyou.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xxy on 2016/10/19 0019.
 */

public class TipDialogFragment extends DialogFragment {

    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.ok)
    Button ok;
    View.OnClickListener onClickListener;
    private String msgStr;

    public static TipDialogFragment init(String msg) {
        TipDialogFragment tipDialogFragment = new TipDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        tipDialogFragment.setArguments(bundle);
        return tipDialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tip_dialog, container, false);
        ButterKnife.bind(this, view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        msg.setText(msgStr);
        return view;
    }

    @OnClick({R.id.msg, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                if (onClickListener != null)
                    onClickListener.onClick(view);
                dismiss();
                break;
            case R.id.msg:

                break;
        }
    }

    public void setMsg(String msg_) {
        this.msgStr = msg_;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
}
