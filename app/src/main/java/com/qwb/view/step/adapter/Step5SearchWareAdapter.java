package com.qwb.view.step.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qwb.utils.MyUtils;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 搜索商品
 */
public class Step5SearchWareAdapter extends BaseAdapter {
	private Activity mActivity;
	private List<ShopInfoBean.Data> searchGoodsList= new ArrayList<>();

	public Step5SearchWareAdapter(Activity activity, List<ShopInfoBean.Data> searchGoodsList) {
		this.mActivity=activity;
		this.searchGoodsList=searchGoodsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(R.layout.x_adapter_step5_search_ware, null);
		}
		TextView tv_name= MyUtils.getViewFromVH(convertView, R.id.tv_name);
		TextView tv_gg= MyUtils.getViewFromVH(convertView, R.id.tv_gg);
		TextView tv_dw= MyUtils.getViewFromVH(convertView, R.id.tv_dw);

		ShopInfoBean.Data item = searchGoodsList.get(position);
		tv_name.setText(item.getWareNm());
		tv_gg.setText("规格：" + item.getWareGg());
		tv_dw.setText("单位：" + item.getWareDw());
		return convertView;
	}

	@Override
	public int getCount() {
		return searchGoodsList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position){
		return 0;
	}



//	public List<HashMap<String, String>> getSearchGoodsList() {
//		return searchGoodsList;
//	}
//
//	public void setSearchGoodsList(List<HashMap<String, String>> searchGoodsList) {
//		this.searchGoodsList = searchGoodsList;
//	}



}
