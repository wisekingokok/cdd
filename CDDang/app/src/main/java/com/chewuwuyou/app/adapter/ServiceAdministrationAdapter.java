package com.chewuwuyou.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarBrand;
import com.chewuwuyou.app.bean.ServiceAdministration;
import com.chewuwuyou.app.ui.EditService;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PinnedHeaderListView;
import com.chewuwuyou.app.widget.PinnedHeaderListView.PinnedHeaderAdapter;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ServiceAdministrationAdapter extends BaseAdapter {


	private LayoutInflater mInflater;
	private  List<ServiceAdministration> mServiceAdminisList;
	private Context mContext;
	private int index;
	private Handler handler;
	private String min;
	private String max;
	public ServiceAdministrationAdapter(Context mContext,List<ServiceAdministration> mServiceAdminisList,Handler handler,String min,String max) {
        this.mContext = mContext;
		this.mServiceAdminisList = mServiceAdminisList;
		this.mInflater = LayoutInflater.from(mContext);
		this.handler = handler;
		this.min = min;
		this.max = max;
	}

	@Override
	public int getCount() {

		return mServiceAdminisList.size();
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BanKuaiViewCache viewCache;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.service_administration_item, null);
			viewCache = new BanKuaiViewCache();
			viewCache.feenMondel = (TextView) convertView.findViewById(R.id.feen_mondel);
			viewCache.serviceMondel = (TextView) convertView.findViewById(R.id.service_mondel);
			viewCache.serviceMondelTotal = (TextView) convertView.findViewById(R.id.service_mondel_total);
			viewCache.serviceEdit = (TextView) convertView.findViewById(R.id.service_edit);
			viewCache.serviceDelete = (TextView) convertView.findViewById(R.id.service_delete);
			viewCache.serviceImg = (ImageView) convertView.findViewById(R.id.service_img);
			viewCache.serviceType = (TextView) convertView.findViewById(R.id.service_type);
			viewCache.mLinefees = (LinearLayout) convertView.findViewById(R.id.linefees);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
		if(mServiceAdminisList.get(position).getType().equals("1")){
			viewCache.mLinefees.setVisibility(View.INVISIBLE);
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		viewCache.feenMondel.setText(df.format(Double.parseDouble(mServiceAdminisList.get(position).getFees()))+"");
		viewCache.serviceMondel.setText(df.format(Double.parseDouble(mServiceAdminisList.get(position).getPrice()))+"");
		viewCache.serviceMondelTotal.setText(df.format(Double.parseDouble(mServiceAdminisList.get(position).getFees())+Double.parseDouble(mServiceAdminisList.get(position).getPrice()))+"");
		viewCache.serviceType.setText(ServiceUtils.getProjectName(mServiceAdminisList.get(position).getProjectNum()));
		ImageUtils.displayImage(mServiceAdminisList.get(position).getProjectImg(), viewCache.serviceImg, 10);// 图片类型显示
		viewCache.serviceEdit.setOnClickListener(new View.OnClickListener() {//编辑
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(mContext, EditService.class);
				intent.putExtra("id",String.valueOf(mServiceAdminisList.get(position).getSid()));
				intent.putExtra("servicePrice",String.valueOf(mServiceAdminisList.get(position).getPrice()));
				intent.putExtra("serviceId",String.valueOf(mServiceAdminisList.get(position).getServiceId()));
				intent.putExtra("fees",String.valueOf(mServiceAdminisList.get(position).getFees()));
				intent.putExtra("projectnum",String.valueOf(mServiceAdminisList.get(position).getProjectNum()));
				intent.putExtra("projectimg",mServiceAdminisList.get(position).getProjectImg());
				intent.putExtra("min",min);
				intent.putExtra("max",max);
				mContext.startActivity(intent);
			}
		});

		viewCache.serviceDelete.setOnClickListener(new View.OnClickListener() {//删除
			@Override
			public void onClick(View v) {
				index = position;
				dialog("","你是否删除该服务","服务");
			}
		});

		return convertView;
	}

	class BanKuaiViewCache {
		TextView feenMondel,serviceMondel,serviceMondelTotal,serviceEdit,serviceDelete,serviceType;//规费，服务费，合计，编辑，删除,服务类型
		private ImageView serviceImg;//图片
		LinearLayout mLinefees;
	}

	/**
	 * 提示用户是否进行操作
	 */
	public void dialog(String title, String context, final String txet) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(title);
		dialog.setMessage(context);
		dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Message message = new Message();
				Bundle b = new Bundle();
				b.putInt("service", mServiceAdminisList.get(index).getServiceIdentification());
				b.putInt("index", index);
				b.putString("sid",mServiceAdminisList.get(index).getSid());
				message.setData(b);
				handler.sendMessage(message);
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
