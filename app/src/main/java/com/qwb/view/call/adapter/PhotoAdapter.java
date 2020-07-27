package com.qwb.view.call.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.qwb.utils.Constans;
import com.qwb.utils.MyGlideUtil;
import com.chiyong.t3.R;

import java.util.List;

/**
 */
@SuppressLint("NewApi")
public class PhotoAdapter extends BaseAdapter {
	private Activity mActivity;
	private List<String> mPhotoList;

	public PhotoAdapter(Activity activity, List<String> photoList , boolean isDownloader) {
		this.mActivity = activity;
		this.mPhotoList = photoList;

	}
	public PhotoAdapter(Activity activity, List<String> photoList ) {
		this.mActivity = activity;
		this.mPhotoList = photoList;
	}

	@Override
	public int getCount() {
		return mPhotoList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPhotoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.x_adapter_photo, null);
			holder = new ViewHolder();
			holder.img_simple = (ImageView) convertView.findViewById(R.id.iv_simple);
			holder.img_del = (ImageView) convertView.findViewById(R.id.iv_delpic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String path = mPhotoList.get(position);
		if (path != null) {
			switch (Constans.pic_tag) {
			case 0://图片1-默认
				if (Constans.isDelModel) {
					holder.img_del.setVisibility(View.VISIBLE);
				} else {
					holder.img_del.setVisibility(View.GONE);
				}
				break;
			case 1://图片2
				if (Constans.isDelModel2) {
					holder.img_del.setVisibility(View.VISIBLE);
				} else {
					holder.img_del.setVisibility(View.GONE);
				}
				break;
			case 2://图片3
				if (Constans.isDelModel3) {
					holder.img_del.setVisibility(View.VISIBLE);
				} else {
					holder.img_del.setVisibility(View.GONE);
				}
				break;
			}
			MyGlideUtil.getInstance().displayImageSquere(path, holder.img_simple);
		}

		return convertView;
	}

	private class ViewHolder {
		public ImageView img_simple;
		public ImageView img_del;
	}
	
}
