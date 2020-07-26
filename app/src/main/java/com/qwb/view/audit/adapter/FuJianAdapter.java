package com.qwb.view.audit.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwb.utils.MyUtils;
import com.qwb.view.file.model.FileBean;
import com.xmsx.qiweibao.R;

import java.util.List;

/**
 * 审批附件的适配器
 */
@SuppressLint("NewApi")
public class FuJianAdapter extends BaseAdapter {
	private Activity mActivity;
	private List<FileBean> fileBeanList;
	private Boolean isDelete;

	public FuJianAdapter(Activity activity, List<FileBean> fileBeanList , Boolean isDelete) {
		this.mActivity = activity;
		this.fileBeanList = fileBeanList;
		this.isDelete = isDelete;
	}

	@Override
	public int getCount() {
		return fileBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.x_adapter_shenpi_fujian, null);
		} 
		
		TextView tv_fileNm = MyUtils.getViewFromVH(convertView,R.id.tv_fileNm);
		TextView tv_fileSize = MyUtils.getViewFromVH(convertView,R.id.tv_fileSize);
		ImageView file_ico = MyUtils.getViewFromVH(convertView,R.id.img_file);
		ImageView img_deleteFile = MyUtils.getViewFromVH(convertView,R.id.img_deleteFile);
		
		if(isDelete){
			img_deleteFile.setVisibility(View.VISIBLE);
		}else{
			img_deleteFile.setVisibility(View.GONE);
		}
		FileBean fileBean = fileBeanList.get(position);
		if (fileBean != null) {
			String fileNm = fileBean.getFileNm();
			String fileType = fileBean.getTp1();
			int indexOf = fileNm.indexOf("￥");
			String name=fileNm.substring(indexOf+1, fileNm.length());//去掉"id(用户id)￥"
			tv_fileNm.setText(name);
			displayImageview(file_ico, fileType);// 根据文件类型设置图片
			
			img_deleteFile.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fileBeanList.remove(position);
					notifyDataSetChanged();
				}
			});
		}

		return convertView;
	}

	// 对应文件类型设置图标
	private void displayImageview(ImageView file_ico, String fileType) {
		if (MyUtils.isEmptyString(fileType)) {
			file_ico.setImageResource(R.drawable.file_other);
		} else {
			fileType = fileType.toLowerCase();
			if ("xls".equals(fileType) || "xlsx".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_excel);
			} else if ("docx".equals(fileType) || "doc".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_word);
			} else if ("pdf".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_pdf);
			} else if ("ppt".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_ppt);
			} else if ("txt".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_txt);
			} else if ("png".equals(fileType) || "jpg".equals(fileType)
					|| "bmp".equals(fileType) || "jpeg".equals(fileType)
					|| "gif".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_pic);
			} else {
				file_ico.setImageResource(R.drawable.file_other);
			}
		}
	}
	
	
	
}
