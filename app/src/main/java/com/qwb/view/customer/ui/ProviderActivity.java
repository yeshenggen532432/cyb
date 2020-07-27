package com.qwb.view.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.object.model.ProviderBean;
import com.qwb.view.object.model.ProviderBean.Customerls;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import okhttp3.Call;

/**
 * 创建描述：选择经销商
 */
public class ProviderActivity extends XActivity{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_provider;
	}

	@Override
	public Object newP() {
		return null;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
		initData();
	}

	/**
	 * 头部
	 */
	private void initHead() {
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Router.pop(context);
			}
		});
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		tv_headTitle.setText("选择供货商");
	}

	/**
	 * UI
	 */
	private void initUI() {
		initHead();
		initAdapter();
	}

	/**
	 * 适配器
	 */
	private ListView mListView;
	private ProviderAdapter mProviderAdapter;
	private List<Customerls> providerList=new ArrayList<Customerls>();
	private void initAdapter() {
		mListView = (ListView) findViewById(R.id.listView_provider);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Customerls customerls = providerList.get(position);
				if(customerls!=null){
					Intent data=new Intent();
					data.putExtra(ConstantUtils.Intent.PROVIDER_NAME, customerls.getKhNm());
					data.putExtra(ConstantUtils.Intent.PROVIDER_ID, customerls.getId());
					setResult(2, data);
					finish();
				}
			}
		});
	}

	/*
	 * 供货商--适配器
	 */
	private class ProviderAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return providerList.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=getLayoutInflater().inflate(R.layout.x_adapter_provider, null);
			}
			TextView tv_clientName= MyUtils.getViewFromVH(convertView, R.id.tv_clientName);
//			TextView tv_name= MyUtils.getViewFromVH(convertView, R.id.tv_title);
//			TextView tv_phone= MyUtils.getViewFromVH(convertView, R.id.tv_phone);
//			TextView tv_location= MyUtils.getViewFromVH(convertView, R.id.tv_location);
			Customerls customerls = providerList.get(position);
			if(customerls!=null){
				tv_clientName.setText(customerls.getKhNm());
			}
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
		
	//TODO -----------------------接口回調----------------------
	private void initData() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		OkHttpUtils
				.post()
				.url(Constans.queryCustomerls1)
				.params(params)
				.id(1)
				.build()
				.execute(new MyStringCallback(),this);
	}

	public class MyStringCallback extends StringCallback {
		@Override
		public void onError(Call call, Exception e, int id) {
			ToastUtils.showCustomToast(e.getMessage());
		}

		@Override
		public void onResponse(String response, int id) {
			switch (id) {
				case 1:
					parseJson1(response);
					break;
			}
		}
	}

	//修改经销商
	private void parseJson1(String response) {
		try {
			ProviderBean parseObject = JSONObject.parseObject(response, ProviderBean.class);
			if(parseObject!=null && parseObject.isState()){
				List<Customerls> customerls = parseObject.getCustomerls();
				providerList.clear();
				providerList.addAll(customerls);
				if(mProviderAdapter==null){
					mProviderAdapter=new ProviderAdapter();
					mListView.setAdapter(mProviderAdapter);
				}else{
					mProviderAdapter.notifyDataSetChanged();
				}
			}else{
				ToastUtils.showCustomToast(parseObject.getMsg());
			}
		}catch (Exception e){
			ToastUtils.showCustomToast(e.getMessage());
		}

	}
}
