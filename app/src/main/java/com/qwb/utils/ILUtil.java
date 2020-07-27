package com.qwb.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.chiyong.t3.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * UIL锟斤拷锟斤拷
 * */
public class ILUtil {

	private static AnimateFirstDisplayListener listener;
	private static DisplayImageOptions options;
	private static DisplayImageOptions squareoptions;

	/**
	 * imageloader锟斤拷锟斤拷
	 * */
	public static ImageLoader getImageLoder() {
		return ImageLoader.getInstance();
	}

	/**
	 * 图片首次加载时有动画效果
	 * */
	public static ImageLoadingListener getAnima() {
		if (listener == null) {
			listener = new AnimateFirstDisplayListener();
		}
		return listener;
	}

	/**
	 * 加载失败时加载圆形的失败示例图图片
	 * */
	public static DisplayImageOptions getOptionsRound() {
		if (options == null) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.user_headico)
					.showImageForEmptyUri(R.drawable.user_headico)
					.showImageOnFail(R.drawable.user_headico)
					.cacheInMemory(true).cacheOnDisc(true)
					.considerExifParams(true).build();
		}
		return options;

	}

	/**
	 * 加载失败是加载正方形的失败示例图片
	 * */
	public static DisplayImageOptions getOptionsSquere() {
		if (squareoptions == null) {
			squareoptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.empty_photo)
					.showImageForEmptyUri(R.drawable.empty_photo)
					.showImageOnFail(R.drawable.empty_photo)
					.cacheInMemory(true).cacheOnDisc(true)
					.considerExifParams(true).build();
		}
		return squareoptions;

	}

	/**
	 * 加载失败是加载正方形的失败示例图片
	 * */
	public static DisplayImageOptions getOptionsSquere2() {
		if (squareoptions == null) {
			// squareoptions = new DisplayImageOptions.Builder()
			// .showImageOnLoading(R.drawable.empty_photo)
			// .showImageForEmptyUri(R.drawable.empty_photo)
			// .showImageOnFail(R.drawable.empty_photo)
			// .cacheInMemory(true).cacheOnDisc(true)
			// .considerExifParams(true).build();

			squareoptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.empty_photo) // 设置图片在下载期间显示的图片
					.showImageForEmptyUri(R.drawable.empty_photo)// 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.empty_photo) // 设置图片加载/解码过程中错误时候显示的图片
					.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
					.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
					.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//					.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//					.preProcessor(BitmapProcessor preProcessor)// 设置图片加入缓存前，对bitmap进行设置
//					.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//					.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
//					.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
					.build();// 构建完成
		}
		return squareoptions;

	}

	private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
