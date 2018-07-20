package com.chewuwuyou.app.transition_view.activity.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.SendRedBean;
import com.chewuwuyou.app.transition_entity.TransferParamBean;
import com.chewuwuyou.app.transition_presenter.PayPresentner;
import com.chewuwuyou.app.ui.VerifyPayActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.KeyboardUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yuyong on 16/10/17.
 * 支付dialog
 */

public class PayDialogFragment extends DialogFragment {


    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_wrong)
    TextView tvWrong;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.tv_forgetpassword)
    TextView tvForgetpassword;
    @BindView(R.id.view_keyboard)
    KeyboardView viewKeyboard;
    private KeyboardUtil keyBoard;
    private PayPresentner payPresentner;
    /**
     * 发红包参数
     */
    public static final String SEND_RED_SER = "send_red_bean";
    /**
     * 转账参数
     */
    public static final String TRANSFER_SER = "transfer_bean";

    /**
     * 发送红包传参
     *
     * @param sendRedBean
     * @return
     */
    public static PayDialogFragment sendRed(SendRedBean sendRedBean) {
        PayDialogFragment selectFirentDialog = new PayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEND_RED_SER, sendRedBean);
        selectFirentDialog.setArguments(bundle);
        return selectFirentDialog;
    }

    public static PayDialogFragment sendRed(String redId) {
        PayDialogFragment selectFirentDialog = new PayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEND_RED_SER, redId);
        selectFirentDialog.setArguments(bundle);
        return selectFirentDialog;
    }

    public static PayDialogFragment sendRed(RedInit redInit) {
        PayDialogFragment selectFirentDialog = new PayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEND_RED_SER, redInit);
        selectFirentDialog.setArguments(bundle);
        return selectFirentDialog;
    }

    /**
     * 转账传参
     *
     * @param transferParamBean
     * @return
     */
    public static PayDialogFragment transferAccounts(TransferParamBean transferParamBean) {
        PayDialogFragment selectFirentDialog = new PayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSFER_SER, transferParamBean);
        selectFirentDialog.setArguments(bundle);
        return selectFirentDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.mystyle); // 添加动画
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        View view = inflater.inflate(R.layout.fragment_pay_dialog, container);
        ButterKnife.bind(this, view);
        payPresentner = new PayPresentner(getActivity(), PayDialogFragment.this);
        keyBoard = new KeyboardUtil(getActivity(), getActivity(), viewKeyboard, llInput,
                new KeyboardUtil.InputFinishListener() {

                    @Override
                    public void inputHasOver(String pwd) {
                        if (getArguments().getSerializable(SEND_RED_SER) != null) {//发红包付款
                            RedInit redInit = (RedInit) getArguments().getSerializable(SEND_RED_SER);
                            payPresentner.payRedPacket(redInit, pwd);
                        } else {//转账付款
                            TransferParamBean transferParamBean = (TransferParamBean) getArguments().getSerializable(TRANSFER_SER);
                            if (transferParamBean == null) {
                                ToastUtil.toastShow(getActivity(), "参数为空");
                                return;
                            }
                            payPresentner.transfer(transferParamBean, pwd);
                        }
                    }
                });
        keyBoard.showKeyboard();
        llInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoard.showKeyboard();
                return false;
            }
        });
        return view;
    }

    @OnClick({R.id.iv_close, R.id.tv_forgetpassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_forgetpassword:
                Intent intent = new Intent(getActivity(), VerifyPayActivity.class);
                intent.putExtra("clilckType", Constant.CLICK_FORGET_PAYPASS);
                intent.putExtra("Identification", Constant.CLICK_FORGET_PAYPASS);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    /**
     * 发送红包成功
     */
    public void showSuccessView(String msg) {
        //TODO 形成动画
        ToastUtil.toastShow(getActivity(), msg);
    }


    public void finishActivity() {
        getActivity().finish();
    }
}
