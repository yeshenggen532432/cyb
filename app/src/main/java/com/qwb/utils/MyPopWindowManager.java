package com.qwb.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chiyong.t3.R;


/**
 * 单实例模式管理获取图片的popwindiow弹窗功能 需要弹窗的时候用
 *  需要实现OnImageBack接口对应从相机或相册2个点击事件
 * */
public class MyPopWindowManager {
	private PopupWindow mPopupWindow;
	private Context context;
	private OnImageBack onImageBack;

	private MyPopWindowManager() {}
	private static MyPopWindowManager instance;
	
	public static MyPopWindowManager getI() {
		if (instance == null) {
			instance = new MyPopWindowManager();
		}
		return instance ;
	}
	
	private Button item_popupwindows_camera ,item_popupwindows_Photo;
	
	/**
	 * bt1_str 第一个按钮显示的字体
	 * bt2_str 第二个按钮显示的字体
	 * */
	@SuppressWarnings("deprecation")
	public void show(Context ct, OnImageBack imageBack, String bt1_str, String bt2_str){
			this.context = ct ;
			this.onImageBack = imageBack ;
			View view = LayoutInflater.from(context).inflate(R.layout.x_popup_sex, null);
			
			item_popupwindows_camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
			item_popupwindows_Photo = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,true);
	        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	        mPopupWindow.setAnimationStyle(R.style.MyPopAnimation3);
	        view.findViewById(R.id.item_popupwindows_camera).setOnClickListener(new OnClickListener()
	        {

				public void onClick(View v)
	            {
	                    //相机直接拍摄
					mPopupWindow.dismiss();
					if (onImageBack != null) {
						onImageBack.fromCamera();
					}
	            }
	        });
	        view.findViewById(R.id.item_popupwindows_Photo).setOnClickListener(new OnClickListener()
	        {

				public void onClick(View v)
	            {
	                //修改头像 从相册
					mPopupWindow.dismiss();
					if (onImageBack != null) {
						onImageBack.fromPhotoAlbum();
					}
	            }
	        });
	        view.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new OnClickListener()
	        {
	            public void onClick(View v)
	            {
	                mPopupWindow.dismiss();
	            }
	        });
		
		if (!TextUtils.isEmpty(bt1_str)) {
			item_popupwindows_camera.setText(bt1_str);
			if("拨打".equals(bt1_str)){
				item_popupwindows_camera.setVisibility(View.GONE);
			}
		}
		if (!TextUtils.isEmpty(bt2_str)) {
			item_popupwindows_Photo.setText(bt2_str);
		}
		
		mPopupWindow.showAtLocation(new TextView(context), Gravity.NO_GRAVITY, 0, 0);
	}
	
	/**
	 * popwindow相机跟相册两个按钮的点击时间的回调
	 * */
	public interface OnImageBack{
		public void fromCamera();
		public void fromPhotoAlbum();
	}
	
	
}
