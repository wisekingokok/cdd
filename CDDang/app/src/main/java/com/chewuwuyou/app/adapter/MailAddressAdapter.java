package com.chewuwuyou.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.LatelyCity;
import com.chewuwuyou.app.bean.MailAddress;
import com.chewuwuyou.app.ui.EditMailAdressActivtiy;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe:邮寄地址适配器
 * @author:liuchun
 * @created:
 */
public class MailAddressAdapter extends BaseAdapter {

    private List<MailAddress> mMailAdress;
    private Context mContext;
    private LayoutInflater mInflater;
    private Handler mHanler;
    private int index;//下标

    public MailAddressAdapter(Context context, List<MailAddress> mMailAdress,Handler mHanler) {
        this.mContext = context;
        this.mMailAdress = mMailAdress;
        this.mHanler = mHanler;
        this.mInflater = LayoutInflater.from(mContext);

    }
    @Override
    public int getCount() {
        return mMailAdress.size();
    }

    @Override
    public Object getItem(int position) {
        return mMailAdress.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        converView cView = null;
        if (convertView == null) {
            cView = new converView();
            convertView = mInflater.inflate(R.layout.mail_address_item, null);
            cView.addressName = (TextView) convertView
                    .findViewById(R.id.address_name);
            cView.addressPhone = (TextView) convertView
                    .findViewById(R.id.address_phone);
            cView.detailedAddress = (TextView) convertView
                    .findViewById(R.id.detailed_address);
            cView.defaultAddress = (TextView) convertView
                    .findViewById(R.id.default_address);
            cView.addressEdit = (TextView) convertView
                    .findViewById(R.id.address_edit);
            cView.addressDelete = (TextView) convertView
                    .findViewById(R.id.address_delete);
            cView.defaultSelected = (ImageView) convertView
                    .findViewById(R.id.default_selected);
            cView.defaultSelectedLayou = (LinearLayout) convertView
                    .findViewById(R.id.default_selected_layou);

            convertView.setTag(cView);
        } else {
            cView = (converView) convertView.getTag();
        }
        if(mMailAdress.get(position).getDefaultAddress() == 1){
            cView.defaultSelected.setImageDrawable(mContext.getResources().getDrawable((R.drawable.checkbox_check)));//选中
        }else{
            cView.defaultSelected.setImageDrawable(mContext.getResources().getDrawable((R.drawable.checkbox_uncheck)));//不选中
        }
        cView.addressName.setText(mMailAdress.get(position).getReceiver());
        cView.addressPhone.setText(mMailAdress.get(position).getPhone());
        cView.detailedAddress.setText(mMailAdress.get(position).getRegion()+mMailAdress.get(position).getAddress());
        cView.addressEdit.setOnClickListener(new View.OnClickListener() {//编辑
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditMailAdressActivtiy.class);
                intent.putExtra("id",mMailAdress.get(position).getId()+"");
                intent.putExtra("receiver",mMailAdress.get(position).getReceiver());
                intent.putExtra("phone",mMailAdress.get(position).getPhone());
                intent.putExtra("address",mMailAdress.get(position).getAddress());
                intent.putExtra("defaultAddress",mMailAdress.get(position).getDefaultAddress()+"");
                intent.putExtra("region",mMailAdress.get(position).getRegion());
                intent.putExtra("zipCode",mMailAdress.get(position).getZipCode());
                mContext.startActivity(intent);
            }
        });
        cView.addressDelete.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                index = position;
                mAilAddressDialog("删除地址","你是否删除该地址","");

            }
        });
        cView.defaultSelectedLayou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AjaxParams params=new AjaxParams();
                params.put("id",mMailAdress.get(position).getId()+"");
                NetworkUtil.postMulti(NetworkUtil.UPDATE_MAIL_DEFAULT, params, new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String o) {
                        super.onSuccess(o);
                        try {
                            JSONObject jo=new JSONObject(o);
                            if(jo.getInt("result")==1){
                                for (int i = 0;i<mMailAdress.size();i++){
                                    if(position == i){
                                        mMailAdress.get(position).setDefaultAddress(1);
                                    }else{
                                        mMailAdress.get(i).setDefaultAddress(0);
                                    }
                                }
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
            }
        });
        return convertView;
    }

    class converView {
        TextView addressName,addressPhone,detailedAddress,defaultAddress,addressEdit,addressDelete;
        ImageView defaultSelected;
        LinearLayout defaultSelectedLayou;
    }

    /**
     * 提示用户是否进行操作
     */
    public void mAilAddressDialog(String title, String context, final String txet) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(title);
        dialog.setMessage(context);
        dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Message message = new Message();//传递删除的下标
                message.obj = index;//下标
                message.what = 1;//id
                mHanler.sendMessage(message);
            }
        });
        dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.create().show();
    }

}
