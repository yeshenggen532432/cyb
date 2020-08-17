package com.qwb.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qwb.application.MyApp;
import com.xmsx.cnlife.view.widget.CircleTransform;
import com.xmsx.cnlife.view.widget.CornersTransform;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 显示图片
 */

public class MyGlideUtil {
    private static MyGlideUtil instance = null;
    private static ImageLoader imageLoader = null;
    private static DisplayImageOptions optionSquere = null;
    private static DisplayImageOptions optionRound = null;

    public static MyGlideUtil getInstance() {
        if (instance == null) {
            instance = new MyGlideUtil();
            imageLoader = ILUtil.getImageLoder();
            optionSquere = ILUtil.getOptionsSquere();
            optionRound = ILUtil.getOptionsRound();
        }
        return instance;
    }

    public void setCircle(ImageView iv, String url){
        url = MyUrlUtil.getUrl(url);
        Glide.with(MyApp.getI())
                .load(url)
                .placeholder(R.drawable.qwb_normal_yuan)
                .error(R.drawable.qwb_normal_yuan)
                .transform(new CircleTransform(MyApp.getI()))//圆形
                .into(iv);
    }

    public void setCorners(ImageView iv, String url){
        url = MyUrlUtil.getUrl(url);
            Glide.with(MyApp.getI())
                    .load(url)
                    .placeholder(R.drawable.qwb_normal_kuang)
                    .error(R.drawable.qwb_normal_kuang)
                    .transform(new CornersTransform(MyApp.getI()))//圆角
                    .into(iv);
    }

    public void setRadius(Context context, ImageView iv, String url, int radius){
        url = MyUrlUtil.getUrl(url);
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.qwb_normal_kuang)
                .error(R.drawable.qwb_normal_kuang)
                .transform(new CornersTransform(context, radius))//圆角
                .into(iv);
    }

    public void displayImageSquere(String url, ImageView imageView){
        try {
            url = MyUrlUtil.getUrl(url);
            if(null != imageLoader && null != optionSquere){
                imageLoader.displayImage(url, imageView, optionSquere);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void displayImageRound(String url, ImageView imageView){
        url = MyUrlUtil.getUrl(url);
        if(null != imageLoader && null != optionRound){
            imageLoader.displayImage(url, imageView, optionRound);
        }
    }

    /**
     * urlConvert：url是否要转换下
     */
    public void displayImageSquere(String url, ImageView imageView, boolean urlConvert){
        if (urlConvert){
            url = MyUrlUtil.getUrl(url);
        }
        if(null != imageLoader && null != optionSquere){
            imageLoader.displayImage(url, imageView, optionSquere);
        }
    }




}
