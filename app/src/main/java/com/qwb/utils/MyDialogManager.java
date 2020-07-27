package com.qwb.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chiyong.t3.R;

public class MyDialogManager {

	private Dialog warnDl ;
	private TextView tv_wanrconten;
	private MyDialogManager() {}
	private static MyDialogManager instance ;
	
	public static MyDialogManager getI(){
		if (instance == null) {
			instance = new MyDialogManager();
		}
		return instance;
	}
	
	/**
	 * 简单的提示dialog
	 * */
	public  void showWarnDialog(Context context, String content) {
		if (warnDl == null) {
			warnDl = new Dialog(context,R.style.Translucent_NoTitle);
			warnDl.setContentView(R.layout.x_dialog_warning);
			tv_wanrconten = (TextView) warnDl.findViewById(R.id.tv_wanrconten);
			warnDl.findViewById(R.id.ll_contentview).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					warnDl.dismiss();
				}
			});
		}
		
		if (tv_wanrconten != null) {
			tv_wanrconten.setText(content);
		}
		if(warnDl!=null){
			warnDl.show();
		}
	}
	
	private Dialog clickDialog;
	private TextView tv_hint;
	private OnCancle currentCancle;
	/**
	 * 有取消跟确定按钮的dialog
	 * */
	public  void showWithClickDialog(Context context, String hint, OnCancle cancle) {
		 	currentCancle = cancle;
			clickDialog = new Dialog(context,R.style.Translucent_NoTitle);
			clickDialog.setContentView(R.layout.x_dialog_zhishiku_del_warm);
			tv_hint = (TextView) clickDialog.findViewById(R.id.tv_upname);
			
			clickDialog.findViewById(R.id.bt_cancle).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					clickDialog.dismiss();
					if (currentCancle != null) {
						currentCancle.cancle();
					}
				}
			});
			clickDialog.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					clickDialog.dismiss();
					if (currentCancle != null) {
						currentCancle.sure();
					}
				}
			});
		
		if (tv_hint != null && !MyUtils.isEmptyString(hint)) {
			tv_hint.setText(hint);
		}
		clickDialog.show();

	}
	
	public interface OnCancle{
		void cancle();
		void sure();
	}
}
