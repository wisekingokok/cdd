package com.chewuwuyou.app.transition_view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.TransferParamBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_presenter.TransferAccountPresenter;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;
import com.chewuwuyou.app.transition_view.activity.fragment.PayDialogFragment;
import com.chewuwuyou.app.transition_view.fragment.FriendTipDialogFragment;
import com.chewuwuyou.app.transition_view.fragment.TipDialogFragment;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.GlideRoundTransform;
import com.chewuwuyou.app.utils.RemoveEmojiWatcher;
import com.chewuwuyou.rong.bean.RongUserBean;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


/**
 * 转账界面
 * Created by xxy on 2016/10/10 0010.
 */

public class TransferAccountActivity extends BaseTitleActivity {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.procedureFee)
    TextView procedureFee;
    @BindView(R.id.toMoney)
    TextView toMoney;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.payType)
    TextView payType;
    @BindView(R.id.changeType)
    TextView changeType;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.textView)
    TextView textView;

    TransferAccountPresenter presenter = null;
    ProgressDialog progressDialog;
    private static final String ID_KEY = "id_key";
    private String userId;//收账人的ID
    private TipDialogFragment dialog;
    private FriendTipDialogFragment friendDialog;

    public static void start(Context context, String userId) {
        Intent intent = new Intent(context, TransferAccountActivity.class);
        intent.putExtra(ID_KEY, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getStringExtra(ID_KEY);
        setContentView(R.layout.activity_transfer_account);
        ButterKnife.bind(this);
        setBarTitle("转账");
        setLeftBarBtnImage(R.drawable.backbutton);
        setRightBarTV("转账说明");
        setRefreshingEnabled(false);
        changeType.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        changeType.getPaint().setAntiAlias(true);//抗锯齿
        remark.addTextChangedListener(new RemoveEmojiWatcher(remark));
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        presenter = new TransferAccountPresenter(this, this);
        presenter.getUserById(userId);
        presenter.getTodayQuota(UserBean.getInstall(this).getId());
        presenter.getLatestTransferRule();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
        }
        progressDialog.show();
    }

    public void disProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void setUserIcon(RongUserBean rongUserBean) {
        Glide.with(this).load(rongUserBean.getHeadUrl()).centerCrop().error(R.drawable.user_fang_icon).placeholder(R.drawable.user_fang_icon).transform(new GlideRoundTransform(this, 8)).crossFade().into(icon);
        name.setText(CarFriendQuanUtils.showCarFriendName(rongUserBean.getNoteName(), rongUserBean.getNick(), rongUserBean.getUserName()));
    }

    public void toPay() {
        TransferParamBean transferParamBean = new TransferParamBean();
        transferParamBean.setAmount(money.getText().toString().trim());//金额
        transferParamBean.setLeaveMessage(remark.getText().toString().trim());//转账说明
        transferParamBean.setMeAccId(UserBean.getInstall(this).getId());//我的用户ID
        transferParamBean.setOtherAccId(userId);//目标用户ID
        PayDialogFragment.transferAccounts(transferParamBean).show(getSupportFragmentManager(), "f");
    }

    public void showTipDialog(String tip) {
        if (dialog == null)
            dialog = new TipDialogFragment();
        dialog.setMsg(tip);
        dialog.show(getSupportFragmentManager(), "f");
    }

    public void showFriendTipDialog(String title, String tip) {
        if (friendDialog == null)
            friendDialog = new FriendTipDialogFragment();
        friendDialog.setMsg(tip);
        friendDialog.setTitle(title);
        friendDialog.setOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPay();
            }
        });
        friendDialog.show(getSupportFragmentManager(), "f");
    }

    public void disFriendTipDialog() {
        if (friendDialog != null)
            friendDialog.dismiss();
    }

    public void disTipDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag) {
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
            case TITLE_TAG_RIGHT_TV:
                startActivity(new Intent(this, ExplainActivity.class));
                break;
        }
    }

    @OnClick({R.id.ok, R.id.changeType})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                presenter.canRate(Double.valueOf(money.getText().toString().trim()));
                break;
            case R.id.changeType:
                break;
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            scrollView.scrollTo(0, textView.getTop());
        }
    };

    @OnTextChanged(value = R.id.money, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void textChanged(Editable s) {
        String str = s.toString();
        if (TextUtils.isEmpty(str)) {
            ok.setEnabled(false);
            setRateFee("0.00", "0.00");
            return;
        }
        if (str.equals(".")) {
            money.setText("");
            ok.setEnabled(false);
            setRateFee("0.00", "0.00");
            return;
        }
        if (str.contains(".")) {
            String[] strings = str.split("[.]");
            if (strings.length == 2 && strings[1].length() > 2) {
                money.setText(strings[0] + "." + strings[1].substring(0, 2));
                Selection.setSelection(s, s.length());
                return;
            }
        }
        if (Double.valueOf(money.getText().toString().trim()) > 0)
            ok.setEnabled(true);
        Selection.setSelection(s, s.length());
        presenter.getTransferRateFee(Double.valueOf(money.getText().toString().trim()));
    }

    public void setRateFee(String RateFee, String toMoneyCount) {
        procedureFee.setText(RateFee);
        toMoney.setText(new DecimalFormat("0.00").format(Double.valueOf(toMoneyCount)));
    }
}
