package com.qwb.view.audit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.qwb.view.common.model.PicList;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.qiweibao.R;

import java.util.List;

public class PicsAdapter extends BaseAdapter {

	private Context context ;
	private List<PicList> picsList;
	public PicsAdapter(Context context , List<PicList> picsList) {
		this.context = context ;
		this.picsList = picsList ;
	}
	
	@Override
	public int getCount() {
		return picsList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.x_adapter_sp_pic, null);
		}
		PicList picList = picsList.get(position);
		ImageView iv_simple = MyUtils.getViewFromVH(convertView, R.id.iv_simple);
		MyGlideUtil.getInstance().displayImageSquere(Constans.IMGROOTHOST + picList.getPicMini(), iv_simple);
		return convertView;
	}
}
