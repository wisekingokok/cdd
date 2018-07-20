package com.chewuwuyou.app.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.Dqdm;
import com.chewuwuyou.app.bean.Wzdm;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.http.AjaxParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WzdmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WzdmFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private SearchView mWzdbSearchView;
    private ListView mWzdmLV;
    private WzdmAdatper mWzdmAdatper;
    private List<Wzdm> mData = new ArrayList<>();


    public WzdmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WzdmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WzdmFragment newInstance(String param1, String param2) {
        WzdmFragment fragment = new WzdmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static WzdmFragment newInstance() {
        WzdmFragment fragment = new WzdmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setupSearchView();
        mWzdmAdatper = new WzdmAdatper(getActivity(), mData, R.layout.item_wzdm);
        mWzdmLV.setAdapter(mWzdmAdatper);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_wzdm, null);


        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    protected void initView(View mView) {

        mWzdbSearchView = (SearchView) mView.findViewById(R.id.wzdm_search_iv);
        mWzdmLV = (ListView) mView.findViewById(R.id.illegal_code_query_lv);
        mWzdmLV.setTextFilterEnabled(true);
        mWzdmLV.dispatchDisplayHint(View.INVISIBLE);
        try {
            Class<?> argClass = mWzdbSearchView.getClass();
            // 指定某个私有属性
            Field mSearchHintIconField = argClass
                    .getDeclaredField("mSearchHintIcon");
            mSearchHintIconField.setAccessible(true);
            View mSearchHintIcon = (View) mSearchHintIconField
                    .get(mWzdbSearchView);
            mSearchHintIcon.setVisibility(View.GONE);

            // 注意mSearchPlate的背景是stateListDrawable(不同状态不同的图片)
            // 所以不能用BitmapDrawable
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            // setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
            ownField.setAccessible(true);
            View v = (View) ownField.get(mWzdbSearchView);
            v.setBackgroundColor(Color.parseColor("#00000000"));

            // 指定某个私有属性
            Field mQueryTextView = argClass.getDeclaredField("mQueryTextView");
            mQueryTextView.setAccessible(true);
            Class<?> mTextViewClass = mQueryTextView.get(mWzdbSearchView)
                    .getClass().getSuperclass().getSuperclass().getSuperclass();

            // mCursorDrawableRes光标图片Id的属性
            // 这个属性是TextView的属性，所以要用mQueryTextView（SearchAutoComplete）
            // 的父类（AutoCompleteTextView）的父 类( EditText）的父类(TextView)
            Field mCursorDrawableRes = mTextViewClass
                    .getDeclaredField("mSubmitButton");
            // setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
            mCursorDrawableRes.setAccessible(true);
            // 注意第一个参数持有这个属性(mQueryTextView)的对象(mSearchView)
            // 光标必须是一张图片不能是颜色，因为光标有两张图片，
            // 一张是第一次获得焦点的时候的闪烁的图片，一张是后边有内容时候的图片，如果用颜色填充的话，就会失去闪烁的那张图片，
            // 颜色填充的会缩短文字和光标的距离（某些字母会背光标覆盖一部分）。
            mCursorDrawableRes.set(mQueryTextView.get(mWzdbSearchView),
                    R.drawable.icon_clear);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void requestData(String mS) {

        AjaxParams params = new AjaxParams();
        params.put("code", mS);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        mData.clear();
                        List<Wzdm> mList = Wzdm.parseList(String.valueOf(msg.obj));
                        mData.addAll(mList);
                        mWzdmAdatper.notifyDataSetChanged();
                        if (mList.isEmpty())
                            ToastUtil.toastShow(getActivity(), "无相关数据");
                        break;
                    case Constant.NET_DATA_FAIL:


                        DataError mDataError = DataError.parse(String.valueOf(msg.obj));
                        ToastUtil.toastShow(getActivity(), mDataError.getErrorMessage());

                        break;
                    case Constant.NET_DATA_NULL:

                        mData.clear();
                        mWzdmAdatper.notifyDataSetChanged();
                        ToastUtil.toastShow(getActivity(), "无相关数据");
                        break;
                    default:
                        break;


                }


            }
        }, params, NetworkUtil.QUERY_WZDM, false, 0);


    }


    private void setupSearchView() {
        mWzdbSearchView.setIconifiedByDefault(false);
        mWzdbSearchView.setOnQueryTextListener(this);
        mWzdbSearchView.setSubmitButtonEnabled(false);

    }


    @Override
    public boolean onQueryTextChange(String newText) {

        if (!TextUtils.isEmpty(newText) && newText.length() <= 6)
            requestData(newText);
        else if (newText.length() > 6) {
            ToastUtil.toastShow(getActivity(), "长度超出限制了哟");
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String arg0) {

        //  requestData(arg0);
        return true;
    }


    public class WzdmAdatper extends CommonAdapter<Wzdm> {


        public WzdmAdatper(Context context, List<Wzdm> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, Wzdm mWzdm, int mPosition) {

            holder.setText(R.id.wzdm_number, "违章代码：" + mWzdm.getCode());
            holder.setText(R.id.wzdm_score, "违章扣分：" + mWzdm.getScore_reduce());
            holder.setText(R.id.wzdm_money, "违章罚款：" + mWzdm.getPunish_money());
            holder.setText(R.id.wzdm_content, "违章内容：" + mWzdm.getAction());

        }
    }
}
