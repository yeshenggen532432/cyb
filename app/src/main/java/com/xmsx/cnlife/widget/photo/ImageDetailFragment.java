package com.xmsx.cnlife.widget.photo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.qwb.utils.ILUtil;
import com.xmsx.cnlife.widget.photo.PhotoViewAttacher.OnPhotoTapListener;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;

import java.io.File;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		creatSavePop();
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.x_image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);
				return false;
			}
		});
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	private PopupWindow mPopupWindow;
	private void creatSavePop() {
		if (mPopupWindow == null) {
			View view = getActivity().getLayoutInflater().inflate(R.layout.x_popup_sex, null);
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT,true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setAnimationStyle(R.style.MyPopAnimation3);
			view.findViewById(R.id.ll_xj).setVisibility(View.GONE);
			Button item_popupwindows_Photo = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			item_popupwindows_Photo.setText("保存图片");
			item_popupwindows_Photo.setOnClickListener(new OnClickListener()
	        {

				public void onClick(View v)
	            {
					
					mPopupWindow.dismiss();
					String fileName = MyUtils.getFileNameFromUrl(mImageUrl);
					//文件夹是否存在 不存在要穿件
					File file2 = new File(Constans.DIR_IMAGE_SAVE);
					if (!file2.exists()) {
						file2.mkdirs();
					}
					
					File file = new File(Constans.DIR_IMAGE_SAVE, fileName);
					if (file.exists()) {
						ToastUtils.showLongCustomToast( "图片已保存到"+ Constans.DIR_IMAGE_SAVE);
					}else{
						Bitmap bitmap= ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
						MyUtils.getFileFromBitmap(bitmap, file);
						ToastUtils.showLongCustomToast( "图片已保存到"+ Constans.DIR_IMAGE_SAVE);
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
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		DisplayImageOptions optionsSquere = ILUtil.getOptionsSquere();
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, optionsSquere,new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
//					message = "下载错误";
					break;
				case DECODING_ERROR:
//					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
//					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
//					message = "图片太大无法显示";
					break;
				case UNKNOWN:
//					message = "未知的错误";
					break;
				}
//				if(!TextUtils.isEmpty(message))
//				{
//				    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//				}
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				mAttacher.update();
			}
		});

		
		
		
	}

}
